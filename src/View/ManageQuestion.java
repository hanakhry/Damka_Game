package View;

import Model.Question;
import Model.SysData;
import Utils.Level;
import net.miginfocom.swing.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class ManageQuestion extends JFrame {
    public static final int DEFAULT_WIDTH = 700;

    public static final int DEFAULT_HEIGHT = 750;

    ArrayList<Question> questions = questionList();
    DefaultListModel Model1 = new DefaultListModel();
    final static SysData sysData = SysData.getInstance();
    final DefaultListModel<String> model = new DefaultListModel();
    ButtonGroup editGroup = new ButtonGroup();
    ButtonGroup addGroup = new ButtonGroup();

    public ManageQuestion() {
        initComponents();
        super.setTitle("Question Manager");
        super.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        super.setLocationRelativeTo(null);
        //allows only 1 radiobutton selection
        editGroup.add(editChoice1);
        editGroup.add(editChoice2);
        editGroup.add(editChoice3);
        editGroup.add(editChoice4);

        addGroup.add(addChoice1);
        addGroup.add(addChoice2);
        addGroup.add(addChoice3);
        addGroup.add(addChoice4);

        setTitle("Question Manager");

        //setup the combobox options
        comboBox1.addItem(Level.EASY);
        comboBox1.addItem(Level.MEDIUM);
        comboBox1.addItem(Level.HARD);
        comboBox2.addItem(Level.EASY);
        comboBox2.addItem(Level.MEDIUM);
        comboBox2.addItem(Level.HARD);

        //add questions to list
        for(int i = 0; i < questions.size(); i++)
            model.add(i, questions.get(i).getQuestion().toString());
        selectQuestionList.setModel(model);


        //display selected question for editing
        editSelectedQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Model1.removeAllElements();
                Question q = questions.get(selectQuestionList.getSelectedIndex());
                questionField.setText((String) q.getQuestion());
                comboBox1.setSelectedIndex(q.getLevel().getLevel());
                editAnswer1.setText((String) q.getAnswers().get(0));
                editAnswer2.setText((String) q.getAnswers().get(1));
                editAnswer3.setText((String) q.getAnswers().get(2));
                editAnswer4.setText((String) q.getAnswers().get(3));
                int correctAnswer = q.getIndexOfCorrectAnswer();
                if(correctAnswer == 1)
                    editChoice1.setSelected(true);
                else if(correctAnswer == 2)
                    editChoice2.setSelected(true);
                else if(correctAnswer == 3)
                    editChoice3.setSelected(true);
                else
                    editChoice4.setSelected(true);
            }
        });


        //delete selected question
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Question q = questions.get(selectQuestionList.getSelectedIndex());
                System.out.println(q);
                sysData.deleteQuestionFromJSON("JSON/questions.JSON", q);
                int index = selectQuestionList.getSelectedIndex();
                ((DefaultListModel) selectQuestionList.getModel()).remove(index);
            }
        });

        //save changes to question
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String question = questionField.getText();
                int level = comboBox1.getSelectedIndex();
                int answer;
                String answer1 = editAnswer1.getText();
                String answer2 = editAnswer2.getText();
                String answer3 = editAnswer3.getText();
                String answer4 = editAnswer4.getText();
                ArrayList<String> answers = new ArrayList<>();
                answers.add(answer1);
                answers.add(answer2);
                answers.add(answer3);
                answers.add(answer4);

                if(editChoice1.isSelected()){
                    answer = 1;
                }
                else if(editChoice2.isSelected()){
                    answer = 2;
                }
                else if(editChoice3.isSelected()){
                    answer = 3;
                } else
                    answer = 4;
                Question q = new Question(question, answers, answer, Level.getLevelByNumber(level), "");
                sysData.editQuestion("JSON/questions.JSON", questions.get(selectQuestionList.getSelectedIndex()), q);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clear();
                dispose();
                MainMenu window = new MainMenu();
                window.setDefaultCloseOperation(MainMenu.DISPOSE_ON_CLOSE);
                window.setVisible(true);
            }
        });

        //add a new question
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String question = addQuestion.getText();
                int level = comboBox2.getSelectedIndex();
                int answer;
                String answer1 = addAnswer1.getText();
                String answer2 = addAnswer2.getText();
                String answer3 = addAnswer3.getText();
                String answer4 = addAnswer4.getText();
                ArrayList<String> answers = new ArrayList<>();
                answers.add(answer1);
                answers.add(answer2);
                answers.add(answer3);
                answers.add(answer4);

                if(addChoice1.isSelected()){
                    answer = 1;
                }
                else if(addChoice2.isSelected()){
                    answer = 2;
                }
                else if(addChoice3.isSelected()){
                    answer = 3;
                } else
                    answer = 4;
                Question q = new Question(question, answers, answer, Level.getLevelByNumber(level), "");
                try {
                    sysData.addQuestionToJSON("JSON/questions.JSON",  q);
                    ((DefaultListModel) selectQuestionList.getModel()).addElement(q.getQuestion().toString());
                    questions = questionList();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    
    }

    public ArrayList<Question> questionList(){
        sysData.importQuestionsFromJSON("JSON/questions.JSON");
        ArrayList<Question> questions = new ArrayList<>();
        for(Map.Entry<Level, ArrayList<Question>> entry : sysData.getQuestions().entrySet()){
            for(Question question : entry.getValue()){
                questions.add(question);
            }
        }

        return questions;

    }

    private void deleteActionPerformed(ActionEvent e) {
        // TODO add your code here
    }


        

    //selectQuestionList = new JList(questions.toArray());
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        scrollPane3 = new JScrollPane();
        selectQuestionList = new JList();
        selectPanel = new JPanel();
        question = new JLabel();
        questionField = new JTextField();
        difficulty = new JLabel();
        comboBox1 = new JComboBox();
        answer = new JLabel();
        editAnswer1 = new JTextField();
        editChoice1 = new JRadioButton();
        editAnswer2 = new JTextField();
        editChoice2 = new JRadioButton();
        editAnswer3 = new JTextField();
        editChoice3 = new JRadioButton();
        editAnswer4 = new JTextField();
        editChoice4 = new JRadioButton();
        panel1 = new JPanel();
        question2 = new JLabel();
        addQuestion = new JTextField();
        difficulty2 = new JLabel();
        comboBox2 = new JComboBox();
        answer2 = new JLabel();
        addAnswer1 = new JTextField();
        addChoice1 = new JRadioButton();
        addAnswer2 = new JTextField();
        addChoice3 = new JRadioButton();
        addAnswer3 = new JTextField();
        addChoice4 = new JRadioButton();
        addAnswer4 = new JTextField();
        addChoice2 = new JRadioButton();
        backButton = new JButton();
        editSelectedQuestionButton = new JButton();
        saveButton = new JButton();
        deleteButton = new JButton();
        addButton = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //======== scrollPane3 ========
        {
            scrollPane3.setViewportView(selectQuestionList);
        }
        contentPane.add(scrollPane3, "cell 0 0 7 9");

        //======== selectPanel ========
        {
            selectPanel.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new
            javax. swing. border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e", javax
            . swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM, new java
            .awt .Font ("Dialo\u0067" ,java .awt .Font .BOLD ,12 ), java. awt
            . Color. red) ,selectPanel. getBorder( )) ); selectPanel. addPropertyChangeListener (new java. beans.
            PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("borde\u0072" .
            equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
            selectPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- question ----
            question.setText("Question");
            selectPanel.add(question, "cell 0 0");
            selectPanel.add(questionField, "cell 2 0 10 1");

            //---- difficulty ----
            difficulty.setText("Difficulty");
            selectPanel.add(difficulty, "cell 0 2");
            selectPanel.add(comboBox1, "cell 2 2 10 1");

            //---- answer ----
            answer.setText("Answers");
            selectPanel.add(answer, "cell 0 4");
            selectPanel.add(editAnswer1, "cell 2 4 8 1");
            selectPanel.add(editChoice1, "cell 10 4");
            selectPanel.add(editAnswer2, "cell 2 5 8 1");
            selectPanel.add(editChoice2, "cell 10 5");
            selectPanel.add(editAnswer3, "cell 2 6 8 1");
            selectPanel.add(editChoice3, "cell 10 6");
            selectPanel.add(editAnswer4, "cell 2 7 8 1");
            selectPanel.add(editChoice4, "cell 10 7");
        }
        contentPane.add(selectPanel, "cell 9 1 14 8");

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- question2 ----
            question2.setText("Question");
            panel1.add(question2, "cell 0 3");
            panel1.add(addQuestion, "cell 1 3 30 1");

            //---- difficulty2 ----
            difficulty2.setText("Difficulty");
            panel1.add(difficulty2, "cell 0 5");
            panel1.add(comboBox2, "cell 2 5 21 1");

            //---- answer2 ----
            answer2.setText("Answers");
            panel1.add(answer2, "cell 0 11");
            panel1.add(addAnswer1, "cell 4 11 18 1");
            panel1.add(addChoice1, "cell 22 11");
            panel1.add(addAnswer2, "cell 4 13 18 1");
            panel1.add(addChoice3, "cell 22 13");
            panel1.add(addAnswer3, "cell 4 15 18 1");
            panel1.add(addChoice4, "cell 22 15");
            panel1.add(addAnswer4, "cell 4 17 18 1");
            panel1.add(addChoice2, "cell 22 17");
        }
        contentPane.add(panel1, "cell 24 0 17 9");

        //---- backButton ----
        backButton.setText("Back");
        contentPane.add(backButton, "cell 3 10");

        //---- editSelectedQuestionButton ----
        editSelectedQuestionButton.setText("Edit");
        contentPane.add(editSelectedQuestionButton, "cell 12 10");

        //---- saveButton ----
        saveButton.setText("Save Edit");
        contentPane.add(saveButton, "cell 21 10");

        //---- deleteButton ----
        deleteButton.setText("Delete");
        deleteButton.addActionListener(e -> deleteActionPerformed(e));
        contentPane.add(deleteButton, "cell 25 10");

        //---- addButton ----
        addButton.setText("Add");
        contentPane.add(addButton, "cell 29 10");
        setSize(960, 700);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JScrollPane scrollPane3;
    private JList selectQuestionList;
    private JPanel selectPanel;
    private JLabel question;
    private JTextField questionField;
    private JLabel difficulty;
    private JComboBox comboBox1;
    private JLabel answer;
    private JTextField editAnswer1;
    private JRadioButton editChoice1;
    private JTextField editAnswer2;
    private JRadioButton editChoice2;
    private JTextField editAnswer3;
    private JRadioButton editChoice3;
    private JTextField editAnswer4;
    private JRadioButton editChoice4;
    private JPanel panel1;
    private JLabel question2;
    private JTextField addQuestion;
    private JLabel difficulty2;
    private JComboBox comboBox2;
    private JLabel answer2;
    private JTextField addAnswer1;
    private JRadioButton addChoice1;
    private JTextField addAnswer2;
    private JRadioButton addChoice3;
    private JTextField addAnswer3;
    private JRadioButton addChoice4;
    private JTextField addAnswer4;
    private JRadioButton addChoice2;
    private JButton backButton;
    private JButton editSelectedQuestionButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton addButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
