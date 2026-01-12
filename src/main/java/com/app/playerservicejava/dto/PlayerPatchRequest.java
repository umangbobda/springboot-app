package com.app.playerservicejava.dto;

import lombok.Data;

@Data
public class PlayerPatchRequest {

    private String firstName;
    private String lastName;
    private String birthCountry;
    private String birthCity;
    private String throwsHand;
    private String batsHand;
    private Integer score;

    // getters/setters
}

