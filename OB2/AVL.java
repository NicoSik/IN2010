import java.io.*;

import java.util.Scanner;

public class AVL {
    File file;
    int size = 0, maxHeight = -1;
    Node start = null;

    public AVL() {

    }

    class Node {
        int val, height = 0;
        Node left = null, right = null;

        public Node(int val) {
            this.val = val;
        }
    }

    public int findHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return Height(node.left) - Height(node.right);
    }

    public int Height(Node n) {
        if (n == null)
            return -1;
        return n.height;
    }

    public void SetHeight(Node n) {
        if (n == null)
            return;
        n.height = 1 + Math.max(Height(n.left), Height(n.right));
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

    public Node Balance(Node node) {
        if (findHeight(node) < -1) { // Vet at venstre er høyere enn høyre
            if (findHeight(node.right) > 0) {
                node.right = rightRotate(node.right);
            }
            return leftRotate(node);
        }
        if (findHeight(node) > 1) {
            if (findHeight(node.left) < 0) {
                node.left = leftRotate(node.left);
            }
            return rightRotate(node);
        }
        return node;
    }

    public Node leftRotate(Node node) {
        Node temp = node.right; // Helt elendig navn på nodene men ga mening når jeg lagde dem
        Node node_right = temp.left;
        temp.left = node;
        node.right = node_right;
        SetHeight(node);
        SetHeight(temp);
        return temp;
    }

    public Node rightRotate(Node node) {
        Node temp = node.left;
        Node node_left = temp.right;
        temp.right = node;
        node.left = node_left;
        SetHeight(node);
        SetHeight(temp);
        return temp;
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
        size--;
        if (parent.left == null) {
            return parent.right;
        }
        if (parent.right == null) {
            return parent.left;
        }

        Node small = new Node(findMin(parent.right));
        parent.val = small.val;
        parent.right = remove(small.val, parent.right);
        size++;
        SetHeight(parent);
        return Balance(parent);
    }

    public int findMin(Node x) {
        int smallestVal = x.val;
        while (x.left != null) {
            x = x.left;
            smallestVal = x.val;
        }
        return smallestVal;
    }

    private Node insert(int num, Node node) {
        if (node == null) {
            size++;
            return new Node(num);
        } else if (num > node.val) {
            node.right = insert(num, node.right);

        } else if (num < node.val) {
            node.left = insert(num, node.left);
        }
        SetHeight(node);
        return Balance(node);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the path to the file as an argument.");
            System.exit(0);
        }
        File file = new File(args[0]);
        AVL tree = new AVL();
        tree.readFile(file);
    }
}