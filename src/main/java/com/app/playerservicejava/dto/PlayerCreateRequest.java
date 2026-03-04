package com.app.playerservicejava.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class PlayerCreateRequest {

    @NonNull
    @NotNull
    private String playerId;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NotNull
    private String birthCountry;
    private String birthCity;
    private String throwsHand;
    private String batsHand;
    private Integer score;

    // getters/setters
}
