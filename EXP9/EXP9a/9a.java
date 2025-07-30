import java.util.*;

public class DVRouting {
    static final int INF = 9999, V = 4;

    public static void main(String[] args) {
        int[][] graph = {
            {0, 1, INF, 3},
            {1, 0, 4, INF},
            {INF, 4, 0, 2},
            {3, INF, 2, 0}
        };

        int[][] dist = new int[V][V];
        for (int i = 0; i < V; i++)
            dist[i] = Arrays.copyOf(graph[i], V);

        for (int k = 0; k < V; k++)
            for (int i = 0; i < V; i++)
                for (int j = 0; j < V; j++)
                    if (dist[i][j] > dist[i][k] + dist[k][j])
                        dist[i][j] = dist[i][k] + dist[k][j];

        System.out.println("Distance Matrix:");
        for (int[] row : dist)
            System.out.println(Arrays.toString(row));
    }
}
