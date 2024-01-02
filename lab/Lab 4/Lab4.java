import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Lab4 {
    private static InputReader in;
    private static PrintWriter out;

    // TODO: implement this method
    static int deteksiDNA(int M, int N, String X, String Y, int[][] arr) {
        if (M == 0 || N == 0) { // base case saat index = 0
            return 0;
        }

        if (arr[M][N] != -1) {
            return arr[M][N];
        }

        if (X.charAt(M-1) == Y.charAt(N-1)) {
            arr[M][N] = 1 + deteksiDNA(M-1, N-1, X, Y, arr);
            return arr[M][N];
        }
        arr[M][N] = Math.max(deteksiDNA(M, N-1, X, Y, arr), deteksiDNA(M-1, N, X, Y, arr));
        return arr[M][N];
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of T
        int T = in.nextInt();

        // Run T test case
        while (T-- > 0) {
            int M = in.nextInt();
            int N = in.nextInt();
            String S1 = in.next();
            String S2 = in.next();

            // Membuat array 2D untuk memorization
            int[][] arr = new int[M+1][N+1]; // index+1, membuat outline array agar tau sudah index out of bound
            for (int i=0; i<M+1; i++) {
                for (int j=0; j<N+1; j++) {
                    arr[i][j] = -1; // memasukkan nilai -1 untuk semua element array
                }
            }
            
            // TODO: implement method deteksiDNA(M, N, S1, S2) to get answer
            int ans = deteksiDNA(M, N, S1, S2, arr);
            out.println(ans);
        }

        // don't forget to close/flush the output
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
    }
}