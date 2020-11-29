package Model;

import Utils.Level;
import org.json.simple.*;

import java.io.*;
import java.util.*;


public final class SysData {
    private static SysData instance;
    //HashMap key: question difficulty level, value: all questions of such difficulty
    private final HashMap<Level, ArrayList<Question>> questions = new HashMap();
    private final HashMap<Integer, Game> games = new HashMap();


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


    public HashMap<Level, ArrayList<Question>> getQuestions() { return this.questions; }
    public  HashMap<Integer, Game> games() { return this.games; }

    public void importQuestionsFromJSON(String path) {
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
            System.out.println("Changes were executed successfully");
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


    public int deleteQeustionFromJSON(String path, Question question){
        boolean flag = false;
        //array that holds all of the existing questions
        ArrayList<Question> appendList = new ArrayList<Question>();
        if(questions.isEmpty()){
            System.out.println("Questions file is empty");
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
            System.out.println("Question was not found");
            return 0;
        }

        prepareToWriteToFile(appendList, path);
        return 1;
    }

    public int editQuestion(String path, Question oldQuestion, Question newQuestion){
        boolean flag = false;
        ArrayList<Question> appendList = new ArrayList<Question>();
        if(questions.isEmpty()){
            System.out.println("Questions file is empty");
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
            System.out.println("Question was not found");
            return 0;
        }
        prepareToWriteToFile(appendList, path);

        return 1;
    }

    //import all games from JSON
    public void importGamesFromJSON(String path) {
        try (FileReader reader = new FileReader(new File(path))) {
            JsonObject doc = (JsonObject) Jsoner.deserialize(reader);
            JsonArray gameObj = (JsonArray) doc.get("games");
            Iterator<Object> iterator = gameObj.iterator();
            while (iterator.hasNext()) {
                JsonObject obj = (JsonObject) iterator.next();
                int id = Integer.parseInt((String) obj.get("Id"));
                ArrayList<Integer> tiles = (ArrayList<Integer>) obj.get("Tiles");
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
        //array that holds all of the existing questions
        ArrayList<Game> appendList = new ArrayList<>();
        ArrayList<Integer> tiles = new ArrayList<Integer>();
        if(games.isEmpty()){
            importGamesFromJSON("JSON/games.JSON");
        }

        for (Map.Entry<Integer, Game> entry : games.entrySet()) {
            appendList.add(entry.getValue());
        }
        try {
            //new array to hold the questions
            JsonArray games = new JsonArray();

            //add the question to array
            JsonObject jsonObject = new JsonObject();
            //~~~~~~
            jsonObject.put("Id", game.getId());
            //game -> get tiles
            //get ->(boolean)turn -> B/W
            jsonObject.put("Tiles", game.getBoard());
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
                jsonObject.put("Tiles", game.getBoard());
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


}