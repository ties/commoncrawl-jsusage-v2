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
 *  The co-lexicographical order is defined as a sequence t1, t2, ..., tk with
 *  t1 > t2 > ... > tk.
 *
 *  From this follows that the representatives of elements in the representatives
 *  are in decreasing order.
 *
 *  The number of subsets whose largest element is less than t1:
 *  # of subsets of (1, 2,..., t1-1) = (t1 - 1 choose k)

 */
public class KSubSetColex<T extends Comparable> extends KSubset<T> {
    public KSubSetColex(int k, Set<T> objects) {
        super(k, objects);
    }

    /**
     * Return the rank of the given set.
     *
     * sets are in co-lexicographical order
     *
     * @param t set to rank
     * @return rank of the set
     */
    public int rank(ImmutableSortedSet<T> t) {
        checkArgument(t.size() == k, String.format("Size of set != k (%d != %d)", new Object[]{t.size(), k}));
        Iterator<T> it = t.iterator();

        int r = 0;

        for (int i=1; i <= k; i++)
            r += binomial(boundedIndexOf(i, it.next()), k + 1 - i); // ith value = i+1

        return r;
    }

    public ImmutableSortedSet<T> unRank(int r) {
        ImmutableSortedSet.Builder<T> res = ImmutableSortedSet.<T>reverseOrder();

        int x = checkElementIndex(r, binomial(n, k)); // valid combination index?
        for (int i=1; i <= k; i++) {
            while (binomial(x, k+1-i) > r) {
                x--;
            }

            res.add(objects.get(x)); // element with x+1'th as value = get(x)
            r -= binomial(x, k+1-i);
        }
        return res.build();
    }
}
