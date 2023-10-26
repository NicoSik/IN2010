import java.util.Arrays;

class Quick extends Sorter {

    void sort() {
        A = mergesort(A);
    }

    public int[] mergesort(int[] Ar) {
        if (leq(Ar.length, 1))
            return Ar;
        int i = Ar.length / 2;
        int[] A1 = Arrays.copyOfRange(Ar, 0, i); // Noe feil her
        int[] A2 = Arrays.copyOfRange(Ar, i, Ar.length);
        A1 = mergesort(A1);
        A2 = mergesort(A2);
        return merge(A1, A2, Ar);
    }

    public int[] merge(int[] A1, int[] A2, int[] Ar) {
        int i = 0, j = 0;

        while (lt(i, A1.length) && lt(j, A2.length)) {
            if (leq(A1[i], A2[j])) {
                Ar[i + j] = A1[i];
                swaps++; // Usikker på om man skal telle swaps eller ikke siden mergesort ikke gjør
                         // "swaps så gjorde for sikkerhets skyld
                i++;
            } else {
                Ar[i + j] = A2[j];
                j++;
                swaps++;
            }
        }
        while (lt(i, A1.length)) {
            Ar[i + j] = A1[i];
            i++;
            swaps++;

        }
        while (lt(j, A2.length)) {
            Ar[i + j] = A2[j];
            j++;
            swaps++;
        }
        return Ar;
    }

    String algorithmName() {
        return "Mergesort";
    }

}
