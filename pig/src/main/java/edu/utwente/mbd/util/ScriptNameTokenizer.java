package edu.utwente.mbd.util;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

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
    private final Joiner join = Joiner.on('.');

    /**
     * Split the given character sequence
     * @return iterator over tokens
     */
    public NameInformation split(String in) {
        // transform in
        final String input = in.toLowerCase().replace(".js", "");

        PeekingIterator<String> tokens = Iterators.peekingIterator(splitter.split(input).iterator());

        ImmutableList.Builder<String> name = ImmutableList.builder();

        // iterate over the tokens - add it to spec string when it is all digit or a resered word
        while(tokens.hasNext()) {
            final String nxt = tokens.peek();
            // concatenate and return
            if (CharMatcher.DIGIT.matchesAllOf(nxt) || RESERVED_WORDS.contains(nxt))
                return new NameInformation(name.build(), join.join(tokens));

            name.add(tokens.next());
        }

        return new NameInformation(name.build());
    }

    public static class NameInformation {
        public final List<String> fileNameParts;
        public final String spec;

        public NameInformation(List<String> fileNameParts, String spec) {
            this.fileNameParts = fileNameParts;
            this.spec = spec;
        }

        public NameInformation(List<String> fileNameParts) {
            this.fileNameParts = fileNameParts;
            this.spec = "";
        }
    }
}