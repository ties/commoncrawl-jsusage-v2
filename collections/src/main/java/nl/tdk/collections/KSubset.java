package nl.tdk.collections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.math.IntMath;

import java.util.Set;
import java.util.SortedSet;
import static com.google.common.base.Preconditions.*;

/**
 * Abstract class with the common operations on K-Subset algorithms
 * - storage, n/k
 * - abstract rank
 * - abstract unrank
 *
 * Reference works used:
 * - Skiena, The Algorithm Design Manual pg. 434-454
 * - example implementations from Kreher & Stinson - Combinatorial Algorithms
 * - http://undergraduate.csse.uwa.edu.au/units/CITS7209/lecture02.pdf
 *
 *
 * Mainly for re-using code in the tests.
 */
public abstract class KSubset<T extends Comparable> {
    /** number of items */
    protected final int n;

    /** number of items in each combination */
    protected final int k;

    /** The objects, in reverse order. It is instantiated in the constructor
     * from a Set - this provides the set guarantee.
     */
    protected final ImmutableList<T> objects;

    /**
     * Instantiate
     * @param k 'n choose <i>k</i>'
     * @param items items to build from
     */
    public KSubset(int k, Set<T> items) {
        this.objects = ImmutableSortedSet.copyOf(items).asList();
        this.n = this.objects.size();
        this.k = checkPositionIndex(k, this.objects.size());
    }

    /**
     * 'n choose k'
     * When no such sets exist, return 0 instead of throwing IllegalArgumentException
     *
     * @param n n
     * @param k k
     * @return # of combinations, 0 if no such combination exists.
     */
    protected final static int binomial (final int n, final int r) {
        if ((r < 0) || (n < r))
            return 0;

        return IntMath.binomial(n, r);
    }

    /**
     * Utility function to change 1-based into 0-based
     */
    protected T objects(int i) {
        return objects.get(checkElementIndex(i - 1, n));
    }

    /**
     * Implementation of indexOf that uses domain knowledge:
     *
     * - item is in objects
     * - item should be in there
     *
     * This means that repeated boundexIndexOf calls over the
     * complete range happen in O(n) instead of O(n^2 log n) or
     * even O(n^2)
     *
     * @param j 1-based index to start from
     * @param obj object to search for
     * @return 1-based index for the element, otherwise -1
     */
    protected int boundedIndexOf(int j, T obj) {
        checkElementIndex(j - 1, n);
        for (; j <= n; j++) {
            if (objects(j).equals(obj))
                return j;
        }
        throw new IllegalStateException(String.format("Element %s not found starting from index %d", new Object[]{obj, j}));
    }
}
