package nl.tdk.collections;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.math.IntMath;

import static com.google.common.base.Preconditions.*;

import java.util.Iterator;
import java.util.Set;

/**
 * Utility functions for subset generation with lexicographical ordering.
 *
 * Reference works used:
 *  - Skiena, The Algorithm Design Manual pg. 434-454
 *  - example implementations from Kreher & Stinson - Combinatorial Algorithms
 *  - http://undergraduate.csse.uwa.edu.au/units/CITS7209/lecture02.pdf
 *
 */
public class KSubsetLexRank<T extends Comparable> extends KSubset<T> {
    /**
     * Create new KSubSet object, which gives the combinations given by
     * 'n choose k' with given k and n being the size of the object input.
     * @param k size k of subset.
     * @param objects
     */
    public KSubsetLexRank(int k, Set<T> objects) {
        super(k, objects);
    }

    /**
     * Return the rank of the given set.
     *
     * The rank of the sum, for each position, of:
     * ('#lower values' choose 'remaining positions')
     *
     * @param t set to rank, |t| = k, in increasing order
     * @return rank of the set
     */
    public int rank(ImmutableSortedSet<T> t) {
        checkArgument(t.size() == k);
        checkArgument(t.comparator().equals(Ordering.natural()), "Set should be ordered in its natural order");
        int r = 0;
        Iterator<T> it = t.iterator();

        int lo = 1;
        for (int i=1; i <= k; i++) {
            int idx = boundedIndexOf(lo, it.next());
            int hi= idx-1;

            if (lo <= hi) {
                for (int j=lo; j<=hi; j++)
                    r += IntMath.binomial(n-j, k-i);
            }

            lo = idx + 1;
        }

        return r;
    }

    public ImmutableSortedSet<T> unRank(int r) {
        r = checkElementIndex(r, IntMath.binomial(n, k));

        ImmutableSortedSet.Builder build = ImmutableSortedSet.naturalOrder();
        int x = 1, y;

        for (int i=1; i<=k; i++) {
            y = binomial(n - x, k - i);
            while (y <= r) {
                r -= y;
                x++;
                y = binomial(n-x, k-i);
            }

            build.add(objects(x));
            x++;
        }

        return build.build();
    }



}
