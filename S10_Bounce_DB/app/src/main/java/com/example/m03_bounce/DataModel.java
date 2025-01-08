package com.example.m03_bounce;

public class DataModel {

    // Data Model for a Ball, name doesn't really have a use, but it was one of the requirements, so I added it
    private long id;
    private String name;
    private float x, y, dx, dy;
    private int color;

    // Default constructor, default values
    public DataModel() {
        id = 0;
        name = "defaultName";
        this.setModelPosVel(0.0f, 0.0f, 0.0f, 0.0f);
    }

    // Use setModelPosVel for slight efficiency
    public DataModel(long id, String name, Float x, Float y, Float dx, Float dy, int color) {
        this.id = id;
        this.name = name;
        this.setModelPosVel(x, y, dx, dy);
        this.color = color;
    }

    // toString, getters and setters, typical stuff
    @Override
    public String toString() {
        return "DataModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", x='" + x + '\'' +
                ", y=" + y +
                ", dx='" + dx + '\'' +
                ", dy=" + dy +
                ", color=" + color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setModelPosVel(Float x, Float y, Float dx, Float dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public float getModelX() {
        return this.x;
    }

    public float getModelY() {
        return this.y;
    }

    public float getModelDX() {
        return this.dx;
    }

    public float getModelDY() {
        return this.dx;
    }

    public void setModelName(String name) {
        this.name = name;
    }

    public String getModelName() {
        return this.name;
    }

    public void setModelColor(int color) {
        this.color = color;
    }

    public int getModelColor() {
        return this.color;
    }

}
