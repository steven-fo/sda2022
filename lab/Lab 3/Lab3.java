import java.io.*;
import java.util.StringTokenizer;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Lab3{
    private static InputReader in;
    private static PrintWriter out;

    // Metode GA
    static String GA(String view) {
        //TODO: Implement this method
        if (view == "KANAN") {
            view = "KIRI";
        } else {
            view = "KANAN";
        }
        return view;
    }

    // Metode S
    static long S(int Si, ArrayList<Deque<Integer>> arrayList, int index, int hancur){
        //TODO: Implement this method
        long num = 0;
        if (index-hancur < 0) { // jika index melebihi size arraylist
            index = arrayList.size()-1;
        } else if (index-hancur > arrayList.size()-1) { // jika index melebihi size arraylist
            index = 0;
        } else {
            index -= hancur;
        }

        if (arrayList.get(index).size() < Si) { // jika tinggi lantai gedung < jumlah S
            Si = arrayList.get(index).size();
        }

        for (int i=0; i<Si; i++) {
            num += arrayList.get(index).pop();
        }
        if (arrayList.get(index).isEmpty()) {
            arrayList.remove(index);
        }
        return num;

    }

    // Template
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        ArrayList<Deque<Integer>> arrayList = new ArrayList<Deque<Integer>>(); // array list untuk menyimpan gedung
        int index = 0; // index untuk menentukan gedung berapa
        String view = "KANAN"; // default view
        long hasil = 0;
        int hancur = 0;
        
        // Read input
        long T = in.nextLong(); // target poin
        long X = in.nextInt(); // banyak gedung
        long C = in.nextInt(); // bil. bulat tiap lantai di gedung
        long Q = in.nextInt(); // banyak query

        // kalau ada yang baca hehe, untuk type input saya ubah ke long karena udh gatau mw ubah apalagi :)
        // dan ternyata ngakaknya pas saya ubah ke long, nilai saya tambah 5 XD

        for (int i = 0; i < X; i++) {
            Deque<Integer> deque = new ArrayDeque<Integer>(); // deque untuk menyimpan lantai gedung

            // Insert into ADT
            for (int j = 0; j < C; j++) {
                int Ci = in.nextInt();
                deque.add(Ci); // memasukkan nilai dari bawah deque
            }
            arrayList.add(i, deque); // memasukkan deque (gedung) ke arraylist
        }

        // Process the query
        for (int i = 0; i < Q; i++) {
            String perintah = in.next();
            if (perintah.equals("GA")) {
                view = GA(view);
                out.println(view);
            } else if (perintah.equals("S")) {
                int Si = in.nextInt();
                if (arrayList.size() == 0) {
                    out.println("MENANG");
                } else {
                    hancur = (int)X - arrayList.size();
                    hasil = S(Si, arrayList, index, hancur);
                    T -= hasil;
                    if (T<=0 || arrayList.size() == 0) {
                        out.println("MENANG");
                    } else {
                        out.println(hasil);
                        if (view == "KANAN") {
                            index += 1; // secara default akan bergerak ke gedung sebelah kanan
                        } else if (view == "KIRI") {
                            index -= 1; // akan bergerak ke gedung sebelah kiri
                        }
                    }
                }
            }
        }

        // don't forget to close the output
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

        public long nextLong(){
            return Long.parseLong(next());
        }

    }
}