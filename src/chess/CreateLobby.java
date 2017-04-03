/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Boo
 */
public class CreateLobby extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 225;
    private JButton acceptCreation;
    private JButton cancelCreation;
    private JTextField lobbyName;
    private JTextField lobbyPassword;
    private JLabel lobbyNameLabel;
    private JLabel lobbyPasswordLabel;
    private JLabel errorLabel;
    private UseButtonHandler uscHandler;
    private JLayeredPane menuPanel;
    private String lobbyNameStore;
    private String lobbyPasswordStore;
    
    public CreateLobby(){
        acceptCreation = new JButton();
        cancelCreation = new JButton();
        lobbyName = new JTextField();
        lobbyPassword = new JTextField();
        lobbyNameLabel = new JLabel();
        lobbyPasswordLabel = new JLabel();
        errorLabel = new JLabel();
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setVerticalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        
        lobbyNameStore = "";
        lobbyPasswordStore = "";
        
        acceptCreation.setText("Create");
        cancelCreation.setText("Back");
        
        uscHandler = new UseButtonHandler();
        
        acceptCreation.addActionListener(uscHandler);
        cancelCreation.addActionListener(uscHandler);
        
        
        menuPanel = getLayeredPane();
        menuPanel.setLayout(null);
        menuPanel.setOpaque(false);
        
        lobbyNameLabel.setText("Lobby name:");
        lobbyPasswordLabel.setText("Lobby password(Optional):");
        
        lobbyName.setBounds(185,25,200,25);
        lobbyPassword.setBounds(185,75,200,25);
        lobbyNameLabel.setBounds(15,25,160,25);
        lobbyPasswordLabel.setBounds(15,75,160,25);
        errorLabel.setBounds(50,112,300,25);
        cancelCreation.setBounds(50,150,75,25);
        acceptCreation.setBounds(275,150,75,25);
        
        menuPanel.add(cancelCreation,new Integer(2));
        menuPanel.add(acceptCreation,new Integer(2));
        menuPanel.add(lobbyName,new Integer(2));
        menuPanel.add(lobbyNameLabel,new Integer(2));
        menuPanel.add(lobbyPassword,new Integer(2));
        menuPanel.add(lobbyPasswordLabel,new Integer(2));
        menuPanel.add(errorLabel,new Integer(2));
        
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    private class UseButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if (source instanceof JButton) {
                JButton buttonz = (JButton) source;
                if(buttonz.equals(acceptCreation)){
                    if(lobbyName.getText().isEmpty()==true)errorLabel.setText("* Lobby name cannot be empty!");
                    else{
                        setVisible(false);
                        dispose();
                    }
                    
                }
                if(buttonz.equals(cancelCreation)){
                    setVisible(false);
                    dispose();
                }
            }
        }
    }
}
