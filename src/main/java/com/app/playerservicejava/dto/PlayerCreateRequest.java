package com.app.playerservicejava.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.lang.NonNull;

@Data
public class PlayerCreateRequest {

    @NonNull
    private String playerId;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    private String birthCountry;
    private String birthCity;
    private String throwsHand;
    private String batsHand;
    private Integer score;

    // getters/setters
}
