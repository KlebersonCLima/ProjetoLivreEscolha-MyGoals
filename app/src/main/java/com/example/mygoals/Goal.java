package com.example.mygoals;

public class Goal {
    private int id;
    private String title;
    private String dateCreated;
    private boolean isCompleted;

    // Construtores
    public Goal() {
    }

    public Goal(String title, String dateCreated, boolean isCompleted) {
        this.title = title;
        this.dateCreated = dateCreated;
        this.isCompleted = isCompleted;
    }

    public Goal(int id, String title, String dateCreated, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
        this.isCompleted = isCompleted;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }
}