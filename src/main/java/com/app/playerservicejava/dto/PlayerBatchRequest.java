package com.app.playerservicejava.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
public class PlayerBatchRequest {
    @NonNull
    @NotBlank
    private List<String> playersIds;

    @NonNull
    @NotBlank
    private String birthCountry;
}
/*

{
  "playersIds": ["P001", "P002", "P003"],
  "birthCountry": "India"
}


 */