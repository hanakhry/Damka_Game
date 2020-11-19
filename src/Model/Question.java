package Model;
import Utils.Level;
import Utils.Constants;

import java.util.ArrayList;


public class Question<String> {
    private int numOfQuestion;
    private String question;
    private ArrayList<String> answers;
    private int IndexOfCorrectAnswer;
    private Level level;
    private int pointAdded;
    private int pointDecreaced;


    public Question(String question, ArrayList<String> answers, int indexOfCorrectAnswer, Level level) {
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

    }

    public int getPointDecreaced() {
        return pointDecreaced;
    }

    public void setPointDecreaced(int pointDecreaced) {
        this.pointDecreaced = pointDecreaced;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public int getIndexOfCorrectAnswer() {
        return IndexOfCorrectAnswer;
    }

    public void setIndexOfCorrectAnswer(int indexOfCorrectAnswer) {
        IndexOfCorrectAnswer = indexOfCorrectAnswer;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getPointAdded() {
        return pointAdded;
    }

    public void setPointAdded(int pointAdded) {
        this.pointAdded = pointAdded;
    }

    public int getNumOfQuestion() { return numOfQuestion; }

    public void setNumOfQuestion(int numOfQuestion) { this.numOfQuestion = numOfQuestion; }

    @Override
    public java.lang.String toString() {
        return("Question [question=" + question + ", answers=" + answers + ", IndexOfCorrectAnswer="
                        + IndexOfCorrectAnswer + ", level=" + level + "]");
    }


}
