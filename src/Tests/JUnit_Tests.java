package Tests;

import Model.Board;
import org.junit.Test;
import java.awt.*;
import static org.junit.Assert.*;

public class JUnit_Tests {
    Board board = new Board();
    @Test //id=1
    public void TheMiddlePointTest(){
        Point point1 = new Point(-1,-1);///expected when x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0 || // Not in the board
                                       ///x1 > 7 || y1 > 7 || x2 > 7 || y2 > 7
                                      ///expected when x1 % 2 == y1 % 2 || x2 % 2 == y2 % 2
                                    ///expected when Math.abs(dx) != Math.abs(dy) || Math.abs(dx) != 2

        Point point2 = new Point(5,4);///expected when Valid Point

        assertEquals(point1,board.middle(1,8,2,9));
        assertEquals(point1,board.middle(1,3,3,4));
        assertEquals(point1,board.middle(1,3,2,5));
        assertEquals(point2,board.middle(4,5,6,3));

    }

    @Test //id=2
    public  void  isValidIndexTest() {

        assertTrue(board.isValidIndex(10));
        assertFalse(board.isValidIndex(34));
        assertFalse(board.isValidIndex(-1));
    }

    @Test //id=3
    public void isValidPointTest (){
        Point testpoint1 = new Point(5,9);
        Point testpoint2 = new Point(2,2);
        Point testpoint3 = new Point(2,5);

        assertFalse(board.isValidPoint(null));//Expected false when point equal null
        assertFalse(board.isValidPoint(testpoint1));//Expected fasle when x < 0 || x > 7 || y < 0 || y > 7
        assertFalse(board.isValidPoint(testpoint2));//Expected false when x % 2 == y % 2
        assertTrue( board.isValidPoint(testpoint3));//Expected true when valid point
    }

    @Test //id=4
    public void toIndexTest (){

        assertEquals(-1 ,board.toIndex(5,9));//Expected -1 when x < 0 || x > 7 || y < 0 || y > 7
        assertEquals(-1 ,board.toIndex(2,2));//Expected -1 when x % 2 == y % 2
        assertEquals(21,board.toIndex(2,5));//Expected y * 4 + x / 2 when valid point
    }

    @Test //id=5
    public void toPointTest (){

        Point point1 = new Point(0, 3);
        Point point2 = new Point(-1, -1);

        assertEquals(point1 ,board.toPoint(12));
        assertEquals(point2 ,board.toPoint(34));

    }

}

