package com.app.playerservicejava.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class PlayerSearchRequest {

    @NonNull
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