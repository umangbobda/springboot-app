package com.app.playerservicejava.dto;

import com.app.playerservicejava.model.Player;
import lombok.Data;

import java.util.List;

@Data
public class PlayerCursorResponse {

    private List<Player> data;
    private String nextCursor;
    private int count; // number of records returned

    public PlayerCursorResponse(List<Player> data, String nextCursor) {
        this.data = data;
        this.nextCursor = nextCursor;
        this.count = data.size();
    }

    // getters and setters
}

