package Model;

import java.util.ArrayList;
import java.util.HashMap;

import Utils.Level;

public final class SysData {
    private static SysData instance;
    //HashMap key: question difficulty level, value: all questions of such difficulty
    private final HashMap<Level, ArrayList<Question>> questions = new HashMap();



    private SysData(){}

    public static SysData getInstance() {
        if (instance == null) {
            //instance = ; TODO
        }

        return instance;
    }

    public HashMap<Level, ArrayList<Question>> getQuestions() { return this.questions; }

}
