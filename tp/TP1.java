import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import java.util.PriorityQueue;
import java.util.ArrayDeque;

public class TP1 {
    private static InputReader in;
    private static PrintWriter out;

    /**
     * Memasukkan pengunjung ke wahana. Jika uang tidak cukup, return -1. Jika uang cukup, return jumlah orang di antrean
     * Menyimpan total bermain pengunjung saat masuk antrean
     * @param idPengunjung
     * @param idWahana
     * @param arrWahana
     * @param arrPengunjung
     * @return jumlah antrean atau -1
     */
    static int A(int idPengunjung, int idWahana, Wahana[] arrWahana, Pengunjung[] arrPengunjung) {
        Wahana wahana = arrWahana[idWahana-1];
        Pengunjung pengunjung = arrPengunjung[idPengunjung-1];
        if (pengunjung.u - wahana.h >= 0) {
            if (pengunjung.t.equals("FT")) {
                pengunjung.arrBermain[idWahana-1] = pengunjung.totalBermain;
                wahana.PQFast.add(pengunjung);
            } else if (pengunjung.t.equals("R")) {
                pengunjung.arrBermain[idWahana-1] = pengunjung.totalBermain;
                wahana.PQReg.add(pengunjung);
            }
            return wahana.PQFast.size() + wahana.PQReg.size();
        } else {
            return -1;
        }
    }

    /**
     * Wahana mengambil orang sesuai kapasitas. Jika antrian kosong return -1. Jika tidak, return id pengunjung yang bermain
     * Set id wahana pada semua pengunjung untuk melakukan sort berdasarkan total bermain saat masuk antrean wahana
     * Mengisi antrean dengan pengunjung fast track sesuai kapasitas ft, kemudian mengisi dengan pengunjung reguler
     * @param idWahana
     * @param arrWahana
     * @param daftarKeluar
     */
    static void E(int idWahana, Wahana[] arrWahana, Pengunjung[] arrPengunjung, ArrayDeque<Pengunjung> daftarKeluar) {
        String hasil = "";
        Wahana wahana = arrWahana[idWahana-1];
        int kapasitas = wahana.kp;
        int kapasitasFT = (int) Math.ceil(wahana.kp * wahana.ft / 100.0);
        int kapasitasReg = kapasitas - kapasitasFT;
        int idPengunjung = -1;
        Pengunjung.setIdWahana(idWahana-1);
        while (kapasitas > 0) { // selama antrean masih kosong
            if (kapasitasFT > 0) { // kalau masih ada kapasitas FT dan belum kosong
                if (!wahana.PQFast.isEmpty()) {
                    Pengunjung temp = wahana.PQFast.poll();
                    Pengunjung pengunjung = arrPengunjung[temp.id-1];
                    if (pengunjung.u - wahana.h >= 0) {
                        if (!pengunjung.keluar) {
                            idPengunjung = Pengunjung.mulaiBermain(pengunjung, wahana, daftarKeluar);
                            hasil = hasil + idPengunjung + " ";
                            kapasitas--;
                            kapasitasFT--;
                        }
                    }
                } else {
                    kapasitasReg += kapasitasFT;
                    kapasitasFT = 0;
                }
            } else if (kapasitasReg > 0) {
                if (!wahana.PQReg.isEmpty()) {
                    Pengunjung temp = wahana.PQReg.poll();
                    Pengunjung pengunjung = arrPengunjung[temp.id-1];
                    if (pengunjung.u - wahana.h >= 0) {
                        if (!pengunjung.keluar) {
                            idPengunjung = Pengunjung.mulaiBermain(pengunjung, wahana, daftarKeluar);
                            hasil = hasil + idPengunjung + " ";
                            kapasitas--;
                            kapasitasReg--;
                        }
                    }
                } else if (!wahana.PQFast.isEmpty()) {
                    Pengunjung temp = wahana.PQFast.poll();
                    Pengunjung pengunjung = arrPengunjung[temp.id-1];
                    if (pengunjung.u - wahana.h >= 0) {
                        if (!pengunjung.keluar) {
                            idPengunjung = Pengunjung.mulaiBermain(pengunjung, wahana, daftarKeluar);
                            hasil = hasil + idPengunjung + " ";
                            kapasitas--;
                            kapasitasReg--;
                        }
                    }
                } else {
                    break;
                }
            }
        }
        if (hasil.equals("")) {
            out.println(-1);
        } else {
            out.println(hasil);
        }
    }

