package Ejercicio3;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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

    public void bellmanFord(String startNodeName) {
        Node source = getNode(startNodeName);
        if (source == null) {
            System.out.println("El nodo inicial no existe.");
            return;
        }

        // 1. Estructuras de datos
        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> predecessors = new HashMap<>();
        int numNodes = nodes.size();

        // 2. Inicialización
        // Distancia a todos es "Infinito", excepto el nodo de inicio (source)
        for (Node node : nodes) {
            distances.put(node, Integer.MAX_VALUE);
            predecessors.put(node, null);
        }
        distances.put(source, 0); // Distancia a sí mismo es 0

        // 3. Relajación (V-1 iteraciones)
        // Repetimos el proceso V-1 veces (V = número de nodos)
        for (int i = 1; i < numNodes; i++) {
            boolean changedInThisIteration = false;
            // En cada iteración, revisamos TODAS las aristas
            for (Edge edge : edges) {
                Node u = edge.getFrom();
                Node v = edge.getTo();
                int weight = edge.getWeight();

                // Si 'u' es alcanzable (distancia no es infinito)
                // Y encontramos un camino más corto a 'v' pasando por 'u'
                if (distances.get(u) != Integer.MAX_VALUE && distances.get(u) + weight < distances.get(v)) {
                    distances.put(v, distances.get(u) + weight);
                    predecessors.put(v, u);
                    changedInThisIteration = true;
                }
            }
            
            // Optimización: Si en una iteración completa no hubo cambios,
            // ya encontramos los caminos más cortos y podemos parar.
            if (!changedInThisIteration) {
                // System.out.println("Terminación temprana en iteración: " + i);
                break;
            }
        }

        // 4. Detección de Ciclos Negativos
        // Hacemos una iteración MÁS (la V-ésima iteración)
        boolean negativeCycleFound = false;
        for (Edge edge : edges) {
            Node u = edge.getFrom();
            Node v = edge.getTo();
            int weight = edge.getWeight();

            // Si después de V-1 iteraciones, todavía podemos encontrar un camino más corto,
            // significa que hay un ciclo de peso negativo.
            if (distances.get(u) != Integer.MAX_VALUE && distances.get(u) + weight < distances.get(v)) {
                negativeCycleFound = true;
                System.out.println("¡Ciclo de peso negativo detectado!");
                // Opcionalmente, se podría marcar 'v' como inalcanzable o con -infinito
                // distances.put(v, Integer.MIN_VALUE); 
                break; // Un ciclo es suficiente para detenernos
            }
        }

        // 5. Mostrar resultados
        System.out.println("--- Resultados de Bellman-Ford (desde " + source + ") ---");
        if (negativeCycleFound) {
            System.out.println("El grafo contiene un ciclo de peso negativo.");
            System.out.println("Las distancias más cortas no están bien definidas (pueden ser -infinito).");
        } 
        else {
            // Si no hay ciclos, mostramos los resultados (igual que en Dijkstra)
            System.out.println("Distancias más cortas:");
            for (Node node : nodes) {
                int dist = distances.get(node);
                if (dist == Integer.MAX_VALUE) {
                    System.out.println("  " + node + ": Inalcanzable");
                } else {
                    System.out.println("  " + node + ": " + dist);
                }
            }
            
            System.out.println("\nCaminos más cortos:");
            // Imprimir el camino a todos los demás nodos
            for (Node node : nodes) {
                if (!node.equals(source)) {
                    // Reutilizamos el método auxiliar de Dijkstra
                    printShortestPath(node.getName(), predecessors, distances);
                }
            }
        }
        System.out.println("----------------------------------------------");
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

}
