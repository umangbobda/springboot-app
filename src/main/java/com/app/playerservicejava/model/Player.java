package com.app.playerservicejava.model;

import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name="players")
@ToString
public class Player {

    @Id
    @Column(name = "PLAYERID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String playerId;

    @Column(name = "BIRTHYEAR")
    private Integer birthYear;

    @Column(name = "BIRTHMONTH")
    private Integer birthMonth;

    @Column(name = "BIRTHDAY")
    private Integer birthDay;

    @Column(name = "BIRTHCOUNTRY")
    private String birthCountry;

    @Column(name = "BIRTHSTATE")
    private String birthState;

    @Column(name = "BIRTHCITY")
    private String birthCity;

    @Column(name = "DEATHYEAR")
    private Integer deathYear;

    @Column(name = "DEATHMONTH")
    private Integer deathMonth;

    @Column(name = "DEATHDAY")
    private Integer deathDay;

    @Column(name = "DEATHCOUNTRY")
    private String deathCountry;

    @Column(name = "DEATHSTATE")
    private String deathState;

    @Column(name = "DEATHCITY")
    private String deathCity;

    @Column(name = "NAMEFIRST")
    private String firstName;

    @Column(name = "NAMELAST")
    private String lastName;

    @Column(name = "NAMEGIVEN")
    private String givenName;

    @Column(name = "WEIGHT")
    private Integer weight;

    @Column(name = "HEIGHT")
    private Integer height;

    @Column(name = "BATS")
    private String bats;

    @Column(name = "THROWS")
    private String throwStats;

    @Column(name = "DEBUT")
    private LocalDate debut;

    @Column(name = "FINALGAME")
    private LocalDate finalGame;

    @Column(name = "RETROID")
    private String retroId;

    @Column(name = "BBREFID")
    private String bbrefId;

    public Player() {}

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(Integer birthMonth) {
        this.birthMonth = birthMonth;
    }

    public Integer getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Integer birthDay) {
        this.birthDay = birthDay;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public String getBirthState() {
        return birthState;
    }

    public void setBirthState(String birthState) {
        this.birthState = birthState;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public Integer getDeathMonth() {
        return deathMonth;
    }

    public void setDeathMonth(Integer deathMonth) {
        this.deathMonth = deathMonth;
    }

    public Integer getDeathDay() {
        return deathDay;
    }

    public void setDeathDay(Integer deathDay) {
        this.deathDay = deathDay;
    }

    public String getDeathCountry() {
        return deathCountry;
    }

    public void setDeathCountry(String deathCountry) {
        this.deathCountry = deathCountry;
    }

    public String getDeathState() {
        return deathState;
    }

    public void setDeathState(String deathState) {
        this.deathState = deathState;
    }

    public String getDeathCity() {
        return deathCity;
    }

    public void setDeathCity(String deathCity) {
        this.deathCity = deathCity;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getBats() {
        return bats;
    }

    public void setBats(String bats) {
        this.bats = bats;
    }

    public String getThrowStats() {
        return throwStats;
    }

    public void setThrowStats(String throwStats) {
        this.throwStats = throwStats;
    }

    public LocalDate getDebut() {
        return debut;
    }

    public void setDebut(LocalDate debut) {
        this.debut = debut;
    }

    public LocalDate getFinalGame() {
        return finalGame;
    }

    public void setFinalGame(LocalDate finalGame) {
        this.finalGame = finalGame;
    }

    public String getRetroId() {
        return retroId;
    }

    public void setRetroId(String retroId) {
        this.retroId = retroId;
    }

    public String getBbrefId() {
        return bbrefId;
    }

    public void setBbrefId(String bbrefId) {
        this.bbrefId = bbrefId;
    }
}
