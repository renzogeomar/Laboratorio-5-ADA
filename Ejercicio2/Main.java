package Ejercicio2;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Node[] nodes = new Node[6];
        for (int i = 0; i < 6; i++) {
            nodes[i] = new Node(i, "N" + i);
        }

        FloydWarshall fw = new FloydWarshall(nodes);
        Random rand = new Random();
        int n = nodes.length;

        //Generar aristas dirigidas con pesos aleatorios
        System.out.println("Aristas generadas:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && rand.nextBoolean()) { // 50% de probabilidad de conexion
                    int peso = rand.nextInt(20) + 1;
                    fw.addEdge(nodes[i], nodes[j], peso);
                    System.out.println(nodes[i].getName() + " -> " + nodes[j].getName() + " = " + peso);
                }
            }
        }

        //matriz inicial
        fw.printMatrix("Matriz de distancias inicial (antes de Floyd-Warshall):");

        fw.computeShortestPaths();

        //matriz final
        fw.printMatrix("Matriz de distancias mínimas (después de Floyd-Warshall):");
    }
}
