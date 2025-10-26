package Ejercicio4;
public class ejercicio4 {
    public static void main(String[] args) {
        System.out.println("Iniciando Pruebas de Rendimiento de Algoritmos de Grafos...");
        
        // --- Caso 1: Grafo Pequeño y Denso ---
        // Floyd-Warshall (V^3) debería ser aceptable.
        runTest(50, 1000, true); // V=50, E=1000

        // --- Caso 2: Grafo Mediano y Disperso ---
        // Dijkstra (E*logV) y Bellman (V*E) deberían ser mucho más rápidos que Floyd (V^3).
        runTest(300, 1500, true); // V=300, E=1500

        // --- Caso 3: Grafo Mediano y Denso ---
        // Bellman (V*E) se vuelve costoso. Floyd (V^3) también.
        runTest(300, 20000, true); // V=300, E=20000

        // --- Caso 4: Grafo Grande y Disperso ---
        // Floyd-Warshall (V^3) será MUY lento.
        // Dijkstra será el más rápido.
        runTest(800, 2000, true); // V=800, E=2000

        // --- Caso 5: Grafo Grande y Denso (Solo para mostrar la diferencia) ---
        // ¡¡PRECAUCIÓN!! Floyd-Warshall puede tardar MUCHO aquí (800^3 operaciones).
        // runTest(800, 100000, true); // V=800, E=100000
    }

    /**
     * Ejecuta una prueba para un tamaño de grafo específico.
     * @param numNodos (V)
     * @param numAristas (E)
     * @param ponderado Genera pesos positivos (1-9)
     */
    public static void runTest(int numNodos, int numAristas, boolean ponderado) {
        System.out.println("\n---------------------------------------------------------");
        System.out.printf("--- INICIO TEST: (Nodos V=%d, Aristas E=%d) ---\n", numNodos, numAristas);

        // 1. Generar el grafo
        Graph g = new Graph();
        // Usamos pesos positivos. Dijkstra funciona, Bellman y Floyd también.
        g.generarGrafoAleatorio(numNodos, numAristas, ponderado); 
        String startNode = "N0"; // Nodo inicial para Dijkstra y Bellman-Ford

        long startTime, endTime, duration;

        // 2. Medir Dijkstra (Single-Source)
        try {
            startTime = System.nanoTime();
            g.dijkstra(startNode);
            endTime = System.nanoTime();
            duration = endTime - startTime;
            printTime("Dijkstra (1-fuente)", duration);
        } catch (Exception e) {
            System.out.println("Error en Dijkstra: " + e.getMessage());
        }

        // 3. Medir Bellman-Ford (Single-Source)
        try {
            startTime = System.nanoTime();
            g.bellmanFord(startNode);
            endTime = System.nanoTime();
            duration = endTime - startTime;
            printTime("Bellman-Ford (1-fuente)", duration);
        } catch (Exception e) {
            System.out.println("Error en Bellman-Ford: " + e.getMessage());
        }

        // 4. Medir Floyd-Warshall (All-Pairs)
        try {
            startTime = System.nanoTime();
            g.floydWarshall();
            endTime = System.nanoTime();
            duration = endTime - startTime;
            printTime("Floyd-Warshall (Todos-contra-Todos)", duration);
        } catch (Exception e) {
            System.out.println("Error en Floyd-Warshall: " + e.getMessage());
        }
        
        System.out.printf("--- FIN TEST: (V=%d, E=%d) ---\n", numNodos, numAristas);
        System.out.println("---------------------------------------------------------");
    }

    /**
     * Imprime el tiempo de ejecución en milisegundos.
     */
    private static void printTime(String algorithm, long nanos) {
        double millis = nanos / 1_000_000.0;
        System.out.printf("  %-35s: %.4f ms\n", algorithm, millis);
    }
}

