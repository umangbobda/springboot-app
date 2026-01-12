package com.app.playerservicejava.dto;

import com.app.playerservicejava.model.Player;
import lombok.Data;

import java.util.List;

@Data
public class PlayerResponse {

    private String playerId;
    private String firstName;
    private String lastName;
    private String birthCountry;
    private String birthCity;
    private String throwsHand;
    private String batsHand;

    public static PlayerResponse from(Player p) {
        PlayerResponse r = new PlayerResponse();
        r.setPlayerId(p.getPlayerId());
        r.setFirstName(p.getFirstName());
        r.setLastName(p.getLastName());
        r.setBirthCountry(p.getBirthCountry());
        r.setBirthCity(p.getBirthCity());
        r.setThrowsHand(p.getThrowStats());
        r.setBatsHand(p.getBats());
        return r;
    }
}

