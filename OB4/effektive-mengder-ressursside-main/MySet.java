import java.util.ArrayList;
import java.util.HashMap;

class MySet {
    int size = 0;
    HashMap<Integer, Integer> hash = new HashMap<>();
    ArrayList<Integer> list = new ArrayList<>();

    public MySet() {

    }

    public void insert(int x) {
        list.add(x, x);
    }

    public boolean contains() {
        return false;

    }

    public int size() {
        return size;

    }

    public void remove() {

    }

    // public int hashVal() {

    // }
    public static void main(String[] args) {
        MySet k = new MySet();
        k.insert(12);
    }
}