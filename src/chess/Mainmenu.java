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

public class Mainmenu extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private JButton exitB;
    private JLayeredPane menupanel;
    private JButton startgame;
    private JButton startai;
    private JButton about;
    private ExitButtonHandler ebHandler;
    private UseButtonHandler usHandler;
    private Image bgmenu;
    private Image startbuttonimage;
    private Image computerbuttonimage;
    private Image aboutbuttonimage;
    private Image exitbuttonimage;
    private ImageIcon frameicon;
    private JLabel menuicon,computericon,abouticon,exiticon;
    
    public Mainmenu()
    {
        try{
            frameicon = new ImageIcon(getClass().getResource("resources/frameicon.png"));
            bgmenu = ImageIO.read(getClass().getResource("resources/menubackground.jpg"));
            startbuttonimage = ImageIO.read(getClass().getResource("resources/starttext3.png"));
            computerbuttonimage = ImageIO.read(getClass().getResource("resources/computertext.png"));
            aboutbuttonimage = ImageIO.read(getClass().getResource("resources/abouttext.png"));
            exitbuttonimage = ImageIO.read(getClass().getResource("resources/exittext.png"));
        }
        catch(IOException e){
            System.out.println("Background image not found");
        }
        super.setIconImage(frameicon.getImage());
        bgmenu = bgmenu.getScaledInstance(800, 800, Image.SCALE_DEFAULT);
        startbuttonimage = startbuttonimage.getScaledInstance(400, 100, Image.SCALE_DEFAULT);
        computerbuttonimage = computerbuttonimage.getScaledInstance(500, 100, Image.SCALE_DEFAULT);
        aboutbuttonimage = aboutbuttonimage.getScaledInstance(400, 100, Image.SCALE_DEFAULT);
        exitbuttonimage = exitbuttonimage.getScaledInstance(250, 100, Image.SCALE_DEFAULT);
        ImageIcon startic = new ImageIcon(startbuttonimage);
        ImageIcon computeric = new ImageIcon(computerbuttonimage);
        ImageIcon aboutic = new ImageIcon(aboutbuttonimage);
        ImageIcon exitic = new ImageIcon(exitbuttonimage);
        setContentPane(new JLabel(new ImageIcon(bgmenu)));
        menupanel = getLayeredPane();
        menupanel.setLayout(null);
        menupanel.setOpaque(false);
        ebHandler = new ExitButtonHandler();
        usHandler = new UseButtonHandler();
        startgame = new JButton();
        startgame.addActionListener(usHandler);
        startgame.setBounds(400,380,400,100);
        
        
        

        
        startai = new JButton();
        startai.addActionListener(usHandler);
        startai.setBounds(310,480,500,100);
        about = new JButton();
        about.addActionListener(usHandler);
        about.setBounds(423,580,400,100);
        exitB = new JButton();
        exitB.setBounds(570,680,250,100);
        exitB.addActionListener(ebHandler);
        
        startgame.setOpaque(false);
        startgame.setContentAreaFilled(false);
        startgame.setBorderPainted(false);
        
        startai.setOpaque(false);
        startai.setContentAreaFilled(false);
        startai.setBorderPainted(false);
        
        about.setOpaque(false);
        about.setContentAreaFilled(false);
        about.setBorderPainted(false);

        exitB.setOpaque(false);
        exitB.setContentAreaFilled(false);
        exitB.setBorderPainted(false);
                menuicon = new JLabel();
        menuicon.setIcon(startic);
        menuicon.setBounds(startgame.getBounds());
        
        computericon = new JLabel();
        computericon.setIcon(computeric);
        computericon.setBounds(startai.getBounds());
        
        abouticon = new JLabel();
        abouticon.setIcon(aboutic);
        abouticon.setBounds(about.getBounds());
        
        exiticon = new JLabel();
        exiticon.setIcon(exitic);
        exiticon.setBounds(exitB.getBounds());
        
        menupanel.add(menuicon,new Integer(3));
        menupanel.add(computericon,new Integer(3));
        menupanel.add(abouticon,new Integer(3));
        menupanel.add(exiticon,new Integer(3));
        menupanel.add(startgame,new Integer(2));
        menupanel.add(startai,new Integer(2));
        menupanel.add(exitB,new Integer(2));
        menupanel.add(about,new Integer(2));
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    public static void main(String[] args)
    {
        Mainmenu instance = new Mainmenu();
    }
    
    private class UseButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if (source instanceof JButton) {
                JButton buttonz = (JButton) source;
                if(buttonz.equals(startgame)){
                    
                    TwoPlayer twogame = new TwoPlayer();
                    setVisible(false);
                }
                if(buttonz.equals(startai)){
                    Chess singlegame = new Chess(1);
                    setVisible(false);
                }
                if(buttonz.equals(about)){
                    //Chess singlegame = new Chess();
                }
            }
        }
    }
    
    public class ExitButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }
}
