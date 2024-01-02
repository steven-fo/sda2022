import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class TP2 {
    private static InputReader in;
    private static PrintWriter out;

    static int T(int poin, int idSiswa, DoublyLL<Kelas> kelas) {
        Kelas tempKelas = kelas.current.data;
        Siswa tempSiswa = tempKelas.tree.get(idSiswa, tempKelas.tree.root);
        if (tempSiswa == null) {
            return -1;
        } else {
            int poinTutor = tempKelas.tree.getTutor(tempSiswa, tempKelas.tree.root);
            if (poinTutor > poin) {
                poinTutor = poin;
            }
            tempKelas.tree.root = tempKelas.tree.delete(tempSiswa.id, tempSiswa.poin, tempKelas.tree.root, false);
            tempSiswa.poin = tempSiswa.poin + poin + poinTutor;
            tempKelas.tree.root = tempKelas.tree.insert(tempSiswa, tempKelas.tree.root);
            return tempSiswa.poin;
        }
    }

    static int C(int idSiswa, DoublyLL<Kelas> kelas) {
        Kelas kelasSekarang = kelas.current.data;
        Siswa tempSiswa = kelasSekarang.tree.get(idSiswa, kelasSekarang.tree.root);
        if (tempSiswa == null) {
            return -1;
        } else if (tempSiswa.sp == 0) {
            if (tempSiswa.poin != 0) {
                kelasSekarang.tree.root = kelasSekarang.tree.delete(idSiswa, tempSiswa.poin, kelasSekarang.tree.root, false);
                tempSiswa.poin = 0;
                kelasSekarang.tree.root = kelasSekarang.tree.insert(tempSiswa, kelasSekarang.tree.root);  
            }
            tempSiswa.sp = 1;
            return 0;
        } else if (tempSiswa.sp == 1) {
            Kelas lastKelas = kelas.last.data;
            if (kelasSekarang == lastKelas) {
                if (tempSiswa.poin != 0) {
                    kelasSekarang.tree.root = kelasSekarang.tree.delete(idSiswa, tempSiswa.poin, kelasSekarang.tree.root, false);
                    tempSiswa.poin = 0;
                    kelasSekarang.tree.root = kelasSekarang.tree.insert(tempSiswa, kelasSekarang.tree.root);
                }
            } else {
                kelasSekarang.tree.root = kelasSekarang.tree.delete(idSiswa, tempSiswa.poin, kelasSekarang.tree.root, false);
                tempSiswa.idKelas = lastKelas.id;
                tempSiswa.poin = 0;
                lastKelas.tree.root = lastKelas.tree.insert(tempSiswa, lastKelas.tree.root);
                kelasSekarang.checkSize(kelas, kelasSekarang.tree.root);
            }
            tempSiswa.sp = 2;
            return tempSiswa.idKelas;
        } else if (tempSiswa.sp == 2) {
            kelasSekarang.tree.delete(tempSiswa.id, tempSiswa.poin, kelasSekarang.tree.root, false);
            kelasSekarang.checkSize(kelas, kelasSekarang.tree.root);
            tempSiswa.sp = 3;
            return tempSiswa.id;
        } else {
            return -1;
        }
    }

    static int G(String arah, DoublyLL<Kelas> kelas) {
        if (arah.equals("L")) {
            if (kelas.current.prev == null) { // ada di start
                kelas.current = kelas.last;
            } else {
                kelas.current = kelas.current.prev;
            }
            return kelas.current.data.id;
        } else {
            if (kelas.current.next == null) { // ada di last
                kelas.current = kelas.start;
            } else {
                kelas.current = kelas.current.next;
            }
            return kelas.current.data.id;
        }
    }

    static String S(DoublyLL<Kelas> kelas) {
        if (kelas.current.next != null && kelas.current.prev != null) { // kasus 1: ada 3 kelas
            NodeTreeSiswa minM1 = kelas.current.data.tree.getMinSiswa(kelas.current.data.tree.root);
            NodeTreeSiswa maxM1 = kelas.current.data.tree.getMaxSiswa(kelas.current.data.tree.root);
            NodeTreeSiswa minMa1 = kelas.current.prev.data.tree.getMinSiswa(kelas.current.prev.data.tree.root);
            NodeTreeSiswa maxMb1 = kelas.current.next.data.tree.getMaxSiswa(kelas.current.next.data.tree.root);

            kelas.current.data.tree.root = kelas.current.data.tree.delete(minM1.key.id, minM1.key.poin, kelas.current.data.tree.root, false);
            kelas.current.data.tree.root = kelas.current.data.tree.delete(maxM1.key.id, maxM1.key.poin, kelas.current.data.tree.root, false);
            kelas.current.prev.data.tree.root = kelas.current.prev.data.tree.delete(minMa1.key.id, minMa1.key.poin, kelas.current.prev.data.tree.root, false);
            kelas.current.next.data.tree.root = kelas.current.next.data.tree.delete(maxMb1.key.id, maxMb1.key.poin, kelas.current.next.data.tree.root, false);

            NodeTreeSiswa minM2 = kelas.current.data.tree.getMinSiswa(kelas.current.data.tree.root);
            NodeTreeSiswa maxM2 = kelas.current.data.tree.getMaxSiswa(kelas.current.data.tree.root);
            NodeTreeSiswa minMa2 = kelas.current.prev.data.tree.getMinSiswa(kelas.current.prev.data.tree.root);
            NodeTreeSiswa maxMb2 = kelas.current.next.data.tree.getMaxSiswa(kelas.current.next.data.tree.root);

            kelas.current.data.tree.root = kelas.current.data.tree.delete(minM2.key.id, minM2.key.poin, kelas.current.data.tree.root, false);
            kelas.current.data.tree.root = kelas.current.data.tree.delete(maxM2.key.id, maxM2.key.poin, kelas.current.data.tree.root, false);
            kelas.current.prev.data.tree.root = kelas.current.prev.data.tree.delete(minMa2.key.id, minMa2.key.poin, kelas.current.prev.data.tree.root, false);
            kelas.current.next.data.tree.root = kelas.current.next.data.tree.delete(maxMb2.key.id, maxMb2.key.poin, kelas.current.next.data.tree.root, false);

            NodeTreeSiswa minM3 = kelas.current.data.tree.getMinSiswa(kelas.current.data.tree.root);
            NodeTreeSiswa maxM3 = kelas.current.data.tree.getMaxSiswa(kelas.current.data.tree.root);
            NodeTreeSiswa minMa3 = kelas.current.prev.data.tree.getMinSiswa(kelas.current.prev.data.tree.root);
            NodeTreeSiswa maxMb3 = kelas.current.next.data.tree.getMaxSiswa(kelas.current.next.data.tree.root);

            kelas.current.data.tree.root = kelas.current.data.tree.delete(minM3.key.id, minM3.key.poin, kelas.current.data.tree.root, false);
            kelas.current.data.tree.root = kelas.current.data.tree.delete(maxM3.key.id, maxM3.key.poin, kelas.current.data.tree.root, false);
            kelas.current.prev.data.tree.root = kelas.current.prev.data.tree.delete(minMa3.key.id, minMa3.key.poin, kelas.current.prev.data.tree.root, false);
            kelas.current.next.data.tree.root = kelas.current.next.data.tree.delete(maxMb3.key.id, maxMb3.key.poin, kelas.current.next.data.tree.root, false);
            
            kelas.current.data.tree.root = kelas.current.data.tree.insert(minMa1.key, kelas.current.data.tree.root);
            kelas.current.data.tree.root = kelas.current.data.tree.insert(maxMb1.key, kelas.current.data.tree.root);
            kelas.current.prev.data.tree.root = kelas.current.prev.data.tree.insert(maxM1.key, kelas.current.prev.data.tree.root);
            kelas.current.next.data.tree.root = kelas.current.next.data.tree.insert(minM1.key, kelas.current.next.data.tree.root);

            kelas.current.data.tree.root = kelas.current.data.tree.insert(minMa2.key, kelas.current.data.tree.root);
            kelas.current.data.tree.root = kelas.current.data.tree.insert(maxMb2.key, kelas.current.data.tree.root);
            kelas.current.prev.data.tree.root = kelas.current.prev.data.tree.insert(maxM2.key, kelas.current.prev.data.tree.root);
            kelas.current.next.data.tree.root = kelas.current.next.data.tree.insert(minM2.key, kelas.current.next.data.tree.root);

            kelas.current.data.tree.root = kelas.current.data.tree.insert(minMa3.key, kelas.current.data.tree.root);
            kelas.current.data.tree.root = kelas.current.data.tree.insert(maxMb3.key, kelas.current.data.tree.root);
            kelas.current.prev.data.tree.root = kelas.current.prev.data.tree.insert(maxM3.key, kelas.current.prev.data.tree.root);
            kelas.current.next.data.tree.root = kelas.current.next.data.tree.insert(minM3.key, kelas.current.next.data.tree.root);

            Siswa bestSiswa = kelas.current.data.tree.getMaxSiswa(kelas.current.data.tree.root).key;
            Siswa worstSiswa = kelas.current.data.tree.getMinSiswa(kelas.current.data.tree.root).key;
            return String.valueOf(bestSiswa.id) + " " + String.valueOf(worstSiswa.id);
        } else if (kelas.current.next != null || kelas.current.prev != null) { // kasus 2: ada 2 kelas
            if (kelas.current == kelas.start) { // kasus 1: kelas terbaik
                NodeTreeSiswa minM1 = kelas.current.data.tree.getMinSiswa(kelas.current.data.tree.root);
                NodeTreeSiswa maxMb1 = kelas.current.next.data.tree.getMaxSiswa(kelas.current.next.data.tree.root);

                kelas.current.data.tree.root = kelas.current.data.tree.delete(minM1.key.id, minM1.key.poin, kelas.current.data.tree.root, false);
                kelas.current.next.data.tree.root = kelas.current.next.data.tree.delete(maxMb1.key.id, maxMb1.key.poin, kelas.current.next.data.tree.root, false);

                NodeTreeSiswa minM2 = kelas.current.data.tree.getMinSiswa(kelas.current.data.tree.root);
                NodeTreeSiswa maxMb2 = kelas.current.next.data.tree.getMaxSiswa(kelas.current.next.data.tree.root);

                kelas.current.data.tree.root = kelas.current.data.tree.delete(minM2.key.id, minM2.key.poin, kelas.current.data.tree.root, false);
                kelas.current.next.data.tree.root = kelas.current.next.data.tree.delete(maxMb2.key.id, maxMb2.key.poin, kelas.current.next.data.tree.root, false);

                NodeTreeSiswa minM3 = kelas.current.data.tree.getMinSiswa(kelas.current.data.tree.root);
                NodeTreeSiswa maxMb3 = kelas.current.next.data.tree.getMaxSiswa(kelas.current.next.data.tree.root);

                kelas.current.data.tree.root = kelas.current.data.tree.delete(minM3.key.id, minM3.key.poin, kelas.current.data.tree.root, false);
                kelas.current.next.data.tree.root = kelas.current.next.data.tree.delete(maxMb3.key.id, maxMb3.key.poin, kelas.current.next.data.tree.root, false);
                
                kelas.current.data.tree.root = kelas.current.data.tree.insert(maxMb1.key, kelas.current.data.tree.root);
                kelas.current.next.data.tree.root = kelas.current.next.data.tree.insert(minM1.key, kelas.current.next.data.tree.root);

                kelas.current.data.tree.root = kelas.current.data.tree.insert(maxMb2.key, kelas.current.data.tree.root);
                kelas.current.next.data.tree.root = kelas.current.next.data.tree.insert(minM2.key, kelas.current.next.data.tree.root);

                kelas.current.data.tree.root = kelas.current.data.tree.insert(maxMb3.key, kelas.current.data.tree.root);
                kelas.current.next.data.tree.root = kelas.current.next.data.tree.insert(minM3.key, kelas.current.next.data.tree.root);

                Siswa bestSiswa = kelas.current.data.tree.getMaxSiswa(kelas.current.data.tree.root).key;
                Siswa worstSiswa = kelas.current.data.tree.getMinSiswa(kelas.current.data.tree.root).key;
                return String.valueOf(bestSiswa.id) + " " + String.valueOf(worstSiswa.id);
            } else { // kasus 2: kelas terburuk
                NodeTreeSiswa maxM1 = kelas.current.data.tree.getMaxSiswa(kelas.current.data.tree.root);
                NodeTreeSiswa minMa1 = kelas.current.prev.data.tree.getMinSiswa(kelas.current.prev.data.tree.root);

                kelas.current.data.tree.root = kelas.current.data.tree.delete(maxM1.key.id, maxM1.key.poin, kelas.current.data.tree.root, false);
                kelas.current.prev.data.tree.root = kelas.current.prev.data.tree.delete(minMa1.key.id, minMa1.key.poin, kelas.current.prev.data.tree.root, false);

                NodeTreeSiswa maxM2 = kelas.current.data.tree.getMaxSiswa(kelas.current.data.tree.root);
                NodeTreeSiswa minMa2 = kelas.current.prev.data.tree.getMinSiswa(kelas.current.prev.data.tree.root);

                kelas.current.data.tree.root = kelas.current.data.tree.delete(maxM2.key.id, maxM2.key.poin, kelas.current.data.tree.root, false);
                kelas.current.prev.data.tree.root = kelas.current.prev.data.tree.delete(minMa2.key.id, minMa2.key.poin, kelas.current.prev.data.tree.root, false);

                NodeTreeSiswa maxM3 = kelas.current.data.tree.getMaxSiswa(kelas.current.data.tree.root);
                NodeTreeSiswa minMa3 = kelas.current.prev.data.tree.getMinSiswa(kelas.current.prev.data.tree.root);

                kelas.current.data.tree.root = kelas.current.data.tree.delete(maxM3.key.id, maxM3.key.poin, kelas.current.data.tree.root, false);
                kelas.current.prev.data.tree.root = kelas.current.prev.data.tree.delete(minMa3.key.id, minMa3.key.poin, kelas.current.prev.data.tree.root, false);
                
                kelas.current.data.tree.root = kelas.current.data.tree.insert(minMa1.key, kelas.current.data.tree.root);
                kelas.current.prev.data.tree.root = kelas.current.prev.data.tree.insert(maxM1.key, kelas.current.prev.data.tree.root);

                kelas.current.data.tree.root = kelas.current.data.tree.insert(minMa2.key, kelas.current.data.tree.root);
                kelas.current.prev.data.tree.root = kelas.current.prev.data.tree.insert(maxM2.key, kelas.current.prev.data.tree.root);

                kelas.current.data.tree.root = kelas.current.data.tree.insert(minMa3.key, kelas.current.data.tree.root);
                kelas.current.prev.data.tree.root = kelas.current.prev.data.tree.insert(maxM3.key, kelas.current.prev.data.tree.root);

                Siswa bestSiswa = kelas.current.data.tree.getMaxSiswa(kelas.current.data.tree.root).key;
                Siswa worstSiswa = kelas.current.data.tree.getMinSiswa(kelas.current.data.tree.root).key;
                return String.valueOf(bestSiswa.id) + " " + String.valueOf(worstSiswa.id);
            }
        } else { // kasus 3: cuma ada 1 kelas
            return "-1 -1";
        }
    }

    static int K(DoublyLL<Kelas> kelas) {
        kelas.start = kelas.mergeSort(kelas.start);
        ListNode flag = kelas.start;
        int urutan = 1;
        while (flag != kelas.current) {
            flag = flag.next;
            urutan++;
        }
        kelas.updateLast(kelas.start);
        return urutan;
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        DoublyLL<Kelas> kelas = new DoublyLL<Kelas>();
        int idKelas = 1;
        int idSiswa = 1;
        int jumlahSiswa = 0;

        int M = in.nextInt(); // Banyak kelas
        for (int i = 0; i<M; i++) {
            int Mi = in.nextInt(); // Banyak siswa di kelas
            Kelas tempKelas = new Kelas(idKelas, Mi);
            idKelas++;
            jumlahSiswa += Mi;
            kelas.insert(tempKelas);
        }

        int counterSiswa = 0;
        kelas.current = kelas.start;
        for (int i = 0; i<jumlahSiswa+M; i++) {
            Kelas tempKelas = kelas.current.data;
            if (counterSiswa != tempKelas.jumlahSiswa) {
                int poin = in.nextInt();
                Siswa tempSiswa = new Siswa(idSiswa, poin, tempKelas.id);
                tempKelas.tree.root = tempKelas.tree.insert(tempSiswa, tempKelas.tree.root);
                idSiswa++;
                counterSiswa++;
            } else {
                kelas.current = kelas.current.next;
                counterSiswa = 0;
            }
        }
        kelas.current = kelas.start;

        int Q = in.nextInt(); // Banyak query
        for (int i = 0; i<Q; i++) {
            String query = in.next();
            if (query.equals("T")) {
                int poin = in.nextInt();
                int id = in.nextInt();
                out.println(T(poin, id, kelas));
            } else if (query.equals("C")) {
                int id = in.nextInt();
                out.println(C(id, kelas));
            } else if (query.equals("G")) {
                String arah = in.next();
                out.println(G(arah, kelas));
            } else if (query.equals("S")) {
                out.println(S(kelas));
            } else if (query.equals("K")) {
                out.println(K(kelas));
            } else if (query.equals("A")) {
                int N = in.nextInt();
                Kelas newKelas = new Kelas(idKelas, N);
                idKelas++;
                kelas.insert(newKelas);
                for (int j = 0; j<N; j++) {
                    Siswa tempSiswa = new Siswa(idSiswa, 0, newKelas.id);
                    newKelas.tree.root = newKelas.tree.insert(tempSiswa, newKelas.tree.root);
                    idSiswa++;
                }
                out.println(newKelas.id);
            }
        }

        out.close();
    }

    static void display(DoublyLL<Kelas> kelas) 
    { 
        ListNode temp = kelas.current; 
        while (temp.next != null) { 
            System.out.print(temp.data + " --> "); 
            temp = temp.next; 
        }
        System.out.print(temp.data + " --> "); 
        System.out.println("STOP"); 
    }

    static void printTree(NodeTree currPtr, String indent, boolean last) {
        if (currPtr != null) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "   ";
            } else {
                System.out.print("L----");
                indent += "|  ";
            }
            System.out.print(currPtr.key + " jumlah: " + currPtr.treeSiswa.height(currPtr.treeSiswa.root));
            System.out.print(indent);
            printTree(currPtr.left, indent, false);
            printTree(currPtr.right, indent, true);
        }
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

        public char nextChar() {
            return next().charAt(0);
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}

