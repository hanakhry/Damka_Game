package View;

import Model.Question;
import Model.SysData;
import Utils.Level;
import net.miginfocom.swing.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class ManageQuestion extends JFrame {
    public static final int DEFAULT_WIDTH = 700;

    public static final int DEFAULT_HEIGHT = 750;

    ArrayList<Question> questions = questionList();
    final static SysData sysData = SysData.getInstance();
    DefaultListModel<String> model = new DefaultListModel();
    ButtonGroup editGroup = new ButtonGroup();
    int selectedIndexList;

    public ManageQuestion() {
        initComponents();
        super.setTitle("Question Manager");
        super.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        super.setLocationRelativeTo(null);


        this.choice1.addActionListener(new radioButtonListener());
        this.choice2.addActionListener(new radioButtonListener());
        this.choice3.addActionListener(new radioButtonListener());
        this.choice4.addActionListener(new radioButtonListener());

        //display selected item in list for editing
        selectQuestionList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(selectQuestionList.getSelectedIndex() > -1) {
                    selectedIndexList = selectQuestionList.getSelectedIndex();
                    Question q = questions.get(selectQuestionList.getSelectedIndex());
                    questionField.setText((String) q.getQuestion());
                    comboBox1.setSelectedIndex(q.getLevel().getLevel());
                    answer1.setText((String) q.getAnswers().get(0));
                    answer2.setText((String) q.getAnswers().get(1));
                    answer3.setText((String) q.getAnswers().get(2));
                    answer4.setText((String) q.getAnswers().get(3));
                    int correctAnswer = q.getIndexOfCorrectAnswer();
                    if (correctAnswer == 1) {
                        choice1.setSelected(true);
                        textFieldColor(answer1);
                    }
                    else if (correctAnswer == 2) {
                        choice2.setSelected(true);
                        textFieldColor(answer2);
                    }
                    else if (correctAnswer == 3) {
                        choice3.setSelected(true);
                        textFieldColor(answer3);
                    }
                    else {
                        choice4.setSelected(true);
                        textFieldColor(answer4);
                    }
                }
            }
        });
       
        //allows only 1 radiobutton selection
        editGroup.add(choice1);
        editGroup.add(choice2);
        editGroup.add(choice3);
        editGroup.add(choice4);


        setTitle("Question Manager");

        //setup the combobox options
        comboBox1.addItem(Level.EASY);
        comboBox1.addItem(Level.MEDIUM);
        comboBox1.addItem(Level.HARD);

        //add questions to list
        for(int i = 0; i < questions.size(); i++)
            model.add(i, questions.get(i).getQuestion().toString());
        selectQuestionList.setModel(model);


        //delete selected question
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!selectQuestionList.isSelectionEmpty()) {
                    Question q = questions.get(selectQuestionList.getSelectedIndex());
                    System.out.println(q);
                    sysData.deleteQuestionFromJSON("JSON/questions.JSON", q);
                    int index = selectQuestionList.getSelectedIndex();
                    ((DefaultListModel) selectQuestionList.getModel()).remove(index);
                } else
                    JOptionPane.showMessageDialog(null, "Select from the list first.");
            }
        });

        //save changes to question
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkFields()) {
                    String question = questionField.getText();
                    int level = comboBox1.getSelectedIndex();
                    int answer;
                    String ans1 = answer1.getText();
                    String ans2 = answer2.getText();
                    String ans3 = answer3.getText();
                    String ans4 = answer4.getText();
                    ArrayList<String> answers = new ArrayList<>();
                    answers.add(ans1);
                    answers.add(ans2);
                    answers.add(ans3);
                    answers.add(ans4);

                    if (choice1.isSelected()) {
                        answer = 1;
                    } else if (choice2.isSelected()) {
                        answer = 2;
                    } else if (choice3.isSelected()) {
                        answer = 3;
                    } else
                        answer = 4;
                    Question q = new Question(question, answers, answer, Level.getLevelByNumber(level), "");
                    sysData.editQuestion("JSON/questions.JSON", questions.get(selectedIndexList), q);
                    sysData.importQuestionsFromJSON("JSON/questions.JSON");
                    questions = questionList();
                    DefaultListModel listModel = (DefaultListModel) selectQuestionList.getModel();
                    listModel.removeAllElements();
                    model.clear();
                    for (int i = 0; i < questions.size(); i++)
                        model.add(i, questions.get(i).getQuestion().toString());
                    selectQuestionList.setModel(model);

                } else
                    JOptionPane.showMessageDialog(null, "At least one field is empty.");
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
                String question = questionField.getText();
                int level = comboBox1.getSelectedIndex();
                int answer;
                String ans1 = answer1.getText();
                String ans2 = answer2.getText();
                String ans3 = answer3.getText();
                String ans4 = answer4.getText();
                ArrayList<String> answers = new ArrayList<>();
                answers.add(ans1);
                answers.add(ans2);
                answers.add(ans3);
                answers.add(ans4);

                if(choice1.isSelected()){
                    answer = 1;
                }
                else if(choice2.isSelected()){
                    answer = 2;
                }
                else if(choice3.isSelected()){
                    answer = 3;
                } else
                    answer = 4;
                Question q = new Question(question, answers, answer, Level.getLevelByNumber(level), "");
                try {
                    sysData.addQuestionToJSON("JSON/questions.JSON",  q);
                    sysData.importQuestionsFromJSON("JSON/questions.JSON");
                    questions = questionList();
                    DefaultListModel listModel = (DefaultListModel) selectQuestionList.getModel();
                    listModel.removeAllElements();
                    model.clear();
                    for (int i = 0; i < questions.size(); i++)
                        model.add(i, questions.get(i).getQuestion().toString());
                    selectQuestionList.setModel(model);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                clearFields();
            }
        });

    
    }

    public void clearFields(){
        questionField.setText("");
        comboBox1.setSelectedIndex(0);
        answer1.setText("");
        answer2.setText("");
        answer3.setText("");
        answer4.setText("");
        choice1.setSelected(false);
        choice2.setSelected(false);
        choice3.setSelected(false);
        choice4.setSelected(false);
        answer1.setBorder(BorderFactory.createLineBorder(Color.gray));
        answer2.setBorder(BorderFactory.createLineBorder(Color.gray));
        answer3.setBorder(BorderFactory.createLineBorder(Color.gray));
        answer4.setBorder(BorderFactory.createLineBorder(Color.gray));
    }

    public boolean checkFields(){
        if(answer1.getText().isEmpty()){
            answer1.setBorder(BorderFactory.createLineBorder(Color.red));
            return false;
        }
        if(answer2.getText().isEmpty()){
            answer2.setBorder(BorderFactory.createLineBorder(Color.red));
            return false;
        }
        if(answer3.getText().isEmpty()){
            answer3.setBorder(BorderFactory.createLineBorder(Color.red));
            return false;
        }
        if(answer4.getText().isEmpty()){
            answer4.setBorder(BorderFactory.createLineBorder(Color.red));
            return false;
        }
        if(questionField.getText().isEmpty()){
            questionField.setBorder(BorderFactory.createLineBorder(Color.red));
            return false;
        }
        return true;
    }

    public ArrayList<Question> questionList(){
        if(sysData.getQuestions().entrySet().isEmpty())
            sysData.importQuestionsFromJSON("JSON/questions.JSON");
        ArrayList<Question> questions = new ArrayList<>();
        for(Map.Entry<Level, ArrayList<Question>> entry : sysData.getQuestions().entrySet()){
            for(Question question : entry.getValue()){
                questions.add(question);
            }
        }
        return questions;
    }


    public void textFieldColor(JTextField field){
        answer1.setBorder(BorderFactory.createLineBorder(Color.gray));
        answer2.setBorder(BorderFactory.createLineBorder(Color.gray));
        answer3.setBorder(BorderFactory.createLineBorder(Color.gray));
        answer4.setBorder(BorderFactory.createLineBorder(Color.gray));
        field.setBorder(BorderFactory.createLineBorder(Color.green));
    }

   public class radioButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            Object src = e.getSource();
            if(src == choice1){
                textFieldColor(answer1);
            }
            if(src == choice2){
                textFieldColor(answer2);
            }
            if(src == choice3){
                textFieldColor(answer3);
            }
            if(src == choice4){
                textFieldColor(answer4);
            }
        }
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
        answer1 = new JTextField();
        choice1 = new JRadioButton();
        answer2 = new JTextField();
        choice2 = new JRadioButton();
        answer3 = new JTextField();
        choice3 = new JRadioButton();
        answer4 = new JTextField();
        choice4 = new JRadioButton();
        backButton = new JButton();
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
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //======== scrollPane3 ========
        {
            scrollPane3.setViewportView(selectQuestionList);
        }
        contentPane.add(scrollPane3, "cell 3 0 41 9");

        //======== selectPanel ========
        {
            selectPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.
            swing.border.EmptyBorder(0,0,0,0), "",javax.swing.border
            .TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM,new java.awt.Font("Dia\u006cog"
            ,java.awt.Font.BOLD,12),java.awt.Color.red),selectPanel. getBorder
            ()));selectPanel. addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java
            .beans.PropertyChangeEvent e){if("\u0062ord\u0065r".equals(e.getPropertyName()))throw new RuntimeException
            ();}});
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
                "[]"));

            //---- question ----
            question.setText("Question");
            selectPanel.add(question, "cell 0 0");
            selectPanel.add(questionField, "cell 2 0 31 1");

            //---- difficulty ----
            difficulty.setText("Difficulty");
            selectPanel.add(difficulty, "cell 0 2");
            selectPanel.add(comboBox1, "cell 2 2 31 1");

            //---- answer ----
            answer.setText("Answers");
            selectPanel.add(answer, "cell 0 4");
            selectPanel.add(answer1, "cell 2 4 30 1");
            selectPanel.add(choice1, "cell 32 4");
            selectPanel.add(answer2, "cell 2 5 30 1");
            selectPanel.add(choice2, "cell 32 5");
            selectPanel.add(answer3, "cell 2 6 30 1");
            selectPanel.add(choice3, "cell 32 6");
            selectPanel.add(answer4, "cell 2 7 30 1");
            selectPanel.add(choice4, "cell 32 7");
        }
        contentPane.add(selectPanel, "cell 3 9 34 8");

        //---- backButton ----
        backButton.setText("Back");
        contentPane.add(backButton, "cell 3 18");

        //---- saveButton ----
        saveButton.setText("Save Edit");
        contentPane.add(saveButton, "cell 6 18");

        //---- deleteButton ----
        deleteButton.setText("Delete");
        contentPane.add(deleteButton, "cell 9 18");

        //---- addButton ----
        addButton.setText("Add");
        contentPane.add(addButton, "cell 12 18");
        setSize(1150, 1055);
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
    private JTextField answer1;
    private JRadioButton choice1;
    private JTextField answer2;
    private JRadioButton choice2;
    private JTextField answer3;
    private JRadioButton choice3;
    private JTextField answer4;
    private JRadioButton choice4;
    private JButton backButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton addButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
