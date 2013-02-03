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
        range = Ranges.closed(0, N).asSet(DiscreteDomains.integers()).asList();
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
        ImmutableList<Integer> ints = Ranges.closed(0, 32).asSet(DiscreteDomains.integers()).asList();
        assertEquals(33, ints.size());
        new BitFilteredSet<>(ints, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsOverSizedMask() {
        ImmutableList<Integer> ints = Ranges.closed(0, 15).asSet(DiscreteDomains.integers()).asList();

        int mask = 0;
        for (int i=0; i<17; i++)
            mask |= (1 << i);

        assertEquals(16, ints.size());
        assertEquals(17, Integer.bitCount(mask));
        new BitFilteredSet<>(ints, mask);
    }

    @Test
    public void testAcceptsMaximumMask() {
        ImmutableList<Integer> ints = Ranges.closed(0, 15).asSet(DiscreteDomains.integers()).asList();

        int mask = 0;
        for (int i=0; i<16; i++)
            mask |= (1 << i);

        assertEquals(16, ints.size());
        assertEquals(16, Integer.bitCount(mask));
        new BitFilteredSet<>(ints, mask);
    }

    @Test(expected = NoSuchElementException.class)
    public void throwsNoSuchElementWhenEmpty() {
        int mask = 0;
        for (int i=0; i<=N; i++)
            mask |= (1 << i);

        BitFilteredSet<Integer> bs = new BitFilteredSet<>(range, mask);

        Iterator<Integer> it = bs.iterator();
        assertEquals(N, Iterators.getLast(it).intValue());

        assertFalse(it.hasNext());
        it.next(); // throw.
    }
}
