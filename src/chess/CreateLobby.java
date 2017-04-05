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
import java.net.*;
import java.util.*;

/**
 *
 * @author Boo
 */
public class CreateLobby extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 275;
    private JButton acceptCreation;
    private JButton cancelCreation;
    private JTextField lobbyName;
    private JTextField lobbyPassword;
    private JTextField lobbyPlayer;
    private JLabel lobbyNameLabel;
    private JLabel lobbyPasswordLabel;
    private JLabel lobbyPlayerLabel;
    private JLabel errorLabel;
    private UseButtonHandler uscHandler;
    private JLayeredPane menuPanel;
    private String lobbyNameStore;
    private String lobbyPasswordStore;
    private String lobbyPlayerStore;
    private Socket loginSocket;
    private String fromUser;
    private String fromServer;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    public CreateLobby(ObjectOutputStream pubout, ObjectInputStream pubin, Socket pubLoginSocket){
        
        loginSocket = pubLoginSocket;
        fromUser = "";
        fromServer = "";
        out = pubout;
        in = pubin;    
        
        acceptCreation = new JButton();
        cancelCreation = new JButton();
        lobbyName = new JTextField();
        lobbyPassword = new JTextField();
        lobbyPlayer = new JTextField();
        lobbyNameLabel = new JLabel();
        lobbyPasswordLabel = new JLabel();
        lobbyPlayerLabel = new JLabel();
        errorLabel = new JLabel();
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setVerticalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        
        lobbyNameStore = "";
        lobbyPasswordStore = "";
        lobbyPlayerStore = "";
        
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
        lobbyPlayerLabel.setText("Host name(Optional):");
        
        lobbyName.setBounds(185,25,200,25);
        lobbyPassword.setBounds(185,75,200,25);
        lobbyPlayer.setBounds(185,125,200,25);
        lobbyNameLabel.setBounds(15,25,160,25);
        lobbyPasswordLabel.setBounds(15,75,160,25);
        lobbyPlayerLabel.setBounds(15,125,160,25);
        errorLabel.setBounds(50,162,300,25);
        cancelCreation.setBounds(50,200,75,25);
        acceptCreation.setBounds(275,200,75,25);
        
        menuPanel.add(cancelCreation,new Integer(2));
        menuPanel.add(acceptCreation,new Integer(2));
        menuPanel.add(lobbyName,new Integer(2));
        menuPanel.add(lobbyNameLabel,new Integer(2));
        menuPanel.add(lobbyPassword,new Integer(2));
        menuPanel.add(lobbyPasswordLabel,new Integer(2));
        menuPanel.add(lobbyPlayer,new Integer(2));
        menuPanel.add(lobbyPlayerLabel,new Integer(2));
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
                    errorLabel.setText("");
                    if(lobbyName.getText().isEmpty()==true)errorLabel.setText("* Lobby name cannot be empty!");
                    else{
                        if(isStringValid(lobbyName.getText())==false)errorLabel.setText("* Lobby name contains invalid characters!");
                        else{
                            if(lobbyName.getText().length()>20)errorLabel.setText("* Lobby name cannot be longer than 20 characters!");
                            else{
                                if(lobbyPassword.getText().isEmpty()==false){
                                    if(isStringValid(lobbyPassword.getText())==false)errorLabel.setText("* Lobby password contains invalid characters!");
                                    else{
                                        if(lobbyPassword.getText().length()>20)errorLabel.setText("* Lobby password cannot be longer than 20 characters!");
                                    }
                                }
                                if(lobbyPlayer.getText().isEmpty()==false){
                                    if(isStringValid(lobbyPlayer.getText())==false)errorLabel.setText("* Host name contains invalid characters!");
                                    else{
                                        if(lobbyPlayer.getText().length()>20)errorLabel.setText("* Host name cannot be longer than 20 characters!");
                                        else{
                                            if(lobbyPlayer.getText().equals("Player2")||lobbyPlayer.getText().equals("Player1"))errorLabel.setText("* Invalid host name!");
                                        }
                                    }
                                }
                            }
                        } 
                    }
                    if(errorLabel.getText().isEmpty()==true){
                        lobbyNameStore = lobbyName.getText();
                        if(lobbyPassword.getText().isEmpty()==true)lobbyPasswordStore = "";
                        else lobbyPasswordStore = lobbyPassword.getText();
                        if(lobbyPlayer.getText().isEmpty()==true)lobbyPlayerStore = "Player1";
                        else lobbyPlayerStore = lobbyPlayer.getText();
                        fromUser="crl/"+lobbyNameStore+"/"+lobbyPasswordStore+"/"+lobbyPlayerStore+"$";
                        if (fromUser != null) {
                            try{
                                System.out.println("1");
                                System.out.println(out);
                                out.writeObject(fromUser);
                                System.out.println("2");
                            }
                            catch(IOException ioex){
                                System.err.println("IO failure.");
                            }
                        }           
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
    
    private boolean isStringValid(String checkedString){
        for(int i = 0; i < checkedString.length(); i++){
            if(!(((int)checkedString.charAt(i)>47&&(int)checkedString.charAt(i)<58)||((int)checkedString.charAt(i)>64&&(int)checkedString.charAt(i)<91)||((int)checkedString.charAt(i)>96&&(int)checkedString.charAt(i)<123))){
                return false;
            }
        }
        return true;
    }
}
