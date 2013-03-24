package edu.utwente.mbd.util;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;

/**
 * This class splits the given string up into tokens.
 *
 * A scriptname is considered to have two main parts;
 *  - name script name
 *  - specifiers version/compressed/etc specifiers
 *    => we detect these after the first number-only token,
 *      or the word 'min'
 *
 *
 *
 */
public class ScriptNameTokenizer {
    private final List<String> RESERVED_WORDS = ImmutableList.of("min");
    private final CharMatcher DOT_EM_DASH = CharMatcher.anyOf("._-");

    private final Splitter splitter = Splitter.on(DOT_EM_DASH).omitEmptyStrings().trimResults();
    private final Joiner join = Joiner.on('-');

    /**
     * Split the given character sequence
     * @return iterator over tokens
     */
    public Iterator<String> split(String in) {
        // transform in
        final String input = in.toLowerCase().replace(".js", "");

        return new ForwardingIterator<String>() {
            Iterator<String> tokens = splitter.split(input).iterator();

            @Override
            protected Iterator<String> delegate() {
                return tokens;
            }

            @Override
            public String next() {
                String nxt = super.next();

                if (CharMatcher.DIGIT.matchesAllOf(nxt)) { // concatenate
                    return join.join(nxt, tokens);
                }
                return nxt;
            }
        };
    }
}