package mylist;

import java.lang.Iterable;
import java.util.Collection;

public interface ILinkedList<E> extends Iterable<E> {
    void add(E element);
    void add(Collection<? extends E> collection);
    void add(int index, E element);
    void clear();
    E get(int index);
    int indexOf(E element);
    E remove(int index);
    E set(int index, E element);
    int size();
    E[] toArray();
    String toString();
}
