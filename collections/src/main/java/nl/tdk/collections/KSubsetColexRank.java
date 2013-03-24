package nl.tdk.collections;

import com.google.common.collect.*;

import java.util.Iterator;
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
public class KSubsetColexRank<T extends Comparable> extends KSubset<T> implements Ranker<T> {
    public KSubsetColexRank(int k, Set<T> objects) {
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
        for (int i=1; i <= k; i++){
            // items are in reverse order, we do not have a reverse search function
            // => search from the highest position it can be at due to the ordering
            r += binomial(boundedIndexOf(1+k-i, it.next()) - 1, k - (i-1));
        }
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

    @Override
    public Iterator<Set<T>> iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
