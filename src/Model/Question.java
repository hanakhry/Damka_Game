package Model;

import Utils.Constants;
import Utils.Level;

import java.util.ArrayList;


public class Question<String> {
    private int numOfQuestion;
    private String question;
    private ArrayList<String> answers;
    private int IndexOfCorrectAnswer;
    private Level level;
    private String team;
    private int pointAdded;
    private int pointDecreaced;


    public Question(String question, ArrayList<String> answers, int indexOfCorrectAnswer, Level level, String team) {
        super();
        this.question = question;
        this.answers = answers;
        IndexOfCorrectAnswer = indexOfCorrectAnswer;
        this.level = level;
        switch (level) {
            case EASY:
                pointAdded = Constants.pointsAddedEasyQ;
                pointDecreaced = Constants.pointsDecreacedEasyQ;
                break;
            case MEDIUM:
                pointAdded = Constants.pointsAddedMediumQ;
                pointDecreaced = Constants.pointsDecreacedMediumQ;
                break;
            case HARD:
                pointAdded = Constants.pointsAddedHardQ;
                pointDecreaced = Constants.pointsDecreacedHardQ;
                break;
        }
        this.team = team;

    }

    public String getQuestion() {
        return question;
    }
    public ArrayList<String> getAnswers() {
        return answers;
    }
    public int getIndexOfCorrectAnswer() {
        return IndexOfCorrectAnswer;
    }
    public Level getLevel() {
        return level;
    }
    public String getTeam() { return team; }
    public void  setTeam(String team) { this.team = team; }

    @Override
    public java.lang.String toString() {
        return("Question [question=" + question + ", answers=" + answers + ", IndexOfCorrectAnswer="
                + IndexOfCorrectAnswer + ", level=" + level + ", team=" + team + "]");
    }


}
