package edu.utwente.mbd.stem;

import com.google.common.base.CharMatcher;
import com.google.common.collect.AbstractIterator;

import java.util.Iterator;

import static com.google.common.base.Preconditions.*;

/**
 * This class splits the given string up into tokens.
 *
 * Tokens consist of (seperator, token) pairs.
 *
 */
public class ScriptNameTokenizer {
    private final CharMatcher   NODASH  = CharMatcher.anyOf("._");
    private final CharMatcher   DASH    = CharMatcher.is('-');

    private final CharMatcher   NUMBER  = CharMatcher.JAVA_LETTER;
    private final CharMatcher   ALNUM   = CharMatcher.JAVA_LETTER_OR_DIGIT;

    private final boolean toLower;
    private final boolean trim;

    /**
     * Instantiate the tokenizer. Define if it should work on lowercase strings
     * @param toLower turn string nto lower case?
     * @param trim trim the resulting string?
     */
    public ScriptNameTokenizer(boolean toLower, boolean trim) {
        this.toLower = toLower;
        this.trim = trim;
    }

    /**
     * Split the given character sequence
     * @return iterator over tokens
     */
    public Iterable<Token> split(final CharSequence in) {
        return new Iterable<Token>() {
            @Override
            public Iterator<Token> iterator() {
                return new AbstractIterator<Token>() {
                    public Token computeNext(){
                        return null;
                    }
                };
            }
        };
    }

    /**
     * Named tuple of (sep, token)
     */
    public static class Token {
        public final String sep;
        public final String token;

        /**
         * @precondition sep != null && token != null
         */
        public Token (String sep, String token) {
            this.sep = checkNotNull(sep);
            this.token = checkNotNull(token);
        }
    }
}
