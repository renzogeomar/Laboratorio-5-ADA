package Ejercicio4;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;
import java.util.Comparator;

public class Graph {
    private List<Node> nodes;
    private List<Edge> edges;

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }


    public void addNode(String name) {
        // 1. Revisar si ya existe por nombre
        if (getNode(name) != null) {
            return;
        }
        
        // 2. Asignar un ID único (el tamaño actual de la lista)
        int newId = nodes.size(); 
        
        // 3. Crear el nodo con nombre e ID
        Node node = new Node(name, newId); 
        
        // 4. Añadirlo
        nodes.add(node);
    }

    public void addEdge(String from, String to, int weight) {
        Node n1 = getNode(from);
        Node n2 = getNode(to);
        if (n1 == null || n2 == null) {
            System.out.println("Uno de los nodos no existe.");
            return;
        }

        // Evitar duplicados
        for (Edge e : edges) {
            if (e.getFrom().equals(n1) && e.getTo().equals(n2)) {
                return;
            }
        }

        edges.add(new Edge(n1, n2, weight));


    }

    public Node getNode(String name) {
        for (Node n : nodes) {
            if (n.getName().equals(name)) return n;
        }
        return null;
    }

    public void showGraph() {
        System.out.println("Nodos:");
        for (Node n : nodes) {
            System.out.println(" - " + n);
        }
        System.out.println("Aristas:");
        for (Edge e : edges) {
            System.out.println(" - " + e);
        }
    }

    public void generarGrafoAleatorio(int cantidadNodos, int cantidadAristas, boolean ponderado) {
        Random rand = new Random();

        // Crear los nodos
        for (int i = 0; i < cantidadNodos; i++) {
            addNode("N" + i); // N0, N1, N2...
        }

        // Crear las aristas aleatorias
        int totalIntentos = 0;
        while (edges.size() < cantidadAristas && totalIntentos < cantidadAristas * 5) {
            int i = rand.nextInt(cantidadNodos);
            int j = rand.nextInt(cantidadNodos);

            if (i == j) { // evitar lazos a sí mismo
                totalIntentos++;
                continue;
            }

            Node from = getNode("N" + i);
            Node to = getNode("N" + j);

            // (Si quieres permitir múltiples aristas, no verifiques duplicados)
            int peso = ponderado ? (rand.nextInt(9) + 1) : 0; // peso aleatorio 1–9
            addEdge(from.getName(), to.getName(), peso);

            totalIntentos++;
        }
    }

    public Map<Node, Integer> bellmanFord(String startNodeName) {
        Node source = getNode(startNodeName);
        if (source == null) {
            System.out.println("El nodo inicial no existe.");
            return null;
        }

        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> predecessors = new HashMap<>();
        for (Node node : nodes) {
            distances.put(node, Integer.MAX_VALUE);
            predecessors.put(node, null);
        }
        distances.put(source, 0);

        int numNodes = nodes.size();
        for (int i = 1; i < numNodes; i++) {
            for (Edge edge : edges) {
                Node u = edge.getFrom();
                Node v = edge.getTo();
                int weight = edge.getWeight();

                if (distances.get(u) != Integer.MAX_VALUE && distances.get(u) + weight < distances.get(v)) {
                    distances.put(v, distances.get(u) + weight);
                    predecessors.put(v, u);
                }
            }
        }

        // Detección de ciclos negativos (opcional)
        for (Edge edge : edges) {
            Node u = edge.getFrom();
            Node v = edge.getTo();
            int weight = edge.getWeight();

            if (distances.get(u) != Integer.MAX_VALUE && distances.get(u) + weight < distances.get(v)) {
                System.out.println("¡Ciclo negativo detectado!");
            }
        }

        return distances;
    }

    public Map<Node, Integer> dijkstra(String startNodeName) {
        Node source = getNode(startNodeName);
        if (source == null) {
            System.out.println("El nodo inicial no existe.");
            return null;
        }

        // 1. Estructuras de datos
        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> predecessors = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // 2. Inicialización
        for (Node node : nodes) {
            distances.put(node, Integer.MAX_VALUE);
            predecessors.put(node, null);
        }
        distances.put(source, 0);

        pq.add(source);

        while (!pq.isEmpty()) {
            Node u = pq.poll();
            if (visited.contains(u)) continue;
            visited.add(u);

            for (Edge edge : edges) {
                if (edge.getFrom().equals(u)) {
                    Node v = edge.getTo();
                    int weight = edge.getWeight();

                    if (visited.contains(v)) continue;

                    int newDist = distances.get(u) + weight;
                    if (newDist < distances.get(v)) {
                        distances.put(v, newDist);
                        predecessors.put(v, u);
                        pq.add(v);
                    }
                }
            }
        }

        return distances;
    }


    public void printResults(Map<Node, Integer> distances, Map<Node, Node> predecessors) {
        System.out.println("Distancias más cortas:");
        for (Node node : nodes) {
            int dist = distances.get(node);
            if (dist == Integer.MAX_VALUE) {
                System.out.println("  " + node + ": Inalcanzable");
            } 
            else {
                System.out.println("  " + node + ": " + dist);
            }
        }

        System.out.println("\nCaminos más cortos:");
        for (Node node : nodes) {
            if (!node.equals(predecessors.get(node))) {
                printShortestPath(node.getName(), predecessors, distances);
            }
        }
    }

    private void printShortestPath(String endNodeName, Map<Node, Node> predecessors, Map<Node, Integer> distances) {
        Node target = getNode(endNodeName);
        if (target == null) return; // No debería pasar si se llama desde dijkstra

        int distance = distances.get(target);
        if (distance == Integer.MAX_VALUE) {
            System.out.println("  No hay camino a " + target);
            return;
        }

        // Reconstruir el camino yendo "hacia atrás"
        List<Node> path = new ArrayList<>();
        Node current = target;
        while (current != null) {
            path.add(current);
            current = predecessors.get(current); // Ir al nodo anterior
        }
        
        // El camino está al revés (Target -> ... -> Source), lo invertimos
        Collections.reverse(path);

        // Imprimir
        System.out.print("  A " + target + " (Costo: " + distance + "): ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println(); // Salto de línea
    }

    public Map<String, Object> floydWarshall() {
        int n = nodes.size();
        final int INF = 99999999; // "Infinito"

        // Matrices de distancias y predecesores
        int[][] dist = new int[n][n];
        Node[][] next = new Node[n][n];

        // Inicialización
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) dist[i][j] = 0;
                else dist[i][j] = INF;
                next[i][j] = null;
            }
        }

        // Cargar las aristas
        for (Edge e : edges) {
            int u = e.getFrom().getId();
            int v = e.getTo().getId();
            dist[u][v] = e.getWeight();
            next[u][v] = e.getTo();
        }

        // --- Algoritmo Floyd–Warshall ---
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF &&
                        dist[i][k] + dist[k][j] < dist[i][j]) {

                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        // --- Detección de ciclos negativos ---
        boolean hasNegativeCycle = false;
        for (int i = 0; i < n; i++) {
            if (dist[i][i] < 0) {
                hasNegativeCycle = true;
                System.out.println("¡Ciclo negativo detectado en el nodo " + nodes.get(i) + "!");
            }
        }

        // Empaquetamos los resultados en un Map
        Map<String, Object> result = new HashMap<>();
        result.put("distances", dist);
        result.put("next", next);
        result.put("hasNegativeCycle", hasNegativeCycle);
        return result;
    }

    public void printFloydResults(Map<String, Object> result) {
        int[][] dist = (int[][]) result.get("distances");
        Node[][] next = (Node[][]) result.get("next");
        boolean hasNegativeCycle = (boolean) result.get("hasNegativeCycle");
        int n = nodes.size();
        final int INF = 99999999;

        System.out.println("\n--- Resultados de Floyd–Warshall ---");

        // Mostrar matriz de distancias
        System.out.println("Matriz de distancias más cortas:");
        System.out.print("      ");
        for (Node node : nodes) {
            System.out.printf("%6s", node.getName());
        }
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.printf("%6s", nodes.get(i).getName());
            for (int j = 0; j < n; j++) {
                if (dist[i][j] == INF)
                    System.out.printf("%6s", "∞");
                else
                    System.out.printf("%6d", dist[i][j]);
            }
            System.out.println();
        }

        if (!hasNegativeCycle) {
            System.out.println("\nCaminos más cortos entre cada par de nodos:");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j && dist[i][j] != INF) {
                        System.out.print("  " + nodes.get(i) + " → " + nodes.get(j) + " (Costo: " + dist[i][j] + "): ");
                        printFloydPath(i, j, next);
                        System.out.println();
                    }
                }
            }
        } else {
            System.out.println("\nEl grafo contiene ciclos de peso negativo. Las distancias no son confiables.");
        }

        System.out.println("----------------------------------------------");
    }

    private void printFloydPath(int i, int j, Node[][] next) {
        if (next[i][j] == null) {
            System.out.print("No hay camino");
            return;
        }

        List<Node> path = new ArrayList<>();
        Node current = nodes.get(i);
        path.add(current);

        while (current != nodes.get(j)) {
            current = next[current.getId()][j];
            path.add(current);
        }

        for (int k = 0; k < path.size(); k++) {
            System.out.print(path.get(k));
            if (k < path.size() - 1)
                System.out.print(" -> ");
        }
    }

}
