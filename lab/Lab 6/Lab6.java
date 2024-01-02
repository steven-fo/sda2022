import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Lab6 {
    private static InputReader in;
    private static PrintWriter out;
    static AVLTree tree = new AVLTree();
    static ArrayList<Integer> arr = new ArrayList<Integer>();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        for (int i = 0; i < N; i++) {
            // TODO: process inputs
            int element = in.nextInt();
            arr.add(element);
            tree.root = tree.insert(tree.root, element); // Membuat node dengan element N dan key i
            printTree(tree.root, "  ", false);
        }

        int Q = in.nextInt();
        for (int i = 0; i < Q; i++) {
            char query = in.nextChar();

            if (query == 'G') { 
                int key = in.nextInt();
                grow(key); 
            }
            else if (query == 'P') { 
                int key = in.nextInt();
                pick(key); 
            }
            else if (query == 'F') { fall(); }
            else { height(); }
        }

        out.close();
    }

    static void grow(int key) {
        // TODO: implement this method
        tree.insert(tree.root, key);
    }

    static void pick(int key) {
        // TODO: implement this method
        if (!arr.contains(key)) {
            out.println(-1);
        } else {
            Node temp = tree.delete(tree.root, key);
            out.println(temp.key);
        }
    }

    static void fall() {
        // TODO: implement this method
        Node temp = tree.delete(tree.root, 0);
        out.println(temp.key);
    }

    static void height() {
        // TODO: implement this method
        out.println(tree.root.height);
    }

    // taken from https://www.programiz.com/dsa/avl-tree
    // a method to print the contents of a Tree data structure in a readable
    // format. it is encouraged to use this method for debugging purposes.
    // to use, simply copy and paste this line of code:
    // printTree(tree.root, "", true);
    static void printTree(Node currPtr, String indent, boolean last) {
        if (currPtr != null) {
            out.print(indent);
            if (last) {
                out.print("R----");
                indent += "   ";
            } else {
                out.print("L----");
                indent += "|  ";
            }
            out.println(currPtr.key);
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

class Node {
    // TODO: modify attributes as needed
    int key;
    Node left, right;
    int height;

    Node(int key) {
        this.key = key;
        this.height = 1;
    }
}

class AVLTree {
    // TODO: modify attributes as needed
    Node root;

    Node insert(Node node, int key) {
        // TODO: implement this method
        if (node == null) {
            node = new Node(key);
        } else if (node.key - key > 0) {
            node.left = insert(node.left, key);
        } else if (node.key - key < 0) {
            node.right = insert(node.right, key);
        }
        node = balancing(node);
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        return node;
    }

    Node delete(Node node, int key) {
        // TODO: implement this method
        if (node == null) {
            return node;
        }

        if (key == 0) {
            node = findMax(node);
            return node;
        }

        if (key > node.key) { // cek kanan
            node.right = delete(node.right, key);
        } else if (key < node.key) { // cek kiri
            node.left = delete(node.left, key);
        } else { // node.key == key
            if (node.left == null || node.right == null) { // kasus no child atau 1 child
                Node temp = null;
                if (node.left == null) {
                    temp = node.right;
                } else {
                    temp = node.left;
                }

                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp;
                }
            } else { // kasus 3: 2 child
                Node temp = findNode(node.left, key); // delete dengan predecessor inorder (cari terbesar dari kiri)
                node.key = temp.key;
                node.right = delete(node.right, temp.key);
            }
        }
        return node;
    }

    Node balancing(Node node) {
        // TODO: implement this method
        int difference = getDifference(node);
        if (difference > 1 && getDifference(node.left) >= 0) { // LL
            node = singleRightRotate(node);
        } else if (difference > 1 && getDifference(node.left) < 0) { // LR
            node.left = singleLeftRotate(node.left);
            node = singleRightRotate(node);
        } else if (difference < -1 && getDifference(node.right) <= 0) { // RR
            node = singleLeftRotate(node);
        } else if (difference < -1 && getDifference(node.right) > 0) { // RR
            node.right = singleRightRotate(node.right);
            node = singleLeftRotate(node);
        }
        return node;
    }

    Node singleLeftRotate(Node node) {
        // TODO: implement this method
        Node k1 = node.right;
        node.right = k1.left;
        k1.left = node;
        return k1;
    }

    Node singleRightRotate(Node node) {
        // TODO: implement this method
        Node k1 = node.left;
        node.left = k1.right;
        k1.right = node;
        return k1;
    }

    int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    int getDifference(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    Node findMax(Node node) {
        Node max = node;
        while (max.right != null) {
            max = max.right;
        }
        return max;
    }

    Node findNode(Node node, int key) {
        Node current = node;
        Node predecessor = null;
        while (current != null) {
            if (current.key == key) {
                if (current.left != null) {
                    predecessor = findMax(node.left);
                }
                break;
            } else if (current.key > key) {
                current = current.left;
            } else {
                predecessor = current;
                current = current.right;
            }
        }
        return predecessor;
    }
}