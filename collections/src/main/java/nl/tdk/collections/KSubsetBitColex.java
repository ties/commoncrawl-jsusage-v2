package nl.tdk.collections;

import  com.google.common.collect.AbstractIterator;
import com.google.common.math.IntMath;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

import static com.google.common.base.Preconditions.*;

/**
 * Bit-Masked-Subsets in Co-lexicographical ordering
 * @param <T>
 */
public class KSubsetBitColex<T extends Comparable> extends KSubset<T> implements Iterable<Set<T>>{
    public KSubsetBitColex(int k, Set<T> objects) {
        super(k, objects);
    }

    /**
     * Get the bit mask for the given rank
     * @param r rank of the set
     * @return bit mask
     */
    private int unRank(int r) {
        r = checkElementIndex(r, IntMath.binomial(n, k));
        int mask = 0;

        int x = n;
        for (int i=1; i<= k; i++) {
            while (binomial(x,  k+1-i) > r)
                x--;

            mask |= (1 << x);
            r -= binomial(x, k+1-i);
        }

        return mask;
    }

    @Override
    public Iterator<Set<T>> iterator() {
        return new AbstractIterator<Set<T>>() {
            private int i = -1;

            @Override
            protected Set<T> computeNext() {
                if (++i < IntMath.binomial(n, k)) // pre-increment!
                    return new BitFilteredSet<T>(objects, unRank(i));

                return endOfData();
            }
        };
    }
}
