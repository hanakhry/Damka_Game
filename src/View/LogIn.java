/*
 * Created by JFormDesigner on Sun Dec 13 17:38:15 IST 2020
 */

package View;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class LogIn extends JFrame {
    public LogIn() {
        initComponents();

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = username.getText();
                String pass = password.getText();
                if(name.equals("Admin") && pass.equals("Admin")) {
                    dispose();
                    ManageQuestion window = new ManageQuestion();
                    window.setDefaultCloseOperation(MainMenu.DISPOSE_ON_CLOSE);
                    window.setVisible(true);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Either the username or the password is invalid.");
                }
            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MainMenu window = new MainMenu();
                window.setDefaultCloseOperation(MainMenu.DISPOSE_ON_CLOSE);
                window.setVisible(true);
            }
        });
    }

    private void loginActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void loginKeyPressed(KeyEvent e) {
        // TODO add your code here
    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        label1 = new JLabel();
        username = new JTextField();
        label2 = new JLabel();
        password = new JPasswordField();
        back = new JButton();
        login = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
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
            "[]"));

        //---- label1 ----
        label1.setText("Username");
        contentPane.add(label1, "cell 1 4");
        contentPane.add(username, "cell 3 4 11 1");

        //---- label2 ----
        label2.setText("Password");
        contentPane.add(label2, "cell 1 6");
        contentPane.add(password, "cell 3 6 11 1");

        //---- back ----
        back.setText("Back");
        contentPane.add(back, "cell 3 8");

        //---- login ----
        login.setText("Log in");
        login.addActionListener(e -> loginActionPerformed(e));
        login.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                loginKeyPressed(e);
            }
        });
        contentPane.add(login, "cell 13 8");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel label1;
    private JTextField username;
    private JLabel label2;
    private JPasswordField password;
    private JButton back;
    private JButton login;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
