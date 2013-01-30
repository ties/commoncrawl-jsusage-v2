package nl.tdk.collections;

import com.google.common.collect.*;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

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
public class KSubSetColex<T extends Comparable> extends KSubSet<T> {
    public KSubSetColex(int k, Set<T> objects) {
        super(k, objects);
    }

    /**
     * Return the rank of the given set.
     *
     * sets are in co-lexicographical order
     *
     * @param t set to rank, in decreasing order, |t| = k
     * @return rank of the set
     */
    public int rank(ImmutableSortedSet<T> t) {
        checkArgument(t.size() == k);
        checkArgument( t.comparator().equals(Ordering.natural().reverse()), "Set should be ordered in reverse natural order");
        Iterator<T> it = t.iterator();

        it = t.iterator();

        int r = 0;
        for (int i=1; i <= k; i++)
            r += binomial(boundedIndexOf(i, it.next()) - 1, k - (i-1));
        return r;
    }

    public ImmutableSortedSet<T> unRank(int r) {
        ImmutableSortedSet.Builder<T> res = ImmutableSortedSet.<T>reverseOrder();

        r = checkElementIndex(r, binomial(n, k)); // valid combination index?
        int x = n;

        for (int i=1; i<=k; i++) {
            while (binomial(x, k+1-i) > r) {
                x--;
            }

            res.add(objects.get(x));
            r -= binomial(x, k+1-i);
        }
        return res.build();
    }
}