class Kelas {
    AVLTree tree;
    double average;
    int id;
    int jumlahSiswa;

    public Kelas(int id, int jumlahSiswa) {
        this.id = id;
        this.jumlahSiswa = jumlahSiswa;
        this.tree = new AVLTree();
    }

    public double countAverage() {
        average = ( (double) tree.countTotal(tree.root) / this.jumlahSiswa);
        return average;
    }

    public void checkSize(DoublyLL<Kelas> kelas, NodeTree node) {
        int size = this.tree.getCount(node);
        if (size < 6) {
            ListNode pointer = kelas.getPosition(this.id);
            if (pointer.next != null || pointer.prev != null) {
                if (pointer.next == null) { // di ujung linkedlist
                    moveSiswa(kelas, node, pointer, true);
                } else {
                    moveSiswa(kelas, node, pointer, false);
                }
            }
        }
    }

    public void moveSiswa(DoublyLL<Kelas> kelas, NodeTree node, ListNode pointer, boolean isLast) {
        ListNode destination = pointer;
        if (isLast) {
            destination = pointer.prev;
            kelas.last = destination;
        } else {
            destination = pointer.next;  
        }
        pointer.data.move(node, destination);
        kelas.delete(pointer.data);
        kelas.current = destination;
        kelas.current.data.jumlahSiswa += pointer.data.jumlahSiswa;
    }

