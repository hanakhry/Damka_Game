package Tests;

import Model.SysData;

import java.util.Map;

public class sortHashMapTest {
    public static void main(String[] args)
    {

        SysData sysdata=new SysData();
        sysdata.readLeaderFromFile("JSON/leaderhistory.JSON");
        for (Map.Entry<String, Integer> en : sysdata.sortByValue(sysdata.leaderboard).entrySet()) {
            System.out.println("Key = " + en.getKey() +
                    ", Value = " + en.getValue());
        }
    }
}
