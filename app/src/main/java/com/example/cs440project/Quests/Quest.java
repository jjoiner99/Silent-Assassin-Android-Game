package com.example.cs440project.Quests;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Quest {
    private String currentDate;
    private QuestType questType;
    private String title, description;
    private int totalPoints;

    // Constructor
    public Quest(QuestType questType, String title, String description, int totalPoints){
        // Current Date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        // Set all parameters
        setQuestType(questType);
        setCurrentDate(dtf.format(now));
        setTitle(title);
        setDescription(description);
        setTotalPoints(totalPoints);
    }
    // Setters
    public void setQuestType(QuestType questType) {
        this.questType = questType;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public QuestType getQuestType() {
        return questType;
    }

    // Getters
    public void createQuest() {

    }

    public void deleteQuest() {

    }
}
