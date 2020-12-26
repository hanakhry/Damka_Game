package View;
import Model.SysData;
import Utils.Constants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;


public class HamkaGameHistory extends JFrame
{
    private JTextField user1 = new JTextField("Username 1");
    private JTextField user2 = new JTextField("Username 2");

    ArrayList<Integer>finalTiles=new ArrayList<>();
    List<String> al = new ArrayList<String>();


    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        HamkaGameHistory.index = index;
    }

    public static int index;


    public Boolean getTurn() {
        return turn;
    }

    Boolean turn;
    public ArrayList<Integer> getFinalTiles() {

        return finalTiles;
    }

    public List<String> getAl() {
        return al;
    }

    public HamkaGameHistory()throws IOException {

        JFrame f= new JFrame();
        final JLabel label = new JLabel();
        label.setSize(500,100);
        JButton b=new JButton("Show");
        b.setForeground(Color.black);
        b.setBounds(200,150,80,30);
        final DefaultListModel<String> l1 = new DefaultListModel<>();

        List tiles1;
        File[] files =countNumberOfTxtFile();
        /** add all the txt file to l1 list**/
        for (File file : files) {

             l1.addElement(file.getName().replace(".txt",""));

        }


        final JList<String> list1 = new JList<>(l1);
        list1.setBounds(100,100, 100,120);
        f.add(list1);  f.add(b);
        f.setSize(450,450);
        f.setLayout(null);

        f.getContentPane().setBackground(new java.awt.Color(250, 240, 0));
        f.setVisible(true);


        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = list1.getSelectedIndex();
                setIndex(index);

                HamkaGameHistory history = null;

                try {

                    history = new HamkaGameHistory();


                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                HamkaWindow window = new HamkaWindow(user1.getText(),user2.getText());


                System.out.println("Index Selected: " + index);
                String s = (String) list1.getSelectedValue();
                System.out.println("Value Selected: " + s);
                String a = String.valueOf(history.getFinalTiles());
                a = a.replace(",","");
                a = a.replace(" ","");
                a = a.replace("[","");
                a = a.replace("]","");
                // System.out.println(a);
                window.setGameState(a);
                window.getBoard().getGame().setP1Turn(history.getTurn());
                window.setDefaultCloseOperation(HamkaWindow.EXIT_ON_CLOSE);
                window.setVisible(true);
                window.getBoard().getGame().refreshColors();
                window.getBoard().handleClick(0, 0, 2);

                f.dispose();  //Remove frame
                final ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/v-icon.png"));
                JOptionPane.showMessageDialog(null,
                        "Game Loaded Successfully!","Load game",
                        JOptionPane.INFORMATION_MESSAGE,
                        icon);

            }
        });
        SysData sysData = new SysData();
        System.out.println("is"+index);
        turn=sysData.importGamesFromTxtFile(files[getIndex()].getCanonicalPath());
        tiles1=sysData.getTiles();

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
    public File[] countNumberOfTxtFile() throws IOException {
        File f = new File("TEXT");

        FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        };

        File[] files = f.listFiles(textFilter);

        //System.out.println(files.length);

        return files;}


}
