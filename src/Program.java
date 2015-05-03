/**
 * GraphenBibliothek
 * 
 * @author Oliver Colin Sauer
 * @MIS
 * 
 * @Class Program
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Program {

	final static int AD_MATRIX = 0;
	final static int EDGE_LIST = 1;
	final static int EDGE_LIST_WEIGHT = 2;

	public static void main(String[] args) {

		double startPrimTime=0;
		double endPrimTime=0;
		double startKruskalTime=0;
		double endKruskalTime=0;

		try {


			// reading file
			BufferedReader in = new BufferedReader(new FileReader(
					"graphen/K_12.txt"));

			Graph g = new Graph(in, EDGE_LIST_WEIGHT);

			Node startNode = g.getNode(0);
			double start = System.nanoTime();
			LinkedList<Graph> tspGraph =  g.tspBruteForce(startNode);

			double end = System.nanoTime();

			System.out.println("\n\nTSP:");
			System.out.println(tspGraph);

			System.out.println("Time: "+((end-start)/1000000000)+"s");
			in.close();
			
			// Error Handling...
		} catch (IOException e) {

			System.out.println("Fehler beim Lesen der Datei.");
		} catch (ArrayIndexOutOfBoundsException e) {

			System.out.println("Startknoten nicht gefunden");
			System.out.println(e);

		}




	}

}
