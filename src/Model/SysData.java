package Model;

import Utils.Constants;
import Utils.Level;
import View.HamkaQuestion;
import org.json.simple.*;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



public final class SysData {
    private static SysData instance;
    //HashMap key: question difficulty level, value: all questions of such difficulty
    public HashMap<Level, ArrayList<Question>> questions = new HashMap();
    private final HashMap<Integer, Game> games = new HashMap();
    ArrayList<Integer> tiles;
    public ArrayList<Integer> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<Integer> tiles) {
        this.tiles = tiles;
    }



    public SysData(){

    }
    //singletone
    public static SysData getInstance() {
        if (instance == null) {
            instance = new SysData();
            return instance;
        } else
            return instance;
    }




    public HashMap<Level, ArrayList<Question>> getQuestions() {
        return this.questions;
    }

    public HashMap<Integer, Game> games() { return this.games; }

    public void importFile() {
        try {
            File myObj = new File("GamesHistory.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void importQuestionsFromJSON(String path) {
        questions = new HashMap();
        try (FileReader reader = new FileReader(new File(path))) {
            JsonObject doc = (JsonObject) Jsoner.deserialize(reader);
            JsonArray questionObj = (JsonArray) doc.get("questions");
            Iterator<Object> iterator = questionObj.iterator();
            while (iterator.hasNext()) {
                JsonObject obj = (JsonObject) iterator.next();
                String question = (String) obj.get("question");
                ArrayList<String> answers = (ArrayList<String>) obj.get("answers");
                int indexOfCorrectAnswer = Integer.parseInt((String) obj.get("correct_ans"));
                int levelValue = Integer.parseInt((String) obj.get("level"));
                Level level;
                if( levelValue == 1)
                    level = Level.EASY;
                else if( levelValue == 2 )
                    level = Level.MEDIUM;
                else
                    level = Level.HARD;
                String team = (String) obj.get("team");
                Question questionToAdd = new Question(question,  answers, indexOfCorrectAnswer, level, team);
                if(questions.containsKey(level)){
                    ArrayList<Question> tempList =  questions.get(level);
                    tempList.add(questionToAdd);
                }
                else{
                    ArrayList<Question> tempList = new ArrayList<>();
                    tempList.add(questionToAdd);
                    questions.put(level, tempList);
                }
            }

        } catch (IOException | DeserializationException e) {
            e.printStackTrace();
        }
    }

    private int writeToFile(String path, JsonObject doc){
        //writing to file
        File file = new File(path);
        file.getParentFile().mkdir();

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(Jsoner.prettyPrint(doc.toJson()));
            writer.flush();
       //     System.out.println("Changes were executed successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    //create a JSON object from the handled question
    private JsonObject createJSONObject(Question question){
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("question", question.getQuestion());
        jsonObject.put("answers", question.getAnswers());
        jsonObject.put("correct_ans", String.valueOf((question.getIndexOfCorrectAnswer())));
        jsonObject.put("level", question.getLevel().castLevel());
        jsonObject.put("team", question.getTeam());
        return jsonObject;
    }

    private int prepareToWriteToFile(ArrayList<Question> appendList, String path){
        try {
            //new array to hold the questions
            JsonArray questions = new JsonArray();
            //the question to be added
            JsonObject jsonObject = new JsonObject();

            for(Question q : appendList){
                questions.add(createJSONObject(q));
            }

            JsonObject doc = new JsonObject();
            doc.put("questions", questions);
            writeToFile(path, doc);

        } catch ( NullPointerException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void addQuestionToJSON(String path, Question question) throws IOException {
        //array that holds all of the existing questions
        ArrayList<Question> appendList = new ArrayList<Question>();
        if(questions.isEmpty()){
            importQuestionsFromJSON("JSON/questions.JSON");
        }
        for (Map.Entry<Level, ArrayList<Question>> entry : questions.entrySet()) {
            for(Question q: entry.getValue()){
                appendList.add(q);
            }
        }

        try {
            //new array to hold the questions
            JsonArray questions = new JsonArray();

            //add the question to array
            questions.add(createJSONObject(question));

            //add already existing objects back into the file
            for(Question q : appendList){
                questions.add(createJSONObject(q));
            }

            JsonObject doc = new JsonObject();
            doc.put("questions", questions);

            writeToFile(path, doc);
        } catch ( NullPointerException e) {
            e.printStackTrace();
        }

    }


    public int deleteQuestionFromJSON(String path, Question question){
        boolean flag = false;
        //array that holds all of the existing questions
        ArrayList<Question> appendList = new ArrayList<Question>();
        if(questions.isEmpty()){
         //   System.out.println("Questions file is empty");
            return 0;
        }

        for (Map.Entry<Level, ArrayList<Question>> entry : questions.entrySet()) {
            for(Question q: entry.getValue()){
                appendList.add(q);
            }
        }
        int counter = 0;
        for(Question q : appendList){
            if(q.getQuestion().equals(question.getQuestion())){
                appendList.remove(counter);
                flag = true;
                break;
            }
            counter++;
        }
        if(!flag){
         //   System.out.println("Question was not found");
            return 0;
        }

        prepareToWriteToFile(appendList, path);
        return 1;
    }

    public int editQuestion(String path, Question oldQuestion, Question newQuestion){
        boolean flag = false;
        ArrayList<Question> appendList = new ArrayList<Question>();
        if(questions.isEmpty()){
        //    System.out.println("Questions file is empty");
            return 0;
        }
        for (Map.Entry<Level, ArrayList<Question>> entry : questions.entrySet()) {
            for(Question q: entry.getValue()){
                appendList.add(q);
            }
        }
        int counter = 0;
        for(Question q : appendList){
            if(q.getQuestion().equals(oldQuestion.getQuestion())){
                appendList.remove(counter);
                appendList.add(newQuestion);
                flag = true;
                break;
            }
            counter++;
        }
        if(!flag){
         //   System.out.println("Question was not found");
            return 0;
        }
        prepareToWriteToFile(appendList, path);

        return 1;
    }

    //import all games from JSON
    public void importGamesFromJSON(String path) {
        List<String> list=new ArrayList<>();
        ArrayList<String> StringTiles = new ArrayList<>();
        int j=0;
        try {
            File myObj = new File("GamesHistory.txt");
            Scanner myReader = new Scanner(myObj);
          while (myReader.hasNextLine()) {

                list.add(myReader.nextLine());}

                for (int i=2;i<list.size()-1;i++){

                        StringTiles.add(j,list.get(i));
                        j++;





//                System.out.println(myReader.nextLine().trim());
//                String data = myReader.nextLine();
//               System.out.println(myReader.nextLine());
           }System.out.println(StringTiles);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        if(list.contains("B"))
            System.out.println("id");
        else
            System.out.println("not found");

        try (FileReader reader = new FileReader(new File(path))) {
            JsonObject doc = (JsonObject) Jsoner.deserialize(reader);
            JsonArray gameObj = (JsonArray) doc.get("games");
            Iterator<Object> iterator = gameObj.iterator();
            while (iterator.hasNext()) {
                JsonObject obj = (JsonObject) iterator.next();
                int id = list.indexOf(1);
               tiles = (ArrayList<Integer>) obj.get("Tiles");
                String turn = (String) obj.get("Turn");
                Boolean isTurn = false;
                if(turn.equals("B")){
                    isTurn = true;
                }
                Game newGame = new Game(id, tiles, isTurn);
                games.put(id, newGame);
            }

        } catch (IOException | DeserializationException e) {
            e.printStackTrace();
        }
    }



    //add game to json(Save)
    public void addGameToJSON(String path, Game game) throws IOException {


        //array that holds all of the existing games
        ArrayList<Game> appendList = new ArrayList<>();
        ArrayList<Integer> tiles = new ArrayList<Integer>();
        if(games.isEmpty()){
            importGamesFromJSON("JSON/games.JSON");
        }

        for (Map.Entry<Integer, Game> entry : games.entrySet()) {
            appendList.add(entry.getValue());
        }
        try {
            //new array to hold the games
            JsonArray games = new JsonArray();

            //add the game to array
            JsonObject jsonObject = new JsonObject();
            //~~~~~~
            jsonObject.put("Id", game.getId());
            //game -> get tiles
            //get ->(boolean)turn -> B/W
            System.out.println("sssss"+game.getGameState());
//            jsonObject.put("Tiles", game.getGameState());
            String turn;
            boolean isP1Turn=game.isP1Turn();
            if(isP1Turn==true)
                turn="B";
            turn="W";
            jsonObject.put("Turn", turn);

            //add already existing objects back into the file
            for(Game q : appendList){
                //~~~~~
                jsonObject.put("Id", game.getId());
                //game -> get tiles
                //get ->(boolean)turn -> B/W
//                jsonObject.put("Tiles", game.getBoard());
                String turn1;
                boolean isP1Turn1=game.isP1Turn();
                if(isP1Turn1==true)
                    turn1="B";
                turn1="W";
                jsonObject.put("Turn", turn1);
            }

            JsonObject doc = new JsonObject();
            doc.put("games", games);

            writeToFile(path, doc);
        } catch ( NullPointerException e) {
            e.printStackTrace();
        }

    }

    public int randomQuestionFromJSON(String path) {
        try (FileReader reader = new FileReader(new File(path))) {
            JsonObject doc = (JsonObject) Jsoner.deserialize(reader);
            JsonArray questionObj = (JsonArray) doc.get("questions");
            Random r = new Random();
            int index = r.nextInt(questionObj.size()) ;
            String wholeQ=questionObj.get(index).toString();
            String q=wholeQ.substring(wholeQ.indexOf("question=")+9,wholeQ.indexOf(","));
            String a=wholeQ.substring(wholeQ.indexOf("answers=[")+9,wholeQ.indexOf("]"));
            String l=wholeQ.substring(wholeQ.indexOf("level=")+6,wholeQ.indexOf(", a"));
            String c=wholeQ.substring(wholeQ.indexOf("correct_ans=")+12,wholeQ.indexOf("}"));
            int ind=Integer.parseInt(c)-1;
            int lev=Integer.parseInt(l);
            String[] sArray=a.split(", ");
            JList list = new JList(sArray);
            HamkaQuestion dialog = new HamkaQuestion("Question: "+q, list);
            dialog.show();

            if(list.getSelectedIndex()==ind){
                if(lev==1)return Constants.trueEasy;
                if(lev==2)return Constants.trueMedium;
                if(lev==3)return Constants.trueHard;
            }
            if(list.getSelectedIndex()==-1 || list.getSelectedIndex()!=ind){
                if(lev==1)return Constants.falseEasy;
                if(lev==2)return Constants.falseMedium;
                if(lev==3)return Constants.falseHard;
            }


           return 0;

        } catch (IOException | DeserializationException e) {
            e.printStackTrace();
            return 0;
        }

    }



}