/**
 * GraphenBibliothek
 *
 * @author Oliver Colin Sauer
 * @MIS
 * @Class Program
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Program {

    final static int AD_MATRIX = 0;
    final static int EDGE_LIST = 1;
    final static int EDGE_LIST_WEIGHT = 2;

    public static void main(String[] args) {

        try {


            // reading file
            BufferedReader in = new BufferedReader(new FileReader(
                    //"graphen/K_12.txt"));
                    //"graphen/K_12e.txt"));
                    "graphen/K_10.txt"));
            //"graphen/K_10e.txt"));
            //"graphen/G_100_200.txt"));

            Graph g = new Graph(in, EDGE_LIST_WEIGHT);

            Node startNode = g.getNode(0);
            Node endNode = g.getNode(6);

            // Branch and Bound

            double start = System.nanoTime();
            LinkedList<Graph> tspGraph = g.tspBruteForce(startNode, false);
            Graph dijkstraGraph = g.dijkstra(startNode, endNode);

            double end = System.nanoTime();
            in.close();

            System.out.println("\n\nTSP:");
            System.out.println(tspGraph.removeLast());
            System.out.println("GraphCount:" + tspGraph.size());
            System.out.println("Time: " + ((end - start) / 1000000000) + "s");

            System.out.println("\n\nDijkstra:");
            System.out.println(dijkstraGraph);
            System.out.println("Time: " + ((end - start) / 1000000000) + "s");
            // Error Handling...
        } catch (IOException e) {

            System.out.println("Fehler beim Lesen der Datei.");
        } catch (ArrayIndexOutOfBoundsException e) {

            System.out.println("Startknoten nicht gefunden");
            System.out.println(e.getMessage());
        }


    }

}
