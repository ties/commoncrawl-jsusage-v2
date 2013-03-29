package edu.utwente.mbd.udf;

/**
 * Make a tuple from a script tag names using the following transformations:
 * - script names consist of:
 *      [name][spec]?[ext]?
 *
 * The spec is defined as the script version, '.min', etc.
 *
 * And we transform it to two parts; tokens for each part of the name, and a concatenation of the specification field.
 */

import edu.utwente.mbd.util.ScriptNameTokenizer;
import org.apache.pig.EvalFunc;
import org.apache.pig.builtin.OutputSchema;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import java.io.IOException;

/**
 * Out output yields combination pairs, which PigStorage reads as strings instead of tuples.
 *
 * This UDF splits the string on the seperator (= comma) and returns a Bag of strings.
 */
@OutputSchema("src:tuple(name:tuple(token:chararray), spec:chararray)")
public class ScriptSrcToTuple extends EvalFunc<Tuple> {
    enum ScriptScrcToTupleErrors {ILLEGAL_INPUT, ILLEGAL_STATE};

    private final TupleFactory tupleFactory     = TupleFactory.getInstance();
    private final BagFactory bagFactory         = BagFactory.getInstance();

    private final ScriptNameTokenizer nameTokenizer = new ScriptNameTokenizer();

    @Override
    public Tuple exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0 || input.get(0) == null){
            warn("illegal input", ScriptScrcToTupleErrors.ILLEGAL_INPUT);
        }

        try { // get String from input and process it
            ScriptNameTokenizer.NameInformation nameInfo = nameTokenizer.split((String)input.get(0));

            Tuple names = tupleFactory.newTupleNoCopy(nameInfo.fileNameParts);

            Tuple result = tupleFactory.newTuple(2);
            result.set(0, names);
            result.set(1, nameInfo.spec);

            return result;
        } catch (IllegalStateException e) {
            warn("illegal state", ScriptScrcToTupleErrors.ILLEGAL_STATE);
            return null;
        }
    }
}