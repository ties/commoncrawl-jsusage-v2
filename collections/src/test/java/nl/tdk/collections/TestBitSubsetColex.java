package nl.tdk.collections;

import java.util.Set;

/**
 * Test the co-lexicographical implementation
 */
public class TestBitSubsetColex extends TestBitSubsets<Integer> {
    public TestBitSubsetColex () {
        super(defaultIntSet());
    }

    @Override
    public Iterable<Set<Integer>> getInstance(int k) {
        return new KSubsetBitColex<Integer>(k, range);
    }
}