    public void move(NodeTree node, ListNode destination) {
        if (node != null) {
            move(node.left, destination);
            destination.data.tree.root = destination.data.tree.insertMove(destination.data.tree.root, node.key, node.height, node.jumlahSiswa, node.count, node.treeSiswa, destination.data.id);
            move(node.right, destination);
        }
    }
}

class Siswa {
    int poin;
    int tutor;
    int id;
    int idKelas;
    int sp;

    public Siswa(int id, int poin, int idKelas) {
        this.id = id;
        this.poin = poin;
        this.idKelas = idKelas;
        this.sp = 0;
    }
}

class ListNode {
    Kelas data;
    ListNode prev;
    ListNode next;

    public ListNode(Kelas data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}

class DoublyLL<E> {
    ListNode current;
    ListNode start;
    ListNode last;

    public DoublyLL() {
        this.current = null;
        this.start = null;
        this.last = null;
    }

    public void insert(Kelas data) { // O(n)
        ListNode newNode = new ListNode(data);
        newNode.next = null;
        if (start == null) {
            newNode.prev = null;
            start = newNode;
            last = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }

    }

    public void delete(Kelas data) {
        ListNode pointer = start;
        if (start == null || pointer == null) {
            return;
        } else {
            while (pointer.data != data) {
                pointer = pointer.next;
            }
            if (pointer.next == null) { // kalau diujung kanan
                last = pointer.prev;
                pointer.prev.next = null;
            } else if (pointer.prev == null) { // kalau diujung kiri
                start = pointer.next;
                pointer.next.prev = null;
            } else {
                pointer.prev.next = pointer.next;
                pointer.next.prev = pointer.prev;
            }
            return;
        }
    }

