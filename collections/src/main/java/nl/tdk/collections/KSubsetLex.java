package nl.tdk.collections;

import com.google.common.collect.ImmutableSortedSet;
import static com.google.common.base.Preconditions.*;

import java.util.SortedSet;

/**
 * Utility functions for subset generation with lexicographical ordering.
 *
 * Reference works used:
 *  - Skiena, The Algorithm Design Manual pg. 434-454
 *  - example implementations from Kreher & Stinson - Combinatorial Algorithms
 *  - http://undergraduate.csse.uwa.edu.au/units/CITS7209/lecture02.pdf
 *
 */
public class KSubsetLex<T> {
    private final int k;
    private final ImmutableSortedSet<T> objects;

    /**
     * Create new KSubset object, which gives the combinations given by
     * 'n choose k' with given k and n being the size of the object input.
     * @param k size k of subset.
     * @param objects
     */
    public KSubsetLex(int k, Iterable<T> objects) {
        this.objects = ImmutableSortedSet.copyOf(checkNotNull(objects));
        this.k = checkElementIndex(k, this.objects.size());
    }

    /**
     * Return the rank of the given set.
     *
     * sets are in lexicographical arder
     *
     * @param t set to rank
     * @return rank of the set
     */
    private int rank(SortedSet<T> t) {
        return -1;

    }

    private SortedSet<T> unRank(int r) {
        int x = 1, y;

        for (int i = 1; i <= k;  i++) {


        }


        return null;
    }



}
