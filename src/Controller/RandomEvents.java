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
    static boolean flag1=false;
    static boolean flag2=false;
    public RandomEvents(List<Point> emptyBlackCells){
        this.emptyBlackCells = emptyBlackCells;
    }

    /**
     * returns 3 random points
     * points must be empty and black
     */
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

    /**
     * checks if a checker can move at all
     * @param p
     * @param emptyBlackCells
     * @param g
     * @return
     */
    private static List<Point> allValidMoves(Point p, List<Point> emptyBlackCells, Game g){
        List<Point> availableCells = new ArrayList<>();
        for(Point point : emptyBlackCells) {
            if(MoveLogic.isValidMove(g, Board.toIndex(p), Board.toIndex(point))){
                availableCells.add(point);
            }
        }
        return availableCells;
    }

    /**
     * list of all troops
     * @param g
     * @param turn
     * @return
     */
    private static  List<Point> getTroops(Game g, boolean turn){
        int soldierColor;
        int queenColor;
        soldierColor = turn ? Constants.WHITE_SOLDIER : Constants.BLACK_SOLDIER;
        queenColor = turn ? Constants.WHITE_QUEEN : Constants.BLACK_QUEEN;

        List<Point> troops = new ArrayList<>();
        troops.addAll(g.getBoard().find(soldierColor));
        troops.addAll(g.getBoard().find(queenColor));
        return troops;
    }

    public static List<Point> orangeEvents(Game g, List<Point> emptyBlackCells){
        List<Point> validMoves = new ArrayList<>();
        List<Point> troops = new ArrayList<>();

        troops.addAll(getTroops(g, !g.isP1Turn()));
        for(int i = 0; i < troops.size(); i++)
            validMoves.addAll(allValidMoves(troops.get(i), emptyBlackCells, g));
        return validMoves;
    }

    //triggers green square
    public static Point greenEvents(Game g, List<Point> emptyBlackCells, Point redPoint){
        //green can't be red
        emptyBlackCells.remove(redPoint);
        List<Point> validMoves = new ArrayList<>();
        List<Point> troops = new ArrayList<>();
        troops.addAll(getTroops(g, !g.isP1Turn()));

        for(int i = 0; i < troops.size(); i++)
            validMoves.addAll(allValidMoves(troops.get(i), emptyBlackCells, g));
        return randomRedPoint(validMoves);
    }

    //triggers red square
    public static Point redEvents(Game g, boolean turn, List<Point> emptyBlackCells){
        List<Point> validMoves = new ArrayList<>();
        List<Point> troops = new ArrayList<>();


        troops.addAll(getTroops(g, turn));

        //the other soldiers
        List<Point> secondaryTroops = new ArrayList<>();
        secondaryTroops.addAll(getTroops(g, !turn));

        for(Point p : troops){
           if(!MoveLogic.isSafe(g.getBoard(), p)) {
               if(turn) flag1=true;

               if(!turn) flag2=true;

               //current player is eligible to eat
               System.out.println("not safe");
               return null;
           }


        }
        if(flag1)g.getBlack1Player().setpScore(g.getBlack1Player().getpScore()+100);
        if(flag2)g.getWhite2Player().setpScore(g.getWhite2Player().getpScore()+100);
        flag1=false;
        flag2=false;


        for(int i = 0; i < secondaryTroops.size(); i++)
            validMoves.addAll(allValidMoves(secondaryTroops.get(i), emptyBlackCells, g));
        return randomRedPoint(validMoves);
    }

    /**
     * returns a random point that a soldier can move to
     * @param p
     * @return
     */
    private static Point randomRedPoint(List<Point> p){
        if(!p.isEmpty()) {
            int rnd = new Random().nextInt(p.size());
            return p.get(rnd);
        }
        return null;
    }
}