    public Kelas getKelas(int idKelas) {
        ListNode temp = start;
        Kelas result = null;
        do {
            if (temp.data.id == idKelas) {
                result = temp.data;
            }
            temp = temp.next;
        } while (temp != null);
        return result;
    }

    public ListNode getPosition(int idKelas) {
        ListNode temp = start;
        while (temp.data.id != idKelas) {
            temp = temp.next;
        }
        return temp;
    }

    public ListNode split(ListNode node) {
        ListNode fast = node;
        ListNode slow = node;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode temp = slow.next;
        slow.next = null;
        return temp;
    }

    public ListNode mergeSort(ListNode node) { // O(n Log n)
        if (node == null || node.next == null) {
            return node;
        }

        ListNode second = split(node);
        node = mergeSort(node);
        second = mergeSort(second);
        return merge(node, second);
    }

    public ListNode merge(ListNode first, ListNode second) {
        if (first == null) {
            return second;
        }

        if (second == null) {
            return first;
        }

        if (first.data.countAverage() > second.data.countAverage()) {
            first.next = merge(first.next, second);
            first.next.prev = first;
            first.prev = null;
            return first;
        } else if (first.data.countAverage() < second.data.countAverage()) {
            second.next = merge(first, second.next);
            second.next.prev = second;
            second.prev = null;
            return second;
        } else { // average sama, cek id
            if (first.data.id < second.data.id) {
                first.next = merge(first.next, second);
                first.next.prev = first;
                first.prev = null;
                return first;
            } else {
                second.next = merge(first, second.next);
                second.next.prev = second;
                second.prev = null;
                return second;
            }
        }
    }

