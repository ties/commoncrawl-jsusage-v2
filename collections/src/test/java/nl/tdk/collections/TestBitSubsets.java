package nl.tdk.collections;

import com.google.common.collect.*;
import com.google.common.math.IntMath;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.google.common.base.Preconditions.checkPositionIndex;

/**
 * Test the subset generation.
 *
 * Note that the implementation
 */
public abstract class TestBitSubsets<T extends Comparable> {
    /** The set generator */
    protected Iterable<Set<T>> generator;

    /** The objects we pick from */
    protected final ImmutableSortedSet<T> range;

    /** number of items */
    protected int n;
    /** (prime) constant number of items */
    private final static int N = 19;

    /** number of items in each combination */
    protected final int k;

    /**
     * Instantiate
     */
    public TestBitSubsets(ImmutableSortedSet<T> range) {
        this.range = checkNotNull(range);
        n = range.size();

        // choose random k
        Random prng = new Random();
        k = checkElementIndex(4 + prng.nextInt(3), n);

        generator = checkNotNull(getInstance(k));
    }

    protected static ImmutableSortedSet<Integer> defaultIntSet() {
        return Ranges.closed(1, N).asSet(DiscreteDomains.integers()).descendingSet();
    }

    public abstract Iterable<Set<T>> getInstance(int k);

    @Before
    public void setup() {
        generator = getInstance(k);
    }

    /**
     * 'n choose 0'
     * by definition:
     * - return a set of sets that only contains the empty set.
     */
    @Test
    public void testEmptyResult() {
        generator = getInstance(0);

        // Output size == 1, this is the empty set
        assertEquals(1, Iterables.size(generator));
        assertEquals(ImmutableSet.of(), Iterables.getOnlyElement(generator));
    }


    @Test
    /**
     * n choose k:
     *  - combinations have size k
     *  - there are (n choose k) combinations
     */
    public void testSizeOfResult() {
        for (Set<T> res : generator)
            assertEquals(k, res.size());

        assertEquals(IntMath.binomial(n, k), Iterables.size(generator));
    }

    /**
     * When choosing k elements out of n:
     * -    each element should occur 'n-1 choose k-1' times,
     *      since this is a combination in which the element was picked,
     *      which implies that n-1 other items exist and k-1 need to be picked from those.
     *
     *
     *  <b>implicit test:</b> all subsets are iterable
     */
    @Test
    public void testRelativeOccurrence() {
        Multiset<T> counter = HashMultiset.create();

        // add all elements from the set to the multimap
        for (Set<T> combination : generator)
            counter.addAll(combination);

        // map(lambda x: len(x), counter) => occurence count map
        for (Multiset.Entry<T> entry : counter.entrySet())
            assertEquals(IntMath.binomial(n-1, k-1), entry.getCount());
    }

    /**
     * All elements from range are in a combination
     */
    @Test
    public void testContainsAll() {
        Set<T> keys = Sets.newHashSet();

        for (Set<T> combination : generator)
            keys.addAll(combination);

        // all elements are seen
        assertTrue(Sets.symmetricDifference(keys, range).isEmpty());
    }

    /**
     * All sets should be unique
     */
    @Test
    public void testUniqueness() {
        Set<Set<T>> sets = Sets.newHashSet();

        for (Set<T> combination : generator)
            assertTrue(sets.add(combination)); // newly added set should not be a duplicate
    }

    /**
     * Test if the sets all are subsets of the input range
     */
    @Test
    public void testSetsAreSubsets() {
        for (Set<T> combination : generator) // no combination contains items not in range.
            assertTrue(Sets.difference(combination, range).isEmpty());
    }
}
