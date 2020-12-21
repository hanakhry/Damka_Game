package View;

import Model.SysData;
import Utils.Constants;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HamkaGameHistory extends JFrame
{

    ArrayList<Integer>finalTiles=new ArrayList<>();
    List<String> al = new ArrayList<String>();

    public Boolean getTurn() {
        return turn;
    }

    Boolean turn;
    public ArrayList<Integer> getFinalTiles() {

        return finalTiles;
    }

    public List<String> getAl() {
        return al;
    }

    public HamkaGameHistory() {
        List tiles1;
        String tiles2[] = {null};

        SysData sysData = new SysData();
        turn=sysData.importGamesFromTxtFile("TEXT/testLoad.txt");
        tiles1=sysData.getTiles();

            //convert json to array with value from Constants
            String a = String.valueOf(tiles1);
            String numTiles = a;
            String str[] = numTiles.split("[\\, \\]\\[]");
            //this parameter save the first value in the array(id)
            al = Arrays.asList(str);
            for (String s : al) {

                //if 0-> Contstans.black
                if(s.equals("0"))
                    finalTiles.add(Constants.EMPTY);
                if (s.equals(" 2") || s.equals("2") || s.equals("2 "))
                    finalTiles.add(Constants.BLACK_SOLDIER);
                if (s.equals("1") || s.equals(" 1") || s.equals("1 "))
                    finalTiles.add(Constants.WHITE_SOLDIER);
                if (s.equals("11") || s.equals(" 11") || s.equals("11 "))
                    finalTiles.add(Constants.WHITE_QUEEN);
                if (s.equals(" 22") || s.equals("22") || s.equals("22 "))
                    finalTiles.add(Constants.BLACK_QUEEN);
            }
         }





}
