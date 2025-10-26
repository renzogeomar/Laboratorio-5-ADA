package Ejercicio3;

public class ejercicio3 {
    public static void main(String[] args) {
        System.out.println("====== PRUEBA BELLMAN-FORD (CON PESO NEGATIVO) ======");
        Graph gNeg = new Graph();
        gNeg.addNode("A");
        gNeg.addNode("B");
        gNeg.addNode("C");
        gNeg.addNode("D");
        gNeg.addNode("E");

        gNeg.addEdge("A", "B", 4);
        gNeg.addEdge("A", "C", 2);
        gNeg.addEdge("B", "D", 5);
        gNeg.addEdge("C", "B", -3); // <-- Arista con peso negativo
        gNeg.addEdge("C", "D", 8);
        gNeg.addEdge("C", "E", 10);
        gNeg.addEdge("D", "E", 2);

        gNeg.showGraph();
        System.out.println();
        gNeg.bellmanFord("A");

        System.out.println("\n====== PRUEBA BELLMAN-FORD (CON CICLO NEGATIVO) ======");
        Graph gCiclo = new Graph();
        gCiclo.addNode("X");
        gCiclo.addNode("Y");
        gCiclo.addNode("Z");

        gCiclo.addEdge("X", "Y", 1);
        gCiclo.addEdge("Y", "Z", 2);
        gCiclo.addEdge("Z", "Y", -5); // <-- Esto crea un ciclo negativo (Y -> Z -> Y) con costo 2 + (-5) = -3
        
        gCiclo.showGraph();
        System.out.println();
        gCiclo.bellmanFord("X");
    }
}

