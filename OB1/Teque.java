import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Teque {
    File file;
    ArrayList<Integer> list = new ArrayList<>();

    public Teque(File file) {
        this.file = file;
        try {
            Scanner sc = new Scanner(file);
            sc.nextLine(); // Hopper over 1 linje
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] splitString = line.split(" ");
                String commands = splitString[0];
                int num = Integer.parseInt(splitString[1]);
                if (commands.equals("push_back"))
                    pushBack(num);
                else if (commands.equals("push_front"))
                    pushFront(num);
                else if (commands.equals("push_middle"))
                    pushMiddle(num);
                else if (commands.equals("get")) {
                    System.out.println(get(num));
                }

            }
            sc.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Procedure pushBack(A)
    // List.add(A);
    public void pushBack(int num) {// O(1) siden man bare må legge til på slutten
        list.add(num);
    }

    // Procedure pushBack(A)
    // List.add(0,A)
    public void pushFront(int num) {// O(n) promblemet er at vi forskyver alle indekser videre
        list.add(0, num);
    }

    // Procedure pushMiddle(A)
    // List.add((List.size()+1) /2, A)
    public void pushMiddle(int num) { // O(n)
        list.add(((list.size() + 1) / 2), num);
    }

    // Procedure get(A)
    // List.get(A)
    public int get(int index) { // O(1)
        return list.get(index);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the path to the file as an argument.");
            return;
        }
        File file = new File(args[0]);
        Teque que = new Teque(file);
    }
}
// D)
// Siden vi har en konstant vil den alltid ha samme tid.
// Det er kun når vi fjerner konstanten, vi faktisk får kunne analysere
// kjøretiden får algoritmene.
