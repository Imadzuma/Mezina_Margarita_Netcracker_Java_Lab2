package test;

import com.sun.deploy.panel.ITreeNode;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import mylist.*;

import java.lang.reflect.Array;
import java.util.*;

public class MainClass {

    static void testClass() {
        System.out.println("<------------------TEST MYLINKEDLIST------------------>");
        ILinkedList<Integer> myList = new MyLinkedList<>(1, 2, 3);
        System.out.println(myList.toString());
        myList.add(4);
        System.out.println("\tAdd 4: " + myList.toString());
        myList.add(0, 5);
        System.out.println("\tAdd 5 on first place: " + myList.toString());
        myList.add(5, 6);
        System.out.println("\tAdd 6 on last place: " + myList.toString());
        myList.add(3, 7);
        System.out.println("\tAdd 7 on 3 place: " + myList.toString());
        System.out.println("\tElement on 2 place: " + myList.get(2));
        System.out.println("\tElement 3 on place: " + myList.indexOf(3));
        myList.set(4, 0);
        System.out.println("\tSet element on 4 place: " + myList.toString());
        myList.remove(0);
        System.out.println("\tRemove first element: " + myList.toString());
        myList.remove(5);
        System.out.println("\tRemove last element: " + myList.toString());
        myList.remove(2);
        System.out.println("\tRemove element on 2 place: " + myList.toString());
        Integer[] arr = myList.toArray();
        System.out.print("\tTo array elements:");
        for (Integer integer : arr)
            System.out.print(" " + integer);
        System.out.println();
        myList.clear();
        System.out.println("\tClear: " + myList.toString());
        System.out.println();
    }

    static void testIterator() {
        System.out.println("<--------------------TEST ITERATOR-------------------->");
        Integer arr[] = {1, 2, 3};
        ILinkedList<Integer> myList = new MyLinkedList<>(arr);
        System.out.println(myList.toString());
        ILinkedListIterator<Integer> iter = (ILinkedListIterator)myList.iterator();
        System.out.print("\tGo from next:");
        while(iter.hasNext()) System.out.print(" " + iter.next());
        System.out.println();
        System.out.print("\tGo from previous:");
        while(iter.hasPrevious()) System.out.print(" " + iter.previous());
        System.out.println();
        iter.next();
        iter.add(4);
        System.out.println("\tGo next and add: " + myList.toString());
        iter.next();
        iter.remove();
        System.out.println("\tGo next and remove: " + myList.toString());
        iter.next();
        iter.set(0);
        System.out.println("\tGo next and set: " + myList.toString());
        System.out.println();
    }

    static void checkPerformanceMyList() {
        System.out.println("<-------------CHECK PERFORMANCE FOR MYLIST------------->");
        int size = 10000;

        System.out.println("\t\t\t\t\t\tLinkedList\t\t\tMyLinkedList");
        long startTime1, finishTime1, startTime2, finishTime2;
        Random rand = new Random();
        List<Integer> linkedList = new LinkedList<>();
        ILinkedList<Integer> myList = new MyLinkedList<>();

        startTime1 = System.nanoTime();
        for(int i = 0; i < size; ++i) linkedList.add(rand.nextInt(100));
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for(int i = 0; i < size; ++i) myList.add(rand.nextInt(100));
        finishTime2 = System.nanoTime();
        System.out.println("Add last\t\t\t\t" + (finishTime1-startTime1) + "\t\t\t\t" + (finishTime2-startTime2));

        int[] positions = new int[size];
        for (int i = 0; i < size; ++i) positions[i] = rand.nextInt(size-i);
        startTime1 = System.nanoTime();
        for (int pos: positions) linkedList.remove(pos);
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int pos: positions) myList.remove(pos);
        finishTime2 = System.nanoTime();
        System.out.println("Remove random\t\t\t" + (finishTime1-startTime1) + "\t\t\t" + (finishTime2-startTime2));

