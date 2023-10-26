public class Bubblesort extends Sorter {

    @Override
    void sort() {
        boolean swapped;
        int len = A.length;
        for (int i = 0; i < len - 1 - i; i++) {
            swapped = false;
            comparisons++;
            for (int j = 0; j < A.length - i; j++) {
                if (lt(j, A.length - 1) && gt(A[j], A[j + 1])) {
                    swap(j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }

    @Override
    String algorithmName() {
        return "Bubblesort";
    }

}
