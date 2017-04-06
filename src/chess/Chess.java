package chess;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Chess extends JFrame
{
    private static final int WIDTH = 806;
    private static final int HEIGHT = 829;
    private JLayeredPane lp;
    private Map<String, Image> map;
    private int tracker = 0;
    private int movecounter = 0;
    private JButton clicked;
    private Map<JButton, JLabel> map2;
    private int globalplayerturn;
    private boolean whitecastleleft;
    private boolean whitecastleright;
    private boolean blackcastleleft;
    private boolean blackcastleright;
    private JButton checkmateButton;
    private int aiactive;
    private int aimovemade;
    JButton[][] buttons;
    JLabel[][] testlabel;
    JLabel[][] ailabel;
    int[][] helpindicator;
    private boolean ishelpdisplayed;
    private UseButtonHandler usHandler;
    private Vector<Integer> globalvector;
    private String lName;
    private String lID; 
    private String lTurn; 
    private String lWhite; 
    private String lBlack; 
    private int playerMove;
    private ObjectOutputStream out; 
    private ObjectInputStream in;
    private int validMove;
    private int passOriginx;
    private int passOriginy;
    private int passDestinationx;
    private int passDestinationy;
    
    public Chess (int computer)
    {
        setupVisual(computer);
        
        
    }
    
    public Chess (int computer, String tempLName, String tempLID, String tempLTurn, String tempLWhite, String tempLBlack, ObjectOutputStream tempOut, ObjectInputStream tempIn)
    {
        lName = tempLName;
        lID = tempLID;
        lTurn = tempLTurn;
        lWhite = tempLWhite;
        lBlack = tempLBlack;
        out = tempOut;
        in = tempIn;
        passOriginx = -1;
        passOriginy = -1;
        passDestinationx = -1;
        passDestinationy = -1;
        
        setupVisual(computer);
        if(lTurn.equals("1")){
            playerMove=1;
            validMove=1;
            setFrameTitle("Your turn");
        }
        else{
            playerMove=0;
            setFrameTitle("Not your turn.");
            new Thread(){
                @Override
                public void run() {
                    handleConnectionStart();
                }
            }.start();
        }
        
    }
    
    
    public void setupVisual(int computer){
        aiactive=computer;
        usHandler = new UseButtonHandler();
        //setFrameTitle("White player's turn");
        lp = getLayeredPane();
        aimovemade=1;
        globalplayerturn = 1;
        globalvector = new Vector<>();
        ishelpdisplayed = false;
        whitecastleleft = true;
        checkmateButton = new JButton();
        whitecastleright = true;
        blackcastleleft = true;
        blackcastleright = true;
        buttons = new JButton[8][8];
        testlabel = new JLabel[8][8];
        helpindicator = new int[8][8];
        for(int i = 0; i < buttons.length; i++){
            for(int j = 0; j < buttons.length; j++){
                buttons[i][j] = new JButton();
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.black, 1));
                buttons[i][j].addActionListener(usHandler);
                testlabel[i][j] = new JLabel();
                helpindicator[i][j]=0;
            }
        }
        
        lp.setLayout(null);
        boardSetup();
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        if(aiactive==1){
            if(globalplayerturn==1){
                computerMove();
            }
        }
    }
    
    
    public void computerMove(){
        int[] movement = new int[4] ;
        if(movecounter==0){
            Random rand = new Random();
            int pawnnr = rand.nextInt(8);
            int movenr = rand.nextInt(2) + 1;
            buttons[1][pawnnr].doClick();
            buttons[1+movenr][pawnnr].doClick();
        }
        else{
            System.out.println("test");
            for(int i = 0; i < 4;i++){
                movement[i] = 0;
             }
            ailabel=testlabel;
            String turncontains;
            if(globalplayerturn==1)turncontains="white";
            else turncontains="black";
            bestMove(movement,turncontains);
            System.out.println("Testnumbers below");
            System.out.println(movement[0] + " " + movement[1] + " " + movement[2] + " " + movement[3]);
            buttons[movement[0]][movement[1]].doClick();
            buttons[movement[2]][movement[3]].doClick();
        }
    }
    
    
    public void bestMove(int[] movement, String turncontains){
        JLabel originlabel, destinationlabel;
        Vector<Integer> decider = new Vector<>();
        int bestpoints = -1000;
        int samepoints = 0;
        int originx = -1, originy = -1, destinationx = -1, destinationy = -1;
        for(int col = 0; col < testlabel.length; col++){
            for(int row = 0; row < testlabel.length; row++){
                if(testlabel[col][row].getName().toLowerCase().contains(turncontains)){
                    originlabel = testlabel[col][row];
                    originx = row;
                    originy = col;
                    for(int col2 = 0; col2 < testlabel.length; col2++){
                        for(int row2 = 0; row2 < testlabel.length; row2++){
                            destinationlabel = testlabel[col2][row2];
                            destinationx = row2;
                            destinationy = col2;
                            if(isMoveValid(originlabel,destinationlabel,globalplayerturn)){
                                if(pointCalculator(originx,originy,destinationx,destinationy)>=bestpoints){
                                    if(pointCalculator(originx,originy,destinationx,destinationy)==bestpoints){
                                        decider.addElement(originy);
                                        decider.addElement(originx);
                                        decider.addElement(destinationy);
                                        decider.addElement(destinationx);
                                    }
                                    else{
                                        decider.clear();
                                        movement[0]=originy;
                                        movement[1]=originx;
                                        movement[2]=destinationy;
                                        movement[3]=destinationx;
                                        decider.addElement(movement[0]);
                                        decider.addElement(movement[1]);
                                        decider.addElement(movement[2]);
                                        decider.addElement(movement[3]);
                                    }
                                    bestpoints=pointCalculator(originx,originy,destinationx,destinationy);
                                }
                            }
                        }
                    }
                }
            }
        }
        if(decider.size()>4){
            int moverandom = decideRandomMove(decider);
            movement[0]=decider.get(moverandom);
            movement[1]=decider.get(moverandom+1);
            movement[2]=decider.get(moverandom+2);
            movement[3]=decider.get(moverandom+3);
        }
        System.out.println(bestpoints);
    }
    
    public int decideRandomMove(Vector decider){
        int choice=0;
        int sizevector=0;
        Random rand = new Random();
        sizevector=decider.size()/4;
        sizevector = rand.nextInt(sizevector);
        choice = sizevector*4;
        return choice;
    }
    
    public int pointCalculator(int originx, int originy, int destinationx, int destinationy){
        int total = 0;
        int piecevalue = 0, enemypiecevalue = 0;
        int enemyturn;
        if(globalplayerturn==1){
            enemyturn=2;
        }
        else{ 
            enemyturn=1;
        }
        JLabel originlabel = testlabel[originy][originx];
        String originname = originlabel.getName();
        JLabel destinationlabel = testlabel[destinationy][destinationx];
        String destinationname = destinationlabel.getName();
        piecevalue = pieceValue(originx,originy);
        enemypiecevalue = pieceValue(destinationx, destinationy);
        System.out.println(originy+"  "+originx + " " + enemyturn);
        if(pieceVulnerable(originx,originy,enemyturn)==true){
            total = total + piecevalue;
            
        }
        //Stalemate scenario???
        //Display of a checkmate/stalemate
        //Bonus points for initiating check
        //A way for AI to pursue checkmate???
        //Piece can be saved by another friendly piece.
        //Instead of falling back, prepare another piece to counter attack.
        //Moving piece makes other friendly pieces vulnerable?
        //Randomise equal trades.
        //Minus movement of rooks to disable castling.
        //Allow rooks to move after 20 turns?
        //Bonus to baiting enemy pieces(enemy piece piece data not returnable yet)
        //Add points to moving pawn forward(after certain threshold)
        //Add max points for promotion(if it does not get killed)
        //!!!!!!!!!!!!!Fix graphics messing up later in the gui.
        //!!!!!!!!!!!!!Fix clicking your buttons during the AI pause.
        
        total = total + enemypiecevalue;
        testlabel[destinationy][destinationx].setName(originname);
        testlabel[originy][originx].setName("empty");
        //Pawns charge forward to their death
        if(pieceVulnerable(destinationx,destinationy,enemyturn)==true){
            total=total-piecevalue;
            
        }
        else{
            if(isCheckActive(enemyturn)==true)total=total+10;
        }
        if(isCheckmateActive(enemyturn)==true)total=total+9000;
        testlabel[destinationy][destinationx].setName(destinationname);
        testlabel[originy][originx].setName(originname);
        return total;
        
    }
    
    public boolean pieceVulnerable(int coordx, int coordy, int enemyturn){       
        for(int col = 0; col < testlabel.length; col++){
            for(int row = 0; row < testlabel.length; row++){
                if(isMoveValid(testlabel[col][row], testlabel[coordy][coordx], enemyturn)==true){
                    System.out.println("MOVEEEE: "+coordy+" "+coordx+"     "+col+" "+row);
                    return true;
                }
            }
        }
        return false;
    }
        
    public int pieceValue(int originx, int originy){
        if(testlabel[originy][originx].getName().contains("pawn"))return 10;
        if(testlabel[originy][originx].getName().contains("rook"))return 50;
        if(testlabel[originy][originx].getName().contains("bishop"))return 30;
        if(testlabel[originy][originx].getName().contains("knight"))return 30;
        if(testlabel[originy][originx].getName().contains("queen"))return 90;
        if(testlabel[originy][originx].getName().contains("king"))return 10000;
        return 0;
    }
    
    /*public Vector<Integer> bestMove(Vector<Integer> vector, int pturn){
        JLabel originlabel, destinationlabel;
        int originx = -1, originy = -1, destinationx = -1, destinationy = -1;
        
        if(!isCheckmateActive(ailabel)){
            if((turncounter<checkmatesize/4)||checkmatesize==0){
                for(int col = 0; col < testlabel.length; col++){
                    for(int row = 0; row < testlabel.length; row++){
                        if(pturn==1){
                            if(ailabel[col][row].getName().toLowerCase().contains("white")){
                                originlabel = ailabel[col][row];
                                for(int col2 = 0; col2 < testlabel.length; col2++){
                                    for(int row2 = 0; row2 < testlabel.length; row2++){
                                        destinationlabel = ailabel[col][row];
                                        if(isMoveValid(originlabel,destinationlabel,pturn)){
                                            for(int col3 = 0; col3 < testlabel.length; col3++){
                                                for(int row3 = 0; row3 < testlabel.length; row3++){
                                                    if(originlabel.equals(ailabel[col3][row3])){
                                                        originx=row;
                                                        originy=col;
                                                    }
                                                    if(destinationlabel.equals(testlabel[col][row])){
                                                        destinationx=row;
                                                        destinationy=col;
                                                    }
                                                }
                                            }
                                            turncounter++;
                                            vector.add(originx);
                                            vector.add(originy);
                                            vector.add(destinationx);
                                            vector.add(destinationy);
                                            ailabel[destinationy][destinationx].setName(originlabel.getName());
                                            ailabel[originy][originx].setName("empty");
                                            vector = bestMove(vector,2);
                                        }
                                    }
                                } 
                            }
                        }
                        else{
                            if(ailabel[col][row].getName().toLowerCase().contains("black")){
                                originlabel = ailabel[col][row];
                                for(int col2 = 0; col2 < testlabel.length; col2++){
                                    for(int row2 = 0; row2 < testlabel.length; row2++){
                                        destinationlabel = ailabel[col][row];
                                        if(isMoveValid(originlabel,destinationlabel,pturn)){
                                            for(int col3 = 0; col3 < testlabel.length; col3++){
                                                for(int row3 = 0; row3 < testlabel.length; row3++){
                                                    if(originlabel.equals(ailabel[col3][row3])){
                                                        originx=row;
                                                        originy=col;
                                                    }
                                                    if(destinationlabel.equals(testlabel[col][row])){
                                                        destinationx=row;
                                                        destinationy=col;
                                                    }
                                                }
                                            }
                                            turncounter++;
                                            vector.add(originx);
                                            vector.add(originy);
                                            vector.add(destinationx);
                                            vector.add(destinationy);
                                            ailabel[destinationy][destinationx].setName(originlabel.getName());
                                            ailabel[originy][originx].setName("empty");
                                            vector = bestMove(vector,1);
                                        }
                                    }
                                } 
                            }
                        }
                    }
                }
            }
            else return vector;
        }
        else{
            if(globalvector.size()>=vector.size()||globalvector.size()==0){
                globalvector = vector;
                checkmatesize = globalvector.size()/4;
            }
            
        }
        return vector;
    }*/
    
    public void boardSetup(){
        try {
            ImageIcon frameicon = new ImageIcon(getClass().getResource("resources/frameicon.png"));
            super.setIconImage(frameicon.getImage());
            Image wsquare = ImageIO.read(getClass().getResource("resources/whitesquare.png"));
            Image bsquare = ImageIO.read(getClass().getResource("resources/blacksquare.png"));
            Image blackpawn = ImageIO.read(getClass().getResource("resources/blackpawn.png"));
            Image blackrook = ImageIO.read(getClass().getResource("resources/blackrook.png"));
            Image blackknight = ImageIO.read(getClass().getResource("resources/blackknight.png"));
            Image blackbishop = ImageIO.read(getClass().getResource("resources/blackbishop.png"));
            Image blackqueen = ImageIO.read(getClass().getResource("resources/blackqueen.png"));
            Image blackking = ImageIO.read(getClass().getResource("resources/blackking.png"));
            Image whitepawn = ImageIO.read(getClass().getResource("resources/whitepawn.png"));
            Image whiterook = ImageIO.read(getClass().getResource("resources/whiterook.png"));
            Image whiteknight = ImageIO.read(getClass().getResource("resources/whiteknight.png"));
            Image whitebishop = ImageIO.read(getClass().getResource("resources/whitebishop.png"));
            Image whitequeen = ImageIO.read(getClass().getResource("resources/whitequeen.png"));
            Image whiteking = ImageIO.read(getClass().getResource("resources/whiteking.png"));

            map = new HashMap<>();
            map.put("wsquare", wsquare);
            map.put("bsquare", bsquare);
            map.put("blackpawn", blackpawn);
            map.put("blackrook", blackrook);
            map.put("blackknight", blackknight);
            map.put("blackbishop", blackbishop);
            map.put("blackqueen", blackqueen);
            map.put("blackking", blackking);
            map.put("whitepawn", whitepawn);
            map.put("whiterook", whiterook);
            map.put("whiteknight", whiteknight);
            map.put("whitebishop", whitebishop);
            map.put("whitequeen", whitequeen);
            map.put("whiteking", whiteking);
            
            map2 = new HashMap<>();
            lp.setOpaque(false);
            int sqrlatch = 1;
            for(int i = 0; i < testlabel.length; i++){
                if(sqrlatch == 1){
                    sqrlatch = 0;
                }
                else{
                    sqrlatch = 1;
                }
                for(int j = 0; j < testlabel.length; j++){
                    testlabel[i][j].setName("empty");
                    if(i==7&&(j==0||j==7)){
                        testlabel[i][j].setName("blackrook");
                    }
                    if(i==7&&(j==1||j==6)){
                        testlabel[i][j].setName("blackknight");
                    }
                    if(i==7&&(j==2||j==5)){
                        testlabel[i][j].setName("blackbishop");
                    }
                    if(i==7&&(j==3)){
                        testlabel[i][j].setName("blackking");
                    }
                    if(i==7&&(j==4)){
                        testlabel[i][j].setName("blackqueen");
                    }
                    if(i==6){
                        testlabel[i][j].setName("blackpawn");
                    }
                    if(i==1){
                        testlabel[i][j].setName("whitepawn");
                    }
                    if(i==0&&(j==0||j==7)){
                        testlabel[i][j].setName("whiterook");
                    }
                    if(i==0&&(j==1||j==6)){
                        testlabel[i][j].setName("whiteknight");
                    }
                    if(i==0&&(j==2||j==5)){
                        testlabel[i][j].setName("whitebishop");
                    }
                    if(i==0&&(j==3)){
                        testlabel[i][j].setName("whiteking");
                    }
                    if(i==0&&(j==4)){
                        testlabel[i][j].setName("whitequeen");
                    } 
                    buttons[i][j].setBounds(100*j,100*i,100,100);
                    if(sqrlatch == 0){
                        if(testlabel[i][j].getName().equals("empty")){
                            buttons[i][j].setIcon(new ImageIcon(wsquare));
                            buttons[i][j].setName("wsquare");
                            map2.put(buttons[i][j], testlabel[i][j]);
                            testlabel[i][j].setBounds(buttons[i][j].getBounds());
                            lp.add(testlabel[i][j],new Integer(2));
                        }
                        else {
                            buttons[i][j].setIcon(new ImageIcon(wsquare));
                            ImageIcon icon = new ImageIcon(map.get(testlabel[i][j].getName()));
                            testlabel[i][j].setIcon(icon);
                            testlabel[i][j].setBounds(buttons[i][j].getBounds());
                            map2.put(buttons[i][j], testlabel[i][j]);
                            lp.add(testlabel[i][j],new Integer(2));

                        }
                        sqrlatch = 1;
                    }
                    else{
                        if(testlabel[i][j].getName().equals("empty")){
                            buttons[i][j].setIcon(new ImageIcon(bsquare));
                            buttons[i][j].setName("bsquare");
                            map2.put(buttons[i][j], testlabel[i][j]);
                            testlabel[i][j].setBounds(buttons[i][j].getBounds());
                            lp.add(testlabel[i][j],new Integer(2));
                        }
                        else{
                            buttons[i][j].setIcon(new ImageIcon(bsquare));
                            ImageIcon icon = new ImageIcon(map.get(testlabel[i][j].getName()));
                            testlabel[i][j].setIcon(icon);
                            testlabel[i][j].setBounds(buttons[i][j].getBounds());
                            map2.put(buttons[i][j], testlabel[i][j]);
                            lp.add(testlabel[i][j],new Integer(2));
                        }
                        sqrlatch = 0;
                    }
                    lp.add(buttons[i][j],new Integer(1));
                }              
            }
        } 
        catch (IOException e) {
        }
    }
    
    private class UseButtonHandler implements ActionListener
    {
        @Override
        
        public void actionPerformed(ActionEvent e)
        {
            new Thread(){
                @Override
                public void run() {
                    try{
                        if(aiactive==1){
                            if(globalplayerturn==1){
                                Thread.sleep(2000);
                            }
                        }
                    }
                    catch(InterruptedException ex){
                        Thread.currentThread().interrupt();
                    }                                    
                    Object source = e.getSource();
                    if (source instanceof JButton) {
                        JButton buttonz = (JButton) source;
                        
                        if(validMove==1){
                            JLabel labeldestination = map2.get(buttonz);

                            if(tracker==0){
                                if((globalplayerturn==1&&labeldestination.getName().toLowerCase().contains("white"))||(globalplayerturn==2&&labeldestination.getName().toLowerCase().contains("black"))){
                                    if(!labeldestination.getName().equals("empty")){
                                        buttonz.setBorder(BorderFactory.createLineBorder(Color.orange, 2));
                                        tracker=1;
                                        findValidHelp(labeldestination);

                                        ishelpdisplayed=true;
                                        displayHelp();
                                        clicked=buttonz;

                                    }
                                }
                            }
                            else{
                               if(buttonz==clicked){
                                   buttonz.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                                   tracker=0; 
                                   ishelpdisplayed=false;
                                   displayHelp();

                               }
                               else{
                                    JLabel labelprevious = map2.get(clicked);
                                    if(isMoveValid(labelprevious,labeldestination,globalplayerturn)==true){
                                        if(labelprevious.getName().equals("blackking")){
                                            blackcastleleft=false;
                                            blackcastleright=false;
                                        }
                                        else{

                                            if(labelprevious.getName().equals("whiteking")){
                                                whitecastleleft = false;
                                                whitecastleright = false;
                                            }
                                            else{
                                                if(labelprevious.getName().equals("blackrook")){
                                                    if(labelprevious.equals(testlabel[7][0])){
                                                        blackcastleleft=false;
                                                    }
                                                    else{
                                                        if(labelprevious.equals(testlabel[7][7])){
                                                            blackcastleright=false;
                                                        }
                                                    }
                                                }
                                                else{
                                                    if(labelprevious.getName().equals("whiterook")){
                                                        if(labelprevious.equals(testlabel[0][0])){
                                                            whitecastleleft=false;
                                                        }
                                                        else{
                                                            if(labelprevious.equals(testlabel[0][7])){
                                                                whitecastleright=false;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        int castlingactive = 0;
                                        if((labelprevious.getName().equals("blackking")&&labeldestination.getName().equals("blackrook"))||(labelprevious.getName().equals("whiteking")&&labeldestination.getName().equals("whiterook"))){

                                            String nameprevious = labelprevious.getName();
                                            String namedestination = labeldestination.getName();
                                            ImageIcon icon2 = new ImageIcon(map.get(labelprevious.getName()));
                                            ImageIcon icon3 = new ImageIcon(map.get(labeldestination.getName()));
                                            if(labelprevious.getName().equals("blackking")){

                                                if(labeldestination.equals(testlabel[7][0])){
                                                    testlabel[7][1].setName(nameprevious);
                                                    testlabel[7][1].setIcon(icon2);
                                                    testlabel[7][2].setName(namedestination);
                                                    testlabel[7][2].setIcon(icon3);
                                                    castlingactive = 1;
                                                }
                                                else{
                                                    if(labeldestination.equals(testlabel[7][7])){
                                                        testlabel[7][5].setName(nameprevious);
                                                        testlabel[7][5].setIcon(icon2);
                                                        testlabel[7][4].setName(namedestination);
                                                        testlabel[7][4].setIcon(icon3);
                                                        castlingactive = 1;
                                                    }
                                                }
                                            }
                                            else{
                                                if(labeldestination.equals(testlabel[0][0])){
                                                    testlabel[0][1].setName(nameprevious);
                                                    testlabel[0][1].setIcon(icon2);
                                                    testlabel[0][2].setName(namedestination);
                                                    testlabel[0][2].setIcon(icon3);
                                                    castlingactive = 1;
                                                }
                                                else{
                                                    if(labeldestination.equals(testlabel[0][7])){
                                                        testlabel[0][5].setName(nameprevious);
                                                        testlabel[0][5].setIcon(icon2);
                                                        testlabel[0][4].setName(namedestination);
                                                        testlabel[0][4].setIcon(icon3);
                                                        castlingactive = 1;
                                                    }
                                                }
                                            }
                                            labeldestination.setName("empty");
                                            labeldestination.setIcon(null);
                                            labelprevious.setName("empty");
                                            labelprevious.setIcon(null);
                                        }
                                        else{
                                            if(labeldestination.getName().equals("blackrook")&&globalplayerturn==1&&labeldestination.equals(testlabel[7][0])){
                                                blackcastleleft=false;
                                            }
                                            else{
                                                if(labeldestination.getName().equals("blackrook")&&globalplayerturn==1&&labeldestination.equals(testlabel[7][7])){
                                                    blackcastleright=false;
                                                }
                                                else{
                                                    if(labeldestination.getName().equals("whiterook")&&globalplayerturn==2&&labeldestination.equals(testlabel[0][0])){
                                                        whitecastleleft=false;
                                                    }
                                                    else{
                                                        if(labeldestination.getName().equals("whiterook")&&globalplayerturn==2&&labeldestination.equals(testlabel[0][7])){
                                                            whitecastleright=false;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        
                                        for(int col = 0; col < testlabel.length; col++){
                                            for(int row = 0; row < testlabel.length; row++){
                                                if(labelprevious.equals(testlabel[col][row])){
                                                    passOriginx=row;
                                                    passOriginy=col;
                                                }
                                                if(labeldestination.equals(testlabel[col][row])){
                                                    passDestinationx=row;
                                                    passDestinationy=col;
                                                }
                                            }
                                        }
                                        
                                        if(castlingactive==0){
                                            replacePiece(labelprevious,labeldestination,buttonz);
                                        }

                                        
                                        System.out.println("Test:10");
                                        ishelpdisplayed=false;
                                        displayHelp();
                                        updateContent();
                                        clicked.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                                        tracker=0; 
                                        if(globalplayerturn==1){
                                            setFrameTitle("Black player's turn");
                                            globalplayerturn=2;
                                            if(isCheckmateActive(globalplayerturn)){
                                                JOptionPane.showMessageDialog(null, "Checkmate! White player wins.");
                                                setVisible(false);
                                                dispose();
                                            }
                                            else{
                                                if(isCheckActive(globalplayerturn)){
                                                    JOptionPane.showMessageDialog(null, "Check!");
                                                }
                                                movecounter++;
                                            }
                                            System.out.println("Test:11");
                                            if(aiactive==2&&playerMove==1){
                                                    System.out.println("Test:12");
                                                    handleConnection(passOriginy, passOriginx, passDestinationy, passDestinationx);
                                                    
                                            }
                                            else playerMove=1;
                                        }
                                        else{
                                            setFrameTitle("White player's turn");
                                            globalplayerturn=1;

                                            if(isCheckmateActive(globalplayerturn)){
                                                JOptionPane.showMessageDialog(null, "Checkmate! Black player wins.");
                                                setVisible(false);
                                                dispose();
                                            }
                                            else{
                                                if(isCheckActive(globalplayerturn)){
                                                    JOptionPane.showMessageDialog(null, "Check!");
                                                }
                                                movecounter++;
                                                if(aiactive==1){
                                                    computerMove();
                                                }
                                            }
                                            System.out.println("Test:11");
                                            if(aiactive==2&&playerMove==1){
                                                        System.out.println("Test:12");
                                                        handleConnection(passOriginy, passOriginx, passDestinationy, passDestinationx);
                                                
                                            }
                                            else playerMove=1;
                                        }
                                   }
                               }
                            }
                        }
                    }
                }
            }.start();
        }
    }   
    
    public void handleConnectionStart(){
        validMove=0;
        String fromUser="";
        String fromServer="";
        System.out.println("Test:1");
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
        System.out.println("Test:2");
        int state = 0;
        String lOriginY = "";
        String lOriginX = "";
        String lDestinationY = "";
        String lDestinationX = "";
        for(int i = 0;fromServer.charAt(i)!='$';i++){
            if(fromServer.charAt(i)=='/'||fromServer.charAt(i)=='$'){
                state++;
            }
            else{
                if(state==0)lOriginY=lOriginY+fromServer.charAt(i);
                if(state==1)lOriginX=lOriginX+fromServer.charAt(i);
                if(state==2)lDestinationY=lDestinationY+fromServer.charAt(i);
                if(state==3)lDestinationX=lDestinationX+fromServer.charAt(i);
            }
        }
        System.out.println("Test:3");
        int readOriginY, readOriginX, readDestinationY, readDestinationX;
        readOriginY = Integer.parseInt(lOriginY);
        readOriginX = Integer.parseInt(lOriginX);
        readDestinationY = Integer.parseInt(lDestinationY);
        readDestinationX = Integer.parseInt(lDestinationX);
        System.out.println("Test:4");
        validMove=1;
        System.out.println(readOriginY+" "+readOriginX+" "+readDestinationY+" "+readDestinationX);
        buttons[readOriginY][readOriginX].doClick();
        System.out.println("Test:5");
        buttons[readDestinationY][readDestinationX].doClick();
        System.out.println("Test:6");
    }
    
    public void handleConnection(int originy, int originx, int destinationy, int destinationx){
        validMove=0;
        String fromUser="";
        String fromServer="";
        fromUser="mkm/"+Integer.toString(originy)+"/"+Integer.toString(originx)+"/"+Integer.toString(destinationy)+"/"+Integer.toString(destinationx)+"/"+lName+"/"+lID+"/"+lWhite+"/"+lBlack+"/"+lTurn+"$";
        
        
        try{
            try{
                out.writeObject(fromUser);
                fromServer = (String)in.readObject();
            }
            catch(ClassNotFoundException cnf){
                System.err.println("Class has been not found.");
            }
        }
        catch(IOException ioex){
            System.err.println("IO failure.");
        }
        System.out.println(fromServer);
        int state = 0;
        String lOriginY = "";
        String lOriginX = "";
        String lDestinationY = "";
        String lDestinationX = "";
        for(int i = 0;fromServer.charAt(i)!='$';i++){
            if(fromServer.charAt(i)=='/'||fromServer.charAt(i)=='$'){
                state++;
            }
            else{
                if(state==0)lOriginY=lOriginY+fromServer.charAt(i);
                if(state==1)lOriginX=lOriginX+fromServer.charAt(i);
                if(state==2)lDestinationY=lDestinationY+fromServer.charAt(i);
                if(state==3)lDestinationX=lDestinationX+fromServer.charAt(i);
            }
        }
        int readOriginY, readOriginX, readDestinationY, readDestinationX;
        readOriginY = Integer.parseInt(lOriginY);
        readOriginX = Integer.parseInt(lOriginX);
        readDestinationY = Integer.parseInt(lDestinationY);
        readDestinationX = Integer.parseInt(lDestinationX);
        System.out.println(readOriginY+" "+readOriginX+" "+readDestinationY+" "+readDestinationX);
        validMove=1;
        playerMove=0;
        buttons[readOriginY][readOriginX].doClick();
        buttons[readDestinationY][readDestinationX].doClick();
        
        
        //replacePiece(testlabel[readOriginY][readOriginX],testlabel[readDestinationY][readDestinationX],buttons[readDestinationY][readDestinationX]);
        //validMove=1;
    }
    
    public void replacePiece(JLabel labelprevious, JLabel labeldestination, JButton buttonz){
        int promotion = 0;
        if(labelprevious.getName().toLowerCase().contains("pawn")){
            if(globalplayerturn==1){
                for(int i = 0; i < testlabel.length; i++){
                    if(labeldestination.equals(testlabel[7][i])){
                        ImageIcon icon2 = new ImageIcon(map.get("whitequeen"));
                        labeldestination.setName("whitequeen");
                        labeldestination.setIcon(icon2);
                        labeldestination.setBounds(buttonz.getBounds());
                        promotion = 1;
                        break;
                    }
                }
            }
            else{
                for(int i = 0; i < testlabel.length; i++){
                    if(labeldestination.equals(testlabel[0][i])){
                        ImageIcon icon2 = new ImageIcon(map.get("blackqueen"));
                        labeldestination.setName("blackqueen");
                        labeldestination.setIcon(icon2);
                        labeldestination.setBounds(buttonz.getBounds());
                        promotion = 1;
                        break;
                    }
                }
            }
        }
        if(promotion!=1){
            ImageIcon icon2 = new ImageIcon(map.get(labelprevious.getName()));
            labeldestination.setName(labelprevious.getName());
            labeldestination.setIcon(icon2);
            labeldestination.setBounds(buttonz.getBounds());
        }
        labelprevious.setName("empty");
        labelprevious.setIcon(null);


    }
    
    public void updateContent(){
        for(int col = 0; col < testlabel.length; col++){
            for(int row = 0; row < testlabel.length; row++){
                testlabel[col][row].repaint();
            }
        }
        lp.repaint();
    }
    
    public void setFrameTitle(String title){
        super.setTitle(title);
    }
    void findValidHelp(JLabel labeldestination){
        
        for(int col = 0; col < testlabel.length; col++){
            for(int row = 0; row < testlabel.length; row++){
                if(!labeldestination.equals(testlabel[col][row])){
                    if(col==0&&row==7){
                        if(isMoveValid(labeldestination,testlabel[col][row],globalplayerturn)){
                        
                            helpindicator[col][row]=1;
                        }
                    }
                    else{
                        if(isMoveValid(labeldestination,testlabel[col][row],globalplayerturn)){
                        
                        helpindicator[col][row]=1;
                    }
                    }
                    
                }
            }
        }
    }
    
    

    
    private void displayHelp(){
        for(int col = 0; col < testlabel.length; col++){
            for(int row = 0; row < testlabel.length; row++){
                if(helpindicator[col][row]==1){
                    if(ishelpdisplayed==true){
                        buttons[col][row].setBorder(BorderFactory.createLineBorder(Color.orange, 2));
                    }
                    else{
                        buttons[col][row].setBorder(BorderFactory.createLineBorder(Color.black, 1));
                        helpindicator[col][row]=0;
                    }
                }
            }
        }
    }
    
    private boolean isCheckmateActive(int playerturn){
        String turncolour = "";
        int originlabelx = 0, originlabely = 0;
        JLabel checkoriginlabel = null;
        JLabel checkdestinationlabel = null;
        String nameorigin = "";
        String namedestination = "";
        if(playerturn==1){
            turncolour = "white";
        }
        else{
            turncolour = "black";
        }
        for(int col = 0; col < testlabel.length; col++){
            for(int row = 0; row < testlabel.length; row++){
                if(testlabel[col][row].getName().toLowerCase().contains(turncolour)){
                    checkoriginlabel = testlabel[col][row]; 
                    nameorigin = checkoriginlabel.getName();
                    originlabelx=row;
                    originlabely=col;
                    for(int col2 = 0; col2 < testlabel.length; col2++){
                        for(int row2 = 0; row2 < testlabel.length; row2++){
                            checkdestinationlabel=testlabel[col2][row2];
                            namedestination = checkdestinationlabel.getName();
                            if(isMoveValid(checkoriginlabel,checkdestinationlabel,playerturn)){
                                testlabel[col2][row2].setName(nameorigin);
                                testlabel[originlabely][originlabelx].setName("empty");
                                if(!isCheckActive(playerturn)){
                                    testlabel[col2][row2].setName(namedestination);
                                    testlabel[originlabely][originlabelx].setName(nameorigin);
                                    return false;
                                }
                                testlabel[col2][row2].setName(namedestination);
                                testlabel[originlabely][originlabelx].setName(nameorigin);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private boolean isCheckActive(int turn){
        int kingx = -1, kingy = -1;
        JLabel kinglabel = null, threatlabel = null;
        int threatx = -1, threaty = -1;
        String kingenemytype = "";
        if(turn==1)kingenemytype="black";
        else kingenemytype="white";
        for(int col = 0; col < testlabel.length; col++){
            for(int row = 0; row < testlabel.length; row++){
                if(turn==1){
                    if(testlabel[col][row].getName().equals("whiteking")){
                        kingx = row;
                        kingy = col;
                        kinglabel=testlabel[col][row];
                    }
                }
                else{
                    if(testlabel[col][row].getName().equals("blackking")){
                        kingx = row;
                        kingy = col;
                        kinglabel=testlabel[col][row];
                    }
                }
            }
        }
        for(int col = 0; col < testlabel.length; col++){
            for(int row = 0; row < testlabel.length; row++){
                if(testlabel[col][row].getName().equals(kingenemytype+"pawn")){
                    threatx=row;
                    threaty=col;
                    threatlabel=testlabel[col][row];
                    if(isMovePawnValid(threatx,threaty,kingx,kingy,threatlabel,kinglabel))return true;
                }
                if(testlabel[col][row].getName().equals(kingenemytype+"rook")){
                    threatx=row;
                    threaty=col;
                    if(isMoveRookValid(kingx,kingy,threatx,threaty))return true;
                }
                if(testlabel[col][row].getName().equals(kingenemytype+"knight")){
                    threatx=row;
                    threaty=col;
                    if(isMoveKnightValid(kingx,kingy,threatx,threaty))return true;
                }
                if(testlabel[col][row].getName().equals(kingenemytype+"bishop")){
                    threatx=row;
                    threaty=col;
                    if(isMoveBishopValid(kingx,kingy,threatx,threaty))return true;
                }
                if(testlabel[col][row].getName().equals(kingenemytype+"queen")){
                    threatx=row;
                    threaty=col;
                    if(isMoveQueenValid(kingx,kingy,threatx,threaty))return true;
                }
                if(testlabel[col][row].getName().equals(kingenemytype+"king")){
                    threatx=row;
                    threaty=col;
                    if(isMoveKingValid(kingx,kingy,threatx,threaty))return true;
                }
            }
        }
        return false;
    }
    
    boolean isMoveValid(JLabel moveorigin, JLabel movedestination, int playerturn){
        int originx = -1, originy = -1, destinationx = -1, destinationy = -1;
        String nameorigin = moveorigin.getName();
        String namedestination = movedestination.getName();
        for(int col = 0; col < testlabel.length; col++){
            for(int row = 0; row < testlabel.length; row++){
                if(moveorigin.equals(testlabel[col][row])){
                    originx=row;
                    originy=col;
                }
                if(movedestination.equals(testlabel[col][row])){
                    destinationx=row;
                    destinationy=col;
                }
            }
        }
        if(nameorigin.equals("empty"))return false;
        if((moveorigin.getName().toLowerCase().contains("white")&&playerturn==2)||(moveorigin.getName().toLowerCase().contains("black")&&playerturn==1))return false;
        if(moveorigin.equals(movedestination))return false;
        if(((playerturn==1&&movedestination.getName().toLowerCase().contains("white"))==true)||
                ((playerturn==2&&movedestination.getName().toLowerCase().contains("black"))==true)){
            
            if((moveorigin.getName().equals("blackking")&&movedestination.getName().equals("blackrook"))||(moveorigin.getName().equals("whiteking")&&movedestination.getName().equals("whiterook"))){
                
                if(!(movedestination==testlabel[7][0]||movedestination==testlabel[7][7]||movedestination==testlabel[0][0]||movedestination==testlabel[0][7]))return false;
                
                if(moveorigin.getName().equals("blackking")){
                    if(movedestination.equals(testlabel[7][0])){
                        if(blackcastleleft==false)return false;
                        if(isCheckActive(playerturn)==true)return false;
                        if(isMoveValidLeft(originx,originy,destinationx,destinationy)==false)return false;
                        testlabel[originy][originx].setName("empty");
                        testlabel[destinationy][destinationx].setName("empty");
                        testlabel[7][1].setName(nameorigin);
                        testlabel[7][2].setName(namedestination);
                        if(isCheckActive(playerturn)==true){
                            testlabel[7][1].setName("empty");
                            testlabel[7][2].setName("empty");
                            testlabel[originy][originx].setName(nameorigin);
                            testlabel[destinationy][destinationx].setName(namedestination);
                            return false;
                        }
                        testlabel[7][1].setName("empty");
                        testlabel[7][2].setName("empty");
                        testlabel[originy][originx].setName(nameorigin);
                        testlabel[destinationy][destinationx].setName(namedestination);
                        
                        testlabel[originy][originx].setName("empty");
                        testlabel[7][1].setName(nameorigin);
                        if(isCheckActive(playerturn)==true){
                            testlabel[originy][originx].setName(nameorigin);
                            testlabel[7][1].setName("empty");
                            return false;
                        }
                        testlabel[originy][originx].setName(nameorigin);
                        testlabel[7][1].setName("empty");
                        
                        testlabel[originy][originx].setName("empty");
                        testlabel[7][2].setName(nameorigin);
                        if(isCheckActive(playerturn)==true){
                            testlabel[originy][originx].setName(nameorigin);
                            testlabel[7][2].setName("empty");
                            return false;
                        }
                        testlabel[originy][originx].setName(nameorigin);
                        testlabel[7][2].setName("empty");
                        
                        return true;
                    }
                    else{
                        if(movedestination.equals(testlabel[7][7])){
                            if(blackcastleright==false)return false;
                            if(isCheckActive(playerturn)==true)return false;
                            if(isMoveValidRight(originx,originy,destinationx,destinationy)==false)return false;
                            testlabel[originy][originx].setName("empty");
                            testlabel[destinationy][destinationx].setName("empty");
                            testlabel[7][5].setName(nameorigin);
                            testlabel[7][4].setName(namedestination);
                            if(isCheckActive(playerturn)==true){
                                testlabel[7][5].setName("empty");
                                testlabel[7][4].setName("empty");
                                testlabel[originy][originx].setName(nameorigin);
                                testlabel[destinationy][destinationx].setName(namedestination);
                                return false;
                            }
                            testlabel[7][5].setName("empty");
                            testlabel[7][4].setName("empty");
                            testlabel[originy][originx].setName(nameorigin);
                            testlabel[destinationy][destinationx].setName(namedestination);

                            testlabel[originy][originx].setName("empty");
                            testlabel[7][5].setName(nameorigin);
                            if(isCheckActive(playerturn)==true){
                                testlabel[originy][originx].setName(nameorigin);
                                testlabel[7][5].setName("empty");
                                return false;
                            }
                            testlabel[originy][originx].setName(nameorigin);
                            testlabel[7][5].setName("empty");

                            testlabel[originy][originx].setName("empty");
                            testlabel[7][4].setName(nameorigin);
                            if(isCheckActive(playerturn)==true){
                                testlabel[originy][originx].setName(nameorigin);
                                testlabel[7][4].setName("empty");
                                return false;
                            }
                            testlabel[originy][originx].setName(nameorigin);
                            testlabel[7][4].setName("empty");                        
                            return true;
                        }
                    }
                }
                else{
                    if(moveorigin.getName().equals("whiteking")){
                        if(movedestination.equals(testlabel[0][0])){
                            if(whitecastleleft==false)return false;
                            if(isCheckActive(playerturn)==true)return false;
                            if(isMoveValidLeft(originx,originy,destinationx,destinationy)==false)return false;
                            testlabel[originy][originx].setName("empty");
                            testlabel[destinationy][destinationx].setName("empty");
                            testlabel[0][1].setName(nameorigin);
                            testlabel[0][2].setName(namedestination);
                            if(isCheckActive(playerturn)==true){
                                testlabel[0][1].setName("empty");
                                testlabel[0][2].setName("empty");
                                testlabel[originy][originx].setName(nameorigin);
                                testlabel[destinationy][destinationx].setName(namedestination);
                                return false;
                            }
                            testlabel[0][1].setName("empty");
                            testlabel[0][2].setName("empty");
                            testlabel[originy][originx].setName(nameorigin);
                            testlabel[destinationy][destinationx].setName(namedestination);

                            testlabel[originy][originx].setName("empty");
                            testlabel[0][1].setName(nameorigin);
                            if(isCheckActive(playerturn)==true){
                                testlabel[originy][originx].setName(nameorigin);
                                testlabel[0][1].setName("empty");
                                return false;
                            }
                            testlabel[originy][originx].setName(nameorigin);
                            testlabel[0][1].setName("empty");

                            testlabel[originy][originx].setName("empty");
                            testlabel[0][2].setName(nameorigin);
                            if(isCheckActive(playerturn)==true){
                                testlabel[originy][originx].setName(nameorigin);
                                testlabel[0][2].setName("empty");
                                return false;
                            }
                            testlabel[originy][originx].setName(nameorigin);
                            testlabel[0][2].setName("empty");                        

                            return true;
                        }
                        else{
                            if(movedestination.equals(testlabel[0][7])){
                                if(whitecastleright==false)return false;
                                if(isCheckActive(playerturn)==true)return false;
                                if(isMoveValidRight(originx,originy,destinationx,destinationy)==false)return false;
                                
                                testlabel[originy][originx].setName("empty");
                                testlabel[destinationy][destinationx].setName("empty");
                                testlabel[0][5].setName(nameorigin);
                                testlabel[0][4].setName(namedestination);
                                if(isCheckActive(playerturn)==true){
                                    testlabel[0][5].setName("empty");
                                    testlabel[0][4].setName("empty");
                                    testlabel[originy][originx].setName(nameorigin);
                                    testlabel[destinationy][destinationx].setName(namedestination);
                                    return false;
                                }
                                testlabel[0][5].setName("empty");
                                testlabel[0][4].setName("empty");
                                testlabel[originy][originx].setName(nameorigin);
                                testlabel[destinationy][destinationx].setName(namedestination);
                                
                                testlabel[originy][originx].setName("empty");
                                testlabel[0][5].setName(nameorigin);
                                if(isCheckActive(playerturn)==true){
                                    testlabel[originy][originx].setName(nameorigin);
                                    testlabel[0][5].setName("empty");
                                    return false;
                                }
                                testlabel[originy][originx].setName(nameorigin);
                                testlabel[0][5].setName("empty");

                                testlabel[originy][originx].setName("empty");
                                testlabel[0][4].setName(nameorigin);
                                if(isCheckActive(playerturn)==true){
                                    testlabel[originy][originx].setName(nameorigin);
                                    testlabel[0][4].setName("empty");
                                    return false;
                                }
                                testlabel[originy][originx].setName(nameorigin);
                                testlabel[0][4].setName("empty");                         
                                
                                return true;
                            }
                        }
                    }
                }
            }
            else{
                return false;
            }
        }
          
        //Checks for movements on rook piece.
        if(moveorigin.getName().equals("blackrook")||moveorigin.getName().equals("whiterook")){
            if(!isMoveRookValid(originx,originy,destinationx,destinationy))return false;
        }
        //Checks for movements on knight piece.
        if(moveorigin.getName().equals("blackknight")||moveorigin.getName().equals("whiteknight")){
            if(!isMoveKnightValid(originx,originy,destinationx,destinationy))return false;
        }
        //Checks for movements on king piece.
        if(moveorigin.getName().equals("blackking")||moveorigin.getName().equals("whiteking")){
            if(!isMoveKingValid(originx,originy,destinationx,destinationy))return false;  
        }
        //Checks for movements on pawn piece.
        if(moveorigin.getName().equals("blackpawn")||moveorigin.getName().equals("whitepawn")){
            if(!isMovePawnValid(originx,originy,destinationx,destinationy,moveorigin,movedestination))return false;
        }
        //Checks for movements on bishop piece.
        if(moveorigin.getName().equals("blackbishop")||moveorigin.getName().equals("whitebishop")){
            if(!isMoveBishopValid(originx,originy,destinationx,destinationy))return false;  
        }
        //Checks for movements on queen piece.
        if(moveorigin.getName().equals("blackqueen")||moveorigin.getName().equals("whitequeen")){
            if(!isMoveQueenValid(originx,originy,destinationx,destinationy))return false; 
        }
        if(nameorigin.toLowerCase().contains("king")){
            testlabel[originy][originx].setName("empty");
            testlabel[destinationy][destinationx].setName(nameorigin);
            if(isCheckActive(playerturn)==true){
                testlabel[originy][originx].setName(nameorigin);
                testlabel[destinationy][destinationx].setName(namedestination);
                return false;
            }
            testlabel[originy][originx].setName(nameorigin);
            testlabel[destinationy][destinationx].setName(namedestination);
        }
        else{
            testlabel[originy][originx].setName("empty");
            testlabel[destinationy][destinationx].setName("empty");
            if(isCheckActive(playerturn)==true){
                testlabel[destinationy][destinationx].setName(nameorigin);
                if(isCheckActive(playerturn)==true){
                    testlabel[originy][originx].setName(nameorigin);
                    testlabel[destinationy][destinationx].setName(namedestination);
                    return false;
                }
            }
        }
        testlabel[originy][originx].setName(nameorigin);
        testlabel[destinationy][destinationx].setName(namedestination);
        
        return true;
    }
    
    boolean isMoveRookValid(int originx, int originy, int destinationx, int destinationy){
        if(!(originx==destinationx||originy==destinationy))return false;
        if(originx==destinationx){
            if(originy>destinationy){
                if(!isMoveValidTop(originx,originy,destinationx,destinationy))return false;
            }
            else{
                if(!isMoveValidBottom(originx,originy,destinationx,destinationy))return false;
            }
        }
        if(originy==destinationy){
            if(originx>destinationx){
                if(!isMoveValidLeft(originx,originy,destinationx,destinationy))return false;
            }
            else{
                if(!isMoveValidRight(originx,originy,destinationx,destinationy))return false;
            }
        }
        return true;
    }
    boolean isMoveKnightValid(int originx, int originy, int destinationx, int destinationy){
        if(!((originx+1==destinationx&&originy+2==destinationy)||
        (originx-1==destinationx&&originy+2==destinationy)||
        (originx-2==destinationx&&originy+1==destinationy)||
        (originx-2==destinationx&&originy-1==destinationy)||
        (originx+2==destinationx&&originy+1==destinationy)||
        (originx+2==destinationx&&originy-1==destinationy)||
        (originx+1==destinationx&&originy-2==destinationy)||
        (originx-1==destinationx&&originy-2==destinationy)))return false;
        return true;
    }
    boolean isMoveBishopValid(int originx, int originy, int destinationx, int destinationy){
        if(!((destinationx-originx==destinationy-originy)||(destinationx-originx==-(destinationy-originy)))){
            return false;
        }
        if(destinationx-originx==-(destinationy-originy)&&destinationx-originx>0&&destinationy-originy<0){
            if(!isMoveValidTopRight(originx,originy,destinationx,destinationy))return false;
        }
        if(destinationx-originx==destinationy-originy&&destinationx-originx<0&&destinationy-originy<0){
            if(!isMoveValidTopLeft(originx,originy,destinationx,destinationy))return false;
        }
        if(destinationx-originx==destinationy-originy&&destinationx-originx>0&&destinationy-originy>0){
            if(!isMoveValidBottomRight(originx,originy,destinationx,destinationy)) return false;
        }
        if(-(destinationx-originx)==destinationy-originy&&destinationx-originx<0&&destinationy-originy>0){
            if(!isMoveValidBottomLeft(originx,originy,destinationx,destinationy))return false;
        }
        return true;
    }
    boolean isMoveQueenValid(int originx, int originy, int destinationx, int destinationy){
        if(!((destinationx-originx==destinationy-originy)||(destinationx-originx==-(destinationy-originy)||(destinationx==originx)||(destinationy==originy)))){
            return false;
        }
        if(originx==destinationx){
            if(originy>destinationy){
                if(!isMoveValidTop(originx,originy,destinationx,destinationy))return false;
            }
            else{
                if(!isMoveValidBottom(originx,originy,destinationx,destinationy))return false;
            }
        }
        if(originy==destinationy){
            if(originx>destinationx){
                if(!isMoveValidLeft(originx,originy,destinationx,destinationy))return false;
            }
            else{
                if(!isMoveValidRight(originx,originy,destinationx,destinationy))return false;
            }
        }
        if(destinationx-originx==-(destinationy-originy)&&destinationx-originx>0&&destinationy-originy<0){
            if(!isMoveValidTopRight(originx,originy,destinationx,destinationy))return false;
        }
        if(destinationx-originx==destinationy-originy&&destinationx-originx<0&&destinationy-originy<0){
            if(!isMoveValidTopLeft(originx,originy,destinationx,destinationy))return false;
        }
        if(destinationx-originx==destinationy-originy&&destinationx-originx>0&&destinationy-originy>0){
            if(!isMoveValidBottomRight(originx,originy,destinationx,destinationy)) return false;
        }
        if(-(destinationx-originx)==destinationy-originy&&destinationx-originx<0&&destinationy-originy>0){
            if(!isMoveValidBottomLeft(originx,originy,destinationx,destinationy))return false;
        }
        return true;
    }
    boolean isMoveKingValid(int originx, int originy, int destinationx, int destinationy){
        if(!((originx+1==destinationx&&originy-1==destinationy)||
        (originx+1==destinationx&&originy==destinationy)||
        (originx+1==destinationx&&originy+1==destinationy)||
        (originx==destinationx&&originy+1==destinationy)||
        (originx-1==destinationx&&originy+1==destinationy)||
        (originx-1==destinationx&&originy==destinationy)||
        (originx-1==destinationx&&originy-1==destinationy)||
        (originx==destinationx&&originy-1==destinationy)))return false;
        return true;
    }
    boolean isMovePawnValid(int originx, int originy, int destinationx, int destinationy, JLabel moveorigin, JLabel movedestination){
        if(moveorigin.getName().equals("blackpawn")){
            if(originy==6){
                if(!((originx==destinationx&&originy-1==destinationy&&testlabel[destinationy][destinationx].getName().equals("empty"))||
                        (originx==destinationx&&originy-2==destinationy&&testlabel[destinationy][destinationx].getName().equals("empty")&&isMoveValidTop(originx,originy,destinationx,destinationy))||
                        (originx+1==destinationx&&originy-1==destinationy&&movedestination.getName().toLowerCase().contains("white"))||
                        (originx-1==destinationx&&originy-1==destinationy&&movedestination.getName().toLowerCase().contains("white"))))
                    return false;
            }
            else if(!((originx==destinationx&&originy-1==destinationy&&testlabel[destinationy][destinationx].getName().equals("empty"))||
                    (originx+1==destinationx&&originy-1==destinationy&&movedestination.getName().toLowerCase().contains("white"))||
                    (originx-1==destinationx&&originy-1==destinationy&&movedestination.getName().toLowerCase().contains("white"))))
                    return false;
        }
        if(moveorigin.getName().equals("whitepawn")){
            if(originy==1){
                if(!((originx==destinationx&&originy+1==destinationy&&testlabel[destinationy][destinationx].getName().equals("empty"))||
                        (originx==destinationx&&originy+2==destinationy&&testlabel[destinationy][destinationx].getName().equals("empty")&&isMoveValidBottom(originx,originy,destinationx,destinationy))||
                        (originx+1==destinationx&&originy+1==destinationy&&movedestination.getName().toLowerCase().contains("black"))||
                        (originx-1==destinationx&&originy+1==destinationy&&movedestination.getName().toLowerCase().contains("black"))))
                        return false;
            }
            else if(!((originx==destinationx&&originy+1==destinationy&&testlabel[destinationy][destinationx].getName().equals("empty"))||
                    (originx+1==destinationx&&originy+1==destinationy&&movedestination.getName().toLowerCase().contains("black"))||
                    (originx-1==destinationx&&originy+1==destinationy&&movedestination.getName().toLowerCase().contains("black"))))
                    return false;
        }
        return true;
    }
    
    
    
    
    boolean isMoveValidTop(int originx, int originy, int destinationx, int destinationy){
        for(int col = originy-1; col > destinationy; col--){
            if(!testlabel[col][originx].getName().equals("empty"))return false;
        }
        return true;
    }
    
    boolean isMoveValidBottom(int originx, int originy, int destinationx, int destinationy){
        for(int col = originy+1; col < destinationy; col++){
            if(!testlabel[col][originx].getName().equals("empty"))return false;
        }
        return true;
    }
    
    boolean isMoveValidLeft(int originx, int originy, int destinationx, int destinationy){
        for(int row = originx-1; row > destinationx; row--){
            if(!testlabel[originy][row].getName().equals("empty"))return false;
        }
        return true;
    }
    
    boolean isMoveValidRight(int originx, int originy, int destinationx, int destinationy){
        for(int row = originx+1; row < destinationx; row++){
            if(!testlabel[originy][row].getName().equals("empty"))return false;
        }
        return true;
    }
    
    boolean isMoveValidTopLeft(int originx, int originy, int destinationx, int destinationy){
        for(int col = originy-1, row = originx-1 ; col > destinationy; col--, row--){
            if(!testlabel[col][row].getName().equals("empty"))return false;
        }
        return true;
    }
    
    boolean isMoveValidTopRight(int originx, int originy, int destinationx, int destinationy){
        for(int col = originy-1, row = originx+1 ; col > destinationy; col--, row++){
            if(!testlabel[col][row].getName().equals("empty"))return false;
        }
        return true;
    }
    
    boolean isMoveValidBottomLeft(int originx, int originy, int destinationx, int destinationy){
        for(int col = originy+1, row = originx-1 ; col < destinationy; col++, row--){
            if(!testlabel[col][row].getName().equals("empty"))return false;
        }
        return true;
    }
    
    boolean isMoveValidBottomRight(int originx, int originy, int destinationx, int destinationy){
        for(int col = originy+1, row = originx+1 ; col < destinationy; col++, row++){
            if(!testlabel[col][row].getName().equals("empty"))return false;
        }
        return true;
    }
    

    

    
}
