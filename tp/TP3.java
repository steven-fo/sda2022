import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class TP3 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // V = banyaknya ruangan, E = banyaknya koridor
        int V = in.nextInt(); int E = in.nextInt();
        Graph graph = new Graph(V);

        ArrayList<Integer> treasureNodes = new ArrayList<Integer>();
        for (int i = 1; i <= V; i++) {
            // Simpan titik yang memiliki harta karun
            String tipe = in.next();
            graph.vertex[i] = new Node(i, tipe);
            if (tipe.equals("S")) {
                treasureNodes.add(i);
            } 
        }

        // Bangun graf
        for (int i = 0; i < E; i++) {
            int A = in.nextInt();
            int B = in.nextInt();
            long N = in.nextLong();
            // Inisiasi jalur antara A dan B
            graph.addEdge(A, B, N);
        }

        // Pre compute djikstra
        for (int i = 0; i < treasureNodes.size(); i++) {
            graph.djikstra(treasureNodes.get(i));
        }
        graph.djikstra(1); // O((V + E) log V)

        int Q = in.nextInt();
        while (Q-- > 0) {
            String query = in.next();
            if (query.equals("M")) {
                long groupSize = in.nextLong();
                int count = 0;
                for (int i = 0; i < treasureNodes.size(); i++) {
                    if (groupSize >= graph.dist[1][treasureNodes.get(i)]) {
                        count++;
                    }
                    if (count == 100) {
                        break;
                    }
                }
                out.println(count);
            } else if (query.equals("S")) {
                int start = in.nextInt();
                if (graph.vertex[start].tipe.equals("S")) {
                    out.println(0);
                } else {
                    long minDist = Long.MAX_VALUE;
                    long thisMin = 0;
                    for (int i = 0; i < treasureNodes.size(); i++) {
                        thisMin = graph.dist[treasureNodes.get(i)][start];
                        if (thisMin < minDist) {
                            minDist = thisMin;
                        }
                    }
                    out.println(minDist);
                }
            } else {
                int start = in.nextInt();
                int middle = in.nextInt();
                int end = in.nextInt();
                long groupSize = in.nextLong();

                int startFlag = 0;
                int endFlag = 0;

                if (!graph.vertex[start].isVisited) {
                    graph.djikstra(start);
                }
                if (!graph.vertex[middle].isVisited) {
                    graph.djikstra(middle);
                }

                if (groupSize >= graph.dist[start][middle]) {
                    startFlag = 1;
                }
                if (groupSize >= graph.dist[middle][end]) {
                    endFlag = 1;
                }

                if (startFlag == 1 && endFlag == 1) {
                    out.println("Y");
                } else if (startFlag == 1 && endFlag != 1) {
                    out.println("H");
                } else {
                    out.println("N");
                }
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

// Implementasi Graph
class Graph {
    int V;
    int count;
    ArrayList<Pair>[] adj;
    long[][] dist;
    Node[] vertex;

    public Graph(int V) {
        this.V = V;
        adj = new ArrayList[V+1];
        dist = new long[V+1][V+1];
        vertex = new Node[V+1];
        for (int i = 1; i <= V; i++) {
            adj[i] = new ArrayList<Pair>();
        }
    }

    // Implementasi tambahkan edge ke graph
    public void addEdge(int from, int to, long weight) {
        adj[from].add(new Pair(to, weight));
        adj[to].add(new Pair(from, weight));
    }

    public void djikstra(int source) {
        for (int i = 1; i <= V; i++) {
            dist[source][i] = Long.MAX_VALUE;
        }
        dist[source][source] = 0;
        vertex[source].isVisited = true;

        MinHeap pq = new MinHeap(); // area hijau
        pq.insert(new Pair(source, 0));

        while (pq.size != 0) {
            Pair curr = pq.remove();
            int node = curr.node;
            long distance = curr.weight;

            if (distance > dist[source][node]) continue;

            // iterasi semua tetangga node
            for (Pair pair : adj[node]) {
                int next = pair.node;
                long cost = pair.weight;

                if (Math.max(distance, cost) < dist[source][next]) { // pilih minimum
                    dist[source][next] = Math.max(distance, cost);
                    pq.insert(new Pair(next, dist[source][next])); // masuk area hijau
                }
            }
        }
    }
}

class Pair implements Comparable<Pair> {
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

class Node {
    String tipe;
    int id;
    boolean isVisited;

    public Node(int id, String tipe) {
        this.id = id;
        this.tipe = tipe;
        this.isVisited = false;
    }
}

class MinHeap {
    Pair[] Heap;
    int size;

    public MinHeap() {
        this.size = 0;
        Heap = new Pair[10000];
    }

    public boolean isEmpty() { return this.size == 0; }

    public int parent(int pos) { return pos / 2; }

    public int leftChild(int pos) { return (2 * pos); }

    public int rightChild(int pos) { return (2 * pos) + 1; }

    public boolean isLeaf(int pos) { return (pos >= (size / 2) && pos <= size); }

    public void swap(int first, int second) {
        Pair tmp = Heap[first];
        Heap[first] = Heap[second];
        Heap[second] = tmp;
    }

    public void minHeapify(int pos) {
        if (isLeaf(pos)) {
            return;
        }

        if (Heap[leftChild(pos)].weight < Heap[pos].weight || Heap[rightChild(pos)].weight < Heap[pos].weight) {
            if (Heap[leftChild(pos)].weight < Heap[rightChild(pos)].weight) {
                swap(pos, leftChild(pos));
                minHeapify(leftChild(pos));
            } else {
                swap(pos, rightChild(pos));
                minHeapify(rightChild(pos));
            }
        }
    }

    public void insert(Pair element) {
        Heap[size] = element;
        int current = size;

        while (Heap[current].weight < Heap[parent(current)].weight) {
            swap(current, parent(current));
            current = parent(current);
        }
        size++;
    }

    public Pair remove() {
        Pair popped = Heap[0];
        Heap[0] = Heap[size-1];
        size--;
        minHeapify(0);
        return popped;
    }
}