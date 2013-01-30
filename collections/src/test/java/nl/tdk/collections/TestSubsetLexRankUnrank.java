package nl.tdk.collections;

/**
 * Return the proper instance to run the test functions on
 */
public class TestSubsetLexRankUnrank extends TestRankUnrank{
    public KSubsetLex<Integer> getInstance() {
        return new KSubsetLex<Integer>(k, range);
    }
}
