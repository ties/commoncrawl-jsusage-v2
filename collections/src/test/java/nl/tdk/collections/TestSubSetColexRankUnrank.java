package nl.tdk.collections;

/**
 * Return proper instance to run methods on
 */
public class TestSubSetColexRankUnrank extends TestRankUnrank {
    public Ranker<Integer> getInstance() {
        return new KSubsetColexRank<Integer>(k, range);
    }
}
