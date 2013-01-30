package nl.tdk.collections;

import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ranges;
import com.google.common.math.IntMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestKSubSetGray {
    private KSubSetGray<Integer> ksg;
    private ImmutableSortedSet<Integer> range;

    @Before
    public void setup() {
        range = Ranges.closed(0, 5).asSet(DiscreteDomains.integers()).descendingSet();
        ksg = new KSubSetGray<Integer>(3, range);
    }

    @Test
    public void testUnrank() {
        for (int i=0; i < IntMath.binomial(5, 3); i++){
            System.out.println(Integer.toBinaryString(ksg.unrank(i)));
        }
    }
}
