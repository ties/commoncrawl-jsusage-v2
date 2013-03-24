package nl.tdk.collections;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.*;

/**
 * Iterator that uses a int to mask the items that are actually in the set. This is used
 * to efficiently represent a subset of a list up to 32 items.
 *
 * Implementation modeled after that from Guava (com.google.common.collect.Sets.PowerSet.BitFilteredSet) which unfortunately is a private class.
 *
 */
public class BitFilteredSet<E> extends AbstractSet<E> {
    private final           ImmutableList<E> items;
    private final int       initialBits;

    /**
     * Instantiate a bit filtered set.
     * @param items <b>Set</b> of items, as list.
     * @param initialBits items that are represented by this set.
     * @require items represents a set, ie. no double items
     */
    public BitFilteredSet(ImmutableList<E> items, int initialBits) {
        checkPositionIndex(32 - Integer.numberOfLeadingZeros(initialBits), items.size());
        checkArgument(items.size() <= 32, "Can not sensibly iterate over more than 31 items");
        this.items = checkNotNull(items);
        this.initialBits = initialBits;
    }

    @Override
    public Iterator<E> iterator() {
        return new BitFilteredSetIterator();
    }

    @Override
    public int size() {
        return Integer.bitCount(initialBits);
    }

    /**
     * Iterator that only exposes the elements that are 'behind' a true bit
     * in the given integer mask.
     */
    private class BitFilteredSetIterator extends UnmodifiableIterator<E> {
        private int remainingBits;

        public BitFilteredSetIterator() {
            remainingBits = initialBits;
        }

        @Override
        public boolean hasNext() {
            return remainingBits != 0;
        }

        @Override
        public E next() {
            int idx = Integer.numberOfTrailingZeros(remainingBits);
            if (idx == 32)
                throw new NoSuchElementException();

            int mask = 1 << idx; // 1= 2-complement, positive: bits are 0...1
            remainingBits &= ~mask;
            return items.get(idx);
        }
    }
}
