import java.util.*;
import java.io.*;

public class Lab7 {

    static class Box {
        int id;
        long value;
        String state;

        Box(int id, long value, String state) {
            this.id = id;
            this.value = value;
            this.state = state;
        }
    }

    static class BoxContainer {
        public ArrayList<Box> heap; // pq
        public int size;
        public HashMap<Integer, Integer> idToIndexMap;

        public BoxContainer() {
            this.heap = new ArrayList<>();
            this.idToIndexMap = new HashMap<>();
            this.size = 0;
        }

        public static int getParentIndex(int i) {
            return (i - 1) / 2;
        }

        public static int leftChildOf(int i) {
            return 2 * i + 1;
        }

        public static int rightChildOf(int i) {
            return 2 * (i+1);
        }

        public void percolateUp(int i) {
            int parent = getParentIndex(i);
            Box temp = heap.get(i);
            while (i > 0 && (heap.get(i).value - heap.get(parent).value <= 0)) {
                if (heap.get(i).value == heap.get(parent).value) {
                    if (heap.get(i).id < heap.get(parent).id) {
                        swap(i, parent);
                        i = parent;
                        parent = getParentIndex(i);
                    } else {
                        return;
                    }
                } else {
                    swap(i, parent);
                    i = parent;
                    parent = getParentIndex(i);
                }
            }
            swap(i, idToIndexMap.get(temp.id));
        }

        public void percolateDown(int i) {
            int heapSize = heap.size();
            Box temp = heap.get(i);
            while (i < heapSize) {
                int childpos = leftChildOf(i);
                if (childpos < heapSize) {
                    if ((rightChildOf(i) < heapSize) && (heap.get(childpos+1).value - heap.get(childpos).value < 0)) {
                        childpos++;
                    }
                    if (heap.get(childpos).value - temp.value <= 0) {
                        if (heap.get(childpos).value == temp.value) {
                            if (heap.get(childpos).id < heap.get(i).id) {
                                swap(i, childpos);
                                i = childpos;
                            } else {
                                return;
                            }
                        } else {
                            swap(i, childpos);
                            i = childpos;
                        }
                    } else {
                        swap(i, idToIndexMap.get(temp.id));
                        return;
                    }
                } else {
                    swap(i, idToIndexMap.get(temp.id));
                    return;
                }
            }
        }

        public void insert(Box box) {
            heap.add(box);
            int current = size;
            idToIndexMap.put(box.id, size);
            while (heap.get(getParentIndex(current)).value < heap.get(current).value) {
                swap(current, getParentIndex(current));
                current = getParentIndex(current);
            }
            size++;
        }

        public Box peek() {
            return heap.get(0);
        }

        public void swap(int firstIndex, int secondIndex) {
            Box temp;
            temp = heap.get(firstIndex);
            idToIndexMap.replace(heap.get(secondIndex).id, firstIndex);
            heap.set(firstIndex, heap.get(secondIndex));
            heap.set(secondIndex, temp);
            idToIndexMap.replace(temp.id, firstIndex);
        }

        public void updateBox(Box box, boolean nambah, boolean kurang) {
            percolateUp(idToIndexMap.get(box.id));
            percolateDown(idToIndexMap.get(box.id));
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
    
        int N = Integer.parseInt(br.readLine());
    
        ArrayList<Box> boxes = new ArrayList<>();
        BoxContainer boxContainer = new BoxContainer();
    
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            long value = Long.parseLong(st.nextToken());
            String state = st.nextToken();
    
            Box box = new Box(boxes.size(), value, state);
            boxes.add(box);
            boxContainer.insert(box);
        }
    
        int T = Integer.parseInt(br.readLine());
    
        for (int i = 0; i < T; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String command = st.nextToken();
    
            if ("A".equals(command)) {
                long V = Long.parseLong(st.nextToken());
                String S = st.nextToken();
                A(V, S, boxes, boxContainer);
            } else if ("D".equals(command)) {
                int I = Integer.parseInt(st.nextToken());
                int J = Integer.parseInt(st.nextToken());
                D(I, J, boxes, boxContainer);
            } else if ("N".equals(command)) {
                int I = Integer.parseInt(st.nextToken());
                N(I, boxes, boxContainer);
            }

            Box topBox = boxContainer.peek();
            pw.println(topBox.value + " " + topBox.state);
        }
    
        pw.flush();
        pw.close();
    }

    static void A(long value, String state, ArrayList<Box> boxes, BoxContainer boxContainer) {
        Box newBox = new Box(boxes.size(), value, state);
        boxes.add(newBox);
        boxContainer.insert(newBox);
    }

