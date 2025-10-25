package Ejercicio1;

public class ejercicio1 {
    public static void main(String[] args) {
        Graph g = new Graph();

        // Generar un grafo aleatorio con 5 nodos, 8 aristas, y con pesos
        g.generarGrafoAleatorio(8, 15, true);

        // Mostrar el grafo generado
        System.out.println("Grafo Generado:");
        g.showGraph();
        
        System.out.println("\n"); // Espacio

        // Ejecutar Dijkstra comenzando desde el nodo "N0"
        g.dijkstra("N0");
    }
}

