/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;
import javax.swing.*;
import java.awt.Image;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;
import java.net.*;
import java.util.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class TwoPlayer extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private JLayeredPane menuPanel;
    private Image bgMenu;
    private ImageIcon frameIcon;
    private JButton hotSeat;
    private JButton createLobby;
    private JButton joinLobby, randomMatch, refresh;
    private Image hotSeatButtonImage, joinLobbyButtonImage, createLobbyButtonImage, randomMatchButtonImage, refreshButtonImage;
    private JLabel hotSeatIcon, joinLobbyIcon, createLobbyIcon, randomMatchIcon, refreshIcon;
    private UseButtonHandler ushHandler;
    private Socket loginSocket;
    private String fromUser;
    private String fromServer;
    private JList list;
    private JScrollPane listScroller;
    private Vector playerList;
    public static List<LobbyPublic> publicLobbyData;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public TwoPlayer()
    {
        publicLobbyData = new ArrayList<LobbyPublic>();
        try{
            frameIcon = new ImageIcon(getClass().getResource("resources/frameicon.png"));
            bgMenu = ImageIO.read(getClass().getResource("resources/menubackground.jpg"));
            hotSeatButtonImage = ImageIO.read(getClass().getResource("resources/hotseattext.png"));
            joinLobbyButtonImage = ImageIO.read(getClass().getResource("resources/joinlobbytext.png"));
            createLobbyButtonImage = ImageIO.read(getClass().getResource("resources/createlobbytext.png"));
            randomMatchButtonImage = ImageIO.read(getClass().getResource("resources/randommatchtext.png"));
            refreshButtonImage = ImageIO.read(getClass().getResource("resources/refreshtext.png"));
        }
        catch(IOException e){
            System.out.println("Images not found");
        }
        
        
        int portNumber = 25565;
        String hostName = "78.62.118.5";
        try{
            try{
                loginSocket = new Socket(hostName, portNumber);    
            }
            catch(UnknownHostException uke){
                System.out.println("Login failed.");
            }
            out = new ObjectOutputStream(loginSocket.getOutputStream());
            in = new ObjectInputStream(loginSocket.getInputStream());
        }
        catch(IOException io){
            System.out.println("I/O failed.");
        }
        
        hotSeatButtonImage = hotSeatButtonImage.getScaledInstance(400, 100, Image.SCALE_DEFAULT);
        randomMatchButtonImage = randomMatchButtonImage.getScaledInstance(400, 100, Image.SCALE_DEFAULT);
        joinLobbyButtonImage = joinLobbyButtonImage.getScaledInstance(400, 100, Image.SCALE_DEFAULT);
        createLobbyButtonImage = createLobbyButtonImage.getScaledInstance(400, 100, Image.SCALE_DEFAULT);
        refreshButtonImage = refreshButtonImage.getScaledInstance(200, 100, Image.SCALE_DEFAULT);
        
        
        
        ImageIcon hotSeatIc = new ImageIcon(hotSeatButtonImage);
        ImageIcon randomMatchIc = new ImageIcon(randomMatchButtonImage);
        ImageIcon joinLobbyIc = new ImageIcon(joinLobbyButtonImage);
        ImageIcon createLobbyIc = new ImageIcon(createLobbyButtonImage);
        ImageIcon refreshIc = new ImageIcon(refreshButtonImage);
        
        
        
        
        
        hotSeat = new JButton();
        createLobby = new JButton();
        joinLobby = new JButton();
        randomMatch = new JButton();
        refresh = new JButton();
        
        ushHandler = new UseButtonHandler();
        hotSeat.addActionListener(ushHandler);
        createLobby.addActionListener(ushHandler);
        joinLobby.addActionListener(ushHandler);
        refresh.addActionListener(ushHandler);
        randomMatch.addActionListener(ushHandler);
        
        
        
        
        
        
        list = new JList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        listScroller = new JScrollPane(list);
        
        listScroller.setBounds(100,100,200,450);
        randomMatch.setBounds(350,100,400,100);
        joinLobby.setBounds(350,266,400,100);
        createLobby.setBounds(350,432,400,100);
        hotSeat.setBounds(350,600,400,100);
        refresh.setBounds(100,600,200,100);
        
        hotSeatIcon = new JLabel();
        hotSeatIcon.setIcon(hotSeatIc);
        hotSeatIcon.setBounds(hotSeat.getBounds());
        
        randomMatchIcon = new JLabel();
        randomMatchIcon.setIcon(randomMatchIc);
        randomMatchIcon.setBounds(randomMatch.getBounds());
        
        createLobbyIcon = new JLabel();
        createLobbyIcon.setIcon(createLobbyIc);
        createLobbyIcon.setBounds(createLobby.getBounds());
        
        joinLobbyIcon = new JLabel();
        joinLobbyIcon.setIcon(joinLobbyIc);
        joinLobbyIcon.setBounds(joinLobby.getBounds());
        
        refreshIcon = new JLabel();
        refreshIcon.setIcon(refreshIc);
        refreshIcon.setBounds(refresh.getBounds());
        
        
        super.setIconImage(frameIcon.getImage());
        bgMenu = bgMenu.getScaledInstance(800, 800, Image.SCALE_DEFAULT);
        setContentPane(new JLabel(new ImageIcon(bgMenu)));
        menuPanel = getLayeredPane();
        menuPanel.setLayout(null);
        menuPanel.setOpaque(false);
       
        randomMatch.setOpaque(false);
        randomMatch.setContentAreaFilled(false);
        randomMatch.setBorderPainted(false);
        
        hotSeat.setOpaque(false);
        hotSeat.setContentAreaFilled(false);
        hotSeat.setBorderPainted(false);
        
        createLobby.setOpaque(false);
        createLobby.setContentAreaFilled(false);
        createLobby.setBorderPainted(false);
        
        joinLobby.setOpaque(false);
        joinLobby.setContentAreaFilled(false);
        joinLobby.setBorderPainted(false);
        
        refresh.setOpaque(false);
        refresh.setContentAreaFilled(false);
        refresh.setBorderPainted(false);
        
        menuPanel.add(listScroller,new Integer(2));
        menuPanel.add(hotSeat,new Integer(2));
        menuPanel.add(randomMatch,new Integer(2));
        menuPanel.add(createLobby,new Integer(2));
        menuPanel.add(joinLobby,new Integer(2));
        menuPanel.add(refresh,new Integer(2));
        
        menuPanel.add(hotSeatIcon,new Integer(3));
        menuPanel.add(randomMatchIcon,new Integer(3));
        menuPanel.add(createLobbyIcon,new Integer(3));
        menuPanel.add(joinLobbyIcon,new Integer(3));
        menuPanel.add(refreshIcon,new Integer(3));
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
                if(buttonz.equals(hotSeat)){
                    Chess singlegame = new Chess(0);
                    setVisible(false);
                }
                else{
                    if(buttonz.equals(createLobby)){
                        CreateLobby tempWindow = new CreateLobby(out,in,loginSocket);
                        
                    }
                    if(buttonz.equals(refresh)){
                        fromUser="ref$";
                        try{
                            out.writeObject(fromUser);
                            try{
                                fromServer = (String)in.readObject();
                                System.out.println(fromServer);
                            }
                            catch(ClassNotFoundException cnf){
                                System.err.println("Class has been not found.");
                            }
                        }
                        catch(IOException ioex){
                            System.err.println("IO failure.");
                        }
                        int state = 0;
                        String lName="", lID="", lSecure="";
                        for(int i = 0; i < fromServer.length() ;i++){
                            if(fromServer.charAt(i)=='/'||fromServer.charAt(i)=='$'){
                                if(state==2){
                                    LobbyPublic newPublicLobby = new LobbyPublic(lName,lID,lSecure);
                                    publicLobbyData.add(newPublicLobby);
                                    lName=""; 
                                    lID=""; 
                                    lSecure="";
                                    state=0;
                                }
                                else state++;
                            }
                            else{
                                if(state==0)lName=lName+fromServer.charAt(i);
                                if(state==1)lID=lID+fromServer.charAt(i);
                                if(state==2)lSecure=lSecure+fromServer.charAt(i);
                            }
                        }
                        System.out.println(publicLobbyData.get(0).getLobbyName());
                        playerList = new Vector();
                        String tempstr;
                        for(int i = 0; i < publicLobbyData.size(); i++){
                            tempstr="";
                            tempstr=publicLobbyData.get(i).getLobbyName();
                            if(publicLobbyData.get(i).getLobbySecure().equals("true"))tempstr=tempstr+" (Password Protected)";
                            playerList.add(tempstr);
                        }
                        //playerList.add(fromServer);
                        list.removeAll();
                        list.setListData(playerList);
                        list.repaint();
                        
                        
                    }
                    if(buttonz.equals(joinLobby)){
                        int selection = list.getSelectedIndex();
                        if(selection<0)JOptionPane.showMessageDialog(null,"Lobby has not been selected.", "Error", JOptionPane.ERROR_MESSAGE);
                        else{
                            JoinLobby tempWindow = new JoinLobby(out,in,loginSocket,selection);
                        }
                    }
                }
            }
        }
    }
}
