package View;

import Model.Game;
import Model.SysData;
import Utils.Constants;

import javax.swing.*;
import java.util.*;

public class HamkaGameHistory extends JFrame
{

    ArrayList<Integer>finalTiles=new ArrayList<>();
    List<String> al = new ArrayList<String>();

    public ArrayList<Integer> getFinalTiles() {

        return finalTiles;
    }

    public List<String> getAl() {
        return al;
    }

    public HamkaGameHistory() {
        ArrayList<Integer> tiles1;
        Game game;

        SysData sysData = new SysData();
        sysData.importGamesFromJSON("JSON/games.JSON");
        tiles1=sysData.getTiles();
        HashMap<Integer, Game> hm = sysData.games();
        for(Map.Entry<Integer, Game> g : hm.entrySet()) {
            game = g.getValue();

            //convert json to array with value from Constants
            String a = String.valueOf(tiles1);
            String numTiles = a;
            String str[] = numTiles.split("[\\, \\]\\[]");

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
            String finalTilesS = String.valueOf(finalTiles);

        }}





}
