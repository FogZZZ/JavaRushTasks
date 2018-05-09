package com.javarush.task.task29.task2909.human;

public class Soldier extends Human {
    protected int course;

    public Soldier(String name, int age) {
        super(name, age);
    }

    public void live() {
        fight();
    }

    public void fight() {
    }

    public int getCourse() {
        return course;
    }
}
