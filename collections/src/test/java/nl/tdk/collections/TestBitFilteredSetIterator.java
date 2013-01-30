package nl.tdk.collections;

import com.google.common.collect.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Some tests for bit filtered set iterator
 */
public class TestBitFilteredSetIterator {
    private ImmutableList<Integer> range;
    private final int N = 31; // highest < 32

    @Before
    public void setup() {
        range = Ranges.closed(0, N).asSet(DiscreteDomains.integers()).asList();
    }

    @Test
    public void testEmptySet() {
        BitFilteredSetIterator<Integer> bsi = new BitFilteredSetIterator<Integer>(range, 0);

        assertEquals(0, Iterators.size(bsi));
    }

    @Test
    public void testFullSet() {
        int mask = ~((~0) << N);

        BitFilteredSetIterator<Integer> bsi = new BitFilteredSetIterator<Integer>(range, mask);

        for (int i=0; i<N; i++)
            assertEquals(range.get(i), bsi.next());

        // No items left?
        assertEquals(0, Iterators.size(bsi));
    }

    @Test
    public void testFullSetSize() {
        int mask = ~((~0) << N);

        BitFilteredSetIterator<Integer> bsi = new BitFilteredSetIterator<Integer>(range, mask);

        // No items left?
        assertEquals(N, Iterators.size(bsi));
    }

    @Test
    public void testOddItems() {
        int mask = 0;

        for (int i=0; i < N; i+= 2) {
            mask |= (1 << i);
        }

        BitFilteredSetIterator<Integer> bsi = new BitFilteredSetIterator<Integer>(range, mask);

        while (bsi.hasNext())
            assertEquals(0, bsi.next() % 2);
    }

    @Test
    public void testEvenItems() {
        int mask = 0;

        for (int i=1; i < N; i+= 2) {
            mask |= (1 << i);
        }

        BitFilteredSetIterator<Integer> bsi = new BitFilteredSetIterator<Integer>(range, mask);

        while (bsi.hasNext())
            assertEquals(1, bsi.next() % 2);
    }



    @Test
    public void testSingleBit() {
        int mask = 0;

        int pos = (int)(N*Math.random()); // 0..N-1

        mask |= 1 << pos;

        BitFilteredSetIterator<Integer> bsi = new BitFilteredSetIterator<Integer>(range, mask);

        assertEquals(range.get(pos), bsi.next());
    }
}
