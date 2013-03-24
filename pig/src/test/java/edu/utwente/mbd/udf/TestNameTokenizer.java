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
    private final Map<String, String> entries = ImmutableMap.of("jquery-1.6.2.min.js", "1.6.2.min",
            "jquery.js", "", "jquery-ui.js", "", "jquery-min.js", "min", "jquery.ui.min.js", "min");

    @Before
    public void init() {
        ntt = new ScriptNameTokenizer();
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
