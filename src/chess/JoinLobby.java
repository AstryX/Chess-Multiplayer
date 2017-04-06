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
public class JoinLobby extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 225;
    private JButton acceptJoin;
    private JButton cancelCreation;
    private JTextField lobbyPassword;
    private JTextField lobbyPlayer;
    private JLabel lobbyPasswordLabel;
    private JLabel lobbyPlayerLabel;
    private JLabel errorLabel;
    private UseButtonHandler uscHandler;
    private JLayeredPane menuPanel;
    private String lobbyPasswordStore;
    private String lobbyPlayerStore;
    private Socket loginSocket;
    private String fromUser;
    private String fromServer;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int lobbyIndex;
    
    public JoinLobby(ObjectOutputStream pubout, ObjectInputStream pubin, Socket pubLoginSocket, int tempIndex){
        lobbyIndex=tempIndex;
        loginSocket = pubLoginSocket;
        fromUser = "";
        fromServer = "";
        out = pubout;
        in = pubin;    
        
        acceptJoin = new JButton();
        cancelCreation = new JButton();
        lobbyPassword = new JTextField();
        lobbyPlayer = new JTextField();
        lobbyPasswordLabel = new JLabel();
        lobbyPlayerLabel = new JLabel();
        errorLabel = new JLabel();
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setVerticalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        
        lobbyPasswordStore = "";
        lobbyPlayerStore = "";
        
        acceptJoin.setText("Join");
        cancelCreation.setText("Back");
        
        uscHandler = new UseButtonHandler();
        
        acceptJoin.addActionListener(uscHandler);
        cancelCreation.addActionListener(uscHandler);
        
        
        menuPanel = getLayeredPane();
        menuPanel.setLayout(null);
        menuPanel.setOpaque(false);
        
        lobbyPlayerLabel.setText("Player name(Optional):");
        
        if(TwoPlayer.publicLobbyData.get(lobbyIndex).getLobbySecure().equals("true")){
            lobbyPasswordLabel.setText("Lobby password:");
            lobbyPassword.setBounds(185,75,200,25);
            lobbyPasswordLabel.setBounds(15,75,160,25);
            menuPanel.add(lobbyPassword,new Integer(2));
            menuPanel.add(lobbyPasswordLabel,new Integer(2));
        }
        
        
        lobbyPlayer.setBounds(185,25,200,25);
        
        lobbyPlayerLabel.setBounds(15,25,160,25);
        errorLabel.setBounds(50,112,300,25);
        cancelCreation.setBounds(50,150,75,25);
        acceptJoin.setBounds(275,150,75,25);
        
        menuPanel.add(cancelCreation,new Integer(2));
        menuPanel.add(acceptJoin,new Integer(2));
        
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
                if(buttonz.equals(acceptJoin)){
                    
                    errorLabel.setText("");
                    if(lobbyPassword.getText().isEmpty()==false){
                        if(isStringValid(lobbyPassword.getText())==false)errorLabel.setText("* Lobby password contains invalid characters!");
                        else{
                            if(lobbyPassword.getText().length()>20)errorLabel.setText("* Lobby password cannot be longer than 20 characters!");
                        }
                    }
                    if(lobbyPlayer.getText().isEmpty()==false){
                        if(isStringValid(lobbyPlayer.getText())==false)errorLabel.setText("* Player name contains invalid characters!");
                        else{
                            if(lobbyPlayer.getText().length()>20)errorLabel.setText("* Player name cannot be longer than 20 characters!");
                            else{
                                if(lobbyPlayer.getText().equals("Player2")||lobbyPlayer.getText().equals("Player1"))errorLabel.setText("* Invalid player name!");
                            }
                        }
                    }
                    if(errorLabel.getText().isEmpty()==true){
                        if(lobbyPassword.getText().isEmpty()==true)lobbyPasswordStore = "";
                        else lobbyPasswordStore = lobbyPassword.getText();
                        if(lobbyPlayer.getText().isEmpty()==true)lobbyPlayerStore = "Player2";
                        else lobbyPlayerStore = lobbyPlayer.getText();
                        fromUser="joi/"+TwoPlayer.publicLobbyData.get(lobbyIndex).getLobbyName()+"/"+TwoPlayer.publicLobbyData.get(lobbyIndex).getLobbyID()+"/"+lobbyPasswordStore+"/"+lobbyPlayerStore+"$";
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
                        
                        try{
                            try{
                                fromServer = (String)in.readObject();
                            }
                            catch(ClassNotFoundException cnf){
                                System.err.println("Class has been not found.");
                            }
                        }
                        catch(IOException ioex){
                            System.err.println("IO failure.");
                        }
                        int state = 0;
                        String lStatus = "";
                        String lName = "";
                        String lID = "";
                        String lTurn = "";
                        String lWPlayer = "";
                        String lBPlayer = "";
                        for(int i = 0;fromServer.charAt(i)!='$';i++){
                            if(fromServer.charAt(i)=='/'||fromServer.charAt(i)=='$'){
                                state++;
                                if(state==1){
                                    if(lStatus.equals("false"))break;
                                }
                            }
                            else{
                                if(state==0)lStatus=lStatus+fromServer.charAt(i);
                                if(state==1)lName=lName+fromServer.charAt(i);
                                if(state==2)lID=lID+fromServer.charAt(i);
                                if(state==3)lTurn=lTurn+fromServer.charAt(i);
                                if(state==4)lWPlayer=lWPlayer+fromServer.charAt(i);
                                if(state==5)lBPlayer=lBPlayer+fromServer.charAt(i);
                            }
                        }
                        
                        Chess beginGame = new Chess(2,lName, lID, lTurn, lWPlayer, lBPlayer, out, in);
                        
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