    public void updateLast(ListNode node) {
        while (node.next != null) {
            node = node.next;
        }
        last = node;
    }
}

class NodeTree {
    int key; // poin
    int height;
    NodeTree left;
    NodeTree right;
    AVLTreeSiswa treeSiswa;
    int jumlahSiswa; // jumlah siswa dengan poin sama
    int count; // jumlah node

    public NodeTree(int poin) {
        this.key = poin;
        this.left = null;
        this.right = null;
        this.height = 0;
        this.treeSiswa = new AVLTreeSiswa();
        this.jumlahSiswa = 1;
        this.count = 1;
    }
}

class AVLTree {
    NodeTree root;

    public AVLTree() {
        this.root = null;
    }

    void updateHeight(NodeTree node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    int height(NodeTree node) {
        return (node == null) ? -1 : node.height;
    }

    int getBalance(NodeTree node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    int getTutor(Siswa siswa, NodeTree node) {
        if (node == null) {
            return 0;
        }

        if (node.key == siswa.poin) {
            return getCount(node.left) + node.jumlahSiswa - 1;
        }
        
        if (node.key < siswa.poin) {
            if (node.left != null) {
                return getCount(node.left) + getTutor(siswa, node.right) + node.jumlahSiswa;
            } else {
                return getTutor(siswa, node.right) + node.jumlahSiswa;
            }
        }
        return getTutor(siswa, node.left); // if nilai di node > poin dicari
    }

    long countTotal(NodeTree node) {
        if (node == null) {
            return 0;
        }
        long total = countTotal(node.left) + countTotal(node.right);
        if (node != null) {
            total += node.key * node.jumlahSiswa;
        }
        return total;
    }

    Siswa get(int idSiswa, NodeTree node) {
        if (node == null) {
            return null;
        }

        Siswa found = node.treeSiswa.get(idSiswa, node.treeSiswa.root);
        if (found != null) {
            return found;
        }

        Siswa foundLeft = get(idSiswa, node.left);
        if (foundLeft != null) {
            return foundLeft;
        }

        return get(idSiswa, node.right);
    }

    NodeTree insertMove(NodeTree node, int poin, int height, int jumlahSiswa, int count, AVLTreeSiswa treeSiswa, int idKelas) {
        if (node == null) {
            NodeTree newNode = new NodeTree(poin);
            newNode.jumlahSiswa = jumlahSiswa;
            newNode.treeSiswa = treeSiswa;
            return newNode;
        }
        if (poin < node.key) {
            node.left = insertMove(node.left, poin, height, jumlahSiswa, count, treeSiswa, idKelas);
        } else if (poin > node.key) {
            node.right = insertMove(node.right, poin, height, jumlahSiswa, count, treeSiswa, idKelas);
        } else { // poin == node.key
            node.treeSiswa.move(treeSiswa.root, node.treeSiswa, idKelas);
            node.jumlahSiswa += jumlahSiswa;
            node.count += count;
        }
        updateHeight(node);
        node.count = node.jumlahSiswa + getCount(node.left) + getCount(node.right);
        int balance = getBalance(node);
        if (balance > 1) {
            if (poin > node.left.key) {
                node = doubleRight(node);
            } else {
                node = rotateRight(node);
            }
        } else if (balance < -1) {
            if (poin > node.right.key) {
                node = rotateLeft(node);
            } else {
                node = doubleLeft(node);
            }
        }
        return node;
    }

    NodeTree insert(Siswa siswa, NodeTree node) {
        if (node == null) {
            node = new NodeTree(siswa.poin);
            node.treeSiswa.root = node.treeSiswa.insert(siswa, node.treeSiswa.root);
        } else if (siswa.poin < node.key) {
            node.left = insert(siswa, node.left);
        } else if (siswa.poin > node.key) {
            node.right = insert(siswa, node.right);
        } else if (siswa.poin == node.key) {
            node.treeSiswa.root = node.treeSiswa.insert(siswa, node.treeSiswa.root);
            node.jumlahSiswa += 1;
            node.count += 1;
        }
        updateHeight(node);
        node.count = node.jumlahSiswa + getCount(node.left) + getCount(node.right);
        int balance = getBalance(node);
        if (balance > 1) {
            if (siswa.poin > node.left.key) {
                node = doubleRight(node);
            } else {
                node = rotateRight(node);
            }
        } else if (balance < -1) {
            if (siswa.poin > node.right.key) {
                node = rotateLeft(node);
            } else {
                node = doubleLeft(node);
            }
        }
        return node;
    }

    NodeTree delete(int idSiswa, int poin, NodeTree node, boolean isMin) {
        if (node == null) {
            return null;
        }
        if (poin < node.key) {
            node.left = delete(idSiswa, poin, node.left, false);
        } else if (poin > node.key) {
            node.right = delete(idSiswa, poin, node.right, false);
        } else {
            if (node.jumlahSiswa > 1 && !isMin) {
                node.treeSiswa.root = node.treeSiswa.delete(idSiswa, node.treeSiswa.root);
                node.jumlahSiswa -= 1;
                node.count -= 1;
            } else {
                if (node.left == null || node.right == null) {
                    if (node.left == null) {
                        node = node.right;
                    } else {
                        node = node.left;
                    }
                } else { // ada 2 children
                    NodeTree temp = minValueNode(node.right); // ambil terkecil di kanan
                    node.key = temp.key; // set key yang diapus jd key terkecil di kanan
                    node.count = temp.count;
                    node.jumlahSiswa = temp.jumlahSiswa;
                    node.treeSiswa = temp.treeSiswa;
                    node.right = delete(idSiswa, temp.key, node.right, true); // set bagian kanan dari node baru
                }
            }
        }
        if (node == null) {
            return node;
        }
        updateHeight(node);
        node.count = node.jumlahSiswa + getCount(node.left) + getCount(node.right);
        int balance = getBalance(node);
        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node = doubleRight(node);
            } else {
                node = rotateRight(node);
            }
        } else if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node = doubleLeft(node);
            } else {
                node = rotateLeft(node);
            }
        }
        return node;
    }

    NodeTree rotateRight(NodeTree node2) {
        NodeTree node1 = node2.left;
        NodeTree temp = node1.right;
        node1.right = node2;
        node2.left = temp;
        updateHeight(node2);
        node2.count = node2.jumlahSiswa + getCount(node2.left) + getCount(node2.right);
        updateHeight(node1);
        node1.count = node1.jumlahSiswa + getCount(node1.left) + getCount(node1.right);
        return node1;
    }

    NodeTree rotateLeft(NodeTree node1) {
        NodeTree node2 = node1.right;
        NodeTree temp = node2.left;
        node2.left = node1;
        node1.right = temp;
        updateHeight(node1);
        node1.count = node1.jumlahSiswa + getCount(node1.left) + getCount(node1.right);
        updateHeight(node2);
        node2.count = node2.jumlahSiswa + getCount(node2.left) + getCount(node2.right);
        return node2;
    }

    NodeTree doubleRight(NodeTree node3) {
        node3.left = rotateLeft(node3.left);
        return rotateRight(node3);
    }

    NodeTree doubleLeft(NodeTree node1) {
        node1.right = rotateRight(node1.right);
        return rotateLeft(node1);
    }

    NodeTree minValueNode(NodeTree node) {
        NodeTree current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    NodeTree maxValueNode(NodeTree node) {
        NodeTree current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }

    NodeTreeSiswa getMinSiswa(NodeTree node) {
        NodeTree worst = minValueNode(node);
        return worst.treeSiswa.maxValueNode(worst.treeSiswa.root);
    }

    NodeTreeSiswa getMaxSiswa(NodeTree node) {
        NodeTree best = maxValueNode(node);
        return best.treeSiswa.minValueNode(best.treeSiswa.root);
    }

    int getCount(NodeTree node) {
        if (node == null) {
            return 0;
        }
        return node.count;
    }
}

