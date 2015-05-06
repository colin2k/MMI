/**
 * GraphenBibliothek
 *
 * @author Oliver Colin Sauer
 * @MIS
 * @Class Node
 */

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Node implements Comparable<Node> {

    private int index;
    private boolean visited = false;
    private LinkedList<Edge> edges;

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    private Double weight;
    /**
     * Construct for Node
     *
     * @param index int
     */
    public Node(int index) {
        this.edges = new LinkedList<>();
        this.index = index;
    }
    public Node(int index,Double weight){
        this.edges = new LinkedList<>();
        this.index = index;
        this.weight = weight;
    }

    public boolean equals(Node n) {
        return (this.index == n.index);
    }

    /**
     * Compare Node to another
     *
     * @param n @NotNull Edge
     * @return int
     */
    public int compareTo(Node n) {
        return Double.compare(this.weight, n.weight);
    }
    /**
     * visit Node
     */
    public void visit() {
        this.visited = true;
    }

    /**
     * unvisit Node
     */
    public void unvisit() {
        this.visited = false;
    }

    /**
     * get visited value
     *
     * @return boolean
     */
    public boolean getVisited() {
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
     * @param e Edge
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

    public Edge getCheapestNaibourEdge() {
        PriorityQueue<Edge> tmpPrio = new PriorityQueue<>();
        tmpPrio.addAll(this.getEdges());
        return tmpPrio.poll();
    }

    public void removeEdge(Edge edge) {
        this.edges.remove(edge);

    }
}

