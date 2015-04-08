/**
 * GraphenBibliothek
 * 
 * @author Oliver Colin Sauer
 *
 * MIS
 * 
 * Class Edge
 */

public class Edge {
	
	private Node start;
	private Node end;
	private double weight = Double.NaN;

	// Node i connects with Node j
	public Edge(Node i, Node j) {
		this.start = i;
		this.end = j;
	}

	// Node i connects with Node j with weight w
	public Edge(Node i, Node j, double w) {
		this.start = i;
		this.end = j;
		this.weight = w;
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
