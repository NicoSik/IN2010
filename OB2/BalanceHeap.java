import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class BalanceHeap {

    PriorityQueue<Integer> sorted;

    public BalanceHeap(PriorityQueue<Integer> sorted) {
        this.sorted = sorted;
        balance(sorted);

    }
    /*
     * Procedure BalanceHeap(Sorted,start,end)
     * leftSide <- new PriorityQueue
     * rightSide <- new PriorityQueue
     * if(size == 0)return
     * if(size = 1)
     * print(Sorted.poll)
     * middleindex <- (start + end) / 2
     * for(i <- start to middleIndex) do
     * leftside.offer(Sorted.poll())
     * print(Sorted.poll())
     * for(i <- middleIndex to end) do
     * rightSide.offer(Sorted.poll())
     */

    // PSeudokoden funket ikke så måtte fjerne med indekser
    public void balance(PriorityQueue<Integer> left) {
        PriorityQueue<Integer> leftSide = new PriorityQueue<>();
        PriorityQueue<Integer> rightSide = new PriorityQueue<>();
        if (left.size() == 0)
            return;
        if (left.size() == 2) {

            int first = left.poll();
            System.out.println(left.poll());
            System.out.println(first);
            return;
        }

        int mid = (0 + left.size()) / 2;
        for (int i = 0; i < mid; i++) {
            leftSide.offer(left.poll());
        }
        if (left.size() == 0)
            return;
        System.out.println(left.poll());

        for (int i = 0; i < mid; i++) {
            if (left.size() == 0)
                break;
            rightSide.offer(left.poll());

        }

        balance(rightSide);
        balance(leftSide);

    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("You only need 1 number");
            return;
        }
        PriorityQueue<Integer> sort = new PriorityQueue<>();
        int end = Integer.parseInt(args[0]);
        for (int i = 0; i < end + 1; i++) {
            sort.add(i);
        }
        // System.out.println(sort);
        BalanceHeap bal = new BalanceHeap(sort);
    }
}
