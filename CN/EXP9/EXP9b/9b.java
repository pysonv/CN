import java.util.*;

public class LinkStateRouting {
    static final int V = 4;
    static final int INF = 9999;

    int minDistance(int[] dist, boolean[] visited) {
        int min = INF, minIndex = -1;
        for (int v = 0; v < V; v++)
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        return minIndex;
    }

    void dijkstra(int[][] graph, int src) {
        int[] dist = new int[V];
        boolean[] visited = new boolean[V];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        for (int count = 0; count < V - 1; count++) {
            int u = minDistance(dist, visited);
            visited[u] = true;
            for (int v = 0; v < V; v++)
                if (!visited[v] && graph[u][v] != 0 &&
                    dist[u] + graph[u][v] < dist[v])
                    dist[v] = dist[u] + graph[u][v];
        }

        System.out.println("Node Distance from Source " + src + ": " + Arrays.toString(dist));
    }

    public static void main(String[] args) {
        int[][] graph = {
            {0, 2, INF, 1},
            {2, 0, 3, 2},
            {INF, 3, 0, 4},
            {1, 2, 4, 0}
        };
        new LinkStateRouting().dijkstra(graph, 0);
    }
}
