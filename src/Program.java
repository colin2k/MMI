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
                    //"graphen/K_10.txt"));
            //"graphen/K_10e.txt"));
            "graphen/Fluss.txt"));

            Graph g = new Graph(in, EDGE_LIST_WEIGHT,true);

            Node startNode = g.getNode(0);
            Node endNode = g.getNode(7);

            double start = System.nanoTime();
            Graph result = g.fordFulkerson(startNode,endNode);
            double end = System.nanoTime();
            in.close();

            System.out.println("\n\nResult:"+startNode+','+endNode);
            System.out.println(result);
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
