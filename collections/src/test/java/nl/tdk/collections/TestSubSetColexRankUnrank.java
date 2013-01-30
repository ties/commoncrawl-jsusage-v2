package nl.tdk.collections;

/**
 * Return proper instance to run methods on
 */
public class TestSubSetColexRankUnrank extends TestRankUnrank {
    public KSubSet<Integer> getInstance() {
        return new KSubSetColex<Integer>(k, range);
    }
}
