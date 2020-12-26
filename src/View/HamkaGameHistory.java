package View;

import Model.SysData;
import Utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HamkaGameHistory extends JFrame {
    private JTextField user1 = new JTextField("Username 1");
    private JTextField user2 = new JTextField("Username 2");
    public ArrayList<Integer> finalTiles = new ArrayList<>();
    public List<String> al = new ArrayList<String>();
    public static int index;
    public JButton showBtn;
    public JButton backBtn;
    public JList<String> list1;
    public Boolean turn;
    public List tiles1;
    public File[] files;
    public ArrayList<Integer> getFinalTiles() { return finalTiles; }
    public Boolean getTurn() {
        return turn;
    }
    public List<String> getAl() {
        return al;
    }
    public static int getIndex() {
        return index;
    }
    public static void setIndex(int index) {
        HamkaGameHistory.index = index;
    }

    public HamkaGameHistory() throws IOException {

        super.setTitle("Game Load List");
        super.setSize(500, 600);
        super.setLocationRelativeTo(null);
        final JLabel label = new JLabel();
        //label.setSize(500,100);
        showBtn = new JButton("Load Game");
        backBtn = new JButton("Back");
        showBtn.setForeground(Color.black);
        //showBtn.setBounds(200, 150, 80, 30);
        showBtn.addActionListener(new HamkaHistoryListener());
        backBtn.addActionListener(new HamkaHistoryListener());
        final DefaultListModel<String> l1 = new DefaultListModel<>();
        files = countNumberOfTxtFile();
        /** add all the txt file to l1 list**/
        for (File file : files) {

            l1.addElement(file.getName().replace(".txt", ""));

        }

        list1= new JList<>(l1);
        list1.setSelectedIndex(0);


        JPanel tmpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel btnPanel= new JPanel(new BorderLayout());
        JPanel layout = new JPanel(new GridLayout(2,0));
        tmpPanel.add(backBtn);
        tmpPanel.add(showBtn);
        btnPanel.add(tmpPanel,BorderLayout.SOUTH);



        layout.add(list1);
        layout.add(btnPanel);

        super.setVisible(true);
        this.add(layout);
    }

    public File[] countNumberOfTxtFile() throws IOException {
        File f = new File("TEXT");

        FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        };

        File[] files = f.listFiles(textFilter);

        return files;
    }

    public void translateTextFile() throws IOException {
        SysData sysData = new SysData();

        this.turn=sysData.importGamesFromTxtFile(files[getIndex()].getCanonicalPath());
        this.tiles1=sysData.getTiles();

        //convert txt to array with value from Constants
        String a = String.valueOf(tiles1);
        String numTiles = a;
        String str[] = numTiles.split("[\\, \\]\\[]");
        //this parameter save the first value in the array(id)
        al = Arrays.asList(str);
        for (String s : al) {

            //if 0-> Contstans.black
            if(s.equals("0"))
                finalTiles.add(Constants.EMPTY);
            if (s.equals(" 2") || s.equals("2") || s.equals("2 "))
                finalTiles.add(Constants.BLACK_SOLDIER);
            if (s.equals("1") || s.equals(" 1") || s.equals("1 "))
                finalTiles.add(Constants.WHITE_SOLDIER);
            if (s.equals("11") || s.equals(" 11") || s.equals("11 "))
                finalTiles.add(Constants.WHITE_QUEEN);
            if (s.equals(" 22") || s.equals("22") || s.equals("22 "))
                finalTiles.add(Constants.BLACK_QUEEN);
        }

    }


    private class HamkaHistoryListener implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {

            Object src = e.getSource();
            // No window to update
            if (showBtn == src) {
                dispose();  //Remove frame
                int index = list1.getSelectedIndex();
                String select=list1.getSelectedValue();
                String[] userArray = select.split(".vs.", 2);
                setIndex(index);
                try {
                    translateTextFile();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                HamkaWindow window = new HamkaWindow(userArray[0], userArray[1]);


                String s = (String) list1.getSelectedValue();

                String a = String.valueOf(getFinalTiles());
                a = a.replace(",", "");
                a = a.replace(" ", "");
                a = a.replace("[", "");
                a = a.replace("]", "");


                window.setGameState(a);
                window.getBoard().getGame().setP1Turn(getTurn());
                window.setDefaultCloseOperation(HamkaWindow.EXIT_ON_CLOSE);
                window.setVisible(true);
                window.getBoard().getGame().refreshColors();
                window.getBoard().handleClick(0, 0, 2);


                final ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/v-icon.png"));
                JOptionPane.showMessageDialog(null,
                        "Game Loaded Successfully!", "Load game",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon);

            }
            if (backBtn == src) {
                dispose();
                MainMenu window = new MainMenu();
                window.setDefaultCloseOperation(MainMenu.DISPOSE_ON_CLOSE);
                window.setVisible(true);
            }
        }
     }

}