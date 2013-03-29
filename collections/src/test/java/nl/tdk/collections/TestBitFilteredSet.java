package nl.tdk.collections;

import com.google.common.collect.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Some tests for bit filtered set iterator
 */
public class TestBitFilteredSet {
    private ImmutableList<Integer> range;
    private final int N = 31; // highest < 32

    @Before
    public void setup() {
        range = ContiguousSet.create(Range.closed(0, N), DiscreteDomain.integers()).asList();
    }

    @Test
    public void testEmptySet() {
        BitFilteredSet<Integer> bsi = new BitFilteredSet<Integer>(range, 0);

        assertEquals(0, Iterables.size(bsi));
    }

    @Test
    public void testFullSet() {
        int mask = ~((~0) << N);

        BitFilteredSet<Integer> bsi = new BitFilteredSet<Integer>(range, mask);

        int i=0;
        for (Integer item : bsi) {
            assertEquals(range.get(i++), item);
        }
        // implicit items in Bsi zijn op
    }

    @Test
    public void testFullSetSize() {
        int mask = ~((~0) << N);

        BitFilteredSet<Integer> bsi = new BitFilteredSet<Integer>(range, mask);

        // No items left?
        assertEquals(N, Iterables.size(bsi));
    }

    @Test
    public void testOddItems() {
        int mask = 0;

        for (int i=0; i < N; i+= 2) {
            mask |= (1 << i);
        }

        BitFilteredSet<Integer> bsi= new BitFilteredSet<Integer>(range, mask);

        for (Integer item : bsi)
            assertEquals(0, item % 2);
    }

    @Test
    public void testEvenItems() {
        int mask = 0;

        for (int i=1; i < N; i+= 2) {
            mask |= (1 << i);
        }

        BitFilteredSet<Integer> bsi= new BitFilteredSet<Integer>(range, mask);

        for (Integer item : bsi)
            assertEquals(1, item % 2);
    }



    @Test
    public void testSingleBit() {
        int mask = 0;

        int pos = (int)(N*Math.random()); // 0..N-1

        mask |= 1 << pos;

        BitFilteredSet<Integer> bsi= new BitFilteredSet<Integer>(range, mask);

        assertEquals(range.get(pos), Iterables.getOnlyElement(bsi));
    }

    @Test
    public void testSizeMatchesActualSize() {
        int mask = 0;

        for (int i=0; i < range.size(); i++) {
            mask |= (1 << i);

            BitFilteredSet<Integer> bsi = new BitFilteredSet<Integer>(range, mask);
            assertEquals(i+1, bsi.size());
            // Make sure it enumerates the iterator.
            assertEquals(i+1, Iterators.size(bsi.iterator()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsOverSized() {
        ImmutableList<Integer> ints = ContiguousSet.create(Range.closed(0, 32), DiscreteDomain.integers()).asList();
        assertEquals(33, ints.size());
        new BitFilteredSet<Integer>(ints, 0);
    }

    /**
     * Reject a mask that has a too high initial bit
     * note that for this to work, range.size needs to be < 32.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRejectsOverSizedMask() {
        range = Ranges.closed(0, 16).asSet(DiscreteDomains.integers()).asList();
        int mask = 1 << range.size();

        new BitFilteredSet<Integer>(range, mask);
    }

    @Test
    public void testAcceptsMaximumMask() {
        ImmutableList<Integer> ints = ContiguousSet.create(Range.closed(0, 15), DiscreteDomain.integers()).asList();

        int mask = 0;
        for (int i=0; i<16; i++)
            mask |= (1 << i);

        assertEquals(16, ints.size());
        assertEquals(16, Integer.bitCount(mask));
        new BitFilteredSet<Integer>(ints, mask);
    }

    @Test(expected = NoSuchElementException.class)
    public void throwsNoSuchElementWhenEmpty() {
        int mask = 0;
        for (int i=0; i<=N; i++)
            mask |= (1 << i);

        BitFilteredSet<Integer> bs = new BitFilteredSet<Integer>(range, mask);

        Iterator<Integer> it = bs.iterator();
        assertEquals(N, Iterators.getLast(it).intValue());

        assertFalse(it.hasNext());
        it.next(); // throw.
    }
}
