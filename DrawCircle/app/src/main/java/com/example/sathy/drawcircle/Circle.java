package com.example.sathy.drawcircle;

/**
 * Created by sathy on 23-Feb-17.
 */

class Circle {

    Circle(Float x, Float y, Float radius, String color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }
    //x and y are the center coordinates of the circle
    private Float x=0f;
    private Float y=0f;
    private Float radius=0f;
    private String color = "BLACK"; //Default color is BLACK
    private Integer canMoveFlag = 0;
    private Float XVelocity = 0f;
    private Float YVelocity = 0f;

    Float getX() {
        return x;
    }

    void setX(Float x) {
        this.x = x;
    }

    Float getY() {
        return y;
    }

    void setY(Float y) {
        this.y = y;
    }

    Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    Integer getCanMoveFlag() {
        return canMoveFlag;
    }

    void setCanMoveFlag(Integer canMoveFlag) {
        this.canMoveFlag = canMoveFlag;
    }

    Float getXVelocity() {
        return XVelocity;
    }

    void setXVelocity(Float XVelocity) {
        this.XVelocity = XVelocity;
    }

    Float getYVelocity() {
        return YVelocity;
    }

    void setYVelocity(Float YVelocity) {
        this.YVelocity = YVelocity;
    }
}
