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
public class KSubsetBitLex<T extends Comparable> extends KSubset<T> implements Iterable<Set<T>>{
    public KSubsetBitLex(int k, Set<T> objects) {
        super(k, objects);
    }

    /**
     * Get the bit mask for the given rank
     * @param rank rank
     * @return bit mask
     */
    private int unRank(int rank) {
        rank = checkElementIndex(rank, IntMath.binomial(n, k));
        int x=1, mask=0, y;

        for(int i=1; i<=k; i++) {
            y = binomial(n-x, k-i);
            while (y <= rank) {
                rank -= y;
                x++;
                y = binomial(n-x, k-i);
            }

            mask |= (1 << (x - 1)); // mask=0-based, x=1-based
            x++;
        }
        return mask;
    }


    @Override
    public Iterator<Set<T>> iterator() {
        return new AbstractIterator<Set<T>>() {
            private int i = -1;

            @Override
            protected Set<T> computeNext() {
                if (++i < IntMath.binomial(n, k))
                    return new BitFilteredSet<T>(objects, unRank(i));

                return endOfData();
            }
        };
    }
}