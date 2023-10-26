import java.io.*;
import java.util.*;

public class Bintree {
    File file;
    int size = 0;
    Node start = null;
    boolean removed = false;

    public Bintree() {

    }

    class Node {
        int val;
        Node left = null, right = null;

        public Node(int val) {
            this.val = val;
        }
    }

    public Node insert(int k, Node parent) {
        if (parent == null) {
            size++;
            return new Node(k);
        } else if (k > parent.val) {
            parent.right = insert(k, parent.right);

        } else if (k < parent.val) {
            parent.left = insert(k, parent.left);
        }
        return parent;

    }

    public Node remove(int k, Node parent) {
        if (parent == null) {
            return null;
        }
        if (k < parent.val) {
            parent.left = remove(k, parent.left);
            return parent;
        }
        if (k > parent.val) {
            parent.right = remove(k, parent.right);
            return parent;
        }
        // Hvis den ikke treffer noen if checks så vet man at man er på noden man skal
        // slette.
        size--;
        if (parent.left == null) {
            return parent.right;
        }
        if (parent.right == null) {
            return parent.left;
        }
        // Finner minste noden under noden vi sletter, for at de skal "bytte" plass
        Node small = new Node(findMin(parent.right));
        parent.val = small.val;
        parent.right = remove(small.val, parent.right);
        size++;
        return parent;
    }

    public int findMin(Node x) {
        int smallestVal = x.val;
        while (x.left != null) {
            x = x.left;
            smallestVal = x.val;
        }
        return smallestVal;
    }

    public Node contains(int k, Node parent) {
        if (parent == null) {
            return null;
        }
        if (k == parent.val) {
            return parent;
        }
        if (k < parent.val) {
            return contains(k, parent.left);
        } else {
            return contains(k, parent.right);
        }
    }

    public void readFile(File file) {
        this.file = file;
        try {
            Scanner sc = new Scanner(file);
            sc.nextLine();
            int num = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] splitString = line.split(" ");
                String commands = splitString[0];
                if (splitString.length > 1) {
                    num = Integer.parseInt(splitString[1]);
                }
                if (commands.equals("insert"))
                    start = insert(num, start);
                else if (commands.equals("remove"))
                    start = remove(num, start);
                else if (commands.equals("contains")) {
                    Node n = contains(num, start);
                    if (n != null) {
                        System.out.println(true);
                    } else
                        System.out.println(false);
                } else if (commands.equals("size")) {
                    System.out.println(size);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the path to the file as an argument.");
            System.exit(0);
        }
        File file = new File(args[0]);
        Bintree tree = new Bintree();
        tree.readFile(file);
    }
}
