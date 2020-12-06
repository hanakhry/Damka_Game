package Controller;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomEvents {
    static List<Point> emptyBlackCells;

    public RandomEvents(List<Point> emptyBlackCells){
        this.emptyBlackCells = emptyBlackCells;
    }
    public static List<Point> yellowEvents() {
        Point point;
        List<Point> yellowSquares = new ArrayList<>();
        for (int i = 0; i <3; i++){
            Random rand = new Random();
            point = emptyBlackCells.get(rand.nextInt(emptyBlackCells.size()));
            yellowSquares.add(point);
            emptyBlackCells.remove(point);
        }

        return yellowSquares;
    }
}