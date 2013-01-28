package nl.tdk.collections;

import com.google.common.collect.*;
import com.google.common.math.IntMath;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test the Colexicographical rank and unrank functions
 */
public class TestColexRankUnrank {
    /** Reverse-Sorted list of some integer range */
    private ImmutableSortedSet<Integer> range;
    private KSubSetColex<Integer> combinations;

    private final int k = 3;
    private int n;

    @Before
    public void setup() {
        range = Ranges.closed(0, 5).asSet(DiscreteDomains.integers()).descendingSet();
        n = range.size();
        combinations = new KSubSetColex<>(k, range);

    }

    @Test
    public void testUnrank() {
        System.out.println("TestUnrank");
        Set<Set<Integer>> seen = Sets.newHashSet();

        // '12 choose 5' options: 792
        for (int i=0; i < IntMath.binomial(n, k); i++) {
            ImmutableSortedSet<Integer> ints = combinations.unRank(i);

            // check size
            assertEquals(ints.size(), k);

            System.out.println(String.format("i: %d rank: %d, set: %s", new Object[]{i, combinations.rank(ints), ints}));


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

            System.out.println(String.format("i: %d rank: %d, set: %s", new Object[]{i, rank, ints}));

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

        System.out.println(seen);
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
