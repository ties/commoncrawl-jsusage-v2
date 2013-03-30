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

import edu.utwente.mbd.scriptparse.URLTools;
import edu.utwente.mbd.util.ScriptNameTokenizer;
import org.apache.pig.EvalFunc;
import org.apache.pig.builtin.OutputSchema;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

/**
 * Out output yields combination pairs, which PigStorage reads as strings instead of tuples.
 *
 * This UDF splits the string on the seperator (= comma) and returns a Bag of strings.
 */
@OutputSchema("src:tuple(host:chararray, filename:chararray)")
public class SplitHostPath extends EvalFunc<Tuple> {
    enum Errors {ILLEGAL_URL, ILLEGAL_INPUT};

    private final TupleFactory tupleFactory     = TupleFactory.getInstance();

    @Override
    public Tuple exec(Tuple input) throws IOException {
        if (input == null || input.size() != 1 || input.get(0) == null){
            warn("illegal input", Errors.ILLEGAL_INPUT);
        }

        try {
            String url = (String)input.get(0);

            Tuple res = tupleFactory.newTuple(2);
            res.set(0, new URL(url).getHost());
            res.set(1, URLTools.getFilename(url));

            return res;
        } catch (MalformedURLException e) {
            warn("Illegal url " + input.get(0), Errors.ILLEGAL_URL);
        } catch (NoSuchElementException e) {
            warn("Missing tokens " + input.get(0), Errors.ILLEGAL_URL);
        }

        return null;
    }
}