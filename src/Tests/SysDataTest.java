package Tests;

import Model.Question;
import Model.SysData;
import Utils.Level;

import java.io.IOException;
import java.util.ArrayList;

public class SysDataTest {

        /** RUN FOR TESTS ONLY !!! */
        /** To RUN the game , run MainHamka in Controller ! */

        public static void main(String[] args) throws IOException {
            /**
             * ~TEST add/edit/delete a question from JSON file
             * can only edit/delete a question that already exists in the file before running the application
             */

            SysData sysData = new SysData();
            //fetch data from questions.JSON
            sysData.importQuestionsFromJSON("JSON/questions.JSON");

            //create an question object to ted ~~TEST~~
            ArrayList<String> answers = new ArrayList<String>();
            answers.add("1");
            answers.add("2");
            answers.add("3");
            answers.add("4");

            Question question = new Question("question3", answers, 3, Level.EASY, "hedgehog");

            //add a question to JSON
          //  sysData.addQuestionToJSON("JSON/questions.JSON", question);

            //delete a JSON object
            //sysData.deleteQeustionFromJSON("JSON/questions.JSON", question);

            //new question to test edit
            Question newQuestion = new Question("question4", answers, 1, Level.MEDIUM, "hedgehog");
            //edit the question
          //  sysData.editQuestion("JSON/questions.JSON", question, newQuestion);
           // HashMap<Level, ArrayList<Question>> L = sysData.getQuestions();





        }
    }

