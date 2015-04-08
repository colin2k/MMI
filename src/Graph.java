/**
 * GraphenBibliothek
 *
 * @author Oliver Colin Sauer
 * @MIS
 * @Class Graph
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;


public class Graph {

    final static int AD_MATRIX = 0;
    final static int EDGE_LIST = 1;
    final static int EDGE_LIST_WEIGHT = 2;

    private List<Node> nodes;
    private List<Edge> edges;

    private Comparator<Edge> edgeComparator = new Comparator<Edge>() {
        @Override
        public int compare(Edge e1, Edge e2) {
            return e1.compare(e2);
        }
    };

    /**
     * empty construct - initialize node list - initialize edge list
     */
    public Graph() {
        this.nodes = new Vector<Node>();
        this.edges = new Vector<Edge>();
    }

    public Graph(List<Node> n, List<Edge> e) {
        this.nodes = n;
        this.edges = e;

    }

    public Graph clone() {
        return new Graph(this.getNodes(), this.getEdges());
    }

    /**
     * construct using file as source
     * - initialize node list
     * - initialize edgelist
     * - read file into graph
     *
     * @param source   BufferedReader
     * @param fileType int (AD_MATRIX | EDGE_LIST)
     */
    public Graph(BufferedReader source, int fileType) {
        this.nodes = new Vector<Node>();
        this.edges = new Vector<Edge>();

        this.initFromSource(source, fileType);
    }


    public String toString() {
        String result = "Nodes: [";
        for (Node n : this.nodes) {
            result += n;

        }
        result += "]\nEdges:\n";
        for (Edge e : this.edges) {
            result += e + "\n";

        }
        return result;
    }

    private void unVisitNodes() {
        for (Node n : this.nodes) {
            n.unvisit();
        }
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

    /**
     * Read file into current Graph
     *
     * @param source   BufferedReader
     * @param fileType int
     */
    public void initFromSource(BufferedReader source, int fileType) {

        try {
            // get number of nodes from first line
            int nodeCount = Integer.parseInt(source.readLine());
            for (int i = 0; i < nodeCount; i++) {
                // create Node and add to Graph
                this.addNode(new Node(i));
            }
            // reading Edges
            String currentLine;

            for (int currentNode = 0; (currentLine = source.readLine()) != null; currentNode++) {
                // for each node in the current Line
                if (fileType == AD_MATRIX) {
                    // remove spaces
                    currentLine = currentLine.replaceAll("\\s", "");
                    for (int i = 0; i < nodeCount; i++) {
                        try {
                            if (currentLine.charAt(i) == '1') {
                                // create new Edge
                                Edge newEdge = new Edge(
                                        this.nodes.get(currentNode),
                                        this.nodes.get(i));

                                // adding Edge to Graph
                                this.nodes.get(currentNode).addOutgoingEdge(
                                        newEdge);
                                this.nodes.get(i).addIncomingEdge(newEdge);

                                this.addEdge(newEdge);

                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            System.out.println("wrong FileType ?!");
                            return;
                        }
                    }
                } else if (fileType == EDGE_LIST) {
                    String[] straBuf = currentLine.split("\\s");
                    Node nodeFrom = this.nodes
                            .get(Integer.parseInt(straBuf[0]));
                    Node nodeTo = this.nodes.get(Integer.parseInt(straBuf[1]));

                    Edge newEdge = new Edge(nodeFrom, nodeTo);

                    nodeFrom.addOutgoingEdge(newEdge);
                    nodeTo.addIncomingEdge(newEdge);
                    this.addEdge(newEdge);
                } else if (fileType == EDGE_LIST_WEIGHT) {
                    String[] straBuf = currentLine.split("\\s");
                    Node nodeFrom = this.nodes
                            .get(Integer.parseInt(straBuf[0]));
                    Node nodeTo = this.nodes.get(Integer.parseInt(straBuf[1]));

                    Edge newEdge = new Edge(nodeFrom, nodeTo, Double.parseDouble(straBuf[2]));

                    nodeFrom.addOutgoingEdge(newEdge);
                    nodeTo.addIncomingEdge(newEdge);
                    this.addEdge(newEdge);
                } else {
                    System.out.println("Fehler: Falscher Dateityp");
                }
            }

        } catch (IOException e) {
            System.out.println("Fehler beim einlesen.");
        }
    }


    // adding a Node to Graph
    private void addNode(Node n) {
        this.nodes.add(n);
    }

    public Node getNode(int index) {
        return this.nodes.get(index);
    }

    // adding an Edge to Graph
    private void addEdge(Edge e) {
        this.edges.add(e);
    }

    // depth-first search
    public Graph depthSearch(Node start) {

        // Initialize result Graph and visitedNodes List
        Graph result = new Graph();

        // reset visited flag for all Nodes
        this.unVisitNodes();

        // partial graphCounter
        int graphCounter = 1;

        // adding start-node to Graph
        this.recDepthSearch(start, result);

        for (Node currentNode : this.nodes) {

            if (!(currentNode.getVisited())) {

                // mark current node as visited
                currentNode.visit();
                result.addNode(currentNode);

                graphCounter++;
                // start recursion
                this.recDepthSearch(currentNode, result);
            }
        }

        System.out.println(graphCounter + " Teilgraph(en)");

        return result;
    }

    // Recursive depth-first search
    private void recDepthSearch(Node start, Graph result) {

        // check incoming edges
        // only non directed graphs
        for (Edge currentEdge : start.getIncomingEdges()) {

            // if start Node of currentEdge has not been visited
            if (!currentEdge.getStart().getVisited()) {

                // visit Start Node
                currentEdge.getStart().visit();

                // and to result-graph if it isn't already in there
                if (!(currentEdge.getStart().getVisited())) {
                    result.nodes.add(currentEdge.getStart());
                }
                // create new Edge from start Node to current Edge start Node
                result.edges.add(new Edge(start, currentEdge.getStart()));

                //recursion call with - start node of current Edge, result graph

                this.recDepthSearch(currentEdge.getStart(), result);
            }
        }


        // check outgoing edges
        for (Edge currentEdge : start.getOutgoingEdges()) {

            // if end Node of the current Edge has not been visited
            if (!currentEdge.getEnd().getVisited()) {

                // visit End Node
                currentEdge.getEnd().visit();

                // and to result-graph if it isn't already in there
                if (!currentEdge.getEnd().getVisited()) {
                    result.nodes.add(currentEdge.getEnd());
                }

                // create new Edge from start Node to current Edge start Node
                result.edges.add(new Edge(start, currentEdge.getEnd()));

				/*
                 * recursion call with end node of current Edge and result graph
				 */
                this.recDepthSearch(currentEdge.getEnd(), result);
            }
        }
    }

    // breadth-first search
    public Graph breadthSearch(Node start) {
        // Initialize result Graph and llNodes LinkedList of Nodes as Stack
        Graph result = new Graph();
        LinkedList<Node> llNodes = new LinkedList<Node>();

        // reset visited flag for all Nodes
        this.unVisitNodes();

        // Temporary Node to pop Nodes from Stack
        Node tmpNode;

        // add Start Node to Queue
        llNodes.add(start);

        // add Start Node to result-graph
        result.addNode(start);

        // until Queue is empty
        while (!llNodes.isEmpty()) {

            // Pop First Element from Stack
            tmpNode = llNodes.pop();

            // for each incoming edge in current node as currentEdge
            for (Edge currentEdge : tmpNode.getIncomingEdges()) {

                // if end of current Edge not in result-graph
                if (!(currentEdge.getStart().getVisited())) {

                    // visit Node
                    currentEdge.getStart().visit();
                    // put start of current Edge on stack
                    llNodes.add(currentEdge.getStart());

                    // add start of current Edge to result Graph
                    result.addNode(currentEdge.getStart());

                    // add current Edge to result Graph
                    result.addEdge(currentEdge);
                }
            }

            // for each outgoing edge in current node as currentEdge
            for (Edge currentEdge : tmpNode.getOutgoingEdges()) {

                // if end of current Edge not in result-graph
                if (!(currentEdge.getEnd().getVisited())) {

                    // put end of current Edge on stack
                    llNodes.add(currentEdge.getEnd());

                    // add end of current Edge to result Graph
                    result.addNode(currentEdge.getEnd());

                    // add current Edge to result Graph
                    result.addEdge(currentEdge);
                }
            }

        }

        return result;
    }


    // Minimal Spanning tree (Prim-Algoithmus)
    public Graph prim(Node start) {

        this.unVisitNodes();

        Graph result = new Graph();
        result.addNode(start);

        PriorityQueue<Edge> prioEdgeQueue = new PriorityQueue<>(start.getOutgoingEdges().size(), this.edgeComparator);
        prioEdgeQueue.addAll(start.getOutgoingEdges());

        start.visit();

        for (Edge currentEdge = prioEdgeQueue.remove(); !prioEdgeQueue.isEmpty(); currentEdge = prioEdgeQueue.remove()) {
            if (currentEdge.getEnd().getVisited() &&
                    !currentEdge.getStart().getVisited()) {

                result.addEdge(currentEdge);

                currentEdge.getStart().visit();
                result.addNode(currentEdge.getStart());
                for (Edge nextEdge : currentEdge.getStart().getOutgoingEdges()) {
                    prioEdgeQueue.add(nextEdge);
                }

            } else if (!currentEdge.getEnd().getVisited() &&
                    currentEdge.getStart().getVisited()) {
                result.addEdge(currentEdge);

                currentEdge.getEnd().visit();
                result.addNode(currentEdge.getEnd());
                for (Edge nextEdge : currentEdge.getEnd().getOutgoingEdges()) {
                    prioEdgeQueue.add(nextEdge);
                }
            }
        }
        return result;
    }

    public Graph kruskal() {

        this.unVisitNodes();

        Graph result = new Graph();

        // initialize union-find structure as HashMap<Node,HashSet<Node>
        UnionFind unionFinder = new UnionFind(this.nodes);

        PriorityQueue<Edge> prioEdgeQueue = new PriorityQueue<>(this.edges.size(), this.edgeComparator);
        prioEdgeQueue.addAll(this.getEdges());

        for (Edge e : prioEdgeQueue) {

            if (!unionFinder.connected(e.getStart(), e.getEnd())) {
                e.getStart().addOutgoingEdge(e);
                e.getEnd().addIncomingEdge(e);
                result.addEdge(e);
                unionFinder.union(e.getStart(), e.getEnd());
            }
        }

        for(Node n :unionFinder.getNodes()){
            result.addNode(n);
        }
        return result;
    }


}
