package nl.tdk.collections;

/**
 * Return the proper instance to run the test functions on
 */
public class TestSubsetLexRankUnrank extends TestRankUnrank{
    public Ranker<Integer> getInstance() {
        return new KSubsetLexRank<Integer>(k, range);
    }
}
