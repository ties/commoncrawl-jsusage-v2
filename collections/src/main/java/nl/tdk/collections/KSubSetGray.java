package nl.tdk.collections;

import com.google.common.primitives.UnsignedInts;

import java.util.Set;
import java.util.SortedSet;

import static com.google.common.base.Preconditions.*;

/**
 * Created with IntelliJ IDEA.
 * User: kockt
 * Date: 29-01-13
 * Time: 22:08
 * To change this template use File | Settings | File Templates.
 */
public class KSubSetGray<T extends Comparable> extends KSubset<T> {
    public static int EMPTY_SET = 0;

    public KSubSetGray(int k, Set<T> objects) {
        super(k, objects);
        checkArgument(objects.size() < 63, "Number of objects is too large to use with gray-code");
    }

    /**
     * Is the number of bits in the mask even?
     * @return true when even.
     */
    private final boolean even(int mask) {
        return Long.bitCount(mask) % 2 == 0;
    }

    /**
     * Rank the current mask
     * @return
     */
    public int rank(int mask) {
        int r = 0, b = 0;
        for (int i=n-1; i>= 0; i--) {
            if ((mask&(1 << (n-i))) != 0)
                b = 1-b;
            if (b == 1)
                r = r + (1<<i);
        }

        return r;
    }

    /**
     * Get the mask of the given rank
     * @param r rank
     */
    public int unrank(int r) {
        int b, bb=0;
        int mask = EMPTY_SET;

        for (int i=n-1; i >= 0; i--) {
            b = r>>i;
            if (b != bb)
                mask |= (1 << (n-i));
            bb = b;
            r = r - b*(1<<i);
        }
        return mask;
    }

    /**
     * Get the gray code successor of the given mask.
     */
    public int successor(int mask) {
        int j = n;
        while (!((mask&(1<<j)) == 1) && j>0)
            j--;
        if (j == 1)
            throw new IllegalStateException(String.format("No successor for mask %s", Integer.toBinaryString(mask)));

        if (even(mask)) {
            mask = mask^(1<<n);
        } else {
            mask = mask^(1<<(j-1));
        }
        return mask;
    }
}
