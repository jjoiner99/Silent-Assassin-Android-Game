package com.example.cs440project.Quests;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class Quest {
    private Integer index;
    private String questId;
    private String dateCreated;
    private QuestType questType;
    private String title, description;
    private int totalPoints;

    // Constructor
    public Quest(Integer index, String questId,QuestType questType, String title, String description, int totalPoints){
        // Current Date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        // Set all parameters
        setIndex(index);
        setQuestId(questId); // uuid
        setQuestType(questType);
        setDateCreated(dtf.format(now));
        setTitle(title);
        setDescription(description);
        setTotalPoints(totalPoints);
    }
    // Setters

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setQuestType(QuestType questType) {
        this.questType = questType;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
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

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    // Getters

    public Integer getIndex() {
        return index;
    }

    public String getDateCreated() {
        return dateCreated;
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

    public String getQuestId() {
        return questId;
    }

    private static HashMap<String, Quest> AllQuests = new HashMap<>();

    // Populate the list
    public static void initAllQuests(){
        // Init 20 Explorer
        for (Integer index = 0; index < 20; index++) {
            String uuid = UUID.randomUUID().toString();
            Quest singleQuest = new Quest(index+1 ,uuid, QuestType.EXPLORER,"Title", "Description", 200);
            AllQuests.put(uuid, singleQuest);
        }

        // TODO init 20 Hunter Quests where index starts at 20

        // Reference to Quests table
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference questRef = database.getReference("Quests");

        // Write the object to the database
        questRef.setValue(AllQuests);
    }

    public void deleteQuest() {

    }
}
