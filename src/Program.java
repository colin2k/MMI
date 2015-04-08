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
					"graphen/G_100_200.txt"));

			Graph g = new Graph(in, EDGE_LIST_WEIGHT);

			Node startNode = g.getNode(1);
			//System.out.println(startNode.getOutgoingEdges());


			startPrimTime = System.nanoTime();
			Graph primGraph = g.prim(startNode);
			endPrimTime = System.nanoTime();

			double msPrimDuration = (endPrimTime-startPrimTime)/1000000; //in ms
			double sPrimDuration = msPrimDuration/1000; //in s

			System.out.println("\n\nPrim done in: \t\t"+String.valueOf(sPrimDuration) +  " s");
			System.out.println("Prim-Weight: \t\t"+primGraph.getTotalWeight());
//			System.out.println(primGraph);

			startKruskalTime = System.nanoTime();
			Graph kruskalGraph = g.kruskal();
			endKruskalTime = System.nanoTime();

			double msKruskalDuration = (endKruskalTime-startKruskalTime)/1000000; //in ms
			double sKruskalDuration = msKruskalDuration/1000; //in s

			System.out.println("\n\nKruskal done in: \t"+String.valueOf(sKruskalDuration) +  " s");
			System.out.println("Kruskal-Weight: \t"+kruskalGraph.getTotalWeight());
			//System.out.println(kruskalGraph);



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
