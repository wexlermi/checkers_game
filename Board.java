import java.util.*;
public class Board{
	private Checker[][] checkersArray;
	private ArrayList allCheckers;
	private ArrayList compCheckers;
	private ArrayList userCheckers;
	public Board(){
		checkersArray = new Checker[8][8];
		allCheckers = new ArrayList();
		compCheckers = new ArrayList();
		userCheckers = new ArrayList();
		for (int r = 0; r < 8; r++){
			for (int c = 0; c < 8; c++){
				Checker checker;
				if (r <= 2){  //this code will put red checkers near the top of the board, and black checkers near the bottom of the board
					if (r%2 ==0 && c%2!=0 || r%2 != 0 && c%2==0){
						checker = new Checker(true, new Location(r,c));			
						add(checker);
					}

				}
				else if (r>=5){
					if (r%2 ==0 && c%2!=0 || r%2 != 0 && c%2==0){
						checker = new Checker(false, new Location(r,c));			
						add(checker);
					}
				}
			}
		}


	}
	
	public ArrayList getAllCheckers(){
		return allCheckers;
		
	}
	
	public ArrayList getCompCheckers(){
		return compCheckers;
	}
	
	public ArrayList getUserCheckers(){
		return userCheckers;
	}
	
	public boolean isEmpty(Location loc){
		return isValidLocation(loc) && checkerAt(loc) == null;
	}
	
	public Checker checkerAt(Location loc){
		int row = loc.getRow();
		int col = loc.getCol();
		//System.out.println(checkersArray[row][col]);
		return checkersArray[row][col];
	}
	
	public void remove(Checker checker){

		checkersArray[checker.location().getRow()][checker.location().getCol()] = null;
		allCheckers.remove(checker);
		compCheckers.remove(checker);
		userCheckers.remove(checker);
		//checker = null;
	}
	
	public Checker[][] getCheckersArray(){
		return checkersArray;
	}
	
	public void add(Checker checker){
		checkersArray[checker.location().getRow()][checker.location().getCol()] = checker;	
		allCheckers.add(checker);
		if (checker.isRed()) compCheckers.add(checker);
		else userCheckers.add(checker);
	}
	public boolean move(Checker checker, Location newLoc){
		if (isValidMove(checker, newLoc)) {
			remove(checker);
			checker.setLocation(newLoc);
			if (!(checker.isKing()) && (checker.isRed() && 
			newLoc.getRow()==7 || (!(checker.isRed())&&
			 newLoc.getRow() ==0))){  //if the checker hits the top or bottom we make it a king
						checker.makeKing();
						if (checker.isRed()) Checkers.madeCompKing = true;
			}
			add(checker);
			return true;
		}
		else add(checker); //return checker to original place
		return false;
	}
	
	public boolean isValidMove(Checker checker, Location newLoc){
		if (isValidLocation(newLoc) && isEmpty(newLoc) && isDiagonal(checker, newLoc)){
			if (!(checker.isKing())){			
				return locAhead(checker, newLoc);
			}
			else
				return true;
				
		}
		return false;
	}
	
	public boolean isValidLocation(Location loc){
		return loc.getRow() < 8 && loc.getCol() < 8 && loc.getRow() >= 0 && loc.getCol() >=0;
	} //only valid if inside board
	
	public boolean locAhead(Checker checker, Location newLoc){
		if (checker.isRed())
			return newLoc.getRow() > checker.location().getRow();
		else
			return newLoc.getRow() < checker.location().getRow();
	}
	
	
	//returns true only if checker is 1 away from other checker and is diagonal
	public boolean isDiagonal(Checker checker, Location newLoc){
		Location oldLoc = checker.location();
		return Math.abs(oldLoc.getRow() - newLoc.getRow()) == 1 && Math.abs(oldLoc.getCol() - newLoc.getCol()) == 1;
	}
	
	//returns true only if checker is 1 away from other checker and is diagonal
	public boolean isDiagonal(Checker check1, Checker check2){
		return Math.abs(check1.location().getRow() - check2.location().getRow()) ==1 && Math.abs(check1.location().getCol() - check2.location().getCol())==1;
	}

	public boolean checkerInDanger(Checker checker){
		ArrayList checkerNeighbors = getCheckerNeighbors(checker);
		for (int i = 0; i < checkerNeighbors.size(); i++){
			if (canJump((Checker)(checkerNeighbors.get(i)), checker)) return true;
		}	
		return false;
	}
	
