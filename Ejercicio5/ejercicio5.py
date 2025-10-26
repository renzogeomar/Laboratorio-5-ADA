import random
import time
import heapq
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from collections import defaultdict

# --- 1. Implementación de Algoritmos ---

def generar_grafo_aleatorio(V, E):
    """
    Genera un grafo con V nodos y E aristas.
    Retorna:
    - V (int): número de nodos (0 a V-1)
    - edges (list): lista de tuplas (u, v, peso) para Bellman y Floyd
    - adj (dict): lista de adyacencia (u: [(v, peso), ...]) para Dijkstra
    """
    edges = []
    adj = defaultdict(list)
    edge_set = set() # Para evitar duplicados
    
    max_intentos = E * 5
    intentos = 0
    
    while len(edges) < E and intentos < max_intentos:
        u = random.randint(0, V - 1)
        v = random.randint(0, V - 1)
        
        if u == v or (u, v) in edge_set:
            intentos += 1
            continue
            
        peso = random.randint(1, 10)
        
        # Guardamos en ambas estructuras
        edges.append((u, v, peso))
        adj[u].append((v, peso))
        edge_set.add((u, v))
        
        intentos += 1
        
    return V, edges, adj

def dijkstra_heap(V, adj, start_node):
    """ Implementación O(E log V) con cola de prioridad """
    distances = {node: float('inf') for node in range(V)}
    distances[start_node] = 0
    pq = [(0, start_node)] # (distancia, nodo)

    while pq:
        dist_actual, u = heapq.heappop(pq)

        if dist_actual > distances[u]:
            continue

        for v, peso in adj[u]:
            if distances[u] + peso < distances[v]:
                distances[v] = distances[u] + peso
                heapq.heappush(pq, (distances[v], v))
                
    return distances

def bellman_ford(V, edges, start_node):
    """ Implementación O(V * E) """
    distances = {node: float('inf') for node in range(V)}
    distances[start_node] = 0

    for _ in range(V - 1):
        for u, v, peso in edges:
            if distances[u] != float('inf') and distances[u] + peso < distances[v]:
                distances[v] = distances[u] + peso
                
    # Opcional: Detección de ciclos negativos
    # ...
    return distances

def floyd_warshall(V, edges):
    """ Implementación O(V^3) """
    # Usar arrays (listas de listas) es más rápido en Python que dicts para esto
    dist = [[float('inf')] * V for _ in range(V)]
    
    for i in range(V):
        dist[i][i] = 0
        
    for u, v, peso in edges:
        dist[u][v] = peso
        
    for k in range(V):
        for i in range(V):
            for j in range(V):
                dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
                
    return dist

# --- 2. Función de Medición ---

def run_test(V, E):
    """
    Genera un grafo, corre los 3 algoritmos y mide el tiempo.
    """
    print(f"\n--- INICIO TEST: (Nodos V={V}, Aristas E={E}) ---")
    V, edges, adj = generar_grafo_aleatorio(V, E)
    start_node = 0
    
    results = {'V': V, 'E': E}
    
    # Medir Dijkstra
    start = time.perf_counter()
    dijkstra_heap(V, adj, start_node)
    end = time.perf_counter()
    results['Dijkstra (O(E log V))'] = (end - start) * 1000 # en ms
    print(f"  Dijkstra: {results['Dijkstra (O(E log V))']:.4f} ms")

    # Medir Bellman-Ford
    start = time.perf_counter()
    bellman_ford(V, edges, start_node)
    end = time.perf_counter()
    results['Bellman-Ford (O(VE))'] = (end - start) * 1000 # en ms
    print(f"  Bellman-Ford: {results['Bellman-Ford (O(VE))']:.4f} ms")

    # Medir Floyd-Warshall
    start = time.perf_counter()
    floyd_warshall(V, edges)
    end = time.perf_counter()
    results['Floyd-Warshall (O(V^3))'] = (end - start) * 1000 # en ms
    print(f"  Floyd-Warshall: {results['Floyd-Warshall (O(V^3))']:.4f} ms")
    
    return results

# --- 3. Función de Gráficos ---

def plot_results(results_list):
    """
    Toma la lista de resultados y la grafica con Pandas/Seaborn.
    """
    df = pd.DataFrame(results_list)
    
    # Convertir de formato "ancho" a "largo" para Seaborn
    df_melted = df.melt(
        id_vars=['V', 'E'], 
        var_name='Algoritmo', 
        value_name='Tiempo (ms)'
    )
    
    # Crear una etiqueta para cada test
    df_melted['Test Case'] = 'V=' + df_melted['V'].astype(str) + ', E=' + \
                             df_melted['E'].astype(str)

    print("\n--- Resultados Finales (Python) ---")
    print(df)

    # Graficar (escala logarítmica es casi obligatoria aquí)
    sns.set_theme(style="whitegrid")
    plt.figure(figsize=(12, 7))
    
    g = sns.barplot(
        data=df_melted,
        x='Test Case',
        y='Tiempo (ms)',
        hue='Algoritmo'
    )
    g.set_yscale("log") # ¡Importante!
    
    plt.title('Rendimiento de Algoritmos (Python) - Escala Logarítmica')
    plt.ylabel('Tiempo (ms) - log scale')
    plt.xticks(rotation=15)
    plt.tight_layout()
    plt.savefig('benchmark_python_log.png')
    plt.show()

# --- 4. Ejecución Principal ---

if __name__ == "__main__":
    # Definimos las pruebas
    tests_a_correr = [
        (50, 1000),     # Denso
        (300, 1500),    # Disperso
        (300, 20000),   # Denso
        (800, 2000),    # Muy Disperso
        # Añadamos uno más grande donde V^3 realmente duela
        (1000, 5000)    # Grande y disperso
    ]
    
    all_results = []
    for v, e in tests_a_correr:
        all_results.append(run_test(v, e))
        
    plot_results(all_results)