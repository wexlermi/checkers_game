
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import java.util.*;

public class Checker implements Comparable {
	private Location location;
	private boolean isRed;
	private boolean isKing;
	
	private boolean inDanger;
	private boolean canJump;
	private int numCheckersCanJump;
	
	public Checker(boolean red, Location loc){
		isRed = red;
		location = loc;
	}
	
	public Location location(){
		return location;
	}
	
	public void setLocation(Location loc){
		location = loc;
	}

	public boolean isRed(){
		return isRed;
	}
	
	public String toString(){
		if (isRed) return "RED CHECKER AT " + location;
		else return "BLACK CHECKER AT " + location;
	}
	
	public boolean isKing(){
		return isKing;
	}
	
	public void makeKing(){
		isKing = true;
	}
	
	public void setInDanger(boolean d){
		inDanger = d;
	}
	public void setNumCheckersCanJump(int n){
		numCheckersCanJump = n;
	}

	public boolean isInDanger(){
		return inDanger;
	}
	public int numCheckersCanJump(){
		return numCheckersCanJump;
	}
	
	public int compareTo(Object check2){
		//compares two checkers by first seeing which one has at least one jump,
		//then which one is in danger, then by how many checkers can be jumped
		Checker check3 = (Checker)check2;
		if (numCheckersCanJump >= 1 && check3.numCheckersCanJump() == 0)
			return 1;
		else if (numCheckersCanJump == 0 && check3.numCheckersCanJump() >= 1)
			return -1;
		
		if (inDanger && !(check3.isInDanger()))
			return 1;
		else if (!inDanger && check3.isInDanger())
			return -1;
		
		if (numCheckersCanJump > check3.numCheckersCanJump())
			return 1;
		else if (numCheckersCanJump < check3.numCheckersCanJump())
			return -1;
		else
			return 0;
	}
}