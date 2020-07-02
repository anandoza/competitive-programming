package library;

import java.util.Comparator;
import java.util.PriorityQueue;

public class AXCTrick {

    public static int[] minimaOfFixedRanges(int[] x, int width) {
        int n = x.length;
        int[] answer = new int[n - (width - 1)];
        PriorityQueue<Pair> q = new PriorityQueue<>(Comparator.comparingInt(p -> p.value));
        for (int i = 0; i < n; i++) {
            q.add(new Pair(i, x[i]));
            int start = i - (width - 1);
            while (q.peek().index < start)
                q.poll();
            if (start >= 0) {
                answer[start] = q.peek().value;
            }
        }
        return answer;
    }

    static class Pair {
        final int index;
        final int value;

        Pair(int index, int value) {
            this.index = index;
            this.value = value;
        }
    }

}
