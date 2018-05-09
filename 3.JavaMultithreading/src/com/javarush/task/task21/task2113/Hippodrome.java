package com.javarush.task.task21.task2113;

import java.util.ArrayList;
import java.util.List;

public class Hippodrome {
    public static Hippodrome game;

    private List<Horse> horses;

    public Hippodrome(List<Horse> horses) {
        this.horses = horses;
    }

    public List<Horse> getHorses() {
        return horses;
    }

    public void run() {
        for (int i = 0; i < 100; i++) {
            move();
            print();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void print() {
        for (Horse horse : horses) {
            horse.print();
        }
        System.out.println("\n\n\n\n\n\n\n\n\n");
    }

    public void move() {
        for (Horse horse : horses) {
            horse.move();
        }
    }

    public Horse getWinner() {
        double maxDistance = 0.0;
        for (Horse horse : horses) {
            maxDistance = maxDistance < horse.getDistance() ? horse.getDistance() : maxDistance;
        }
        for (Horse horse : horses) {
            if (horse.getDistance() == maxDistance)
                return horse;
        }
        return null;
    }

    public void printWinner() {
        System.out.println("Winner is " + getWinner().getName() + "!");
    }

    public static void main(String[] args) {
        List<Horse> horses = new ArrayList<>();
        horses.add(new Horse("IA", 3.0, 0.0));
        horses.add(new Horse("Tenegriv", 3.0, 0.0));
        horses.add(new Horse("Rezviy", 3.0, 0.0));

        game = new Hippodrome(horses);
        game.run();
        game.printWinner();
    }
}
