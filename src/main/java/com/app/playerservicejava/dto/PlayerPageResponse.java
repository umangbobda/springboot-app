package com.app.playerservicejava.dto;

import com.app.playerservicejava.model.Player;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter @Setter
public class PlayerPageResponse {
    private List<Player> players;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    // constructor
    public PlayerPageResponse(Page<Player> pageData) {
        this.players = pageData.getContent();
        this.page = pageData.getNumber();
        this.size = pageData.getSize();
        this.totalElements = pageData.getTotalElements();
        this.totalPages = pageData.getTotalPages();
    }

    // getters and setters (or use Lombok @Data)
}

