package nl.tdk.collections;

import com.google.common.collect.*;
import com.google.common.math.IntMath;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test the Colexicographical rank and unrank functions
 */
public abstract class TestRankUnrank {
    /** Reverse-Sorted list of some integer range */
    protected ImmutableSortedSet<Integer> range;
    protected KSubset<Integer> combinations;

    protected int k;
    protected int n;

    private final int N = 13;

    @Before
    public void setup() {
        range = Ranges.closed(1, N).asSet(DiscreteDomains.integers()).descendingSet();
        n = range.size();

        List<Integer> shuffled = Lists.newArrayList(range);
        Collections.shuffle(shuffled);

        k = shuffled.get(0);

        combinations = getInstance();
    }

    public abstract KSubset<Integer> getInstance();

    @Test
    public void testUnrank() {
        Set<Set<Integer>> seen = Sets.newHashSet();

        // '12 choose 5' options: 792
        for (int i=0; i < IntMath.binomial(n, k); i++) {
            ImmutableSortedSet<Integer> ints = combinations.unRank(i);

            // check size
            assertEquals(ints.size(), k);
            // already seen?
            assertTrue(String.format("Set %s was already in the set, rank %d", new Object[]{ints, i}), seen.add(ints));
        }
    }

    @Test
    public void testRank() {
        Set<Set<Integer>> seen = Sets.newHashSet();

        for (int i=0; i < IntMath.binomial(n, k); i++) {
            ImmutableSortedSet<Integer> ints = combinations.unRank(i);

            // Get the objects rank
            int rank = combinations.rank(ints);
            assertEquals(i, rank);
        }
    }

    @Test
    public void testAllItemsAreSeen() {
        Set<Integer> seen = Sets.newHashSet();
        Set<Set<Integer>> sets = Sets.newHashSet();

        for (int i=0; i < IntMath.binomial(n, k); i++) {
            ImmutableSortedSet<Integer> ints = combinations.unRank(i);

            seen.addAll(ints);
            sets.add(ints);
        }

        assertEquals("All elements are seen", n, seen.size());
        assertEquals("All elements are unique", sets.size(), IntMath.binomial(n, k));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void rejectIllegalRankUpper() {
        combinations.unRank(IntMath.binomial(n, k) + 1);
    }


    @Test(expected=IndexOutOfBoundsException.class)
    public void rejectIllegalRankLower() {
        combinations.unRank(-1);
    }
}
