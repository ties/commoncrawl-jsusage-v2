package nl.tdk.collections;

import com.google.common.collect.ImmutableSortedSet;

/**
 * Abstract functions used in the ranking varieties
 */
public interface RankingSubset<T extends Comparable> {
    /**
     * Abstract rank function
     * get the rank of this set
     * @param t set
     */
    public abstract int rank(ImmutableSortedSet<T> t);

    /**
     * Abstract un-ranking function
     * get the set with given rank
     * @param r rank
     */
    public abstract ImmutableSortedSet<T> unRank(int r);
}
