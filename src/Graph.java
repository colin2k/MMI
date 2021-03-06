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

    private Double totalWeight;

    private Comparator<Edge> edgeComparator = new Comparator<Edge>() {
        @Override
        public int compare(Edge e1, Edge e2) {
            return Double.compare(e1.getWeight(), e2.getWeight());
        }
    };

    /**
     * empty construct - initialize node list - initialize edge list
     */
    public Graph() {
        this.nodes = new LinkedList<>();
        this.edges = new LinkedList<>();
        this.totalWeight = Double.valueOf(0);
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
        this.nodes = new Vector<>();
        this.edges = new Vector<>();

        this.initFromSource(source, fileType);
    }


    public Double getTotalWeight() {
        return this.totalWeight;
    }

    private void addToTotalWeight(Double weight) {
        this.totalWeight += weight;
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
        result += "\nWeight:" + this.getTotalWeight();
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
                                this.nodes.get(currentNode).addEdge(newEdge);
                                this.nodes.get(i).addEdge(newEdge);


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

                    nodeFrom.addEdge(newEdge);
                    nodeTo.addEdge(newEdge);
                    this.addEdge(newEdge);
                } else if (fileType == EDGE_LIST_WEIGHT) {
                    String[] straBuf = currentLine.split("\\s");
                    Node nodeFrom = this.nodes.get(Integer.parseInt(straBuf[0]));
                    Node nodeTo = this.nodes.get(Integer.parseInt(straBuf[1]));

                    Edge newEdge = new Edge(nodeFrom, nodeTo, Double.parseDouble(straBuf[2]));
                    Edge newEdgeReverse = new Edge(nodeTo, nodeFrom, Double.parseDouble(straBuf[2]));

                    nodeFrom.addEdge(newEdge);
                    nodeFrom.addEdge(newEdgeReverse);
                    nodeTo.addEdge(newEdge);
                    nodeTo.addEdge(newEdgeReverse);

                    this.addEdge(newEdge);
                    this.addEdge(newEdgeReverse);

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
        for (Node n : this.nodes) {
            if (n.getIndex() == index) {
                return n;
            }
        }
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

        for (Node currentNode : result.nodes) {

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

        if (!start.getVisited()) {
            start.visit();
            result.addNode(start);
        }
        // check incoming edges
        // only non directed graphs
        for (Edge currentEdge : start.getEdges()) {

            // if start Node of currentEdge has not been visited
            if (!currentEdge.getStart().getVisited() && start != currentEdge.getStart()) {


                // add to result-graph if it isn't already in there
                result.addNode(currentEdge.getStart());


                // visit Start Node
                currentEdge.getStart().visit();
                // create new Edge from start Node to current Edge start Node
                result.addEdge(new Edge(start, currentEdge.getStart(), currentEdge.getWeight()));

                //recursion call with - start node of current Edge, result graph

                this.recDepthSearch(currentEdge.getStart(), result);
            }


            // if end Node of the current Edge has not been visited
            if (!currentEdge.getEnd().getVisited() && start != currentEdge.getEnd()) {

                result.addNode(currentEdge.getEnd());
                // visit End Node
                currentEdge.getEnd().visit();

                // create new Edge from start Node to current Edge start Node
                result.addEdge(new Edge(start, currentEdge.getEnd(), currentEdge.getWeight()));

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
        LinkedList<Node> llNodes = new LinkedList<>();

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
            for (Edge currentEdge : tmpNode.getEdges()) {

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
        }

        return result;
    }


    // Minimal Spanning tree (Prim-Algoithmus)
    public Graph prim(Node start) {

        this.unVisitNodes();

        Graph result = new Graph();
        result.addNode(start);

        PriorityQueue<Edge> prioEdgeQueue = new PriorityQueue<>();

        prioEdgeQueue.addAll(start.getEdges());

        start.visit();

        for (Edge currentEdge = prioEdgeQueue.remove(); !prioEdgeQueue.isEmpty(); currentEdge = prioEdgeQueue.remove()) {
            if (currentEdge.getEnd().getVisited() &&
                    !currentEdge.getStart().getVisited()) {

                result.addEdge(currentEdge);
                result.addToTotalWeight(currentEdge.getWeight());

                currentEdge.getStart().visit();
                result.addNode(currentEdge.getStart());
                for (Edge nextEdge : currentEdge.getStart().getEdges()) {
                    prioEdgeQueue.add(nextEdge);
                }

            } else if (!currentEdge.getEnd().getVisited() &&
                    currentEdge.getStart().getVisited()) {
                result.addEdge(currentEdge);
                result.addToTotalWeight(currentEdge.getWeight());
                currentEdge.getEnd().visit();
                result.addNode(currentEdge.getEnd());
                for (Edge nextEdge : currentEdge.getEnd().getEdges()) {
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
        UnionFind unionFinder = new UnionFind(this.getNodes());

        PriorityQueue<Edge> prioEdgeQueue = new PriorityQueue<>();


        prioEdgeQueue.addAll(this.getEdges());
        //prioEdgeQueue.addAll(this.getEdges());


        for (Edge e = prioEdgeQueue.remove(); !prioEdgeQueue.isEmpty(); e = prioEdgeQueue.remove()) {
            if (!unionFinder.connected(e.getStart(), e.getEnd())) {
                unionFinder.union(e.getStart(), e.getEnd());
                if (!e.getEnd().getVisited()) {
                    result.addNode(e.getEnd());
                }
                if (!e.getStart().getVisited()) {
                    result.addNode(e.getStart());
                }
                e.getStart().visit();
                e.getEnd().visit();
                e.getStart().addEdge(e);
                e.getEnd().addEdge(e);

                result.addEdge(e);
                result.addToTotalWeight(e.getWeight());


            }
        }

        return result;
    }

    public Graph tspNearestNeighbourTour(Node startNode) {
        Graph result = new Graph();
        this.unVisitNodes();
        startNode.visit();
        result.addNode(startNode);
        PriorityQueue<Edge> prioEdgeQueue = new PriorityQueue<>();

        prioEdgeQueue.addAll(startNode.getEdges());
        Edge currentEdge = prioEdgeQueue.remove();

        while (result.getNodes().size() < this.getNodes().size()) {

            if (!currentEdge.getEnd().getVisited()) {
                result.addEdge(currentEdge);
                result.addNode(currentEdge.getEnd());
                result.addToTotalWeight(currentEdge.getWeight());
                currentEdge.getEnd().visit();
                prioEdgeQueue.clear();
                prioEdgeQueue.addAll(currentEdge.getEnd().getEdges());
            } else {
                currentEdge = prioEdgeQueue.remove();
            }
        }
        for (Edge e : currentEdge.getEnd().getEdges()) {
            if (e.getEnd() == startNode) {
                result.addNode(startNode);
                result.addEdge(e);
                result.addToTotalWeight(e.getWeight());
                break;
            }
        }


        return result;
    }


    public Graph tspDoubleTreeTour(Node startNode) {
        // start with MST
        Graph result = new Graph();
        Graph mst = this.kruskal();

        Graph newmst = mst.removeUnusedEdges();

        Graph depth = newmst.depthSearch(newmst.getNode(startNode.getIndex()));
        Node lastNode = startNode;
        for (int i = 0; result.getNodes().size() < depth.getNodes().size() - 1; i++) {
            Node newNodeFrom = new Node(depth.nodes.get(i).getIndex());
            Node newNodeTo = new Node(depth.nodes.get(i + 1).getIndex());
            double weight = 0;
            for (Edge e : this.getEdges()) {
                if ((e.getStart().getIndex() == newNodeFrom.getIndex()
                        && e.getEnd().getIndex() == newNodeTo.getIndex())
                        || (e.getEnd().getIndex() == newNodeFrom.getIndex()
                        && e.getStart().getIndex() == newNodeTo.getIndex())) {
                    weight = e.getWeight();
                }
            }
            Edge newEdge = new Edge(newNodeFrom, newNodeTo, weight);
            newNodeFrom.visit();
            newNodeTo.visit();
            result.addNode(newNodeFrom);
            result.addToTotalWeight(weight);

            result.addEdge(newEdge);
            lastNode = newNodeTo;
        }
        result.addNode(lastNode);
        result.addNode(depth.getNode(startNode.getIndex()));
        double weight = 0;
        for (Edge e : this.getEdges()) {
            if ((e.getStart().getIndex() == startNode.getIndex()
                    && e.getEnd().getIndex() == lastNode.getIndex())
                    || (e.getEnd().getIndex() == startNode.getIndex()
                    && e.getStart().getIndex() == lastNode.getIndex())) {
                weight = e.getWeight();
            }
        }
        Edge newEdge = new Edge(lastNode, result.getNode(startNode.getIndex()), weight);
        result.addEdge(newEdge);
        result.addToTotalWeight(weight);
        return result;
    }


    private void getTspTour(Node startNode, Graph currentGraph, Double MaxWeight) {
        PriorityQueue<Edge> startEdgeList = new PriorityQueue<>();
        startEdgeList.addAll(startNode.getEdges());
        Node currentStartNode = currentGraph.getNode(startNode.getIndex());
        currentStartNode.visit();
        Edge wayBack = null;
        while (!startEdgeList.isEmpty()) {
            Edge currentEdge = startEdgeList.remove();

            if (!currentGraph.getNode(currentEdge.getEnd().getIndex()).getVisited()) {
                Node currentEndNode = currentGraph.getNode(currentEdge.getEnd().getIndex());
                currentStartNode = currentGraph.getNode(currentEdge.getStart().getIndex());
                Edge newEdge = new Edge(currentStartNode, currentEndNode, currentEdge.getWeight());
                currentGraph.addEdge(newEdge);
                currentGraph.addToTotalWeight(newEdge.getWeight());
                currentEndNode.visit();
                boolean vAll = true;
                for (Node n : currentGraph.getNodes()) {
                    if (!n.getVisited()) {
                        vAll = false;
                        break;
                    }
                }
                if (vAll) {
                    for (Edge e : this.getEdges()) {
                        if (e.getEnd().getIndex() == currentEndNode.getIndex() && e.getStart().getIndex() == startNode.getIndex()) {
                            wayBack = new Edge(currentGraph.getNode(e.getStart().getIndex()), currentGraph.getNode(e.getEnd().getIndex()), e.getWeight());
                            currentGraph.addEdge(wayBack);
                            currentGraph.addToTotalWeight(wayBack.getWeight());
                            return;
                        }

                    }
                }

                startEdgeList.clear();
                startEdgeList.addAll(currentEdge.getEnd().getEdges());
            }
        }

    }

    public Graph tspBruteForce() {
        Graph result = new Graph();

        LinkedList<Node> StartNodeList = new LinkedList<>(this.getNodes());
        Double bestWeight = Double.POSITIVE_INFINITY;



        while (!StartNodeList.isEmpty()) {
            Graph currentGraph = new Graph();
            for (Node n : this.getNodes()) {
                Node newStartNode = new Node(n.getIndex());
                currentGraph.addNode(newStartNode);
            }

            this.getTspTour(StartNodeList.poll(), currentGraph, bestWeight);
            boolean vAll = true;
            for(Node n : currentGraph.getNodes()){
                if(!n.getVisited())
                    vAll = false;
            }

            if (vAll && bestWeight > currentGraph.getTotalWeight()) {

                result = currentGraph;
                bestWeight = currentGraph.getTotalWeight();

            }

        }


        return result;
    }

    private Graph removeUnusedEdges() {
        Graph result = new Graph();
        for (Node n : this.getNodes()) {
            result.addNode(new Node(n.getIndex()));
        }
        for (Edge e : this.getEdges()) {
            Edge newEdge = new Edge(result.getNode(e.getStart().getIndex()), result.getNode(e.getEnd().getIndex()), e.getWeight());
            result.addEdge(newEdge);
            result.getNode(e.getStart().getIndex()).addEdge(newEdge);
            result.getNode(e.getEnd().getIndex()).addEdge(newEdge);
        }
        return result;
    }

}
