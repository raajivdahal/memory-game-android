package com.example.memorygame;

public class Card {
    private int id;
    private boolean isVisible;
    private boolean isMatched;

    public Card(int id) {
        this.id = id;
        this.isVisible = true;
        this.isMatched = false;
    }

    public int getId() {
        return id;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }
}