    static void D(int id1, int id2, ArrayList<Box> boxes, BoxContainer boxContainer) {
        Box box1 = boxes.get(id1);
        Box box2 = boxes.get(id2);
        if (box1.state.equals(box2.state) || box1.id == box2.id) {
            return;
        } else {
            if (box1.state.equals("R")) {
                if (box2.state.equals("S")) { // win
                    box1.value += box2.value;
                    box2.value /= 2;
                    boxContainer.updateBox(box1, true, false);
                    boxContainer.updateBox(box2, false, true);
                } else { // lose
                    box2.value += box1.value;
                    box1.value /= 2;
                    boxContainer.updateBox(box1, false, true);
                    boxContainer.updateBox(box2, true, false);
                }
            } else if (box1.state.equals("S")) {
                if (box2.state.equals("P")) { // win
                    box1.value += box2.value;
                    box2.value /= 2;
                    boxContainer.updateBox(box1, true, false);
                    boxContainer.updateBox(box2, false, true);
                } else { // lose
                    box2.value += box1.value;
                    box1.value /= 2;
                    boxContainer.updateBox(box1, false, true);
                    boxContainer.updateBox(box2, true, false);
                }
            } else {
                if (box2.state.equals("R")) { // win
                    box1.value += box2.value;
                    box2.value /= 2;
                    boxContainer.updateBox(box1, true, false);
                    boxContainer.updateBox(box2, false, true);
                } else { // lose
                    box2.value += box1.value;
                    box1.value /= 2;
                    boxContainer.updateBox(box1, false, true);
                    boxContainer.updateBox(box2, true, false);
                }
            }
        }
    }

    static void N(int id, ArrayList<Box> boxes, BoxContainer boxContainer) {
        Box current = boxes.get(id);
        if (id == 0) { // di ujung kiri
            Box kanan = boxes.get(id+1);
            if (current.state.equals(kanan.state)) {
                return;
            } else {
                if (current.state.equals("R")) {
                    if (kanan.state.equals("S")) { // win
                        current.value += kanan.value;
                        boxContainer.updateBox(current, true, false);
                    }
                } else if (current.state.equals("S")) {
                    if (kanan.state.equals("P")) { // win
                        current.value += kanan.value;
                        boxContainer.updateBox(current, true, false);
                    }
                } else {
                    if (kanan.state.equals("R")) { // win
                        current.value += kanan.value;
                        boxContainer.updateBox(current, true, false);
                    }
                }
            }
        } else if (id == boxes.size()) { // di ujung kanan
            Box kiri = boxes.get(id-1);
            if (current.state.equals(kiri.state)) {
                return;
            } else {
                if (current.state.equals("R")) {
                    if (kiri.state.equals("S")) { // win
                        current.value += kiri.value;
                        boxContainer.updateBox(current, true, false);
                    }
                } else if (current.state.equals("S")) {
                    if (kiri.state.equals("P")) { // win
                        current.value += kiri.value;
                        boxContainer.updateBox(current, true, false);
                    }
                } else {
                    if (kiri.state.equals("R")) { // win
                        current.value += kiri.value;
                        boxContainer.updateBox(current, true, false);
                    }
                }
            }
        } else { // di tengah
            Box kanan = boxes.get(id+1);
            Box kiri = boxes.get(id-1);
            if (current.state.equals(kanan.state)) {
                return;
            } else {
                if (current.state.equals("R")) {
                    if (kanan.state.equals("S")) { // win
                        current.value += kanan.value;
                        boxContainer.updateBox(current, true, false);
                    }
                } else if (current.state.equals("S")) {
                    if (kanan.state.equals("P")) { // win
                        current.value += kanan.value;
                        boxContainer.updateBox(current, true, false);
                    }
                } else {
                    if (kanan.state.equals("R")) { // win
                        current.value += kanan.value;
                        boxContainer.updateBox(current, true, false);
                    }
                }
            }
            if (current.state.equals(kiri.state)) {
                return;
            } else {
                if (current.state.equals("R")) {
                    if (kiri.state.equals("S")) { // win
                        current.value += kiri.value;
                        boxContainer.updateBox(current, true, false);
                    }
                } else if (current.state.equals("S")) {
                    if (kiri.state.equals("P")) { // win
                        current.value += kiri.value;
                        boxContainer.updateBox(current, true, false);
                    }
                } else {
                    if (kiri.state.equals("R")) { // win
                        current.value += kiri.value;
                        boxContainer.updateBox(current, true, false);
                    }
                }
            }
        }
    }
}