    /**
     * Mencari urutan bermain suatu pengunjung pada suatu wahana
     * Set id wahana untuk melakukan sort berdasarkan total bermain saat masuk antrean
     * @param idPengunjung
     * @param idWhana
     * @param arrWahana
     * @param arrPengunjung
     * @param daftarKeluar
     * @return
     */
    static int S(int idPengunjung, int idWhana, Wahana[] arrWahana, Pengunjung[] arrPengunjung, ArrayDeque<Pengunjung> daftarKeluar) {
        Wahana wahana = arrWahana[idWhana-1];
        Pengunjung pengunjung = arrPengunjung[idPengunjung-1];
        int kapasitasFT = (int) Math.ceil(wahana.kp * wahana.ft / 100.0);
        boolean isFound = false;
        Pengunjung.setIdWahana(idWhana-1);
        PriorityQueue<Pengunjung> TempAntreanFT = new PriorityQueue<Pengunjung>(wahana.PQFast);
        PriorityQueue<Pengunjung> TempAntreanR = new PriorityQueue<Pengunjung>(wahana.PQReg);
        if ((TempAntreanFT.contains(pengunjung) || TempAntreanR.contains(pengunjung)) && (pengunjung.u - wahana.h >= 0) && !pengunjung.keluar) {
            int counter = 0; // jumlah orang di antrian wahana
            int urutan = 1; // urutan bermain pengunjung
            int counterPengunjung = 1;
            while (counterPengunjung != arrPengunjung.length && !isFound) { // iterate semua pengunjung dan selama belum menemukan pengunjung yand dicari
                if (counter < kapasitasFT && !TempAntreanFT.isEmpty()) { // kalau jumlah pengunjung di antrean < kapasitas FT dan antreanFT tidak kosong
                    Pengunjung temp = TempAntreanFT.poll();
                    if (temp.id == pengunjung.id) {
                        isFound = true;
                        break;
                    } else if (temp.u - wahana.h >= 0 && !temp.keluar) { // kalau uang orang tersebut mencukupi, urutan dan jumlah orang di antrian ditambah
                        urutan++;
                        counter++;
                        counterPengunjung++;
                    } else {
                        counterPengunjung++;
                    }
                } else if (counter < wahana.kp && !TempAntreanR.isEmpty()) {
                    Pengunjung temp = TempAntreanR.poll();
                    if (temp.id == pengunjung.id) {
                        isFound = true;
                        break;
                    } else if (temp.u - wahana.h >= 0 && !temp.keluar) {
                        urutan++;
                        counter++;
                        counterPengunjung++;
                    } else {
                        counterPengunjung++;
                    }
                } else if (counter < wahana.kp && !TempAntreanFT.isEmpty()) {
                    Pengunjung temp = TempAntreanFT.poll();
                    if (temp.id == pengunjung.id) {
                        isFound = true;
                        break;
                    } else if (temp.u - wahana.h >= 0) {
                        urutan++;
                        counter++;
                        counterPengunjung++;
                    } else {
                        counterPengunjung++;
                    }
                } else if (counter >= wahana.kp) {
                    counter = 0;
                }
            } 
            return urutan;
        } else {
            return -1;
        }
    }

    /**
     * Mengeluarkan pengjung di awal/akhir daftar keluar
     * @param P
     * @param daftarKeluar
     * @return poin pengunjung yang dikeluarkan
     */
    static int F(int P, ArrayDeque<Pengunjung> daftarKeluar) {
        if (daftarKeluar.isEmpty()) {
            return -1;
        } else if (P == 1) {
            Pengunjung pengunjung = daftarKeluar.pollLast();
            pengunjung.keluar = true;
            return pengunjung.poin;
        } {
            Pengunjung pengunjung = daftarKeluar.pollFirst();
            pengunjung.keluar = true;
            return pengunjung.poin;
        }
    }

