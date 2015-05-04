/**
 * GraphenBibliothek
 *
 * @author Oliver Colin Sauer
 *         <p/>
 *         MIS
 *         <p/>
 *         Class Edge
 */

public class Edge implements Comparable<Edge> {

    private Node start;
    private Node end;
    private Double weight = Double.NaN;
    private Edge e1;

    /**
     * Initialize Edge
     *
     * @param i Node from
     * @param j Node to
     */
    public Edge(Node i, Node j) {
        this.start = i;
        this.end = j;
    }

    /**
     * Initialize Edge
     *
     * @param i Node from
     * @param j Node to
     * @param w Double weight
     */
    public Edge(Node i, Node j, Double w) {
        this.start = i;
        this.end = j;
        this.weight = w;
    }

    /**
     * Compare Edge to another
     *
     * @param e1 @NotNull Edge
     * @return int
     */
    public int compareTo(Edge e1) {

        return Double.compare(this.weight, e1.weight);

    }

    /**
     * Get Edge weight
     *
     * @return Double weight
     */
    public double getWeight() {
        return (this.weight == null) ? 0.0 : this.weight;
    }

    /**
     * get Start Node
     *
     * @return Node from
     */
    public Node getStart() {
        return this.start;
    }

    /**
     * get End Node
     *
     * @return Node to
     */
    public Node getEnd() {
        return this.end;
    }

    /**
     * Edge to String
     *
     * @return String
     */
    public String toString() {

        if (this.weight != Double.NaN) {
            return "(" + String.valueOf(this.start) + ","
                    + String.valueOf(this.end) + ", "
                    + String.valueOf(this.weight) + " )";
        } else {
            return "(" + String.valueOf(this.start) + ","
                    + String.valueOf(this.end) + ")";
        }

    }

    /**
     * Compare current Edge to another
     *
     * @param e Edge
     * @return int
     */
    public int compare(Edge e) {
        return Double.compare(this.getWeight(), e.getWeight());
    }

    /**
     * Reverse Edge
     * set StartNode as Endnode
     * and Endnode as Startnode
     *
     * @return Edge
     */
    public Edge reverse() {
        return new Edge(this.getEnd(), this.getStart(), this.getWeight());
    }
}
