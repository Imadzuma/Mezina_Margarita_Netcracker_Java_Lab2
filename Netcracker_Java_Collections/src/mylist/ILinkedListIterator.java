package mylist;

import java.util.Iterator;
import java.util.function.Consumer;

public interface ILinkedListIterator<E> extends Iterator<E> {
    // Iterator methods
    @Override
    boolean hasNext();
    @Override
    E next();
    @Override
    void remove();
    @Override
    void forEachRemaining(Consumer<? super E> action);

    // New methods
    int nextIndex();
    int prevIndex();
    boolean hasPrevious();
    E previous();
    void set(E element);
    void add(E element);
}