	public ArrayList getNeighbors(Checker checker){
		ArrayList neighbors = new ArrayList();
		Location loc = checker.location();
		for (int i = 1; i <=4; i++){
			Location newLoc = getNeighbor(loc, new Direction(i));
			if (isValidLocation(newLoc)) neighbors.add(newLoc);
		}
		return neighbors;
	}
	
	public ArrayList getCheckerNeighbors(Checker checker){
		ArrayList neighbors = getNeighbors(checker);
		ArrayList checkerNeighbors = new ArrayList();
		for (int i = 0; i < neighbors.size(); i++){
			if (!(isEmpty((Location)(neighbors.get(i)))))
				checkerNeighbors.add(checkerAt((Location)(neighbors.get(i))));
		}
		return checkerNeighbors;
	}
	
	public boolean canJump(Checker check1, Checker check2){		
		return check1.isRed() != check2.isRed() && isDiagonal(check1, check2) 
			&& isEmpty(getNeighbor(check2.location(), getDirection(check1.location(), check2.location())))
			&& (check1.isKing() || isBelow(check1, check2));
	}
	
	public boolean checkerCanJump(Checker checker){
		ArrayList checkerNeighbors = getCheckerNeighbors(checker);
		for (int i = 0; i < checkerNeighbors.size(); i++){
			if (canJump(checker, ((Checker)(checkerNeighbors.get(i))))) return true;
		}
		return false;
	}
	
	
	//relative to player or computer
	public boolean isBelow(Checker check1, Checker check2){
		return check1.isRed() && (check1.location().getRow() < check2.location().getRow()) || (!(check1.isRed())) && (check1.location().getRow() > check2.location().getRow());
	}
	
	public boolean checkerCanMove(Checker check){
		if (checkerCanJump(check) || checkerCanMoveWithoutJumping(check)) return true;
		return false;
	}
	
	public boolean checkerCanMoveWithoutJumping(Checker check){
		ArrayList emptyNeighbors = emptyNeighbors(check);
		for (int i = 0; i < emptyNeighbors.size(); i++){
			if (isValidMove(check, (Location)(emptyNeighbors.get(i)))) return true;
		}
		return false;
	}
	
	public ArrayList locationsCheckerCanMoveToWithoutJumping(Checker check){
		ArrayList emptyNeighbors = emptyNeighbors(check);
		ArrayList validNewLocs = new ArrayList();
		for (int i = 0; i < emptyNeighbors.size(); i++){
			if (isValidMove(check, (Location)(emptyNeighbors.get(i))))
				validNewLocs.add(emptyNeighbors.get(i));
		}
		return validNewLocs;
	}
	
	
	public ArrayList emptyNeighbors(Checker check){
		ArrayList neighbors = getNeighbors(check);
		ArrayList emptyNeighbors = new ArrayList();
		for (int i = 0; i < neighbors.size(); i++){
			if (isEmpty((Location)(neighbors.get(i)))) emptyNeighbors.add(neighbors.get(i));
		}
		return emptyNeighbors;
	}
	public Location getNeighbor(Location fromLoc, Direction dir){
		if (dir.direction() == 1) return new Location(fromLoc.getRow()-1, fromLoc.getCol() +1);
		else if (dir.direction() == 2) return new Location(fromLoc.getRow()-1, fromLoc.getCol() - 1);
		else if (dir.direction() == 3) return new Location(fromLoc.getRow()+1, fromLoc.getCol() -1);
		else if (dir.direction() == 4) return new Location(fromLoc.getRow()+1, fromLoc.getCol() +1);
		return null;
	}
	
	//directions are numbered counter clockwise from 45 deg on a unit circle:
		//45:1; 135:2 etc.
	//only in immediate vicinity, abs val of rowdiff and coldiff should = 1
	public Direction getDirection(Location fromLoc, Location toLoc){
		int rowDiff = toLoc.getRow() - fromLoc.getRow();
		int colDiff = toLoc.getCol() - fromLoc.getCol();
		if (colDiff > 0 && rowDiff <0) return new Direction(1);
		else if (colDiff < 0 && rowDiff <0) return new Direction(2);
		else if (colDiff < 0 && rowDiff >0) return new Direction(3);
		else if (colDiff > 0 && rowDiff >0) return new Direction(4);
		return new Direction(-1);
	}
	
