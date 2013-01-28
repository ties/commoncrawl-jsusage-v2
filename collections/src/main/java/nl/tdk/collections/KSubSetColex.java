package nl.tdk.collections;

import com.google.common.collect.*;
import com.google.common.math.IntMath;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import static com.google.common.base.Preconditions.*;

/**
 * Subset generation in co-lexicographical ordering.
 *
 * Reference works used:
 *  - Skiena, The Algorithm Design Manual pg. 434-454
 *  - example implementations from Kreher & Stinson - Combinatorial Algorithms
 *  - http://undergraduate.csse.uwa.edu.au/units/CITS7209/lecture02.pdf
 *
 *  The co-lexicographical order is defined as a sequence t1, t2, ..., tk with
 *  t1 > t2 > ... > tk.
 *
 *  From this follows that the representatives of elements in the representatives
 *  are in decreasing order.
 *
 *  The number of subsets whose largest element is less than t1:
 *  # of subsets of (1, 2,..., t1-1) = (t1 - 1 choose k)

 */
public class KSubSetColex<T> {
    private final int k;
    private final int n;
    /** The objectes, in reverse order. Set guarantee is provided by instantiating from a set */
    final ImmutableList<T> objects;

    public KSubSetColex(int k, Iterable<T> objects) {
        this.objects = ImmutableList.copyOf(Sets.newHashSet(objects)).reverse();

        n = this.objects.size();
        this.k = checkElementIndex(k, n);
    }

    /**
     * Return the rank of the given set.
     *
     * sets are in co-lexicographical order
     *
     * @param t set to rank
     * @return rank of the set
     */
    int rank(ImmutableSortedSet<T> t) {
        assert (t.size() == k);
        // get the reverse iterators;
        Iterator<T> itT = t.descendingIterator();

        int rank = 0, j = 0;

        // sum (1..k): (ti - 1 choose k - (i - 1))
        // invariant: Since t and objects are sorted in the same order, use bounded indexof
        for (int i = 0; i < k; i++) {
            T current = itT.next();
            j = boundedIndexOf(j, current);

            rank += IntMath.binomial(n - j - 1, k - (i - 1));
        }

        return rank;
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
     * @param i index to start from
     * @param obj object to search for
     * @return index in the array, otherwise -1
     */
    int boundedIndexOf(int i, T obj) {
        // search for obj in the positions starting from j
        for (int j = checkPositionIndex(i, n); j < n; j++) {
            if (objects.get(j).equals(obj))
                return j;
        }
       return -1;
    }
}
