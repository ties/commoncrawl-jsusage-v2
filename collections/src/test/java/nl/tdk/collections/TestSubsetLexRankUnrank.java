package nl.tdk.collections;

/**
 * Return the proper instance to run the test functions on
 */
public class TestSubsetLexRankUnrank extends TestRankUnrank{
    public KSubSetLex<Integer> getInstance() {
        return new KSubSetLex<Integer>(k, range);
    }
}
