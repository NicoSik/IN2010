
import java.io.IOException;
import java.util.ArrayList;

public class BalanceArray {
    ArrayList<Integer> sorted;
    ArrayList<Integer> balanced = new ArrayList<>();

    public BalanceArray(ArrayList<Integer> sorted) {
        this.sorted = sorted;
        balance(0, sorted.size());
        for (Integer integer : balanced) {
            System.out.println(integer);
        }
    }

    /*
     * Procedure BalanceArray(sorted)
     * if(start > end) then
             return
     * middleindex <- (start + end) / 2
     * balanced.add(sorted.get(middleindex))
     * BalanceArray(middleindex,end)
     * BalanceArray(start,middleindex)
     */
    // FOR Å KJØRE KODEN SKRIV java BalanceArray <DIN VERDI> | java BalanceChecker
    // Bruker windows så fikk ikke bruke seq
    public void balance(int start, int end) { // Splitter bare midten hver gang og legger miderste elementet til
                                              // lista,repeteres rekursivt
        if (start >= end)
            return;
        int middleInd = (start + end) / 2;
        balanced.add(sorted.get(middleInd));
        balance(middleInd + 1, end);
        balance(start, middleInd);

    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("You only need 1 number");
            return;
        }
        ArrayList<Integer> sorted = new ArrayList<>();
        int end = Integer.parseInt(args[0]);
        for (int i = 0; i < end + 1; i++) {
            sorted.add(i);
        }
        BalanceArray bal = new BalanceArray(sorted);
    }
}