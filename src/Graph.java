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
    final static int EDGE_LIST_BALANCED = 3;

    private List<Node> nodes;
    private List<Edge> edges;

    private Double totalWeight;
    private Double flow;

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    private boolean directed = false;

    /**
     * empty construct - initialize node list - initialize edge list
     */
    public Graph() {
        this.nodes = new LinkedList<>();
        this.edges = new LinkedList<>();
        this.totalWeight = 0.0;
        this.flow = 0.0;

    }

    /**
     * Initialize Graph with node and edge List
     *
     * @param n List<Node>
     * @param e List<Node>
     */
    public Graph(List<Node> n, List<Edge> e) {
        this.nodes = n;
        this.edges = e;


    }

    /**
     * Clone Graph object
     *
     * @return Graph
     */
    public Graph clone() {
        try {
            super.clone();
        } catch (Exception e) {
            //System.out.println("Cloning not supported by "+this.getClass());
        }
        return new Graph(this.getNodes(), this.getEdges());
    }

    /**
     * construct using file as source
     * - initialize node list
     * - initialize edgelist
     * - read file into graph
     *
     * @param source   BufferedReader
     * @param fileType int (AD_MATRIX | EDGE_LIST | EDGE_LIST_WEIGHT)
     */
    public Graph(BufferedReader source, int fileType, boolean directed) {
        this.nodes = new Vector<>();
        this.edges = new Vector<>();
        this.directed = directed;

        this.initFromSource(source, fileType);
    }

    /**
     * get Current Graph weight
     *
     * @return Double weight
     */
    public Double getTotalWeight() {
        return this.totalWeight;
    }

    public Double getFlow() {
        return this.flow;
    }

    public void setFlow(Double flow) {
        this.flow = flow;
    }

    /**
     * Set current Graph weight
     *
     * @param weight Double
     */
    private void setTotalWeight(Double weight) {
        this.totalWeight = weight;
    }

    /**
     * add Weight to current Graph weight
     *
     * @param weight Double
     */
    private void addToTotalWeight(Double weight) {
        if (!(weight == null)) {
            this.totalWeight += weight;
        }
    }


    /**
     * Connecting to Nodes with matching edge or new Edge
     *
     * @param i Node
     * @param j Node
     * @return Edge
     */
    protected Edge connect(Node i, Node j) {
        for (Edge e : this.getEdges()) {
            if (this.directed) {
                if ((e.getStart() == i && e.getEnd() == j)) {
                    return e;

                }
            } else {
                if ((e.getStart() == i && e.getEnd() == j) ||
                        (e.getStart() == j && e.getEnd() == i)) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * Graph to String
     *
     * @return String
     */
    public String toString() {
        String result = "Nodes: [";
        for (Node n : this.nodes) {
            result += n;
        }
        result += "]\nEdges:\n";
        result += "Start,Ende,Kosten,Kapazität\n";
        for (Edge e : this.edges) {
            result += e + "\n";
        }
        result += "\nWeight:" + this.getTotalWeight();
        result += "\nFlow:" + this.getFlow();
        return result;
    }

    /**
     * set all Nodes unvisited
     */
    private void unVisitNodes() {
        for (Node n : this.nodes) {
            n.unvisit();
        }
    }

    /**
     * get List of Edges
     *
     * @return List<Edge>
     */
    public List<Edge> getEdges() {
        return this.edges;
    }

    /**
     * get List of Nodes
     *
     * @return List<Node>
     */
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
            int nodeCount = Integer.parseInt(source.readLine().replaceAll("\\s", ""));


            for (int i = 0; i < nodeCount; i++) {
                // create Node and add to Graph
                this.addNode(new Node(i));
            }
            // reading Edges
            String currentLine;


            int nodeCounter = 0;

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
                    if (!this.directed) {
                        Edge newEdgeReverse = new Edge(nodeTo, nodeFrom, Double.parseDouble(straBuf[2]));
                        nodeFrom.addEdge(newEdgeReverse);
                        nodeTo.addEdge(newEdgeReverse);
                        this.addEdge(newEdgeReverse);
                    }

                    nodeTo.addEdge(newEdge);

                    nodeFrom.addEdge(newEdge);

                    this.addEdge(newEdge);

                } else if (fileType == EDGE_LIST_BALANCED) {
                    String[] straBuf = currentLine.split("\\s");


                    //balance values
                    if (nodeCounter < nodeCount) {
                        Node bNode = this.nodes.get(nodeCounter);
                        bNode.setBalance(Double.parseDouble(straBuf[0]));
                        nodeCounter++;
                        continue;
                    }


                    Node nodeFrom = this.nodes.get(Integer.parseInt(straBuf[0]));
                    Node nodeTo = this.nodes.get(Integer.parseInt(straBuf[1]));

                    Edge newEdge = new Edge(nodeFrom, nodeTo, Double.parseDouble(straBuf[2]), Double.parseDouble(straBuf[3]));

                    if (!this.directed) {
                        Edge newEdgeReverse = new Edge(nodeTo, nodeFrom, Double.parseDouble(straBuf[2]), Double.parseDouble(straBuf[3]));
                        nodeFrom.addEdge(newEdgeReverse);
                        nodeTo.addEdge(newEdgeReverse);
                        this.addEdge(newEdgeReverse);
                    }

                    nodeTo.addEdge(newEdge);

                    nodeFrom.addEdge(newEdge);

                    this.addEdge(newEdge);

                } else {
                    System.out.println("Fehler: Falscher Dateityp");
                }
            }

        } catch (IOException e) {
            System.out.println("Fehler beim einlesen.");
        }
    }


    /**
     * add Node to Graph
     *
     * @param n Node
     */
    private void addNode(Node n) {
        this.nodes.add(n);
    }

    /**
     * get a Node from Graph by Index
     *
     * @param index Integer
     * @return Node
     */
    public Node getNode(int index) {
        for (Node n : this.nodes) {
            if (n.getIndex() == index) {
                return n;
            }
        }
        return this.nodes.get(index);
    }

    /**
     * add Edge to Graph
     *
     * @param e Edge
     */
    private void addEdge(Edge e) {
        this.edges.add(e);
    }

    private void removeEdge(Edge e) {
        this.edges.remove(e);
        for (Node n : this.getNodes()) {
            if (n.getEdges().contains(e)) {
                n.removeEdge(e);
            }
        }

    }

    /**
     * do Depthfirst Search on current Graph
     * - Recursion start
     *
     * @param start Node
     * @return Graph
     */
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


    /**
     * Depthfirst search on current Graph
     * - Recurstion
     *
     * @param start  Node
     * @param result Graph
     */
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

    /**
     * Breadth first search on current Graph without end
     *
     * @param start Node
     * @return Graph
     */
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
                if (!(currentEdge.getEnd().getVisited())) {

                    // visit Node
                    currentEdge.getEnd().visit();
                    // put start of current Edge on stack
                    llNodes.add(currentEdge.getEnd());

                    // add start of current Edge to result Graph
                    result.addNode(currentEdge.getEnd());

                    // add current Edge to result Graph
                    result.addEdge(currentEdge);
                }
            }
        }

        return result;
    }

    /**
     * Breadth first search on current Graph from start to end
     *
     * @return Double
     */
    public Double findBottelneck(Graph g) {
        Double bottleNeck = Double.POSITIVE_INFINITY;
        for (Edge e : g.getEdges()) {
            if (e.getWeight() < bottleNeck)
                bottleNeck = e.getWeight();
        }
        return bottleNeck;
    }


    /**
     * Minimal Spanning tree (Prim-Algorithm)
     *
     * @param start Node
     * @return Graph
     */
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

    /**
     * Minimal spanning tree Kruskal-Algorithm
     *
     * @return Graph
     */
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

    /**
     * Solving Traveling Sales Man Problem
     * - NearestNeighbour Algorithm
     *
     * @param startNode Node
     * @return Graph
     */
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


    /**
     * Solving Traveling Sales Man Problem
     * - Double Tree Algorithm
     *
     * @param startNode Node
     * @return Graph
     */
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

    /**
     * Solving Traveling Sales Man Problem
     * - by trying all Tours (Brute Force Algorithm)
     * <p>
     * - including Branch and Bound
     *
     * @param startNode Node
     * @param bnb       boolean
     * @return LinkedList<Graph>
     */
    public LinkedList<Graph> tspBruteForce(Node startNode, boolean bnb) {

        /**
         * get Copy of current Graph
         */
        Graph toTraverse = this.clone();
        /**
         * Bound init to Double MaxValue
         */
        toTraverse.totalWeight = Double.MAX_VALUE;
        /**
         * initialize LinkList of Graphs to hold resultGraphs
         */
        LinkedList<Graph> resultGraphs = new LinkedList<>();

        /**
         * initialize Graph with starting node to start recursion
         */
        Graph currentGraph = new Graph();
        currentGraph.addNode(startNode);
        /**
         * start recursion
         */
        tspRecBruteForce(startNode, resultGraphs, toTraverse, currentGraph, bnb);
        return resultGraphs;

    }

    /**
     * Solving Traveling Sales Man Problem
     * - by trying all Tours (Brute Force Algorithm)
     * <p>
     * - including Branch and Bound
     *
     * @param startNode    Node
     * @param resultGraphs LinkedList
     * @param toTraverse   Graph
     * @param currentTour  Graph
     * @param bnb          boolean
     */
    private void tspRecBruteForce(Node startNode, LinkedList<Graph> resultGraphs, Graph toTraverse, Graph currentTour, boolean bnb) {

        /**
         * if currentTour contains all nodes
         */
        if (toTraverse.nodes.size() == currentTour.nodes.size()) {
            /**
             * add the way back to start Node to the tour
             */
            Edge newEdge = this.connect(currentTour.nodes.get(0), startNode);
            currentTour.addEdge(newEdge);
            currentTour.addToTotalWeight(newEdge.getWeight());
            /**
             * Branch and Bound
             * - set new Bound
             */
            if (bnb) {
                if (toTraverse.totalWeight > currentTour.getTotalWeight()) {
                    toTraverse.totalWeight = currentTour.getTotalWeight();
                    resultGraphs.add(currentTour);
                }
            } else {
                resultGraphs.add(currentTour);
            }

        } else {
            /**
             * if there are still Nodes to add to the tour
             */
            for (Node n : toTraverse.getNodes()) {
                if (!currentTour.getNodes().contains(n)) {
                    /**
                     * create new Graph and add all nodes and edges from current Graph
                     */
                    Graph newGraph = new Graph();
                    for (Node no : currentTour.getNodes()) {
                        newGraph.addNode(no);
                    }
                    for (Edge e : currentTour.getEdges()) {
                        newGraph.addEdge(e);
                        newGraph.addToTotalWeight(e.getWeight());
                    }
                    /**
                     * get connecting Edge from original Graph
                     * and add it to new Graph
                     */
                    Edge tobeAdded = this.connect(startNode, n);
                    newGraph.addEdge(tobeAdded);
                    newGraph.addToTotalWeight(tobeAdded.getWeight());
                    newGraph.addNode(n);

                    /**
                     * if Graph weight > current Bound
                     *  do recursion
                     * else
                     *  break
                     */
                    if (bnb) {
                        if (toTraverse.totalWeight > newGraph.getTotalWeight()) {
                            tspRecBruteForce(n, resultGraphs, toTraverse, newGraph, true);
                        }
                    } else {
                        tspRecBruteForce(n, resultGraphs, toTraverse, newGraph, false);
                    }

                }
            }
        }
    }

    public Graph dijkstra(Node startNode, Node endNode) {

        this.unVisitNodes();
        Graph workingGraph = this.clone();
        Node newStartnode = workingGraph.getNode(startNode.getIndex());
        /**
         * initialize
         */
        Edge currentEdge;


        PriorityQueue<Edge> prioEdgeQueue = new PriorityQueue<>();
        prioEdgeQueue.addAll(newStartnode.getEdges());
        /**
         * HashMap of shortest Paths to all Nodes
         */
        HashMap<Node, Graph> hmNodeGraph = new HashMap<>();

        /**
         * initialize HashMap with new Graphs for all Nodes.
         * - All others then startNode Double.POSITIVE_INFINITY
         */
        for (Node n : workingGraph.getNodes()) {
            Graph tmpGraph = new Graph();
            tmpGraph.setDirected(workingGraph.directed);

            if (n == newStartnode) {
                tmpGraph.addNode(startNode);
            } else {
                tmpGraph.setTotalWeight(Double.POSITIVE_INFINITY);
            }
            hmNodeGraph.put(n, tmpGraph);
        }

        newStartnode.visit();

        /**
         * Take Edges out of Prio Queue untill it's empty
         */
        while (!prioEdgeQueue.isEmpty()) {
            currentEdge = prioEdgeQueue.remove();
            /**
             * currentEndGraph - Graph from HashMap for endNode of current Edge
             * currentStartGraph - Graph from HashMap for startNode of current Edge
             * connectingEdge - Edge from original Graph with correct weight
             */
            Graph currentEndGraph = hmNodeGraph.get(currentEdge.getEnd());
            Graph currentStartGraph = hmNodeGraph.get(currentEdge.getStart());
            Edge connectingEdge = workingGraph.connect(currentEdge.getStart(), currentEdge.getEnd());
            if (!currentEdge.getEnd().getVisited()) {

                /**
                 * put Nodes and Edges from StartGraph into Endgraph
                 */
                currentEndGraph.edges.addAll(currentStartGraph.getEdges());
                currentEndGraph.setTotalWeight(currentStartGraph.getTotalWeight());
                currentEndGraph.nodes.addAll(currentStartGraph.getNodes());

                /**
                 * Add the connecting Edge to the endNodeGraph
                 */
                currentEndGraph.addToTotalWeight(connectingEdge.getWeight());

                currentEndGraph.addNode(currentEdge.getEnd());
                currentEndGraph.addEdge(connectingEdge);
                currentEdge.getEnd().visit();
                /**
                 * add all Edges from endnode to priority queue + the way travled already
                 */
                for (Edge enqueEdge : currentEdge.getEnd().getEdges()) {
                    if (!enqueEdge.getEnd().getVisited()) {
                        prioEdgeQueue.add(new Edge(enqueEdge.getStart(), enqueEdge.getEnd(), enqueEdge.getWeight() + currentStartGraph.getTotalWeight()));
                    }
                }
            } else {
                if (currentEndGraph.getTotalWeight() > currentStartGraph.getTotalWeight() + connectingEdge.getWeight()) {
                    /**
                     * put Nodes and Edges from StartGraph into Endgraph
                     */
                    currentEndGraph.edges = currentStartGraph.getEdges();
                    currentEndGraph.setTotalWeight(currentStartGraph.getTotalWeight());
                    currentEndGraph.nodes = currentStartGraph.getNodes();

                    /**
                     * Add the connecting Edge Weight to the endNodeGraph
                     */
                    currentEndGraph.addToTotalWeight(connectingEdge.getWeight());

                }
            }

        }

        return hmNodeGraph.get(workingGraph.getNode(endNode.getIndex()));
    }

    public Graph bellmanFordMoore(Node StartNode, Node end) {
        Graph result = new Graph();

        ArrayList<Double> distance = new ArrayList<>();
        ArrayList<Integer> prev = new ArrayList<>();
        this.unVisitNodes();
        for (Node v : this.getNodes()) {
            distance.add(v.getIndex(), Double.POSITIVE_INFINITY);
            prev.add(v.getIndex(), null);
        }
        distance.set(StartNode.getIndex(), 0.0);
        prev.set(0, 0);
        for (int i = 0; i < this.getNodes().size() - 1; ++i) {
            for (Edge e : this.getEdges()) {
                if (distance.get(e.getStart().getIndex()) + e.getWeight() < distance.get(e.getEnd().getIndex())) {
                    distance.set(e.getEnd().getIndex(), e.getWeight() + distance.get(e.getStart().getIndex()));
                    prev.set(e.getEnd().getIndex(), e.getStart().getIndex());
                }
            }
        }
        for (Edge e : this.getEdges()) {
            if (distance.get(e.getStart().getIndex()) + e.getWeight() < distance.get(e.getEnd().getIndex())) {
                System.out.println("Zyklus gefunden:" + e + "\n");
                //get Zyclus
                this.unVisitNodes();
                Node Start = e.getStart();
                Node PrevNode  =this.getNode(prev.get(StartNode.getIndex()));
                Node tmpNode;

                while(Start != PrevNode)
                {
                    tmpNode = this.getNode(prev.get(PrevNode.getIndex()));
                    result.addNode(PrevNode);
                    result.addNode(tmpNode);
                    result.addEdge(this.connect(tmpNode,PrevNode));
                }
                return result;
            }
        }

        Node tmpNode = end;
        while (tmpNode != StartNode) {
            result.addNode(tmpNode);
            Node nextNode = this.getNode(prev.get(tmpNode.getIndex()));
            Edge connectingEdge = this.connect(tmpNode, nextNode);
            result.addEdge(connectingEdge);
            result.addToTotalWeight(connectingEdge.getWeight());
            tmpNode = nextNode;
        }
        result.addNode(StartNode);
        return result;
    }


    public Graph fordFulkerson(Node source, Node target) {


        //result Graph
        Graph result = new Graph();
        result.setDirected(this.directed);

        //adding all Nodes to result Graph
        result.nodes = this.getNodes();

        //Init result graph Edges with Flow 0.0
        for (Edge e : this.getEdges()) {
            e.setFlow(0.0);
            result.addEdge(e);
        }
        //residualGraph
        Graph residual = result.buildResidualGraph();

        //residual source
        Node rSource = residual.getNode(source.getIndex());

        //residual target
        Node rTarget = residual.getNode(target.getIndex());


        // while augumented path is found process in residual Graph
        while (residual.findPath(rSource, rTarget)) ;

        //set actual flow in result graph
        for (Edge e : result.getEdges()) {
            Node rStart = residual.getNode(e.getStart().getIndex());
            Node rEnd = residual.getNode(e.getEnd().getIndex());
            Edge tmpEdge = residual.connect(rStart, rEnd);
            //if edge is not fully used, take current flow
            if (tmpEdge != null) {
                e.setFlow(tmpEdge.getFlow());
            } else {
                //if edge flow takes full capacity, take reverse edge from residual graph
                tmpEdge = residual.connect(rEnd, rStart);
                e.setFlow(tmpEdge.getWeight());
            }
        }
        // set total flow
        result.setFlow(residual.getFlow());

        return result;
    }

    private Graph buildResidualGraph() {
        Graph residual = new Graph();
        residual.setDirected(this.directed);

        //adding new Nodes
        for (Node n : this.getNodes()) {
            residual.addNode(new Node(n.getIndex()));
        }

        //Create residual graph edges
        for (Edge e : this.getEdges()) {
            Node rFrom = residual.getNode(e.getStart().getIndex());
            Node rTo = residual.getNode(e.getEnd().getIndex());
            Edge rEdge = new Edge(rFrom, rTo, e.getWeight());
            residual.addEdge(rEdge);
            rFrom.addEdge(rEdge);
        }
        return residual;
    }

    private boolean findPath(Node start, Node end) {
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

        Double bottleneck = Double.POSITIVE_INFINITY;

        // until Queue is empty
        while (!llNodes.isEmpty()) {

            // Pop First Element from Stack
            tmpNode = llNodes.pop();

            // for each incoming edge in current node as currentEdge
            for (Edge currentEdge : tmpNode.getEdges()) {

                // if end of current Edge not in result-graph
                if (!(currentEdge.getEnd().getVisited())) {

                    // visit Node
                    currentEdge.getEnd().visit();
                    // put start of current Edge on stack
                    llNodes.add(currentEdge.getEnd());

                    // add start of current Edge to result Graph
                    result.addNode(currentEdge.getEnd());


                    // add current Edge to result Graph
                    result.addEdge(currentEdge);
                    if (currentEdge.getEnd() == end) {
                        llNodes.clear();
                        break;

                    }
                }
            }
        }
        if (!result.getNodes().contains(end)) {
            return false;

        }

        Graph path = new Graph();
        //update residual Graph
        while (start != end) {
            for (Edge e : result.getEdges()) {
                if (e.getEnd() == end) {
                    if (bottleneck > e.getWeight()) {
                        bottleneck = e.getWeight();
                    }
                    end = e.getStart();
                    path.addEdge(e);
                    break;
                }
            }
        }

        for (Edge e : path.getEdges()) {
            Edge rEdge = new Edge(e.getEnd(), e.getStart(), bottleneck);
            this.addEdge(rEdge);
            e.getEnd().addEdge(rEdge);
            if (e.getWeight() - bottleneck > 0) {
                e.setWeight(e.getWeight() - bottleneck);
                e.setFlow(bottleneck + e.getFlow());
            } else {
                this.removeEdge(e);
                e.getStart().removeEdge(e);
            }
        }
        this.setFlow(this.getFlow() + bottleneck);
        return true;
    }

    public Graph CCP() {

        Graph result = new Graph();

        Node superSink = new Node(this.getNodes().size(), 0.0);
        Node superSource = new Node(this.getNodes().size() + 1, 0.0);

        //preprocess nodes
        for (Node n : this.getNodes()) {
            result.addNode(n);
            if (n.getBalance() > 0) {
                Edge newEdge = new Edge(superSource, n, n.getBalance());
                superSource.addEdge(newEdge);
                result.addEdge(newEdge);
                superSource.setBalance(n.getBalance() + superSource.getBalance());

            } else if (n.getBalance() < 0) {
                Edge newEdge = new Edge(superSink, n, 0.0,n.getBalance());
                n.addEdge(newEdge);
                superSink.addEdge(newEdge);
                result.addEdge(newEdge);
                superSink.setBalance(n.getBalance() + superSink.getBalance());
            }
        }


        result.addNode(superSink);
        result.addNode(superSource);

        result.edges = this.edges;

        Graph residual = result.buildResidualGraph();

        return result.bellmanFordMoore(superSource,superSink);


    }

    private Graph removeUnusedEdges() {
        Graph result = new Graph();
        result.setDirected(this.directed);
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
