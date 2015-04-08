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

public class Program {

	final static int AD_MATRIX = 0;
	final static int EDGE_LIST = 1;
	final static int EDGE_LIST_WEIGHT = 2;

	public static void main(String[] args) {

		double startTime=0;
		double endTime=0;


		try {
			// reading file
			BufferedReader in = new BufferedReader(new FileReader(
					"graphen/G_100_200.txt"));

			Graph g = new Graph(in, EDGE_LIST_WEIGHT);
			Node startNode = g.getNode(0);
			//System.out.println(startNode.getOutgoingEdges());

			startTime = System.nanoTime();
			Graph testGraph = g.prim(startNode);
			endTime = System.nanoTime();

			System.out.println("Nodes:"+testGraph.getNodes().size());
			System.out.println("Edges:"+testGraph.getEdges().size());
			//System.out.println("Graph:\n"+testGraph);


			in.close();
			
			// Error Handling...
		} catch (IOException e) {

			System.out.println("Fehler beim Lesen der Datei.");
		} catch (ArrayIndexOutOfBoundsException e) {

			System.out.println("Startknoten nicht gefunden");
		}

		double msDuration = (endTime-startTime)/1000000; //in ms
		double sDuration = msDuration/1000; //in s
		double mDuration = sDuration/60; //in m

		System.out.println("\n\ndone in:");
		System.out.println(String.valueOf(msDuration)+ " ms");
		System.out.println(String.valueOf(sDuration) +  " s");
		System.out.println(String.valueOf(mDuration) +  " m");

	}

}
