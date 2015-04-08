/**
 * Created by colin on 07.04.2015.
 */
import java.util.List;
public class UnionFind {
    private Node[] nodes;
    private int[] rank;
    int count;

    public UnionFind(List<Node> NL) {
        count = NL.size();
        nodes = new Node[count];
        rank = new int[count];
        for (Node n :NL) {
            nodes[n.getIndex()] = n;
            rank[n.getIndex()] = 0;
        }
    }

    public Node find(Node i) {

        Node p = nodes[i.getIndex()];
        if (i == p) {
            return i;
        }
        return nodes[i.getIndex()] = find(p);

    }

    public Node[] getNodes(){
        return this.nodes;
    }

    public void union(Node i, Node j) {

        Node root1 = find(i);
        Node root2 = find(j);

        if (root2 == root1) return;

        if (rank[root1.getIndex()] > rank[root2.getIndex()]) {
            nodes[root2.getIndex()] = root1;
        } else if (rank[root2.getIndex()] > rank[root1.getIndex()]) {
            nodes[root1.getIndex()] = root2;
        } else {
            nodes[root2.getIndex()] = root1;
            rank[root1.getIndex()]++;
        }
    }

    public boolean connected(Node  p, Node q) {
        return find(p) == find(q);
    }




}
