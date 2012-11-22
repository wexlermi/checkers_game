/** 
Mike Wexler
Block 7
Checkers Project	
 */

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import java.util.*;

public class Checkers extends javax.swing.JApplet implements ActionListener{
	private Image mainPage;
	private Image splashPage;
	private Image offScreenBuffer;
	private Image redChecker;
	private Image blackChecker;
	private Image redKing;
	private Image blackKing;

	private Image youWin;
	private Image computerWins;	
	
	public javax.swing.Timer timer = new javax.swing.Timer(50, this);
	
	
	
	
	
	
	private static final int BOARD_WIDTH = 593;
	private  static final int BOARD_X_POS = 28; //refers to coordinates of upper left hand corner of board
	private  static final int BOARD_Y_POS = 87;
	private static final int SQUARE_WIDTH = (int)(BOARD_WIDTH/8.0);
	private int movingCheckerX;
	private int movingCheckerY;
	private int pressedX;
	private int pressedY;
	private int leftBoarder;
	private int topBoarder;
	private int upLeftCornerX;
	private int upLeftCornerY; 
	
	private int correction = 2; //corrects when computer moves, when roundoff error
	
	private static final int v = 15; //velocity of a computer moving checker
	private static  double vy;
	private static  double vx; //all these variables defined in method moveCheckerSlowly()
	private static double compMovCheckX;
	private static double compMovCheckY;
	private static int t; //time
	private static int timerTime; 
	
	private boolean loadSplashPage;
	private boolean loadMainPage;
	
	private static boolean userWinsBool;
	private static boolean computerWinsBool;
	private static boolean gameOver;
	private static boolean gameStarted =false;
	private static boolean compCheckerMoving = false;
	private boolean compCheckerJustMoving = false;
	private boolean compCheckerJumping = false;
	
	private boolean jumpRecur;
	public static boolean madeCompKing;
	private boolean compMoving;
	
	private Checker movingChecker;
	private static Checker  movingCompCheck;
	private Checker recurCheckerToMove;
	
	private Checker jumpedCheck;
	private static Game game;
	private static Board board;
	
	//private static WexTimer wexTimer;
	
	
	
	
	
	
	
	

	
	
	
	
// IMPORTANT: Source code between BEGIN/END comment pair will be regenerated
// every time the form is saved. All manual changes will be overwritten.
// BEGIN GENERATED CODE
	// member declarations
// END GENERATED CODE

	boolean isStandalone = false;

	public Checkers() {

	}

	// Retrieve the value of an applet parameter
	public String getParameter(String key, String def) {
		return isStandalone ? System.getProperty(key, def) :
			(getParameter(key) != null ? getParameter(key) : def);
	}

	// Get info on the applet parameters
	public String[][] getParameterInfo() {
		return null;
	}

	// Get applet information
	public String getAppletInfo() {
		return "Applet Information";
	}

	// Initialize the applet
	public void init() {
		mainPage =  getImage(getDocumentBase(),"Java Classes/mainPage.png");
 		offScreenBuffer=this.createImage(size().width,size().height);
		splashPage = getImage(getDocumentBase(),"Java Classes/splashPage.jpg");
		
		redChecker = getImage(getDocumentBase(),"Java Classes/redChecker.png");
		blackChecker = getImage(getDocumentBase(),"Java Classes/blackChecker.png");
		redKing = getImage(getDocumentBase(),"Java Classes/redKing.png");
		blackKing = getImage(getDocumentBase(),"Java Classes/blackKing.png");

		youWin =  getImage(getDocumentBase(),"Java Classes/youWin.png");
		computerWins =  getImage(getDocumentBase(),"Java Classes/computerWins.png");
		loadSplashPage = true;

		//timer.start();





		try {
			initComponents();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initComponents() throws Exception {
// IMPORTANT: Source code between BEGIN/END comment pair will be regenerated
// every time the form is saved. All manual changes will be overwritten.
// BEGIN GENERATED CODE
		// the following code sets the frame's initial state
		setLocation(new java.awt.Point(0, 0));
		getContentPane().setLayout(null);


		setSize(new java.awt.Dimension(637, 706));

		// event handling
		addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseDragged(java.awt.event.MouseEvent e) {
				thisMouseDragged(e);
			}
		});
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				thisMouseClicked(e);
			}
			public void mousePressed(java.awt.event.MouseEvent e) {
				thisMousePressed(e);
			}
			public void mouseReleased(java.awt.event.MouseEvent e) {
				thisMouseReleased(e);
			}
		});

