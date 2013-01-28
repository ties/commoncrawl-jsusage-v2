package nl.tdk.collections;

import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.ImmutableList;
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
    private ImmutableList<Integer> range;

    @Before
    public void setup() {
        range = ImmutableList.copyOf(Ranges.closed(0, 30).asSet(DiscreteDomains.integers())).reverse();
    }

    @Test
    public void testBoundedIndexOfCanFind() {
        KSubSetColex<Integer> ksc = new KSubSetColex<Integer>(10, range);

        for (int i = 0; i < ksc.objects.size(); i++) {
            int j = ksc.boundedIndexOf(0, ksc.objects.get(i));

            assertEquals(i, j);
        }
    }

    @Test
    public void testBoundedIndexOfCanNotFind() {
        KSubSetColex<Integer> ksc = new KSubSetColex<Integer>(10, range);

        for (int i = 0; i < ksc.objects.size(); i++) {
            int j = ksc.boundedIndexOf(i + 1, ksc.objects.get(i));

            assertEquals(j, -1);
        }
    }

    @Test
    public void testBoundedIndexOfBounded() {
        KSubSetColex<Integer> ksc = new KSubSetColex<Integer>(10, range);

        int j = 0;
        for (int i = 0; i < ksc.objects.size(); i++) {
            j = ksc.boundedIndexOf(j, ksc.objects.get(i));

            assertEquals(j, i);
        }
    }

}
