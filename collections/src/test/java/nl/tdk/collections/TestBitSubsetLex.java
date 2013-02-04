package nl.tdk.collections;

import java.util.Set;

/**
 * Test lexicographical ordering implementation
 */
public class TestBitSubsetLex extends TestBitSubsets<Integer> {
    public TestBitSubsetLex () {
        super(defaultIntSet());
    }

    @Override
    public Iterable<Set<Integer>> getInstance(int k) {
        return new KSubsetBitLex<Integer>(k, range);
    }
}
