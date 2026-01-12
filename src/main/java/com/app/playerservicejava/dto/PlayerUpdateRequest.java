package com.app.playerservicejava.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class PlayerUpdateRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String birthCountry;
    private String birthCity;
    private String throwsHand;
    private String batsHand;
    private Integer score;

    // getters/setters
}