// END GENERATED CODE
	}
	
	public void paint(Graphics g){
		Graphics gr= offScreenBuffer.getGraphics();
		if (loadSplashPage) gr.drawImage(splashPage, 0, 0, this);
		else if (loadMainPage) {
			gr.drawImage(mainPage,0,0, this); 
			if (gameStarted||gameOver){ //draw checkers if game started
				Checker[][] checkersArray = game.getBoard().getCheckersArray();
				
				for (int row = 0; row < 8; row++){ //goes to each checker in array and draws it
					for (int col = 0; col < 8; col++){
						Checker checker = checkersArray[row][col];
						if (movingCompCheck==null || !(movingCompCheck.equals(checker))){
							if (checker != null){
								if (checker.isRed()) {
									if (checker.isKing()) gr.drawImage(redKing,getX(checker), getY(checker), this); 
									else gr.drawImage(redChecker,getX(checker), getY(checker), this); 
								}
								else{
									if (checker.isKing()) gr.drawImage(blackKing,getX(checker), getY(checker), this); 
									else gr.drawImage(blackChecker,getX(checker), getY(checker), this); 
								}
							}
						}
					}
				}
				if (movingChecker != null){
					if (movingChecker.isRed()) {
						if (movingChecker.isKing()) gr.drawImage(redKing,movingCheckerX - (pressedX - leftBoarder), movingCheckerY - (pressedY - topBoarder), this);
						else gr.drawImage(redChecker,movingCheckerX - (pressedX - leftBoarder), movingCheckerY - (pressedY - topBoarder), this);
					}
					else{
						if (movingChecker.isKing()) gr.drawImage(blackKing,movingCheckerX - (pressedX - leftBoarder), movingCheckerY - (pressedY - topBoarder), this);
						else gr.drawImage(blackChecker,movingCheckerX - (pressedX - leftBoarder), movingCheckerY - (pressedY - topBoarder), this);
					}
				}
				if (movingCompCheck != null){
					if (movingCompCheck.isKing() && !madeCompKing) gr.drawImage(redKing,(int)compMovCheckX+correction,(int)compMovCheckY+correction, this);
					else gr.drawImage(redChecker,(int)compMovCheckX+correction, (int)compMovCheckY+correction, this);
				}
			}
			if (userWinsBool)
				gr.drawImage(youWin,290,20, this); 
			else if (computerWinsBool)
				gr.drawImage(computerWins,290,20, this); 
		}
			
		g.drawImage(offScreenBuffer,0,0,this);

	}
	
	public void thisMouseClicked(java.awt.event.MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.println("Clicked (" + x + ", " + y + ")");
		
		if (loadSplashPage){
			if (x>52 && x < 283 && y > 464 & y < 591) {  //if clicked button on first page
				loadSplashPage = false; 
				loadMainPage = true;
			}
		}
		else if (loadMainPage){
			if (x>28 && x < 222 && y > 9 & y < 36) {  // if clicked new game me first
				newGame(true);
			}
			else if (x>29 && x < 224 && y > 44 & y < 71){//if clicked new hard comp first
				newGame(false);
			}
		}

				
		repaint();

	}
	
	public void newGame(boolean meFirst){
		initializeStuff();
		game = new Game(meFirst);
		board = game.getBoard();
		gameStarted = true;
		userWinsBool = false;
		computerWinsBool = false;
		gameOver = false;
		if (!meFirst) computerMove();
	}
	
	public void initializeStuff(){
			vy=0;
			vx=0; //all these variables defined in method moveCheckerSlowly()
			compMovCheckX=0;
			compMovCheckY=0;
			t=0; //time
			timerTime=0; 

			
			 userWinsBool= false;
			 computerWinsBool= false;
			 gameOver= false;
			 gameStarted =false;
			 compCheckerMoving = false;
	         compCheckerJustMoving = false;
		     compCheckerJumping = false;		
		     jumpRecur = false;
		     madeCompKing= false;
			 compMoving= false;
			
			 movingChecker = null;
			 movingCompCheck = null;
			 recurCheckerToMove = null;
			
			 jumpedCheck = null;
			 Game game = null;
			 Board board = null;
	}
	
	public static int getX(Checker c){
		return (int)(c.location().getCol() * BOARD_WIDTH/8.0 + BOARD_X_POS);
	}
	public static int getY(Checker c){
		return (int)(c.location().getRow() * BOARD_WIDTH/8.0 + BOARD_Y_POS);
	}

	public static int getX(Location loc){
		return (int)(loc.getCol() * BOARD_WIDTH/8.0 + BOARD_X_POS);
	}
	public static int getY(Location loc){
		return (int)(loc.getRow() * BOARD_WIDTH/8.0 + BOARD_Y_POS);
	}

	public static int getCol(int x){
		return (int)((x - BOARD_X_POS)/SQUARE_WIDTH);
	}
	
	public static int getRow(int y){
		return (int)((y - BOARD_Y_POS)/SQUARE_WIDTH);
	}
	
	public void thisMouseDragged(java.awt.event.MouseEvent e) {
		
		if (gameStarted && !compMoving){
			int movingCheckerX1 = e.getX(); //these are temporary, and will not be used if checker outside range of board
			int movingCheckerY1 = e.getY();
			if (movingChecker != null){
				movingCheckerX = movingCheckerX1;
				movingCheckerY = movingCheckerY1;
			}
			repaint();
		}
	}
	
	public void thisMouseReleased(java.awt.event.MouseEvent e) {
		if (gameStarted && !compMoving){
			int x = e.getX();
			int y = e.getY();
			int col = getCol(x);
			int row = getRow(y);
			
			if (movingChecker != null) {
				Location newLoc = new Location(row, col);
		
				if (game.getBoard().canJump(movingChecker, newLoc)){
					game.getBoard().jump(movingChecker, newLoc);
					if (!(game.getBoard().checkerCanJump(movingChecker))){
						computerMove();
					}
				}
				else if (game.getBoard().playerCanJump(false)){
					game.getBoard().move(movingChecker, movingChecker.location());
				}
				else if(game.getBoard().move(movingChecker, newLoc)){ //returns true if moved successfuly, and alerts computer to move
					computerMove();

				}
				if (!(game.getBoard().computerCanMove()) || game.getBoard().getCompCheckers().size() == 0) userWins();

				movingChecker = null;	
			}
			repaint();
			
		}		
	}
	
	public void thisMousePressed(java.awt.event.MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if (loadMainPage&&gameStarted && !compMoving ){
			if (x > 28 && x < 620 && y > 86 && y < 677){ //if clicked in board
				int row = getRow(y);
				int col = getCol(x);
				Location loc = new Location(row, col);
				Board board = game.getBoard();
				if (!(board.isEmpty(loc)) && !(board.checkerAt(loc).isRed())) { //if square at click has a checker and the checker there is black (player checker color)
					//test if clicked outside of checker, or in checker itself (using precalc circle formula)
				
					
					
					int radius = 37;
					int centerX = BOARD_X_POS + col * SQUARE_WIDTH + SQUARE_WIDTH/2;
					int centerY = BOARD_Y_POS + row * SQUARE_WIDTH + SQUARE_WIDTH/2;					
					
					int yPred1 = -(int)(Math.sqrt(1225- Math.pow(x - centerX, 2))
						- centerY); //predicted y on top of circle;
					int yPred2 = yPred1 + 2 * (centerY - yPred1); //predicted y on bottom of circle
					
					if (x > BOARD_X_POS + col * SQUARE_WIDTH + 4 && x < BOARD_X_POS + (col + 1)*SQUARE_WIDTH - 4){
						if (y < centerY && y > yPred1 || y>= centerY && y < yPred2){
							//clicked in circle     =)
							leftBoarder = BOARD_X_POS + col * SQUARE_WIDTH;
							topBoarder = BOARD_Y_POS + row * SQUARE_WIDTH;
							movingChecker = board.checkerAt(loc);
							board.remove(movingChecker);
							movingCheckerX = x;
							movingCheckerY = y;
							pressedX = x;
							pressedY = y;
						}
					}
				}
			}
			
			
			
			
		}
		repaint();

	}
	
	public boolean movingCheckerIsInBoard(){
		return movingCheckerX > 29 && movingCheckerX < 619 - 70 && movingCheckerY > 86 && movingCheckerY < 677-70;
	}
	
	public static  Game getGame(){
		return game;
	}
	
	public  void userWins(){
		System.out.println("USER WINS");
		userWinsBool = true;
		computerWinsBool = false;
		gameStarted = false;
		gameOver = true;
	}
	
	public static   void computerWins(){
		System.out.println("COMP WINS");
		userWinsBool = false;
		computerWinsBool = true;
		gameStarted = false;
		gameOver = true;
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e){
		
		if (timerTime < t){
			compMovCheckX+=vx;
			compMovCheckY+=vy;
			timerTime++;
			repaint();
		}
		else {
			timerTime=0;
			timer.stop();
			compMoving = false;
			movingCompCheck = null;
			if (jumpRecur) {
				board.remove(jumpedCheck);
				jumpedCheck = null;
				computerMove();
			}
			repaint();
		}
	}
	
	public void moveCheckerSlowly(Checker check, Location newLoc){
		movingCompCheck = check;
		double dx = getX(newLoc) - getX(check.location());//(newLoc.getCol() - movingCompCheck.location().getCol())*SQUARE_WIDTH; //xdistance between locations
		double dy = getY(newLoc) - getY(check.location());//(newLoc.getRow() - movingCompCheck.location().getRow())*SQUARE_WIDTH;//ydist ""
		double d = (Math.sqrt(dx*dx+dy*dy)); //distance between two locs
		
		t = (int)(d/v); //time
		vx = (Math.sqrt(v*v/(1+dy*dy/(dx*dx)))); //x velocity
		vy = (Math.sqrt(v*v-vx*vx));//y velocity
		if (dx<0) vx*=-1;
		if(dy<0) vy*=-1;
		compMovCheckX = getX(movingCompCheck);
		compMovCheckY = getY(movingCompCheck);
		
		repaint();
		timer.start();
		compMoving = true;
	}
	
	public void computerMove(){
		compMoving = false;
		if (jumpRecur){
			Location newLoc = findNewLocation(recurCheckerToMove);
			if (newLoc != null && board.canJump(recurCheckerToMove, newLoc)){


				jumpedCheck = board.preyToBeJumped(recurCheckerToMove, newLoc);			
				moveCheckerSlowly(recurCheckerToMove, newLoc);				
				board.jump(recurCheckerToMove, newLoc);
				board.add(jumpedCheck);
				jumpRecur = true;

			}
			else{
				jumpRecur = false;			
			}
			
		}
		else{
			Checker checkerToMove;
			boolean checkerCanJump = true;
			checkerToMove = findCheckerToMove(); //returns null if no checkers can move
			if (checkerToMove != null){
				Location newLoc = findNewLocation(checkerToMove);
				if (board.canJump(checkerToMove, newLoc)){
					if (newLoc != null && board.canJump(checkerToMove, newLoc)){
						jumpedCheck = board.preyToBeJumped(checkerToMove, newLoc);			
						moveCheckerSlowly(checkerToMove, newLoc);				
						board.jump(checkerToMove, newLoc);
						board.add(jumpedCheck);
						jumpRecur = true;
						recurCheckerToMove = checkerToMove;
					}
				}
				else {
					moveCheckerSlowly(checkerToMove, newLoc);
					board.move(checkerToMove, newLoc);
				}
			
			}
		}
		if (!(board.userCanMove()) || board.getUserCheckers().size() == 0) Checkers.computerWins();

		madeCompKing = false;
		repaint();
	}
	
	public Checker findCheckerToMove(){
		ArrayList compCheckers = board.getCompCheckers();
		ArrayList movableCheckers = new ArrayList();
		//remove checkers that dont have a move
		for (int i = 0; i < compCheckers.size(); i++){
			Checker check = (Checker)(compCheckers.get(i));
			if (board.checkerCanMove(check)) movableCheckers.add(check);
		}
		
		if(movableCheckers.size()==0) return null;
		//rank movable checkers by in danger or not, then by how many checkers they can jump
		for (int i = 0; i < movableCheckers.size(); i++){
			Checker check = (Checker)(movableCheckers.get(i));
			check.setInDanger(board.checkerInDanger(check));
			check.setNumCheckersCanJump(board.howManyCheckersCanJump(check));
		}
		

		Collections.sort(movableCheckers); //sorts by in danger and then how many checkers each can jump
		ArrayList bestCheckers = new ArrayList();		
		boolean equalRank = true;
		int size = movableCheckers.size();
		Checker last = (Checker)(movableCheckers.get(size-1));
		int i = size;
		while(equalRank && i>=1){
			i--;
			Checker here = (Checker)(movableCheckers.get(i));
			equalRank = last.compareTo(here) == 0;
			if (equalRank)	bestCheckers.add(here);
		}
		
		ArrayList kingableCheckers = new ArrayList();
		for (int h = 0; h < bestCheckers.size(); h++){
			Checker checker = (Checker)(bestCheckers.get(h));
			if (board.isKingable(checker)) kingableCheckers.add(checker);
		}
		
		int rand;
		Checker checker;
		if (kingableCheckers.size() > 0) {
			rand = (int)(kingableCheckers.size()*Math.random());
			checker = (Checker)(kingableCheckers.get(rand));
		}
		else{
			rand = (int)(bestCheckers.size()*Math.random());
			checker = (Checker)(bestCheckers.get(rand));
		}
		return checker;
	}
	
	public Location findNewLocation(Checker checker){
		ArrayList prey = new ArrayList();
		if (board.checkerCanJump(checker)) {
			Checker checkerToJump = board.checkerToJumpForMostJumps(checker);
			return board.getNextLocationInSequence(checker.location(), checkerToJump.location());
		}
		else{
			ArrayList possibleNewLocs = 
				board.locationsCheckerCanMoveToWithoutJumping(checker);
			boolean checkerInDanger = true;
			int rand = 0;
			if (possibleNewLocs.size() == 0) return null;
			rand = (int)(Math.random()*possibleNewLocs.size());
			Location loc =  (Location)(possibleNewLocs.get(rand));
			while (checkerInDanger && possibleNewLocs.size()>=1){ //will check if the new location will be one that will put the checker in danger
				rand = (int)(Math.random()*possibleNewLocs.size());
				Checker newChecker = new Checker(checker.isRed(), (Location)(possibleNewLocs.get(rand)));
				checkerInDanger = board.checkerInDanger(newChecker);
				if (checkerInDanger) possibleNewLocs.remove(rand);
			}
			if (possibleNewLocs.size()>0) return (Location)(possibleNewLocs.get(rand));
			else return loc;
		}
	}
	
}