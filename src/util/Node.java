		package util;

import java.util.ArrayList;

public abstract class Node implements Comparable<Node> {
    protected int depth; //number of moves made so far (is equal to the number of nodes developed) on this branch
    protected int parentID; //parent ID for node tracking
    protected int heuristicValue; //value of the calculated heuristic

	public Node() { //values by default
    	depth = 0; 
    	parentID = -1; //it does not have parent unless we say another thing
	}
	
    public int getDepth() {return depth;}
	
    public int getParentID() {return parentID;}
    
	public int getHeuristicValue() { return heuristicValue; }
	
    public boolean equals(Node n) {
		return (n.toString().equals(toString()));
	}
    
	public int initialValuePruneLimit() {
		return Integer.MAX_VALUE; //implementation by default
	}
    
	@Override
	public int compareTo(Node node) { //BRANCHING METHOD
		int totalValue = heuristicValue;
		int totalValueToBeCompared = node.getHeuristicValue();
		
		if (totalValue > totalValueToBeCompared) return 1; //node has less priority (is bigger)
		else if (totalValue == totalValueToBeCompared) return 0; //the same priority
		else return -1; //node has more priority (is smaller)
	}
    
	public abstract void calculateHeuristicValue();
	public abstract ArrayList<Node> expand();
	public abstract boolean isSolution();
}
