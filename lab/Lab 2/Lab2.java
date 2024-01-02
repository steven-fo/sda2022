import java.io.*;
import java.util.StringTokenizer;

public class Lab2 {
    private static InputReader in;
    private static PrintWriter out;

    // penjelasan soal: cari nilai / total terbesar sesuai jumlah input
    // jumlah input = ganjil, cari nilai terbesar dengan angka ganjil
    // jumlah input = genap, cari nilai terbesar dengan angka genap
    static long maxOddEvenSubSum(long[] a) {
        long maxSum = -1000000001;
        long thisSum = 0;
        int counter = 0;
        if (a.length % 2 == 0) { // input bernilai genap
            for (int i = 0; i<a.length; i++) {
                if (a[i] % 2 == 0) {
                    thisSum += a[i];
                    if (thisSum > maxSum) {
                        maxSum = thisSum;
                    } else if (thisSum < 0) {
                        thisSum = 0;
                    }
                } else if (a[i] % 2 != 0) {
                    thisSum = 0; // reset thisSum
                    counter++;
                    if (counter == a.length) {
                        maxSum = 0;
                    }
                }
            }
        } else if (a.length % 2 == 1) { // input bernilai ganjil
            for (int i = 0; i<a.length; i++) {
                if (a[i] % 2 != 0) {
                    thisSum += a[i];
                    if (thisSum > maxSum) {
                        maxSum = thisSum;
                    } else if (thisSum < 0) {
                        thisSum = 0;
                    }
                } else if (a[i] % 2 != 0) {
                    thisSum = 0; // reset thisSum
                    counter++;
                    if (counter == a.length) {
                        maxSum = 0;
                    }
                }
            }
        }
        return maxSum;
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of N
        int N = in.nextInt();

        // Read value of x
        long[] x = new long[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.nextLong();
        }

        long ans = maxOddEvenSubSum(x);
        out.println(ans);

        // don't forget to close/flush the output
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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