class NodeTreeSiswa {
    Siswa key;
    int height;
    NodeTreeSiswa left;
    NodeTreeSiswa right;

    public NodeTreeSiswa(Siswa siswa) {
        this.key = siswa;
        this.left = null;
        this.right = null;
        this.height = 0;
    }
}

class AVLTreeSiswa {
    NodeTreeSiswa root;

    public AVLTreeSiswa() {
        this.root = null;
    }

    void updateHeight(NodeTreeSiswa node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    int height(NodeTreeSiswa node) {
        return (node == null) ? -1 : node.height;
    }

    int getBalance(NodeTreeSiswa node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    Siswa get(int idSiswa, NodeTreeSiswa node) {
        if (node == null) {
            return null;
        }
        if (node.key.id == idSiswa) {
            return node.key;
        }
        if (idSiswa < node.key.id) {
            return get(idSiswa, node.left);
        } else {
            return get(idSiswa, node.right);
        }
    }

    public void move(NodeTreeSiswa node, AVLTreeSiswa destination, int idKelas) {
        if (node != null) {
            move(node.left, destination, idKelas);
            destination.root = destination.insertMove(node.key, destination.root, idKelas);
            move(node.right, destination, idKelas);
        }
    }

    NodeTreeSiswa insert(Siswa siswa, NodeTreeSiswa node) {
        if (node == null) {
            node = new NodeTreeSiswa(siswa);
        } else if (siswa.id < node.key.id) {
            node.left = insert(siswa, node.left);
        } else if (siswa.id > node.key.id) {
            node.right = insert(siswa, node.right);
        }
        updateHeight(node);
        int balance = getBalance(node);
        if (balance > 1) {
            if (siswa.id > node.left.key.id) {
                node = doubleRight(node);
            } else {
                node = rotateRight(node);
            }
        } else if (balance < -1) {
            if (siswa.id > node.right.key.id) {
                node = rotateLeft(node);
            } else {
                node = doubleLeft(node);
            }
        }
        return node;
    }

    NodeTreeSiswa insertMove(Siswa siswa, NodeTreeSiswa node, int idKelas) {
        if (node == null) {
            node = new NodeTreeSiswa(siswa);
            node.key.idKelas = idKelas;
        } else if (siswa.id < node.key.id) {
            node.left = insertMove(siswa, node.left, idKelas);
        } else if (siswa.id > node.key.id) {
            node.right = insertMove(siswa, node.right, idKelas);
        }
        updateHeight(node);
        int balance = getBalance(node);
        if (balance > 1) {
            if (siswa.id > node.left.key.id) {
                node = doubleRight(node);
            } else {
                node = rotateRight(node);
            }
        } else if (balance < -1) {
            if (siswa.id > node.right.key.id) {
                node = rotateLeft(node);
            } else {
                node = doubleLeft(node);
            }
        }
        return node;
    }

    NodeTreeSiswa delete(int id, NodeTreeSiswa node) {
        if (node == null) {
            return node;
        } else if (node.key.id > id) {
            node.left = delete(id, node.left);
        } else if (node.key.id < id) {
            node.right = delete(id, node.right);
        } else { // if node.key == siswa.id
            if (node.left == null || node.right == null) {
                if (node.left == null) {
                    node = node.right;
                } else {
                    node = node.left;
                }
            } else { // ada 2 children
                NodeTreeSiswa temp = minValueNode(node.right); // ambil terkecil di kanan
                node.key = temp.key; // set key yang diapus jd key terkecil di kanan
                node.right = delete(temp.key.id, node.right); // set bagian kanan dari node baru
            }
        }
        if (node == null) {
            return node;
        }
        updateHeight(node);
        int balance = getBalance(node);
        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node = doubleRight(node);
            } else {
                node = rotateRight(node);
            }
        } else if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node = doubleLeft(node);
            } else {
                node = rotateLeft(node);
            }
        }
        return node;
    }

    NodeTreeSiswa rotateRight(NodeTreeSiswa node2) {
        NodeTreeSiswa node1 = node2.left;
        NodeTreeSiswa temp = node1.right;
        node1.right = node2;
        node2.left = temp;
        updateHeight(node2);
        updateHeight(node1);
        return node1;
    }

    NodeTreeSiswa rotateLeft(NodeTreeSiswa node1) {
        NodeTreeSiswa node2 = node1.right;
        NodeTreeSiswa temp = node2.left;
        node2.left = node1;
        node1.right = temp;
        updateHeight(node1);
        updateHeight(node2);
        return node2;
    }

    NodeTreeSiswa doubleRight(NodeTreeSiswa node3) {
        node3.left = rotateLeft(node3.left);
        return rotateRight(node3);
    }

    NodeTreeSiswa doubleLeft(NodeTreeSiswa node1) {
        node1.right = rotateRight(node1.right);
        return rotateLeft(node1);
    }

    NodeTreeSiswa minValueNode(NodeTreeSiswa node) {
        NodeTreeSiswa current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    NodeTreeSiswa maxValueNode(NodeTreeSiswa node) {
        NodeTreeSiswa current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }
}

// Mistakes:
// T: indentasi di delete salah, set ke node bukan return pas recursive search, set boolean buat cek ini nge replace ato delete, blm assign ke root abis delete
// C: logic pas bikin new Node / move node, height sama count ga sama alias g di copy. Pas kelas < 6: salah di delete kelas (kyny), tree siswa pas dipindah g di assign k root ny. idKelas siswa pindah g berubah
// K: salah logic compare
// A: infinite for loop, j lupa d increment

// Problem:
// T: sth wrong --> cek insert & delete, TLE
// S: RTE