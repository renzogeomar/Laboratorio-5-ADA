package Ejercicio2;

public class FloydWarshall {
    int[][] dist;
    private Node[] nodes;

    public FloydWarshall(Node[] nodes) {
        this.nodes = nodes;
        int n = nodes.length;
        dist = new int[n][n];

        // inicializar matriz de distancias
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0; //distancia a si mismo
                } else {
                    dist[i][j] = Integer.MAX_VALUE / 2; // infinito
                }
            }
        }
    }

    public void addEdge(Node node1, Node node2, int weight) {
        int i = node1.getId();
        int j = node2.getId();
        dist[i][j] = weight;
    }

    public void computeShortestPaths() {
        int n = nodes.length;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][j] > dist[i][k] + dist[k][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
    }

    public void printMatrix(String title) {
        System.out.println("\n" + title);
        int n = nodes.length;

        System.out.print("       ");
        for (int j = 0; j < n; j++) {
            System.out.printf("%6s", nodes[j].getName());
        }
        System.out.println();

        //filas
        for (int i = 0; i < n; i++) {
            System.out.printf("%6s", nodes[i].getName());
            for (int j = 0; j < n; j++) {
                if (dist[i][j] == Integer.MAX_VALUE / 2) {
                    System.out.printf("%6s", "infin");
                } else {
                    System.out.printf("%6d", dist[i][j]);
                }
            }
            System.out.println();
        }
    }
}
