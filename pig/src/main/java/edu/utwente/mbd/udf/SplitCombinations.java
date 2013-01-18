package edu.utwente.mbd.udf;

import java.io.IOException;

import com.google.common.base.Splitter;
import edu.utwente.mbd.JSUsageMapper;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * Out output yields combination pairs, which PigStorage reads as strings instead of tuples.
 *
 * This UDF splits the string on the seperator (= comma) and returns a Bag of strings.
 */
public class SplitCombinations extends EvalFunc<DataBag>{
    private final static Splitter splitter = Splitter.on(JSUsageMapper.COMMA).omitEmptyStrings().trimResults();

    /**
     * Split the input string on the provided boundaries. Return Tuple(String+) or null.
     * @param input string of comma seperated values
     * @return Tuple with at least one argument or null
     * @throws IOException when input fails.
     */
    public DataBag exec(Tuple input) throws IOException {
        final TupleFactory tf = TupleFactory.getInstance();

        if (input == null || input.size() == 0)
            return null;
        try{
            DataBag result = BagFactory.getInstance().newDistinctBag();

            for (String token : splitter.split((String)input.get(0))){ // split the input string
                Tuple t = tf.newTuple();
                t.set(0, token);

                result.add(t);
            }

            // when result is empty, return null. Otherwise return the bag.
            if (result.size() == 0)
                return null;

            return result;
        }catch(Exception e){
            throw new IOException("Caught exception processing input row ", e);
        }
    }

}