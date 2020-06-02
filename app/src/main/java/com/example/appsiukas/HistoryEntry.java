package com.example.appsiukas;

import java.util.Date;

/**
 * Created by super on 10/6/2016.
 */
public class HistoryEntry {

    private int id;
    private String word;
    private String lifesRemaining;
    private String gameDate;

    public HistoryEntry() {
    }

    public HistoryEntry(String word, String lifesRemaining, String gameDate) {
        this.word = word;
        this.lifesRemaining = lifesRemaining;
        this.gameDate = gameDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLifesRemaining() {
        return lifesRemaining;
    }

    public void setLifesRemaining(String lifesRemaining) {
        this.lifesRemaining = lifesRemaining;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }
}
