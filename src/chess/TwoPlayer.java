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
    public TwoPlayer()
    {
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
            PrintWriter out = new PrintWriter(loginSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(loginSocket.getInputStream()));
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
        
        
        
        Vector playerList = new Vector();
        
        
        
        playerList.add("Robert");
        playerList.add("Robert");
        playerList.add("Robert");
        playerList.add("Robert");
        playerList.add("Robert");
        playerList.add("Robert");
        playerList.add("Moo");
        playerList.add("Paulius");
        
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
        
        
        
        
        
        
        JList list = new JList(playerList);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        JScrollPane listScroller = new JScrollPane(list);
        
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
                        System.out.println("TEST");
                        CreateLobby tempWindow = new CreateLobby();
                        
                    }
                }
            }
        }
    }
}
