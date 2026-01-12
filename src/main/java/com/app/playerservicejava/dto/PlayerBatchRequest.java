package com.app.playerservicejava.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
public class PlayerBatchRequest {
    @NonNull
    private List<String> playersIds;

    @NonNull
    private String birthCountry;
}
/*

{
  "playersIds": ["P001", "P002", "P003"],
  "birthCountry": "India"
}


 */