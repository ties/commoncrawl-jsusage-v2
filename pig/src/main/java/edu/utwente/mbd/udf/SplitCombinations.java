package edu.utwente.mbd.udf;

import java.io.IOException;

import com.google.common.base.Splitter;
import edu.utwente.mbd.JSUsageMapper;
import org.apache.pig.EvalFunc;
import org.apache.pig.builtin.OutputSchema;
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.schema.Schema;

/**
 * Out output yields combination pairs, which PigStorage reads as strings instead of tuples.
 *
 * This UDF splits the string on the seperator (= comma) and returns a Bag of strings.
 */
@OutputSchema("tokens:bag{token:tuple(token:chararray)}")
public class SplitCombinations extends EvalFunc<DataBag>{
    private final static Splitter splitter = Splitter.on(JSUsageMapper.COMMA).trimResults().omitEmptyStrings();

    // tuple and bag factory
    final TupleFactory tupleFactory = TupleFactory.getInstance();
    final BagFactory bagFactory = BagFactory.getInstance();

    /**
     * Split the input string on the provided boundaries. Return Tuple(String+) or null.
     * @param input string of comma seperated values
     * @return Tuple with at least one argument or null
     * @throws IOException when input fails.
     */
    public DataBag exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0 || input.get(0) == null)
            return null;
        try{
            DataBag result = bagFactory.newDistinctBag();

            // add all the tokens to the bag as a tuple.
            for (String token : splitter.split((String)input.get(0)))
                result.add(tupleFactory.newTuple(token));

            // when result is empty, return null. Otherwise return the bag.
            if (result.size() == 0)
                return null;

            return result;
        }catch(Exception e) {
            throw new IOException("Caught exception processing input row", e);
        }
    }
}