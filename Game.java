//import java.awt.*;
//import java.awt.event.*;
//import java.applet.*;
//import javax.swing.*;
//import java.util.*;
public class Game { //implements ActionListener{
	//private Computer computer;
	private Board board;
	
	////private static final int v = 10; //velocity of a computer moving checker
	//private static  int vy;
	//private static  int vx; //all these variables defined in method moveCheckerSlowly()
	//private static int compMovCheckX;
	//private static int compMovCheckY;
	//private static int t; //time
	//private static int timerTime; 
	
	private static Checker  movingCompCheck;
	//public javax.swing.Timer timer = new javax.swing.Timer(100, this);
	
	public Game (boolean m){
		board = new Board();
		//computer = new Computer();
	}
	
	public Board getBoard(){
		return board;
	}
	
	//public Computer getComputer(){
		//return computer;
	//}
	
	//public void actionPerformed(java.awt.event.ActionEvent e){
	//	System.out.println("action performed");
	//	
	//	if (timerTime < t){
	//		compMovCheckX+=vx;
	//		compMovCheckY+=vy;
	//		timerTime++;
	//	}
	//	timerTime=0;
	//	movingCompCheck = null;
	//}
	
}