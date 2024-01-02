import java.io.*;
import java.util.StringTokenizer;

/**
 * Note:
 * 1. Mahasiswa tidak diperkenankan menggunakan data struktur dari library seperti ArrayList, LinkedList, dll.
 * 2. Mahasiswa diperkenankan membuat/mengubah/menambahkan class, class attribute, instance attribute, tipe data, dan method 
 *    yang sekiranya perlu untuk menyelesaikan permasalahan.
 * 3. Mahasiswa dapat menggunakan method {@code traverse()} dari class {@code DoublyLinkedList}
 *    untuk membantu melakukan print statement debugging.
 */
public class Lab5 {

  private static InputReader in;
  private static PrintWriter out;
  private static DoublyLinkedList rooms = new DoublyLinkedList();

  public static void main(String[] args) {
    InputStream inputStream = System.in;
    in = new InputReader(inputStream);
    OutputStream outputStream = System.out;
    out = new PrintWriter(outputStream);

    int N = in.nextInt();

    for (int i = 0; i < N; i++) {
      char command = in.nextChar();
      char direction;

      switch (command) {
        case 'A':
          direction = in.nextChar();
          char type = in.nextChar();
          add(type, direction);
          break;
        case 'D':
          direction = in.nextChar();
          out.println(delete(direction));
          break;
        case 'M':
          direction = in.nextChar();
          out.println(move(direction));
          break;
        case 'J':
          direction = in.nextChar();
          out.println(jump(direction));
          break;
      }
    }

    out.close();
  }

  public static void add(char type, char direction) {
    // TODO: implement
    rooms.add(type, direction);
  }

  public static int delete(char direction) {
    // TODO: implement
    ListNode node = rooms.delete(direction);
    return node.id;
  }

  public static int move(char direction) {
    // TODO: implement
    ListNode node = rooms.move(direction);
    return node.id;
  }

  public static int jump(char direction) {
    // TODO: implement
    if (rooms.current.element.equals('C')) {
        return -1;
    } else {
        do {
            rooms.move(direction);
        }
        while (!rooms.current.element.equals('S'));
        return rooms.current.id;
    }
  }

  // taken from https://codeforces.com/submissions/Petr
  // together with PrintWriter, these input-output (IO) is much faster than the
  // usual Scanner(System.in) and System.out
  // please use these classes to avoid your fast algorithm gets Time Limit
  // Exceeded caused by slow input-output (IO)
  private static class InputReader {

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

    public long nextLong() {
      return Long.parseLong(next());
    }
  }
}

class DoublyLinkedList {

  private int nodeIdCounter = 1;
  ListNode first;
  ListNode current;
  ListNode last;
  int size = 0;

  boolean isLast = false;
  boolean isFirst = false;

  /*
   * Method untuk menambahkan ListNode ke sisi kiri (prev) atau kanan (next) dari {@code current} ListNode
   */
  public ListNode add(Object element, char direction) {
    // TODO: implement
    ListNode newNode = new ListNode(element, nodeIdCounter);
    if (nodeIdCounter == 1) {
        this.current = newNode;
        this.current.next = newNode;
        this.current.prev = newNode;
    } else if (direction == 'R') {
        newNode.prev = current;
        newNode.next = current.next;
        newNode.prev.next = newNode;
        newNode.next.prev = newNode;
    } else {
        newNode.next = current;
        newNode.prev = current.prev;
        newNode.next.prev = newNode;
        newNode.prev.next = newNode;
    }
    nodeIdCounter++;
    size++;
    return newNode;
  }

  /**
   * Method untuk menghapus ListNode di sisi kiri (prev) atau kanan (next) dari {@code current} ListNode
   */
  public ListNode delete(char direction) {
    // TODO: implement
    ListNode tmp = current;
    if (direction == 'R') {
        tmp = current.next;
        current.next = current.next.next;
        current.next.prev = current;
    } else {
        tmp = current.prev;
        current.prev = current.prev.prev;
        current.prev.next = current;
    }
    return tmp;
  }

  /*
   * Method untuk berpindah ke kiri (prev) atau kanan (next) dari {@code current} ListNode
   */
  public ListNode move(char direction) {
    // TODO: implement
    if (direction == 'R') {
        current = current.next;
    } else {
        current = current.prev;
    }
    return current;
  }

  /**
   * Method untuk mengunjungi setiap ListNode pada DoublyLinkedList
   */
  public String traverse() {
    ListNode traverseNode = first;
    StringBuilder result = new StringBuilder();
    do {
      result.append(traverseNode + ((traverseNode.next != first) ? " | " : ""));
      traverseNode = traverseNode.next;
    } while (traverseNode != first);

    return result.toString();
  }
}

class ListNode {

  Object element;
  ListNode next;
  ListNode prev;
  int id;

  ListNode(Object element, int id) {
    this.element = element;
    this.id = id;
  }

  public String toString() {
    return String.format("(ID:%d Elem:%s)", id, element);
  }
}