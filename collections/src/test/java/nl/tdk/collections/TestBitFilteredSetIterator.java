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
        range = Ranges.closed(1, N).asSet(DiscreteDomains.integers()).asList();
    }

    @Test
    public void testEmptySet() {
        BitFilteredSetIterator<Integer> bsi = new BitFilteredSetIterator<Integer>(range, 0);

        assertEquals(0, Iterators.size(bsi));
    }


    @Test
    public void testFullSet() {
        int fullMask = ~0 & ((~0) << N);

        BitFilteredSetIterator<Integer> bsi = new BitFilteredSetIterator<Integer>(range, fullMask);

        assertEquals(N, Iterators.size(bsi));
    }
}
