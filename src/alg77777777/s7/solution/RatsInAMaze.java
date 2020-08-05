package alg77777777.s7.solution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import util.BranchAndBound;
import util.Node;


public class RatsInAMaze extends BranchAndBound {

    private static final int size = 7; ////////
	private static int[][]maze = new int[size][size];
    
	public static void main(String[] args) {
		
		String fileName = args[0];
		int initialPosition = Integer.parseInt(args[1]);
		int finalPosition = Integer.parseInt(args[2]);
		
		readFile(fileName);
		System.out.println("THE INITIAL MAZE IS AS FOLLOWS:");
		printMaze();
		
		System.out.println("\nThe goal is to go from position " + initialPosition
				+ " to position " + finalPosition + '\n');
		
		RatsInAMaze problem = new RatsInAMaze(initialPosition, finalPosition, maze);
		
		long t1 = System.currentTimeMillis();
		problem.branchAndBound(problem.getRootNode());
		long t2 = System.currentTimeMillis();
		
		problem.printSolutionTrace();
		problem.printInfo(fileName, (t2-t1));

	}
	
	private void printInfo(String fileName, long execTime) {
		System.out.println("Case: " + fileName + "\t size: " + size 
				+ "\t Execution Time: " + execTime
				+ "\t Generated Nodes: " + this.generatedNodes
				+ "\t Processed Nodes: " + this.processedNodes
				+ "\t Pruned Nodes: " + this.prunedNodes);
		
	}

	public RatsInAMaze(int initPos, int finalPos, int[][] maze){
		rootNode = new Table(maze, initPos, finalPos);

	}
	
	static void readFile(String name) {
		  String line = "";
		  StringTokenizer dividedLine; //to get the parts of each line
		  BufferedReader reader=null; //to read each line of the file

		  try {
			  reader = new BufferedReader(new FileReader(name));
			  for (int i=0;i<size;i++) {
				  line = reader.readLine();
				  dividedLine = new StringTokenizer(line);
				  for(int j=0; j<size; j++)
					  maze[i][j] = Integer.parseInt(dividedLine.nextToken());
			  }
		  } catch(Exception e) { 
			  System.out.println("There is no file!");
		  }
		  try { //to close the reader
			  if (reader != null) reader.close();
		  } catch (IOException e)
		  {}
	  }
	
	private static void printMaze() {
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++)
				System.out.print(maze[i][j] + " ");
			System.out.println();
		}
		
	}
	

}
/***************************************************/


/***************************************************/
class Table extends Node {
    
	private static final int ANIMAL = 2;
	private int[][] maze; //board
    private int currentPosX;
    private int currentPosY;
    private static final int size = 7; /////////
	private  int finalPosition; //goal position to exit the maze
	
    
    public Table(int[][] maze, int initialPosition, int finalPosition) { //generates a fresh maze (ROOT NODE)
    	this.maze=maze;
    	int[] coord = calculateCoordinates(initialPosition);
    	this.currentPosX = coord[0];
    	this.currentPosY = coord[1];
    	maze[currentPosY][currentPosX]=ANIMAL;
    	this.finalPosition = finalPosition;
    	calculateHeuristicValue();
    }

    public Table(int[][] maze, int currentPosX, int currentPosY, int depth, int parentID, int finalPosition) {
        this.maze = maze;
        this.currentPosX = currentPosX;
        this.currentPosY = currentPosY;
        this.depth = depth;
        this.parentID = parentID;
        this.finalPosition=finalPosition;
        calculateHeuristicValue();
    }
    
	private static int[] calculateCoordinates(int currentPosition){
		int[] aux = new int[2];
		aux[0] = currentPosition%size; // x position
		aux[1] = currentPosition/size; // y position
		return aux;
	}

