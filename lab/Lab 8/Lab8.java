import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Lab8 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        int E = in.nextInt();
        Graph graph = new Graph(N);

        // Bangun graf
        for (int i = 0; i < E; i++) {
            int A = in.nextInt();
            int B = in.nextInt();
            long W = in.nextLong();
            // Inisiasi jalur antara A dan B
            graph.addEdge(A, B, W);
        }
        graph.shortest(1);
        int H = in.nextInt();
        for (int i = 0; i < H; i++) {
            int K = in.nextInt();
            // Simpan titik yang memiliki harta karun
            graph.shortest(K);
        }

        int Q = in.nextInt();
        int O = in.nextInt();
        while (Q-- > 0) {
            Long totalOxygenNeeded = (long) 0;

            int T = in.nextInt();
            int davePosition = 1;
            while (T-- > 0) {
                int D = in.nextInt();
                // Update total oksigen dibutuhkan
                totalOxygenNeeded += graph.dist[davePosition][D];

                // Update posisi Dave
                davePosition = D;
            }
            // Implementasi Dave kembali ke daratan
            totalOxygenNeeded += graph.dist[davePosition][1];

            // Cetak 0 (rute tidak aman) atau 1 (rute aman)
            if (totalOxygenNeeded >= O) {
                out.println(0);
            } else {
                out.println(1);
            }

        }

        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}

class Graph {
    int totalNode;
    ArrayList<Pair>[] adj;
    long[][] dist;
    
    public Graph(int total) {
        this.totalNode = total;
        adj = new ArrayList[totalNode + 1];
        dist = new long[totalNode+1][totalNode + 1]; 
        for (int i = 1; i <= totalNode; i++) {
            adj[i] = new ArrayList<Pair>();
        }
    }

    public void addEdge(int from, int to, long weight) {
        adj[from].add(new Pair(to, weight));
		adj[to].add(new Pair(from, weight));
    }

    public void shortest(int source) {
        // inisiasi jarak awal
        for (int i = 1; i <= totalNode; i++) {
            dist[source][i] = Long.MAX_VALUE; 
        }
        dist[source][source] = 0;

        PriorityQueue<Pair> pq = new PriorityQueue<Pair>();
        pq.add(new Pair(source, 0)); 

        while (!pq.isEmpty()) {
            Pair curr = pq.poll();
            int node = curr.node;
            long distance = curr.weight;

            if (distance > dist[source][node]) continue;

            // iterasi semua tetangga node
            for (Pair pair : adj[node]) {
                int next = pair.node;
                long cost = pair.weight;

                if (distance + cost < dist[source][next]) {
                    dist[source][next] = distance + cost;
                    pq.add(new Pair(next, dist[source][next]));
                }
            }
        }
    }
}

class Pair implements Comparable<Pair>{
    int node;
    long weight;

    public Pair(int node, long weight) {
        this.node = node;
        this.weight = weight;
    }

    public int compareTo(Pair other) {
        return Long.compare(this.weight, other.weight);
    }
    
}