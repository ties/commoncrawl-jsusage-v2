package nl.tdk.collections;

import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ranges;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the utility functions for colexicographical subset generation
 */
public class TestBoundedIndexOf {
    /** Reverse-Sorted list of some integer range */
    private ImmutableSortedSet<Integer> range;

    @Before
    public void setup() {
        range = Ranges.closed(0, 30).asSet(DiscreteDomains.integers()).descendingSet();
    }

    @Test
    public void testBoundedIndexOfCanFind() {
        KSubsetColex<Integer> ksc = new KSubsetColex<>(10, range);

        for (int i = 0; i < ksc.objects.size(); i++) {
            int j = ksc.boundedIndexOf(1, ksc.objects.get(i));

            assertEquals(i, j-1);
        }
    }

    @Test
    public void testBoundedIndexOfCanNotFind() {
        KSubsetColex<Integer> ksc = new KSubsetColex<>(10, range);

        for (int i = 0; i < ksc.objects.size() - 1; i++) { // look one item behind the actual item
            try {
                int j = ksc.boundedIndexOf(i+2, ksc.objects.get(i));
                assertFalse("not reached", true);
            } catch (IllegalStateException e) {
                // ok
            }
        }
    }

    @Test
    public void testBoundedIndexOfBounded() {
        KSubsetColex<Integer> ksc = new KSubsetColex<>(10, range);

        int j = 1;
        for (int i = 0; i < ksc.objects.size(); i++) {
            j = ksc.boundedIndexOf(j, ksc.objects.get(i));

            assertEquals(i, j-1);
        }
    }

}