    /* Returns a copy of the maze but in this
     * case, moving the animal upwards */
    private int[][] up() {
    	int[][] temp = new int[size][size];
    	for(int i=0; i < size; i++){
			for(int j=0; j < size; j++){
				temp[i][j]=maze[i][j];
			}
    	}
        //temp[currentPosY][currentPosX] = 0;
        temp[currentPosY-1][currentPosX] = 2;
        return temp;
    }
  
    /* Returns a copy of the board but in this
     * case, moving the animal downwards */
    private int[][] down() {
    	int[][] temp = new int[size][size];
    	for(int i=0; i < size; i++){
			for(int j=0; j < size; j++){
				temp[i][j]=maze[i][j];
			}
    	}
        temp[currentPosY+1][currentPosX] = 2;
        return temp;
    }

    /* Returns a copy of the board but in this
     * case, moving the animal to the left */
    private int[][] left() {
    	int[][] temp = new int[size][size];
    	for(int i=0; i < size; i++){
			for(int j=0; j < size; j++){
				temp[i][j]=maze[i][j];
			}
    	}
        temp[currentPosY][currentPosX-1] = 2;
        return temp;
    }
 
    /* Returns a copy of the board but in this
     * case, moving the animal to the right */
    private int[][] right() {
        int[][] temp = new int[size][size];
    	for(int i=0; i < size; i++){
			for(int j=0; j < size; j++){
				temp[i][j]=maze[i][j];
			}
    	}
        temp[currentPosY][currentPosX+1] = 2;
        return temp;
    }

 
    //Heuristic: Manhattan distance
    private int getManhattanHeuristicValue() {
        double manhattan = 0;
        int[] finalPos = calculateCoordinates(finalPosition);
        double horizontalDistance = Math.abs(currentPosX-finalPos[0]);
        double verticalDistance = Math.abs(currentPosY-finalPos[1]);
        
        manhattan = Math.sqrt(horizontalDistance*horizontalDistance + verticalDistance*verticalDistance);
        
        return (int)manhattan;
    }
    

   
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("===============\n");
    	for(int i=0; i<size; i++){
			for(int j=0; j<size; j++)
				sb.append(maze[i][j] + " ");
			sb.append('\n');
		}
        return sb.toString();
    }
    

    @Override
    public void calculateHeuristicValue() {
		heuristicValue =  getManhattanHeuristicValue();
    }
    
    
    
    /* To get the children of the current node. They 
     * point to their parent through the parentID */
	@Override
	public ArrayList<Node> expand() {
		ArrayList<Node> result = new ArrayList<Node>();
		int[][] testBoard;
	    Table temp;
	       
	    //Possible movements of the animal towards an empty cell
	    if(currentPosY != 0 && maze[currentPosY-1][currentPosX]==0){
	    	testBoard = up(); //UP
	    	temp = new Table(testBoard, currentPosX, currentPosY-1, depth+1, hashCode(), this.finalPosition);
	    	result.add(temp);
	    }
	        
	    if(currentPosY!= size -1 && maze[currentPosY+1][currentPosX]==0){
	    	testBoard = down(); //DOWN
	    	temp = new Table(testBoard, currentPosX, currentPosY+1, depth+1, hashCode(), this.finalPosition);
	    	result.add(temp);
	    }
	        
	    if(currentPosX != 0 && maze[currentPosY][currentPosX-1]==0){
	    	testBoard = left(); //LEFT
	    	temp = new Table(testBoard, currentPosX-1, currentPosY, depth+1, hashCode(), this.finalPosition);
	    	result.add(temp);
	    }
	        
	    if(currentPosX != size-1 && maze[currentPosY][currentPosX+1]==0){
	    	testBoard = right(); //RIGHT
	    	temp = new Table(testBoard, currentPosX+1, currentPosY, depth+1, hashCode(),  this.finalPosition);
	    	result.add(temp);
	    }
	    
	    return result;
	}

	@Override
	public boolean isSolution() {
		return (getHeuristicValue() == 0) ? true : false;
	}

}


