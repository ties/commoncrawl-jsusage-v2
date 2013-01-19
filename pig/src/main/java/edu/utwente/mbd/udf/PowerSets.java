package edu.utwente.mbd.udf;

import com.google.common.collect.*;
import static com.google.common.base.Preconditions.*;

import org.apache.pig.EvalFunc;
import org.apache.pig.FuncSpec;
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.util.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * When counting combinations, we only yield one tuple per
 * (script1, script2, …, scriptn) combination.
 *
 * This combination is not counted for
 * (script1, script2, …, scriptn-1), which in fact it should,
 * since this is also an occurence of that combination of scripts.
 *
 * The input is a Bag, output is a bag of bags.
 *
 *
 */
public class PowerSets extends EvalFunc<DataBag> {
    /** The output contains 2^items sets, this is the default limit */
    private final static int DEFAULT_SET_SIZE_LIMIT = 12;

    // tuple and bag factory
    final TupleFactory tupleFactory = TupleFactory.getInstance();
    final BagFactory bagFactory = BagFactory.getInstance();

    private int setSizeLimit = DEFAULT_SET_SIZE_LIMIT;


    /**
     * Creates the PowerSet of the given bag of tuples of strings
     *
     * Pig guarantees that the input arguments are either (maxLength, bag) or (bag).
     */
    public DataBag exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0)
            return null;

        DataBag inputBag;

        // switch on the number of args;
        if (input.size() == 1){
            inputBag = (DataBag)checkNotNull(input.get(0));
        } else { // invoked with (limit, bag)
            setSizeLimit = (Integer)input.get(0);
            inputBag = (DataBag)checkNotNull(input.get(1));
        }

        try {
            Set<Tuple> items = ImmutableSet.copyOf(inputBag);

            // if the input set is empty, return null instead of Bag { {} }
            if (items.size() == 0)
                return null;

            Set<Set<Tuple>> powerSet = Sets.powerSet(items);

            DataBag result = nestedSetsToNestedBags(powerSet);

            // result SHOULD have an item
            if (result.size() > 0)
                return result;
        } catch (Exception e) {
            throw new IOException("Caught exception processing input row", e);
        }
        return null;
    }

    /**
     * This utility function unpacks the nested sets of tuples and creates a DataBag containing all items
     * @param sets nested set of tuples
     * @return DataBag containing a bag for each set in the input, or null
     * @throws Exception when there is an error with the tuple or bag
     */
    private DataBag nestedSetsToNestedBags (Iterable<Set<Tuple>> sets) throws Exception {
        final DataBag res = bagFactory.newDistinctBag();

        for (Set<Tuple> set : sets){
            // skip empty sets
            if (set.size() == 0)
                continue;

            // create new bag with all items
            DataBag inner = bagFactory.newDistinctBag();

            for (Tuple t : set)
                inner.add(t);

            Tuple innerRes = tupleFactory.newTuple(1);
            innerRes.set(0, inner);

            res.add(innerRes);
        }
        return res;
    }

    /**
     * Describe the possible calls to this UDF
     * this UDF accepts:
     * - int length_limit, Bag<String>
     * - Bag<String>
     * @return
     */
    public List<FuncSpec> getArgsToFuncMapping() {
        return ImmutableList.of(
                Utils.buildSimpleFuncSpec(this.getClass().getName(), DataType.BAG),
                Utils.buildSimpleFuncSpec(this.getClass().getName(), DataType.INTEGER, DataType.BAG)
        );
    }

    /**
     * Get the output schema for this function
     *
     * @param input schema of input
     * @return schema of output
     */
    public Schema outputSchema(Schema input) {
        try {
            if (input.size() != 1)
                throw new IllegalArgumentException("Expected input to have only one single field of type Bag");

            Schema.FieldSchema inputField = input.getField(0);

            // this should be a bag
            if (inputField.type != DataType.BAG)
                throw new IllegalArgumentException("Expected input to be a bag of items");

            // itemSchema: first field is tuple<type>
            Schema.FieldSchema itemSchema = inputField.schema.getField(0);

            if (itemSchema.type != DataType.TUPLE)
                throw new IllegalArgumentException("Expected Bag to contain Tuples");

            // We will yield the same type of tuples
            Schema innerBagItemSchema = new Schema(itemSchema);
            innerBagItemSchema.setTwoLevelAccessRequired(true);

            Schema.FieldSchema innerBagFieldSchema = new Schema.FieldSchema("bag of tupeles", innerBagItemSchema, DataType.BAG);

            // Define the outer bag's type
            Schema bagSchema = new Schema(innerBagFieldSchema);
            bagSchema.setTwoLevelAccessRequired(true);

            Schema.FieldSchema outerBagFS = new Schema.FieldSchema("bag_of_bags", bagSchema, DataType.BAG);

            return new Schema(outerBagFS);
        } catch (Exception e) {
            return null;
        }
    }

}
