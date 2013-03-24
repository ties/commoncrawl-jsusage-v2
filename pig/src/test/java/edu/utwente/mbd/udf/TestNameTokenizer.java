package edu.utwente.mbd.udf;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import edu.utwente.mbd.util.ScriptNameTokenizer;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;

/**
 * Some tests that verify that names are properly split
 */
public class TestNameTokenizer {
    private ScriptNameTokenizer ntt;
    /** Map script names -> spec */
    private ImmutableMap<String, String> entries;

    @Before
    public void init() {
        ntt = new ScriptNameTokenizer();

        ImmutableMap.Builder<String, String> examples = ImmutableMap.builder();

        examples.put("jquery-1.6.2.min.js", "1.6.2.min");
        examples.put("jquery.js", "");
        examples.put("jquery-ui.js", "");
        examples.put("jquery-min.js", "min");
        examples.put("jquery.ui.min.js", "min");
        // test positioning of the first skippable token
        examples.put("jquery.ui.v2.min.js", "min");
        examples.put("jquery.ui.min.v2.js", "min.v2");
        examples.put("jquery.ui.2.min.js", "2.min");
        examples.put("jquery.ui.min.2.js", "min.2");
        // always break on number
        examples.put("jquery.ui.2.win.js", "2.win");

        // badly named file
        examples.put("try..it.js", "");
        examples.put("try.2.it.js", "2.it"); // empty token is ignored

        // no extensions
        examples.put("another_case.", "");
        examples.put("another_case", "");

        // overlapping regex
        examples.put("overlapping.js.regex.test.js", "");

        entries = examples.build();
    }

    @Test
    public void testSplitsSpecs() {
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            ScriptNameTokenizer.NameInformation res = ntt.split(entry.getKey());

            assertEquals(entry.getValue(), res.spec);
        }
    }

    /**
     * Check that input contains all tokens (since indeOf >= 0, index > -1,
     * and that the last occurence of a token does not start before the current pos
      * @param input
     * @param res
     */
    private void checkResults(String input, ScriptNameTokenizer.NameInformation res) {
        int pos = 0;

        for (String t : res.fileNameParts) {
            assertTrue(pos <= input.lastIndexOf(t));
            pos += t.length();
        }

        // size of input should be the # of dots + token size
        assertTrue(pos + res.fileNameParts.size() + res.spec.length() < input.length());
    }

}
