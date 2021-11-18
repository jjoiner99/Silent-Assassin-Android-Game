package com.example.cs440project.leaderboardEntry;

public class LeaderboardEntry{
    private String username;
    private int score;
    private int role;

    public LeaderboardEntry(String username, int score, int role) {
        this.username = username;
        this.score = score;
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public int getRole() {
        return role;
    }

}
