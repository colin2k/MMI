import java.util.Comparator;

/**
 * GraphenBibliothek
 * 
 * @author Oliver Colin Sauer
 *
 * MIS
 * 
 * Class Edge
 */

public class Edge implements Comparable<Edge> {
	
	private Node start;
	private Node end;
	private Double weight = Double.NaN;

	// Node i connects with Node j
	public Edge(Node i, Node j) {
		this.start = i;
		this.end = j;
	}

	// Node i connects with Node j with weight w
	public Edge(Node i, Node j, Double w) {
		this.start = i;
		this.end = j;
		this.weight = w;
	}
	public int compareTo(Edge e1){
		//System.out.println(this.weight+" : "+ e1.weight+":"+Double.compare(this.weight, e1.weight));
		return Double.compare(this.weight, e1.weight);
	}

	// returns weight
	public double getWeight() {
		return this.weight;
	}

	public Node getStart() {
		return this.start;
	}

	public Node getEnd() {
		return this.end;
	}

	public String toString() {
		
		if(this.weight != Double.NaN){
			return "(" + String.valueOf(this.start) + ","
					+ String.valueOf(this.end) + ", "
					+ String.valueOf(this.weight) +" )";	
		}else{
			return "(" + String.valueOf(this.start) + ","
					+ String.valueOf(this.end) + ")";
		}
		
	}
	public int compare(Edge e){
		return Double.compare(this.getWeight(), e.getWeight());
	}

}
