package nl.tdk.collections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.*;

/**
 * Iterator that uses a int to mask the items that are actually in the set. This is used
 * to efficiently represent a subset of a list up to 32 items.
 *
 * Implementation modeled after that from Guava (com.google.common.collect.Sets.PowerSet.BitFilteredSetIterator) which unfortunately is a private class.
 *
 */
public class BitFilteredSetIterator<E> extends UnmodifiableIterator<E> {
    private final   ImmutableList<E> items;
    private int     remainingBits;

    /**
     * Instantiate a bit filtered set.
     * @param items <b>Set</b> of items, as list.
     * @param initialBits items that are represented by this set.
     * @require items represents a set, ie. no double items
     */
    public BitFilteredSetIterator(ImmutableList<E> items, int initialBits) {
        checkArgument(items.size() <= 32, "Can not iterate over more than 31 items");
        this.items = checkNotNull(items);
        this.remainingBits = initialBits;
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

        int mask = 1 << idx;
        remainingBits &= ~mask;
        return items.get(idx);
    }
}
