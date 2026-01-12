package com.app.playerservicejava.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlayerSearchRequestSort {
    private List<String> countries;
    private Integer minAge;
    private List<String> handedness;
    private SortOption sort;

    @Data
    public static class SortOption {
        private String field;
        private String order; // e.g., "asc" or "desc"
    }
}

/*
{
  "countries": ["India", "USA"],
  "minAge": 30,
  "handedness": ["Right", "Left"],
  "sort": { "field": "score", "order": "desc" }
}

 */
