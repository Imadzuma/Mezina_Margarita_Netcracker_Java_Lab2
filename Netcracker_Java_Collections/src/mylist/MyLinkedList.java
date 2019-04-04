package mylist;

import java.util.*;
import java.util.function.Consumer;

public class MyLinkedList<E> implements ILinkedList<E> {
    // Nested classes
    private class Node {
        // Fields
        E element;
        Node nextNode;
        Node prevNode;

        // Constructors
        public Node(E element, Node prevNode, Node nextNode) {
            this.element = element;
            this.prevNode = prevNode;
            this.nextNode = nextNode;
        }

        // Override methods
        public String toString() {
            String res = "Node[ ";
            res += "PrevNode=" + ((prevNode!=null) ? prevNode.element.toString() : "null");
            res += ", value=" + element.toString();
            res += ", NextNode=" + ((nextNode!=null) ? nextNode.element.toString() : "null");
            return res;
        }
    }
    private class MyListIterator implements ILinkedListIterator<E> {
        // Fields
        private Node nextNode=firstNode;
        private Node returnedNode=null;
        private int nextIndex = 0;
        private int extendedModCount = modCount;

        // Constructors
        MyListIterator() {}

        // Private methods
        private void checkForModifications() {
            if (extendedModCount!=modCount) throw new ConcurrentModificationException();
        }

