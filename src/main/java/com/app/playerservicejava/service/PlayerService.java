package com.app.playerservicejava.service;

import com.app.playerservicejava.dto.*;
import com.app.playerservicejava.exception.AiModelException;
import com.app.playerservicejava.exception.InvalidRequestParameterException;
import com.app.playerservicejava.exception.PlayerNotFoundException;
import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.repository.PlayerRepository;
import com.app.playerservicejava.service.chat.ChatClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
public class PlayerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerService.class);
    private static final String CHAT_SERVICE_URL = "http://localhost:8080/v1/chat/promptWithPost";
    private static final String FINAL_SUMMARY_PROMPT = "You are given multiple partial summaries of baseball player groups."
            +"Combine them into one coherent overall summary"
            +" that highlights key trends and avoids repetition."
            +" Keep it concise and factual within 150 words. Here are the summaries:";
    private static final String SUMMARY_PROMPT = "You are an AI assistant summarizing baseball players based ONLY on the provided data.\n" +
            "Do not add any information not present in the input.\n" +
            "Generate a short factual summary using only these fields.\n";

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ChatClientService chatClientService;

    @Autowired
    RestTemplate restTemplate;

    public Players getPlayers() {
        Players players = new Players();
            playerRepository.findAll()
                    .forEach(players.getPlayers()::add);
        return players;
    }


    @Cacheable("players")
    public Optional<Player> getPlayerById(String playerId) {
        Optional<Player> player = null;

        /* simulated network delay */
        try {
            LOGGER.info("Getting details for playerId:" + playerId +" from DB");
            player = playerRepository.findById(playerId);
            Thread.sleep((long)(Math.random() * 2000));
        } catch (Exception e) {
            LOGGER.error("message=Exception in getPlayerById; exception={}", e.toString());
            return Optional.empty();
        }
        return player;
    }

    //pagination with sort
    public Page<Player> getPlayersPaginated(int page, int size) {
        Sort sort = Sort.by("firstName").ascending(); //ordering is must for pagination
        Pageable pageable = PageRequest.of(page, size, sort);

        return playerRepository.findAll(pageable);
    }

    //Cursor based pagination
    public List<Player> getPlayersAfter(String afterId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("playerId").ascending());
        if (afterId == null || afterId.isEmpty()) {
            // First request → return first page.
            return playerRepository.findAllByOrderByPlayerIdAsc(pageable);
        }

        return playerRepository.findByPlayerIdGreaterThanOrderByPlayerIdAsc(afterId, pageable);
    }

    //Cursor based pagination with sort on other column
    public List<Player> getPlayersByCursor(String firstName, String playerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return playerRepository.findNextPage(firstName, playerId, pageable);
    }

    @Cacheable("players")
    public Players getAllPlayersById(List<String> playerIds) {
            Players players = new Players();
            //findAllById is not pageable or sortable
            List<Player> playersList = playerRepository.findAllById(playerIds);
            if(playersList.isEmpty())
                throw new PlayerNotFoundException("No players found with given ids");
            players.setPlayers(playersList);
            return players;
    }

    @Cacheable("playersByCountry") // also example to catch exception here instead of global
    public Players getAllPlayersByIdAndCountry(PlayerBatchRequest pbr) {
        Players players = new Players();
        //findAllById is not pageable or sortable
        List<Player> playersList = playerRepository.findByPlayerIdInAndBirthCountry(pbr.getPlayersIds(), pbr.getBirthCountry());
        if(playersList.isEmpty())
            throw new PlayerNotFoundException("No players found with given ids");
        players.setPlayers(playersList);
        return players;
    }

    public Page<Player> getPlayersByIdOrderByLastName(List<String> ids, int page, int size){
            Sort sort = Sort.by("LastName").ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Player> players = playerRepository.findByPlayerIdIn(ids, pageable);
            return players;
    }

    public Players getPlayersTop10ByLastName() {
        List<Player> top10PlayersList= playerRepository.findTop10ByOrderByLastNameAsc();
        Players players = new Players();
        players.setPlayers(top10PlayersList);
        return players;
    }

    //FIND top N
    public Players getTopNPlayers(int n) {
        Pageable page = PageRequest.of(0, n);
        Players players = new Players();
        players.setPlayers(playerRepository.findAll(page).getContent());
        return players;
    }

    //FIND top N with a crtieria
    public List<Player> getTopNPlayersByCountry(String country, int n) {
        Pageable page = PageRequest.of(0, n);
        return playerRepository.findByBirthCountry(country, page).getContent();
    }

    public Players getPlayersByCountryAndThrows(String country, String throwsHand){

        //validate throwsHand
        Set set = Set.of("L","R");

        if(!set.contains(throwsHand)) {
            LOGGER.warn("Invalid arguments: Throws can be L or R");
            throw new InvalidRequestParameterException("Invalid arguments: Throws can be L or R");
        }
        Players players = new Players();
        players.setPlayers(playerRepository.findByBirthCountryAndThrowStatsOrderByBirthCountry(country,throwsHand));
        return players;
    }

    //with Specification - most dynamic and better approach
    public Players getFilteredPlayers(Integer minHeight, String country, Integer birthYear, LocalDate debutBefore){
        Specification<Player> spec = Specification.where(null);

        if(minHeight != null){
            spec = spec.and((root,query,cb) ->  cb.greaterThanOrEqualTo(root.get("height"), minHeight));
        }

        if(birthYear != null){
            spec = spec.and((root,query,cb) -> cb.lessThanOrEqualTo(root.get("birthYear"), birthYear));
        }

        if(debutBefore != null){
            spec = spec.and((root,query,cb) -> cb.lessThan(root.get("debut"), debutBefore));
        }

        if(country != null){
            spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("team")), country.toLowerCase()));
        }

        Players players = new Players();
        players.setPlayers(playerRepository.findAll(spec));
        return players;
    }

    /**
     * For AI calls since they are external service calls,
     * we should add the circuit breaker for repeated failures
     * @CircuitBreaker(name = "statsService", fallbackMethod = "getFallbackStats")
     */

    public String getInsightsforOnePlayer(String playerId){

        try {
            LOGGER.info("fetching data for player id:"+ playerId);
            Optional<Player> player = playerRepository.findById(playerId);
            if(!player.isPresent())
                throw new PlayerNotFoundException("Player with id " + playerId + " not found");
            Player p = player.get();
            StringBuilder prompt = new StringBuilder("Act like an analyst and Analyze the following player data\n");
            prompt.append("height:"+ p.getHeight() + " weight:"+p.getWeight()+ " bats:"+p.getBats()+ "throws:"+p.getThrowStats());
            prompt.append("\nProvide concise insights about patterns, outliers, and top performers.");

            String response = chatClientService.chatWithPrompt(prompt.toString());
            return response;
        }catch (AiModelException ex){
            LOGGER.error("Error in getting insights for players");
            throw ex;
        }
    }

    public String getInsightsforPlayers(List<String> playerIds){

        List<Player> playerList = playerRepository.findAllById(playerIds);
        if(playerList.isEmpty())
            throw new PlayerNotFoundException("No players found with given ids");

        //create chunks for token limitation
        int chunkSize = 50;
        List<List<Player>> chunks = new ArrayList<>();
        for(int i=0; i< playerList.size(); i+=chunkSize){
            chunks.add(playerList.subList(i, Math.min(i+chunkSize, playerList.size())));
        }

        //for everyChunk build prompt and call AI model
        StringBuilder aggregatedInsights  = new StringBuilder();
        List<String> insightsList = new ArrayList<>();
        for(int i=0; i< chunks.size(); i++){
            List<Player> players = chunks.get(i);
            LOGGER.info("Sending chunk {} of {} players to AI model", i + 1, chunks.size());

            StringBuilder prompt = new StringBuilder(SUMMARY_PROMPT);
            for(Player p : players){
                prompt.append(p.toString()+"\n");
                //prompt.append("height:"+ p.getHeight() + " weight:"+p.getWeight()+ " bats:"+p.getBats()+ "throws:"+p.getThrowStats());
            }
            prompt.append("\nProvide concise insights about patterns and top performers.");

            try{
                String response = chatClientService.chatWithPrompt(prompt.toString());
                insightsList.add(response);
                aggregatedInsights.append("\n\n--- Insights for Chunk ").append(i + 1).append(" ---\n");
                aggregatedInsights.append(response);
                LOGGER.info("Completed AI analysis for chunk {} of {} players", i + 1, chunks.size());
            } catch (AiModelException ex){
                LOGGER.error("AI request failed for chunk {}", i + 1, ex);
                aggregatedInsights.append("\n[Error generating insights for chunk ").append(i + 1).append("]");
                throw ex;
            }
        }

        // return aggregatedInsights.toString();

        // optional could get overall insights for all the players
        StringBuilder finalPrompt = new StringBuilder(FINAL_SUMMARY_PROMPT);
        for(String s: insightsList){
            finalPrompt.append(s);
        }

        return chatClientService.chatWithPrompt(finalPrompt.toString());
    }

    /** ✅ Builds chunks to avoid token overflow */
    private List<List<Player>> chunkPlayers(List<Player> players, int chunkSize) {
        List<List<Player>> chunks = new ArrayList<>();
        for (int i = 0; i < players.size(); i += chunkSize) {
            chunks.add(players.subList(i, Math.min(i + chunkSize, players.size())));
        }
        return chunks;
    }

    /** ✅ Builds prompt text for the AI model */
    private String buildPrompt(List<Player> players, int chunkNum) {
        StringBuilder prompt = new StringBuilder(
                "You are a baseball data analyst. Analyze the following set of players (chunk " + chunkNum + "):\n"
        );
        for (Player p : players) {
//            prompt.append(String.format("- %s: throwStats=%s, Bats=%s, height=%s%n",
//                    p.getFirstName(), p.getThrowStats(), p.getBats(), p.getHeight()));
            prompt.append(p.toString());
        }
        prompt.append("\nProvide concise insights about patterns, outliers, and top performers.");
        return prompt.toString();
    }

    public String generatePlayerInsightsRest() {
        List<Player> allPlayers = playerRepository.findAll();

        if (allPlayers.isEmpty()) {
            throw new IllegalArgumentException("No players found to generate insights");
        }

        // 🧠 Construct AI prompt
        StringBuilder prompt = new StringBuilder("Analyze the following baseball players and provide insights:\n");
        for (Player p : allPlayers.stream().limit(10).toList()) { // limit for brevity
            prompt.append(p.toString()+"\n");
        }

        // ✅ Prepare POST request
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // ✅ Invoke Chat API via RestTemplate
            ResponseEntity<String> response = restTemplate.postForEntity(CHAT_SERVICE_URL, requestEntity, String.class);
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("Error while calling Chat Service ", e.getMessage());
            return "Error generating insights: " + e.getMessage();
        }
    }

    public Players getPlayersWithSearchParam(PlayerSearchRequest psr){
        String birthCity = psr.getBirthCity();
        String birthCountry = psr.getBirthCountry();
        String throwsHand = psr.getThrowsHand();

        List<Player> playerList = playerRepository.findByBirthCityAndBirthCountryAndThrowStats(birthCity,birthCountry, throwsHand);
        Players players = new Players();
        players.setPlayers(playerList);

        return players;
    }

    //ADDING UPDATING DATA

    public PlayerResponse create(PlayerCreateRequest req) {
        if (playerRepository.existsById(req.getPlayerId())) {
            throw new InvalidRequestParameterException("Player already exists: " + req.getPlayerId());
        }

        Player p = new Player();
        p.setPlayerId(req.getPlayerId());
        p.setFirstName(req.getFirstName());
        p.setLastName(req.getLastName());
        p.setBirthCountry(req.getBirthCountry());
        p.setBirthCity(req.getBirthCity());
        p.setThrowStats(req.getThrowsHand());
        p.setBats(req.getBatsHand());

        return PlayerResponse.from(playerRepository.save(p));
    }

    public PlayerResponse update(String id, PlayerUpdateRequest req) {
        Player p = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found: " + id));

        p.setFirstName(req.getFirstName());
        p.setLastName(req.getLastName());
        p.setBirthCountry(req.getBirthCountry());
        p.setBirthCity(req.getBirthCity());
        p.setThrowStats(req.getThrowsHand());
        p.setBats(req.getBatsHand());

        return PlayerResponse.from(playerRepository.save(p));
    }

    public PlayerResponse patch(String id, PlayerPatchRequest req) {
        Player p = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found: " + id));

        if (req.getFirstName() != null) p.setFirstName(req.getFirstName());
        if (req.getLastName() != null) p.setLastName(req.getLastName());
        if (req.getBirthCountry() != null) p.setBirthCountry(req.getBirthCountry());
        if (req.getBirthCity() != null) p.setBirthCity(req.getBirthCity());
        if (req.getThrowsHand() != null) p.setThrowStats(req.getThrowsHand());
        if (req.getBatsHand() != null) p.setBats(req.getBatsHand());

        return PlayerResponse.from(playerRepository.save(p));
    }
}
