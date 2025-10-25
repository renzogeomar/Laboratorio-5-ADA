package Ejercicio1;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;

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
        Node node = new Node(name);
        if (!nodes.contains(node)) {
            nodes.add(node);
        }
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

    public void dijkstra(String startNodeName) {
        Node source = getNode(startNodeName);
        if (source == null) {
            System.out.println("El nodo inicial no existe.");
            return;
        }

        // 1. Estructuras de datos
        // Guarda la distancia más corta encontrada *hasta ahora* desde source a cada nodo
        Map<Node, Integer> distances = new HashMap<>();
        // Guarda el nodo "anterior" en el camino más corto
        Map<Node, Node> predecessors = new HashMap<>();
        // Nodos para los que ya hemos encontrado la distancia final (visitados)
        Set<Node> visited = new HashSet<>();
        // Cola de prioridad para obtener siempre el nodo no visitado con la menor distancia
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // 2. Inicialización
        // Distancia a todos es "Infinito", excepto el nodo de inicio (source)
        for (Node node : nodes) {
            distances.put(node, Integer.MAX_VALUE);
            predecessors.put(node, null);
        }
        distances.put(source, 0); // Distancia a sí mismo es 0

        // 3. Empezar el algoritmo
        pq.add(source);

        while (!pq.isEmpty()) {
            // 4. Obtener el nodo (u) no visitado con la menor distancia
            Node u = pq.poll();

            // Si ya lo visitamos (encontramos su camino final), lo saltamos
            if (visited.contains(u)) {
                continue;
            }
            // Marcar como visitado (distancia final encontrada)
            visited.add(u);

            // 5. Relajación: Revisar todos los vecinos (v) de (u)
            // (Buscamos todas las aristas que *salen* de u)
            for (Edge edge : edges) {
                if (edge.getFrom().equals(u)) {
                    Node v = edge.getTo();
                    int weight = edge.getWeight();

                    // Si v ya fue visitado (distancia final), no hay nada que hacer
                    if (visited.contains(v)) {
                        continue;
                    }

                    // 6. Calcular la nueva distancia a (v) pasando por (u)
                    int newDist = distances.get(u) + weight;

                    // 7. Si es un camino más corto que el que teníamos...
                    if (newDist < distances.get(v)) {
                        // ...actualizamos la distancia y el predecesor
                        distances.put(v, newDist);
                        predecessors.put(v, u);
                        
                        // Añadimos 'v' a la cola para procesarlo.
                        // (Java PQ no tiene 'decreaseKey', pero re-añadir funciona
                        // gracias al chequeo de 'visited' al inicio del bucle)
                        pq.add(v);
                    }
                }
            }
        }
    }

}