	public Location getNextLocationInSequence(Location loc1, Location loc2){
		return getNeighbor(loc2, getDirection(loc1, loc2));
	} 
	
	public ArrayList getPrey(Checker check){
		ArrayList checkerNeighbors = getCheckerNeighbors(check);
		ArrayList prey = new ArrayList();
		for (int i = 0; i < checkerNeighbors.size(); i++){
			Checker check2 = (Checker)(checkerNeighbors.get(i));
			if (canJump(check, check2)) prey.add(check2);
		}
		return prey;
	}
	
	//returns how many checkers this checker can jump
	public int howManyCheckersCanJump(Checker check){
		ArrayList prey = getPrey(check);
		int maxJumps = 0;
		int temp = 0;
		if (!(checkerCanJump(check))) return 0;
		for (int i = 0; i < prey.size(); i++){
			Location jumpToLoc = getNextLocationInSequence(check.location(), 
				((Checker)(prey.get(i))).location());
			if (isEmpty(jumpToLoc)) temp = 1 + howManyCheckersCanJump(new Checker(check.isRed(), jumpToLoc));
			if (temp > maxJumps) maxJumps = temp;
		}
		return maxJumps;
	}
	public Checker checkerToJumpForMostJumps(Checker check){
		ArrayList prey = getPrey(check); //this method tries to pick the checker a checker should initially jump for the maximum amount of jumps
		int maxJumps = 0;
		int temp = 0; 
		if (!(checkerCanJump(check))) return null;
		Checker bestCheckerToJump = null;
		for (int i = 0; i < prey.size(); i++){
			Location jumpToLoc = getNextLocationInSequence(check.location(), 
				((Checker)(prey.get(i))).location());
			if (isEmpty(jumpToLoc)) temp = 1 + howManyCheckersCanJump(new Checker(check.isRed(), jumpToLoc)); //recursion here
			if (temp > maxJumps) {
				maxJumps = temp;
				bestCheckerToJump = (Checker)(prey.get(i));
			}
		}
		return bestCheckerToJump;
	}
	
	public boolean canJump(Checker check, Location newLoc){
		//System.out.println("6 checker " + check + " newLoc " + newLoc);
		Location preyLoc = getNeighbor(check.location(), getDirection(check.location(), newLoc));
		if (preyLoc!=null){
			Checker prey = checkerAt(preyLoc);
			if (prey != null){
				if (canJump(check, prey)) return true;
			}
		}
		return false;
	}
	
	
	//returns checker that is jumped
	public Checker jump(Checker check, Location newLoc){
		Location preyLoc = getNeighbor(check.location(), getDirection(check.location(), newLoc));
		Checker prey = checkerAt(preyLoc);
		remove(prey);
		move(check, preyLoc);
		move(check, newLoc);
		return prey;
	}
	
	public Checker preyToBeJumped(Checker check, Location newLoc){
		Location preyLoc = getNeighbor(check.location(), getDirection(check.location(), newLoc));
		Checker prey = checkerAt(preyLoc);
		return prey;
	}
	
	public boolean userCanMove(){
		ArrayList userCheckers = getUserCheckers();
		for (int i = 0; i < userCheckers.size(); i++){
			if (checkerCanMove((Checker)(userCheckers.get(i)))) return true;
		}
		return false;
	}
	
	public boolean computerCanMove(){
		ArrayList compCheckers = getCompCheckers();
		for (int i = 0; i < compCheckers.size(); i++){
			if (checkerCanMove((Checker)(compCheckers.get(i)))) return true;
		}
		return false;	
	}
	
	public boolean playerCanJump(boolean isRed){
		ArrayList checkers;
		if (isRed)	checkers = getCompCheckers();
		else	checkers = getUserCheckers();
		for (int i = 0; i < checkers.size(); i++){
			Checker checker = (Checker)(checkers.get(i));
			if (checkerCanJump(checker)) return true;
		}
		return false;
	}
	
	public boolean isKingable(Checker check){
		return !(check.isKing()) && (check.isRed() && 
		check.location().getRow() == 6 || !(check.isRed()) 
		&& check.location().getRow()==1);
	}
}