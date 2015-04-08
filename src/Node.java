/**
 * GraphenBibliothek
 * 
 * @author Oliver Colin Sauer
 * @MIS
 * 
 * @Class Node
 */

import java.util.List;
import java.util.Vector;

public class Node {

	private int index;
	private boolean visited = false;
	private double weight;
	private List<Edge> incommingEdges;
	private List<Edge> outgoingEdges;

	/**
	 * Construct for Node
	 * 
	 * @param int
	 */
	public Node(int index) {
		this.incommingEdges = new Vector<Edge>();
		this.outgoingEdges = new Vector<Edge>();
		this.index = index;
	}

	public boolean equals(Node n){
		return (this.index == n.index);
	}
	
	/**
	 * visit Node
	 */
	public void visit(){
		this.visited = true;
	}
	
	/**
	 * unvisit Node
	 */
	public void unvisit(){
		this.visited = false;
	}
	/**
	 * get visited value
	 * 
	 * @return
	 */
	public boolean getVisited(){
		return this.visited;
	}

	/**
	 * get Node index
	 * 
	 * @return index
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Node index to String
	 * 
	 * @return String index
	 */
	public String toString() {
		return " " + String.valueOf(this.index) + " ";
	}

	/**
	 * Add Incoming Edge to List
	 * 
	 * @param Edge
	 */
	public void addIncomingEdge(Edge e) {
		this.incommingEdges.add(e);
	}

	/**
	 * Add Outgoing Edge to List
	 * 
	 * @param Edge
	 */
	public void addOutgoingEdge(Edge e) {
		this.outgoingEdges.add(e);
	}

	/**
	 * Get Incoming Edges
	 * 
	 * @return List<Edge> IncomingEdges
	 */
	public List<Edge> getIncomingEdges() {
		return this.incommingEdges;
	}

	/**
	 * Get Outgoing Edges
	 * 
	 * @return List<Edge> OutgoingEdges
	 */
	public List<Edge> getOutgoingEdges() {
		return this.outgoingEdges;
	}

}
