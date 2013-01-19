package edu.utwente.mbd.udf;

import java.io.IOException;

import com.google.common.base.Splitter;
import edu.utwente.mbd.JSUsageMapper;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.schema.Schema;

/**
 * Out output yields combination pairs, which PigStorage reads as strings instead of tuples.
 *
 * This UDF splits the string on the seperator (= comma) and returns a Bag of strings.
 */
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

    /**
     * Get the output schema for this function/
     * @param input schema of input
     * @return schema of output
     */
    public Schema outputSchema(Schema input) {
        try{
            if (input.size() != 1)
                throw new IllegalArgumentException("Expected input to have only a single field");

            Schema.FieldSchema inputField = input.getField(0);

            if (inputField.type != DataType.CHARARRAY)
                throw new IllegalArgumentException("Expected input to be a charArray");


            Schema.FieldSchema tokenFieldSchema = new Schema.FieldSchema("script", DataType.CHARARRAY);
            Schema tupleSchema = new Schema(tokenFieldSchema);

            Schema.FieldSchema tupleFS = new Schema.FieldSchema("tuple_of_scripts", tupleSchema, DataType.TUPLE);

            Schema bagSchema = new Schema(tupleFS);
            bagSchema.setTwoLevelAccessRequired(true);

            Schema.FieldSchema bagFS = new Schema.FieldSchema("bag_of_script_tokens", bagSchema, DataType.BAG);

            return new Schema(bagFS);
        }catch (Exception e) {
            return null;
        }
    }
}