package Controller;

import Model.Board;
import Model.Game;
import Model.MoveLogic;
import Utils.Constants;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomEvents {
    static List<Point> emptyBlackCells;

    public RandomEvents(List<Point> emptyBlackCells) {
        RandomEvents.emptyBlackCells = emptyBlackCells;
    }

    /**
     * returns 3 random points
     * points must be empty and black
     */
    public List<Point> yellowEvents() {
        Point point;
        List<Point> yellowSquares = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Random rand = new Random();
            point = emptyBlackCells.get(rand.nextInt(emptyBlackCells.size()));
            yellowSquares.add(point);
            emptyBlackCells.remove(point);
        }

        return yellowSquares;
    }

    /**
     * checks if a checker can move at all
     */
    private static List<Point> allValidMoves(Point p, List<Point> emptyBlackCells, Game g) {
        List<Point> availableCells = new ArrayList<>();
        for (Point point : emptyBlackCells) {
            if (MoveLogic.isValidMove(g, Board.toIndex(p), Board.toIndex(point))) {
                availableCells.add(point);
            }
        }
        return availableCells;
    }

    /**
     * list of all troops
     */
    private static List<Point> getTroops(Game g, boolean turn) {
        int soldierColor;
        int queenColor;
        soldierColor = turn ? Constants.WHITE_SOLDIER : Constants.BLACK_SOLDIER;
        queenColor = turn ? Constants.WHITE_QUEEN : Constants.BLACK_QUEEN;

        List<Point> troops = new ArrayList<>();
        troops.addAll(g.getBoard().find(soldierColor));
        troops.addAll(g.getBoard().find(queenColor));
        return troops;
    }

    public static List<Point> orangeEvents(Game g, List<Point> emptyBlackCells) {
        List<Point> validMoves = new ArrayList<>();
        List<Point> troops = new ArrayList<>();

        troops.addAll(getTroops(g, !g.isP1Turn()));
        for (int i = 0; i < troops.size(); i++)
            validMoves.addAll(allValidMoves(troops.get(i), emptyBlackCells, g));
        return validMoves;
    }

    //triggers green square
    public static Point greenEvents(Game g, List<Point> emptyBlackCells, Point redPoint) {
        //green can't be red
        emptyBlackCells.remove(redPoint);
        List<Point> validMoves = new ArrayList<>();
        List<Point> troops = new ArrayList<>();
        troops.addAll(getTroops(g, !g.isP1Turn()));

        for (int i = 0; i < troops.size(); i++)
            validMoves.addAll(allValidMoves(troops.get(i), emptyBlackCells, g));
        return randomPoint(validMoves);
    }

    //triggers red square
    public Point redEvents(Game g, boolean turn, List<Point> yellowPoints) {
        List<Point> validMoves = new ArrayList<>();
        List<Point> troops = new ArrayList<>();


        troops.addAll(getTroops(g, turn));

        //the other soldiers
        List<Point> secondaryTroops = new ArrayList<>();
        secondaryTroops.addAll(getTroops(g, !turn));

        for (Point p : troops) {
            if (!MoveLogic.isSafe(g.getBoard(), p)) {

                //current player is eligible to eat

                return null;
            }


        }
        for (int i = 0; i < 3; i++) {
            emptyBlackCells.remove(yellowPoints.get(i));
        }
        for (int i = 0; i < secondaryTroops.size(); i++)
            validMoves.addAll(allValidMoves(secondaryTroops.get(i), emptyBlackCells, g));
        return randomPoint(validMoves);
    }

    /**
     * returns a random point that a soldier can move to
     */
    private static Point randomPoint(List<Point> p) {
        if (!p.isEmpty()) {
            int rnd = new Random().nextInt(p.size());
            return p.get(rnd);
        }
        return null;
    }

    public static Point blueEvents(Game g, List<Point> available) {
        int soldierColor = g.isP1Turn() ? Constants.BLACK_SOLDIER : Constants.WHITE_SOLDIER;
        int queenColor = g.isP1Turn() ? Constants.BLACK_QUEEN : Constants.WHITE_QUEEN;
        Point blue = new Point(35, 35);
        List<Point> soldiers = g.getBoard().find(soldierColor);
        List<Point> queen = g.getBoard().find(queenColor);
        if (soldiers.size() == 2 && queen.size() == 1) {
            blue = randomPoint(available);
        }
        return blue;
    }
}