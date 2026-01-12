package com.app.playerservicejava.controller;

import com.app.playerservicejava.dto.*;
import com.app.playerservicejava.exception.InvalidRequestParameterException;
import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "v1/players", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Players> getPlayers() {
            Players players = playerService.getPlayers();
               return ResponseEntity.ok(players);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable("id") String id) {
        Optional<Player> player = playerService.getPlayerById(id);

        if (player.isPresent()) {
            return new ResponseEntity<>(player.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/paged")
    public ResponseEntity<Page<Player>> getPlayersPaginated(
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "50") int size) {

        Page<Player> playersPage = playerService.getPlayersPaginated(page, size);
        return ResponseEntity.ok(playersPage);
    }

    //CURSOR based pagination
    @GetMapping("/cursor")
    public ResponseEntity<Map<String, Object>> getPlayersCursor(
            @RequestParam(required = false) String afterId,
            @RequestParam(defaultValue = "50") int limit) {

        List<Player> playersList = playerService.getPlayersAfter(afterId, limit);

        String nextCursor = playersList.isEmpty() ? null :
                playersList.get(playersList.size() - 1).getPlayerId();

        Players players = new Players();
        players.setPlayers(playersList);

        return ResponseEntity.ok(
                Map.of(
                        "data", players,
                        "nextCursor", nextCursor
                )
        );
    }

    @GetMapping("/cursor/by-firstname")
    public ResponseEntity<Map<String, Object>> getPlayersCursor(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String playerId,
            @RequestParam(defaultValue = "50") int limit
    ) {
        List<Player> players = playerService.getPlayersByCursor(firstName, playerId, limit);

        if (players.isEmpty()) {
            return ResponseEntity.ok(Map.of("data", List.of(), "nextCursorFirstName", null, "nextCursorPlayerId", null));
        }

        Player last = players.get(players.size() - 1);

        return ResponseEntity.ok(
                Map.of(
                        "data", players,
                        "nextCursorFirstName", last.getFirstName(),
                        "nextCursorPlayerId", last.getPlayerId()
                )
        );
    }



    @GetMapping("/paginationCustom")
    public ResponseEntity<PlayerPageResponse> getPlayersPaginatedCustom(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Page<Player> playersPage = playerService.getPlayersPaginated(page, size);
        PlayerPageResponse playerPageResponse = new PlayerPageResponse(playersPage);
        return ResponseEntity.ok(playerPageResponse);
    }

    @PostMapping("/batch")
    public ResponseEntity<Players> getPlayersByIdsPost(@RequestBody List<String> playerIds) {

        if (playerIds.isEmpty()) {
            throw new InvalidRequestParameterException("Player ids cannot be empty");
        }
        Players players = playerService.getAllPlayersById(playerIds);
        return ResponseEntity.ok(players);
    }

    @PostMapping("/batchByCoutry")
    public ResponseEntity<Players> getPlayersByIdsAndCountryPost(@Validated @RequestBody PlayerBatchRequest pbr) {

        if (pbr.getPlayersIds().isEmpty()) {
            throw new InvalidRequestParameterException("Player ids cannot be empty");
        }
        Players players = playerService.getAllPlayersByIdAndCountry(pbr);
        return ResponseEntity.ok(players);
    }

    @PostMapping("/batch/paged")
    public ResponseEntity<Page<Player>> getPlayersByIdOrderedPost(
            @RequestBody List<String> playerIds,
            @RequestParam (name="page", defaultValue = "0") int page,
            @RequestParam (name="size", defaultValue = "50") int size
    ){
        if (playerIds.isEmpty()) {
            throw new InvalidRequestParameterException("Player ids cannot be empty");
        }
        return ResponseEntity.ok(playerService.getPlayersByIdOrderByLastName(playerIds, page, size));
    }

    @GetMapping("/top")
    public ResponseEntity<Players> getTopNPlayers(
            @RequestParam (name="size", defaultValue = "10") int size){
        if(size <= 0)
            throw new InvalidRequestParameterException("Size should be a positive number");
        return ResponseEntity.ok(playerService.getTopNPlayers(size));
    }

    @GetMapping("/filter")
    ResponseEntity<Players> getFilteredPlayers(
            @RequestParam (name = "country") String country,
            @RequestParam (name = "throws") String throwsHand
    ){

        if(country.isBlank() || throwsHand.isBlank())
            throw new InvalidRequestParameterException("Country or Throws cannot be empty");

        return ResponseEntity.ok(playerService.getPlayersByCountryAndThrows(country, throwsHand));
    }

    @GetMapping("/filterBySpec")
    ResponseEntity<Players> getFilteredPlayersByThrowStats(
            @RequestParam (name = "minHeight") Integer minHeight,
            @RequestParam (required = false, name = "country") String country,
            @RequestParam (required = false, name = "birthYear") Integer birthYear,
            @RequestParam (required = false, name = "debutBefore")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debutBefore
    ){
        //sample input validation
        if (minHeight == null || country == null || country.isBlank()) {
            throw new InvalidRequestParameterException("minHeight and country are required fields");
        }
        if(minHeight < 0 || (birthYear != null && birthYear  < 0))
            throw new InvalidRequestParameterException("Min height or birth year cannot be negative");

        return ResponseEntity.ok(playerService.getFilteredPlayers(minHeight, country, birthYear, debutBefore));

    }

    /// calling ai model
    @GetMapping("/insights/{id}")
    public ResponseEntity<String> getInsightsForOnePlayer(@PathVariable ("id") String id){

        return ResponseEntity.ok(playerService.getInsightsforOnePlayer(id));
    }

    @PostMapping("/insights/players")
    public ResponseEntity<String> getInsightsForPlayers(@RequestBody List<String> playerIds){

        if (playerIds.isEmpty()) {
            throw new InvalidRequestParameterException("Player ids cannot be empty");
        }
        return ResponseEntity.ok(playerService.getInsightsforPlayers(playerIds));
    }

    @PostMapping("/search")
    public ResponseEntity<Players> getPlayersWithSearch(@Validated  @RequestBody PlayerSearchRequest psr){

        return ResponseEntity.ok(playerService.getPlayersWithSearchParam(psr));
    }

    //ADDING UPDATING

    @PostMapping
    public ResponseEntity<PlayerResponse> create(@Validated @RequestBody PlayerCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponse> update(@PathVariable String id, @Validated @RequestBody PlayerUpdateRequest req) {
        return ResponseEntity.ok(playerService.update(id, req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PlayerResponse> patch(@PathVariable String id, @RequestBody PlayerPatchRequest req) {
        return ResponseEntity.ok(playerService.patch(id, req));
    }
}