        // Iterator methods
        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }
        @Override
        public E next() {
            checkForModifications();
            if (!hasNext()) throw new IllegalStateException();
            returnedNode = nextNode;
            nextNode=nextNode.nextNode;
            nextIndex++;
            return returnedNode.element;
        }
        @Override
        public void remove() {
            checkForModifications();
            if (returnedNode==null) throw new IllegalStateException();
            removeNode(returnedNode);
            returnedNode=null;
            nextIndex--;
            extendedModCount++;
        }
        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            if (action == null) throw new NullPointerException();
            while(hasNext())
                action.accept(next());
            checkForModifications();
        }

        // ILinkedIterator methods
        @Override
        public int nextIndex() {
            checkForModifications();
            return nextIndex;
        }
        @Override
        public int prevIndex() {
            checkForModifications();
            return nextIndex-1;
        }
        @Override
        public boolean hasPrevious() {
            return (nextIndex > 0);
        }
        @Override
        public E previous() {
            checkForModifications();
            if (!hasPrevious()) throw new IllegalStateException();
            returnedNode = nextNode = (nextNode!=null) ? nextNode.prevNode : lastNode;
            nextIndex--;
            return returnedNode.element;
        }
        @Override
        public void set(E element) {
            checkForModifications();
            if (returnedNode==null) throw new IllegalStateException();
            returnedNode.element=element;
        }
        @Override
        public void add(E element) {
            checkForModifications();
            returnedNode=null;
            if (nextNode==null) {
                addLast(element);
                returnedNode = lastNode;
            }
            else if (nextNode==firstNode) {
                addFirst(element);
                returnedNode = firstNode;
            }
            else {
                addBefore(element, nextNode);
                returnedNode=nextNode.prevNode;
            }
            nextIndex++;
            extendedModCount++;
        }
    }

    // Fields
    private Node firstNode = null;
    private Node lastNode = null;
    private int size = 0;
    private int modCount = 0;

    // Constructors
    public MyLinkedList() {}
    public MyLinkedList(E... elements) {
        for(E element: elements)
            add(element);
    }
    public MyLinkedList(Collection<? extends E> collection) {
        add(collection);
    }

    // Private methods
    private void checkIndex(int index, int size) {
        if (!(index >=0 && index < size)) throw new NoSuchElementException();
    }
    private void addFirst(E element) {
        Node newNode = new Node(element, null, firstNode);
        if (firstNode != null) firstNode.prevNode = newNode;
        if (lastNode == null) lastNode = newNode;
        firstNode = newNode;
        size++;
        modCount++;
    }
    private void addLast(E element) {
        Node newNode = new Node(element, lastNode, null);
        if (lastNode != null) lastNode.nextNode = newNode;
        if (firstNode == null) firstNode = newNode;
        lastNode = newNode;
        size++;
        modCount++;
    }
    private void addBefore(E element, Node node) {
        Node newNode = new Node(element, node.prevNode, node);
        node.prevNode.nextNode = newNode;
        node.prevNode = newNode;
        size++;
        modCount++;
    }
    private Node getNode(int index) {
        if (index < (size >> 1)) {
            Node curNode = firstNode;
            for(int i = 0; i < index; ++i) curNode = curNode.nextNode;
            return curNode;
        }
        else {
            Node curNode = lastNode;
            for (int i = size - 1; i > index; --i) curNode = curNode.prevNode;
            return curNode;
        }
    }
    private E removeFirst() {
        Node delNode = firstNode;
        if (size == 1) firstNode = lastNode = null;
        else {
            firstNode = firstNode.nextNode;
            if (firstNode!=null) firstNode.prevNode=null;
        }
        E value = delNode.element;
        delNode.prevNode = null;
        delNode.nextNode = null;
        delNode.element = null;
        size--;
        modCount++;
        return value;
    }
    private E removeLast() {
        Node delNode = lastNode;
        if (size == 1) firstNode = lastNode = null;
        else {
            lastNode = lastNode.prevNode;
            if (lastNode!=null) lastNode.nextNode=null;
        }
        E value = delNode.element;
        delNode.element = null;
        delNode.nextNode = null;
        delNode.prevNode = null;
        size--;
        modCount++;
        return value;
    }
    private E removeNode(Node node) {
        node.prevNode.nextNode = node.nextNode;
        node.nextNode.prevNode = node.prevNode;
        E value = node.element;
        node.prevNode = null;
        node.nextNode = null;
        node.element = null;
        size--;
        modCount++;
        return value;
    }

    // Iterable methods
    @Override
    public Iterator<E> iterator() {
        return new MyListIterator();
    }

    // ILinkedList methods
    @Override
    public void add(E element) {
        addLast(element);
    }
    @Override
    public void add(Collection<? extends E> collection) {
        Object[] arr = collection.toArray();
        for(Object obj: arr)
            add((E)obj);
    }
    @Override
    public void add(int index, E element) {
        checkIndex(index, size+1);
        if (index == 0) addFirst(element);
        else if (index == size) addLast(element);
        else addBefore(element, getNode(index));
    }
    @Override
    public void clear() {
        Node nextNode=null;
        for (Node curNode = firstNode; curNode!=null; curNode=nextNode) {
            nextNode=curNode.nextNode;
            curNode.element=null;
            curNode.nextNode=null;
            curNode.prevNode=null;
        }
        firstNode=null;
        lastNode=null;
        size=0;
    }
    @Override
    public E get(int index) {
        checkIndex(index, size);
        return getNode(index).element;
    }
    @Override
    public int indexOf(E element) {
        int index = 0;
        for(Node curNode = firstNode; curNode!=null; curNode=curNode.nextNode) {
            if ((curNode.element!=null) ? curNode.element.equals(element) : element==null)
                return index;
            index++;
        }
        return -1;
    }
    @Override
    public E remove(int index) {
        checkIndex(index, size);
        if (index==0) return removeFirst();
        if (index == size-1) return removeLast();
        return removeNode(getNode(index));
    }
    @Override
    public E set(int index, E element) {
        checkIndex(index, size);
        Node node = getNode(index);
        E val = node.element;
        node.element = element;
        return val;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public E[] toArray() {
        if (firstNode==null) return null;
        E[] arr = (E[])java.lang.reflect.Array.newInstance(firstNode.element.getClass(), size);
        int index = 0;
        for(Node curNode=firstNode; curNode!=null; curNode=curNode.nextNode, index++)
            arr[index] = curNode.element;
        return arr;
    }
    @Override
    public String toString() {
        if (firstNode==null) return "EMPTY LIST";
        String result = "MyLinkedList[" + firstNode.element.toString();
        for (Node curNode = firstNode.nextNode; curNode!=null; curNode=curNode.nextNode)
            result += ", " + curNode.element.toString();
        result += "]";
        return result;
    }

    // New methods
    public MyListIterator listIterator() {return new MyListIterator();}
}
