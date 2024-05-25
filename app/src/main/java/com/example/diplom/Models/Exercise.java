package com.example.diplom.Models;

public class Exercise {
    private String name;
    private String description;
    private String demonstration;
    private String setsReps;
    private int caloriesPerRep;

    public Exercise() {
        // Пустой конструктор требуется для Firebase
    }

    public Exercise(String name, String description, String demonstration, String setsReps) {
        this.name = name;
        this.description = description;
        this.demonstration = demonstration;
        this.setsReps = setsReps;
    }

    // Геттеры и сеттеры для полей класса
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDemonstration() {
        return demonstration;
    }

    public void setDemonstration(String demonstration) {
        this.demonstration = demonstration;
    }

    public String getSetsReps() {
        return setsReps;
    }

    public void setSetsReps(String setsReps) {
        this.setsReps = setsReps;
    }

    public int getCaloriesPerRep() {
        return caloriesPerRep;
    }

    public void setCaloriesPerRep(int caloriesPerRep) {
        this.caloriesPerRep = caloriesPerRep;
    }
}
