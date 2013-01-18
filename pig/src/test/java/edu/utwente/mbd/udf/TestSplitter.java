package edu.utwente.mbd.udf;

import com.google.common.base.Joiner;
import edu.utwente.mbd.JSUsageMapper;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test cases for combination splitter
 */
public class TestSplitter {
    private SplitCombinations udf;
    private Joiner join;

    private Tuple tupleWithValue(String str) throws IOException {
        Tuple tpl = TupleFactory.getInstance().newTuple(1);
        tpl.set(0, str);

        return tpl;
    }


    private Tuple tupleWithValue(String[] str) throws IOException {
        Tuple tpl = TupleFactory.getInstance().newTuple(1);
        tpl.set(0, join.join(str));

        return tpl;
    }

    @Before
    public void setup(){
        udf = new SplitCombinations();
        join = Joiner.on(JSUsageMapper.COMMA);
    }

    @Test
    public void testTupleContainsValue() throws IOException {
        String val = new DateTime().toString();

        Tuple res = tupleWithValue(val);
        assertEquals(val, res.get(0));

        String vals[] = {val, "en"+val};

        Tuple res2 = tupleWithValue(vals);
        assertEquals(res2.get(0), join.join(vals));
    }

    @Test
    public void testSplitEmptyString() throws IOException {
        DataBag res = udf.exec(tupleWithValue(""));

        assertNull(res);
    }

    @Test
    public void testSplitNull() throws IOException {
        assertNull(udf.exec(null));
    }

    @Test
    public void testSplitSingle() throws IOException {
        String input = "FOOBAR";

        DataBag res = udf.exec(tupleWithValue(input));

        assertEquals(res.size(), 1);
        assertEquals(res.iterator().next().get(0), input);
    }

    @Test
    public void testSplitMultipleItems() throws IOException {
        String[] input = {"first", "second", "third", "fourth", "fifth"};

        DataBag res = udf.exec(tupleWithValue(input));

        assertEquals(input.length, 5);

    }

    @Test
    public void testCombineRepeatedItems() throws IOException {
        String[] input = {"first", "second", "third", "first", "last"};

        DataBag res = udf.exec(tupleWithValue(input));

        // one repeated item
        assertEquals(res.size(), input.length - 1);
    }

    @Test
    public void testItemsWithSpaces() throws IOException {
        String[] input = {"foo ", " bar", " baz"};

        DataBag res = udf.exec(tupleWithValue(input));

        for(Tuple t : res){
            assertTrue(!((String)t.get(0)).contains(" "));
        }
    }

    @Test
    public void testSpace() throws IOException {
        DataBag res = udf.exec(tupleWithValue(" "));

        assertNull(res);
    }


}
