package View;

import Model.Question;
import Model.SysData;
import Utils.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class QuestionManager extends JFrame{
    private JList questionList;
    private JPanel questionPanel;
    private JButton editSelectedQuestionButton;
    private JFormattedTextField formattedTextField1;
    private JTextArea textArea1;
    ArrayList<Question> questions = questionList();

    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setContentPane(new QuestionManager().questionPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    public QuestionManager(){
        add(questionPanel);
        setTitle("Question Manager");
        setLayout(new FlowLayout());
        setSize(400,500);
        questionList = new JList(questions.toArray());
        questionList.setVisibleRowCount(questions.size());
        add(new JScrollPane(questionList));
        //display only the question of the object
        questionList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Question) {
                    ((JLabel) renderer).setText((String) ((Question) value).getQuestion());
                }
                return renderer;
            }
        });

        editSelectedQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static ArrayList<Question> questionList(){
        final SysData sysData = SysData.getInstance();
        sysData.importQuestionsFromJSON("JSON/questions.JSON");
        ArrayList<Question> questions = new ArrayList<>();
        for(Map.Entry<Level, ArrayList<Question>> entry : sysData.getQuestions().entrySet()){
            for(Question question : entry.getValue()){
                questions.add(question);
            }
        }

        return questions;

    }

}
