package nl.tdk.collections;

import com.google.common.collect.AbstractIterator;
import com.google.common.math.IntMath;

import java.util.Iterator;
import java.util.Set;

import static com.google.common.base.Preconditions.checkElementIndex;

/**
 * Iterator over the combinations (n choose k) of the given objects.
 *
 * Iterms are listed in lexicographical order
 */
public class KSubsetBitLex<T extends Comparable> extends KSubset<T> {
    public KSubsetBitLex(int k, Set<T> objects) {
        super(k, objects);
    }

    /**
     * Get the bit mask for the given rank
     * @param r rank
     * @return bit mask
     */
    private int unRankBits(int r) {
        r = checkElementIndex(r, IntMath.binomial(n, k));
        int x=1, mask=0, y;

        for(int i=1; i<=k; i++) {
            y = binomial(n-x, k-i);
            while (y <= r) {
                r -= y;
                x++;
                y = binomial(n-x, k-i);
            }
            mask |= (1 << (x - 1)); // x=0-based.
            x++;
        }
        return mask;
    }

    protected Set<T> unRank(int r) {
        return new BitFilteredSet<T>(objects, unRankBits(r));
    }
}
