package com.app.playerservicejava.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class PlayerSearchRequest {

    @NonNull
    @NotNull
    private String birthCountry;
    private String birthCity;
    @NonNull
    private String throwsHand;
}
/*
{
  "birthCountry": "India",
  "birthCity": "Mumbai",
  "throwsHand": "R"
}

 */