    static void O(int idPengunjung) {
        out.println(0);
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of M
        int M = in.nextInt(); // jumlah wahana
        Wahana[] arrayWahana = new Wahana[M]; // membuat array daftar wahana

        // Run M test case
        for (int i = 1; i<M+1; i++) {
            int h = in.nextInt(); // harga wahana
            int p = in.nextInt(); // poin yang didapat 
            int kp = in.nextInt(); // kapasitas pengunjung / antrean
            int ft = in.nextInt(); // kuota prioritas
            Wahana wahana = new Wahana(i, h, p, kp, ft);
            arrayWahana[i-1] = wahana;
        }

        // Read value of N
        int N = in.nextInt(); // jumlah pengunjung
        Pengunjung[] arrayPengunjung = new Pengunjung[N];

        // Run N test case
        for (int i = 1; i<N+1; i++) {
            String t = in.next(); // tipe pengunjung
            int u = in.nextInt(); // uang yang dimiliki pengunjung
            Pengunjung pengunjung = new Pengunjung(i, t, u, M);
            arrayPengunjung[i-1] = pengunjung;
        }

        // Read value of T
        int T = in.nextInt(); // jumlah aktivitas
        ArrayDeque<Pengunjung> daftarKeluar = new ArrayDeque<Pengunjung>();

        // Run T test case
        for (int i = 0; i<T; i++) {
            String query = in.next();
            if (query.equals("A")) {
                int idPengunjung = in.nextInt();
                int idWahana = in.nextInt();
                int hasil = A(idPengunjung, idWahana, arrayWahana, arrayPengunjung);
                out.println(hasil);
            } else if (query.equals("E")) {
                int idWahana = in.nextInt();
                E(idWahana, arrayWahana, arrayPengunjung, daftarKeluar);
            } else if (query.equals("S")) {
                int idPengunjung = in.nextInt();
                int idWahana = in.nextInt();
                int hasil = S(idPengunjung, idWahana, arrayWahana, arrayPengunjung, daftarKeluar);
                out.println(hasil);
            } else if (query.equals("F")) {
                int P = in.nextInt();
                int hasil = F(P, daftarKeluar);
                out.println(hasil);
            } else if (query.equals("O")) {
                int idPengunjung = in.nextInt();
                O(idPengunjung);
            }
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

class Pengunjung implements Comparable<Pengunjung>{
    int id; // id pengunjung
    String t; // tipe pengunjung
    int u; // uang yang dimiliki
    int totalBermain = 0;
    int poin = 0;
    boolean keluar = false;
    int[] arrBermain;
    static int index;

    public Pengunjung(int id, String t, int u, int jumlahWahana) {
        this.id = id;
        this.t = t;
        this.u = u;
        this.arrBermain = new int[jumlahWahana];
    }

    @Override public int compareTo(Pengunjung otherPengunjung) {
        int r = Integer.compare(this.arrBermain[index], otherPengunjung.arrBermain[index]);
        return (r==0) ? Integer.compare(this.id, otherPengunjung.id) : r;
    }

    public static void setIdWahana(int idWahana) {
        index = idWahana;
    }

    /**
     * Function untuk handle pengunjung bermain di wahana. Cek apakah duit pengunjung mencukupi dan belum keluar dari daftar keluar
     * Jika tidak mencukupi, return 0. Jika mencukupi, return id pengunjung
     * Jika uang pengunjung habis saat bermain, maka pengunjung masuk ke daftar keluar
     * @param isRegular
     * @param pengunjung
     * @param wahana
     * @param counter
     * @param daftarKeluar
     * @return id pengunjung atau 0
     */
    public static int mulaiBermain(Pengunjung pengunjung, Wahana wahana, ArrayDeque<Pengunjung> daftarKeluar) {
        pengunjung.poin += wahana.p;
        pengunjung.totalBermain++;
        pengunjung.u -= wahana.h;
        if (pengunjung.u <= 0) {
            daftarKeluar.add(pengunjung);
        }
        return pengunjung.id;
    }
}

class Wahana {
    int id; // id wahana
    int h; // harga wahana
    int p; // poin wahana
    int kp; // kapasitas pengunjung wahana
    int ft; // persentase prioritas fast track
    PriorityQueue<Pengunjung> PQReg; // antrean untuk pengunjung reguler
    PriorityQueue<Pengunjung> PQFast; // antrean untuk pengunjung fast track

    public Wahana(int id, int h, int p, int kp, int ft) {
        PriorityQueue<Pengunjung> PQReg = new PriorityQueue<Pengunjung>();
        PriorityQueue<Pengunjung> PQFast = new PriorityQueue<Pengunjung>();

        this.id = id;
        this.h = h;
        this.p = p;
        this.kp = kp;
        this.ft = ft;
        this.PQReg = PQReg;
        this.PQFast = PQFast;
    }
}