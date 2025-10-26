package Ejercicio4;

import java.util.concurrent.TimeUnit;

public class ejercicio4 {
    public static void main(String[] args) {
        // --- Resultados de Dijkstra/Bellman-Ford son impresos en consola ---
        // --- Podemos desactivarlos para limpiar la salida de la medición ---
        // (Para esta prueba, simplemente mediremos el tiempo, 
        // aunque los métodos impriman sus resultados)

        // Define los tamaños de grafos a probar: {Nodos, Aristas}
        int[][] sizes = {
            {10, 20},
            {50, 150},
            {100, 300},
            {200, 800},
            {400, 2000} // Floyd-Warshall (V^3) empezará a ser muy lento aquí
            // {500, 5000} // Descomenta para ver diferencias más grandes
        };

        System.out.println("Iniciando comparación de tiempos de ejecución...");
        System.out.println("(Los resultados de cada algoritmo se imprimirán durante la ejecución)");
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("%-10s | %-12s | %-15s | %-15s | %-15s\n", 
                          "Nodos (V)", "Aristas (E)", "Dijkstra (ms)", "Bellman-Ford (ms)", "Floyd-Warshall (ms)");
        System.out.println("----------------------------------------------------------------------");

        for (int[] size : sizes) {
            int numNodes = size[0];
            int numEdges = size[1];

            // 1. Generar el grafo
            // (Usamos el mismo grafo para los 3 algoritmos)
            Graph graph = new Graph();
            graph.generarGrafoAleatorio(numNodes, numEdges, true); // true = ponderado

            // --- 2. Medir Dijkstra ---
            long startTime = System.nanoTime();
            graph.dijkstra("N0", false); // Siempre desde el nodo N0
            long endTime = System.nanoTime();
            long dijkstraTime = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);

            // --- 3. Medir Bellman-Ford ---
            startTime = System.nanoTime();
            graph.bellmanFord("N0", false); // Siempre desde el nodo N0
            endTime = System.nanoTime();
            long bellmanTime = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);

            // --- 4. Medir Floyd-Warshall ---
            startTime = System.nanoTime();
            
            // 4a. Convertir el Graph a la estructura de FloydWarshall
            // (Esta conversión es parte del "costo" de usar Floyd-Warshall aquí)
            Node[] nodesArray = graph.getNodes().toArray(new Node[0]);
            FloydWarshall fw = new FloydWarshall(nodesArray);
            
            // 4b. Llenar la matriz de adyacencia
            for (Edge edge : graph.getEdges()) {
                fw.addEdge(edge.getFrom(), edge.getTo(), edge.getWeight());
            }
            
            // 4c. Ejecutar el algoritmo
            fw.computeShortestPaths();
            
            endTime = System.nanoTime();
            long floydTime = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            
            // Imprimir la fila de tiempos
            System.out.printf("%-10d | %-12d | %-15d | %-15d | %-15d\n", 
                              numNodes, numEdges, dijkstraTime, bellmanTime, floydTime);
        }
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Prueba completada.");
    }
}

