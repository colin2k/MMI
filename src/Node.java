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
	private List<Edge> edges;

	/**
	 * Construct for Node
	 * 
	 * @param index
	 */
	public Node(int index) {
		this.edges = new Vector<>();
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
	 * @param e
	 */
	public void addEdge(Edge e) {
		this.edges.add(e);
	}


	/**
	 * Get Outgoing Edges
	 * 
	 * @return List<Edge> edges
	 */
	public List<Edge> getEdges() {
		return this.edges;
	}

}