        for(int i = 0; i < size; ++i) positions[i] = rand.nextInt(i+1);
        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedList.add(positions[i], rand.nextInt(100));
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) myList.add(positions[i], rand.nextInt(100));
        finishTime2 = System.nanoTime();
        System.out.println("Add random\t\t\t\t" + (finishTime1-startTime1) + "\t\t\t" + (finishTime2-startTime2));

        for(Integer pos: positions) pos = rand.nextInt(size);
        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedList.get(positions[i]);
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) myList.get(positions[i]);
        finishTime2 = System.nanoTime();
        System.out.println("Get random\t\t\t\t" + (finishTime1-startTime1) + "\t\t\t" + (finishTime2-startTime2));

        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedList.set(positions[i], rand.nextInt(100));
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) myList.set(positions[i], rand.nextInt(100));
        finishTime2 = System.nanoTime();
        System.out.println("Set random\t\t\t\t" + (finishTime1-startTime1) + "\t\t\t" + (finishTime2-startTime2));

        ListIterator<Integer> iter1 = (ListIterator)linkedList.iterator();
        startTime1 = System.nanoTime();
        while(iter1.hasNext()) iter1.next();
        finishTime1 = System.nanoTime();
        ILinkedListIterator<Integer> iter2 = (ILinkedListIterator)myList.iterator();
        startTime2 = System.nanoTime();
        while(iter2.hasNext()) iter2.next();
        finishTime2 = System.nanoTime();
        System.out.println("Next iterator\t\t\t" + (finishTime1-startTime1) + "\t\t\t\t" + (finishTime2-startTime2));

        startTime1 = System.nanoTime();
        while (iter1.hasPrevious()) iter1.previous();
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        while (iter2.hasPrevious()) iter2.previous();
        finishTime2 = System.nanoTime();
        System.out.println("Previous iterator\t\t" + (finishTime1-startTime1) + "\t\t\t\t" + (finishTime2-startTime2));

        startTime1 = System.nanoTime();
        linkedList.clear();
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        myList.clear();
        finishTime2 = System.nanoTime();
        System.out.println("Clear\t\t\t\t\t" + (finishTime1-startTime1) + "\t\t\t\t" + (finishTime2-startTime2));
        System.out.println();
    }

    static void checkPerfomanceList() {
        System.out.println("<--------------CHECK PERFORMANCE FOR LIST-------------->");
        int size = 10000;

        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        long startTime1, startTime2, finishTime1, finishTime2;
        int[] positions = new int[size];
        Random rand = new Random();
        System.out.println("\t\t\t\t\tArrayList\tLinkedList");

        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) arrayList.add(rand.nextInt(100));
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedList.add(rand.nextInt(100));
        finishTime2 = System.nanoTime();
        System.out.println("Add last\t\t\t" + (finishTime1-startTime1) + "\t\t" + (finishTime2-startTime2));

        for (int i = 0; i < size; ++i) positions[i] = rand.nextInt(size-i);
        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) arrayList.remove(positions[i]);
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedList.remove(positions[i]);
        finishTime2 = System.nanoTime();
        System.out.println("Remove random\t\t" + (finishTime1-startTime1) + "\t\t" + (finishTime2-startTime2));

        for (int i = 0; i < size; ++i) positions[i] = rand.nextInt(i+1);
        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) arrayList.add(positions[i], rand.nextInt(100));
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedList.add(positions[i], rand.nextInt(100));
        finishTime2 = System.nanoTime();
        System.out.println("Add random\t\t\t" + (finishTime1-startTime1) + "\t\t" + (finishTime2-startTime2));

        for (int i = 0; i < size; ++i) positions[i] = rand.nextInt(size);
        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) arrayList.get(positions[i]);
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedList.get(positions[i]);
        finishTime2 = System.nanoTime();
        System.out.println("Get random\t\t\t" + (finishTime1-startTime1) + "\t\t" + (finishTime2-startTime2));

        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) arrayList.set(positions[i], rand.nextInt(100));
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedList.set(positions[i], rand.nextInt(100));
        finishTime2 = System.nanoTime();
        System.out.println("Set random\t\t\t" + (finishTime1-startTime1) + "\t\t" + (finishTime2-startTime2));

        ListIterator<Integer> arrayIter = arrayList.listIterator();
        ListIterator<Integer> linkIter = linkedList.listIterator();
        startTime1 = System.nanoTime();
        while(arrayIter.hasNext()) arrayIter.next();
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        while(linkIter.hasNext()) linkIter.next();
        finishTime2 = System.nanoTime();
        System.out.println("Next iterator\t\t" + (finishTime1-startTime1) + "\t\t" + (finishTime2-startTime2));

        startTime1 = System.nanoTime();
        while(arrayIter.hasPrevious()) arrayIter.previous();
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        while(linkIter.hasPrevious()) linkIter.previous();
        finishTime2 = System.nanoTime();
        System.out.println("Previous iterator\t" + (finishTime1-startTime1) + "\t\t" + (finishTime2-startTime2));

        startTime1 = System.nanoTime();
        arrayList.clear();
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        linkedList.clear();
        finishTime2 = System.nanoTime();
        System.out.println("Clear\t\t\t\t" + (finishTime1-startTime1) + "\t\t" + (finishTime2-startTime2));
        System.out.println();
}

    static void checkPerformanceSet() {
        System.out.println("<--------------CHECK PERFORMANCE FOR SET-------------->");
        int size = 10000;

        System.out.println("\t\t\t\tHashSet \tLinkedHashSet\tTreeSet");
        Set<Integer> hashSet = new HashSet<>();
        Set<Integer> linkedHashSet = new LinkedHashSet<>();
        Set<Integer> treeSet = new TreeSet<>();
        Random rand = new Random();
        long startTime1, startTime2, startTime3, finishTime1, finishTime2, finishTime3;
        int[] values = new int[size];

        for (int i = 0; i < size; ++i) values[i] = rand.nextInt(size);
        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) hashSet.add(values[i]);
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedHashSet.add(values[i]);
        finishTime2 = System.nanoTime();
        startTime3 = System.nanoTime();
        for (int i = 0; i < size; ++i) treeSet.add(values[i]);
        finishTime3 = System.nanoTime();
        System.out.println("Add\t\t\t\t"+(finishTime1-startTime1)+"\t\t"+(finishTime2-startTime2)+"\t\t\t"+(finishTime3-startTime3));

        for (int i = 0; i < size; ++i) values[i] = rand.nextInt(size);
        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) hashSet.contains(values[i]);
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedHashSet.contains(values[i]);
        finishTime2 = System.nanoTime();
        startTime3 = System.nanoTime();
        for (int i = 0; i < size; ++i) treeSet.contains(values[i]);
        finishTime3 = System.nanoTime();
        System.out.println("Contains\t\t"+(finishTime1-startTime1)+"\t\t"+(finishTime2-startTime2)+"\t\t\t"+(finishTime3-startTime3));

        for (int i = 0; i < size; ++i) values[i] = rand.nextInt(size);
        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) hashSet.remove(values[i]);
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedHashSet.remove(values[i]);
        finishTime2 = System.nanoTime();
        startTime3 = System.nanoTime();
        for (int i = 0; i < size; ++i) treeSet.remove(values[i]);
        finishTime3 = System.nanoTime();
        System.out.println("Remove\t\t\t"+(finishTime1-startTime1)+"\t\t"+(finishTime2-startTime2)+"\t\t\t"+(finishTime3-startTime3));

        for (int i = 0; i <  size; ++i) {
            hashSet.add(i);
            linkedHashSet.add(i);
            treeSet.add(i);
        }
        Iterator<Integer> hashIter = hashSet.iterator();
        Iterator<Integer> linkIter = linkedHashSet.iterator();
        Iterator<Integer> treeIter = linkedHashSet.iterator();
        startTime1 = System.nanoTime();
        while (hashIter.hasNext()) hashIter.next();
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        while (linkIter.hasNext()) linkIter.next();
        finishTime2 = System.nanoTime();
        startTime3 = System.nanoTime();
        while (treeIter.hasNext()) treeIter.next();
        finishTime3 = System.nanoTime();
        System.out.println("Iterator next\t"+(finishTime1-startTime1)+"\t\t"+(finishTime2-startTime2)+"\t\t\t"+(finishTime3-startTime3));

        startTime1 = System.nanoTime();
        hashSet.clear();
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        linkedHashSet.clear();
        finishTime2 = System.nanoTime();
        startTime3 = System.nanoTime();
        treeSet.clear();
        finishTime3 = System.nanoTime();
        System.out.println("Clear\t\t\t"+(finishTime1-startTime1)+"\t\t"+(finishTime2-startTime2)+"\t\t\t"+(finishTime3-startTime3));
        System.out.println();
    }

    static void checkPerformanceMap() {
        System.out.println("<--------------CHECK PERFORMANCE FOR MAP-------------->");
        int size = 10000;

        System.out.println("\t\tHashMap \tLinkedHashMap\tTreeMap");
        Map<Integer, Integer> hashMap = new HashMap<>();
        Map<Integer, Integer> linkedHashMap = new LinkedHashMap<>();
        Map<Integer, Integer> treeMap = new TreeMap<>();
        Random rand = new Random();
        long startTime1, startTime2, startTime3, finishTime1, finishTime2, finishTime3;
        int[] values = new int[size];

        for (int i = 0; i < size; ++i) values[i] = rand.nextInt(size);
        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) hashMap.put(values[i], rand.nextInt(100));
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedHashMap.put(values[i], rand.nextInt(100));
        finishTime2 = System.nanoTime();
        startTime3 = System.nanoTime();
        for (int i = 0; i < size; ++i) treeMap.put(values[i], rand.nextInt(100));
        finishTime3 = System.nanoTime();
        System.out.println("Add\t\t"+(finishTime1-startTime1)+"\t\t"+(finishTime2-startTime2)+"\t\t\t"+(finishTime3-startTime3));

        for (int i = 0; i < size; ++i) values[i] = rand.nextInt(size);
        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) hashMap.get(values[i]);
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedHashMap.get(values[i]);
        finishTime2 = System.nanoTime();
        startTime3 = System.nanoTime();
        for (int i = 0; i < size; ++i) treeMap.get(values[i]);
        finishTime3 = System.nanoTime();
        System.out.println("Get\t\t"+(finishTime1-startTime1)+"\t\t"+(finishTime2-startTime2)+"\t\t\t"+(finishTime3-startTime3));

        for (int i = 0; i < size; ++i) values[i] = rand.nextInt(size);
        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) hashMap.replace(values[i], rand.nextInt(100));
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedHashMap.replace(values[i], rand.nextInt(100));
        finishTime2 = System.nanoTime();
        startTime3 = System.nanoTime();
        for (int i = 0; i < size; ++i) treeMap.replace(values[i], rand.nextInt(100));
        finishTime3 = System.nanoTime();
        System.out.println("Set\t\t"+(finishTime1-startTime1)+"\t\t"+(finishTime2-startTime2)+"\t\t\t"+(finishTime3-startTime3));

        for (int i = 0; i < size; ++i) values[i] = rand.nextInt(size);
        startTime1 = System.nanoTime();
        for (int i = 0; i < size; ++i) hashMap.remove(values[i]);
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        for (int i = 0; i < size; ++i) linkedHashMap.remove(values[i]);
        finishTime2 = System.nanoTime();
        startTime3 = System.nanoTime();
        for (int i = 0; i < size; ++i) treeMap.remove(values[i]);
        finishTime3 = System.nanoTime();
        System.out.println("Remove\t"+(finishTime1-startTime1)+"\t\t"+(finishTime2-startTime2)+"\t\t\t"+(finishTime3-startTime3));

        for(int i = 0; i < size; ++i) {
            hashMap.put(i, rand.nextInt(100));
            linkedHashMap.put(i, rand.nextInt(100));
            treeMap.put(i, rand.nextInt(100));
        }
        startTime1 = System.nanoTime();
        hashMap.clear();
        finishTime1 = System.nanoTime();
        startTime2 = System.nanoTime();
        linkedHashMap.clear();
        finishTime2 = System.nanoTime();
        startTime3 = System.nanoTime();
        treeMap.clear();
        finishTime3 = System.nanoTime();
        System.out.println("Clear\t"+(finishTime1-startTime1)+"\t\t"+(finishTime2-startTime2)+"\t\t\t"+(finishTime3-startTime3));
        System.out.println();
    }

    public static void main(String[] args) {
        // MyLinkedList tests
        testClass();

        // Iterator tests
        testIterator();

        // check performance for MyLinkedList
        checkPerformanceMyList();

        // check performance for list
        checkPerfomanceList();

        // check performance for set
        checkPerformanceSet();

        // check performance for map
        checkPerformanceMap();
    }
}
