package com.app.playerservicejava.repository;
import com.app.playerservicejava.dto.AvgHeightByCountry;
import com.app.playerservicejava.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String>, JpaSpecificationExecutor<Player> {
    List<Player> findTop10ByOrderByLastNameAsc();

    @Query("SELECT p FROM Player p WHERE LOWER(p.birthCity) = LOWER(:birthCity) " +
            "AND (LOWER(p.birthState) = LOWER(:birthState) OR LOWER(p.birthCountry) = LOWER(:birthCountry))")
    List<Player> findByBirthCityAndBirthStateOrCountry(
            @Param("birthCity") String birthCity,
            @Param("birthState") String birthState,
            @Param("birthCountry") String birthCountry);

    //acg by height and group by country
    @Query("SELECT p.birthCountry AS country, AVG(p.height) AS avgHeight " +
            "FROM Player p " +
            "WHERE (:minHeight IS NULL OR p.height >= :minHeight) " +
            "AND (:birthYear IS NULL OR p.birthYear <= :birthYear) " +
            "AND (:debutBefore IS NULL OR p.debut < :debutBefore) " +
            "AND (:birthCountry IS NULL OR LOWER(p.birthCountry) = LOWER(:birthCountry)) " +
            "GROUP BY p.birthCountry")
    List<AvgHeightByCountry> findAvgHeightGroupedByCountry(
            @Param("minHeight") Integer minHeight,
            @Param("birthYear") Integer birthYear,
            @Param("debutBefore") LocalDate debutBefore,
            @Param("birthCountry") String birthCountry);

    // Fetch top N players by country
    Page<Player> findByBirthCountry(String country, Pageable pageable);

    Page<Player> findByPlayerIdIn(List<String> ids, Pageable pageable);

    List<Player> findByBirthCountryAndThrowStatsOrderByBirthCountry(String country, String throwsHand);

    List<Player> findByBirthCityAndBirthCountryAndThrowStats(String birthCity, String birthCountry, String throwsHand);

    List<Player> findByPlayerIdInAndBirthCountry(List<String> playerIds, String birthCountry);

    //Cusor based pagination based on id
    List<Player> findAllByOrderByPlayerIdAsc(Pageable pageable); //for 1st request
    List<Player> findByPlayerIdGreaterThanOrderByPlayerIdAsc(String afterId, Pageable pageable);

    //cursor with no-null
    // First page (no cursor) → just exclude null birthCity and sort
    List<Player> findByBirthCityIsNotNullOrderByPlayerIdAsc(Pageable pageable);

    // Next pages → exclude null + start after cursor + sort
    List<Player> findByBirthCityIsNotNullAndPlayerIdGreaterThanOrderByPlayerIdAsc(
            String playerId,
            Pageable pageable
    );

    //cursor with id and other column sort
    @Query("""
        SELECT p FROM Player p
        WHERE (:firstName IS NULL AND :playerId IS NULL)
           OR (p.firstName > :firstName)
           OR (p.firstName = :firstName AND p.playerId > :playerId)
        ORDER BY p.firstName ASC, p.playerId ASC
    """)
    List<Player> findNextPage(
            @Param("firstName") String firstName,
            @Param("playerId") String playerId,
            Pageable pageable
    );

}
