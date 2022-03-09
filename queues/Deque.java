/* *****************************************************************************
 *  Name:              Florian Woerz
 *  Course:            Algorithms, Part I by Princeton University
 *  Submitted:         2022-02-25
 *  Score:             100/100
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements a double-ended queue or deque (pronounced "deck"), which is a generalization of a
 * stack and a queue that supports adding and removing items from either the front or the back of
 * the data structure.
 * <p>
 * Performance.
 * The deque implementation supports each deque operation (including construction) in constant
 * worst-case time. A deque containing n items uses at most 48n + 192 bytes of memory. Additionally,
 * the iterator implementation supports each operation (including construction) in constant
 * worst-case time.
 * (In the file RandomizedQueue.java we will also implement a randomized queue.)
 * The time and memory complexities of the implementation abide by the following summary table:
 * | Deque                        | Randomized Queue
 * --------------------------|------------------------------|-----------------------------
 * Non-iterator operations   | Constant worst-case time     | Constant amortized time
 * Iterator constructor      | Constant worst-case time     | linear in current # of items
 * Other iterator operations | Constant worst-case time     | Constant worst-case time
 * Non-iterator memory use   | Linear in current # of items | Linear in current # of items
 * Memory per iterator       | Constant                     | Linear in current # of items
 *
 * @author Florian Woerz
 */
public class Deque<Item> implements Iterable<Item> {
    private int n;      // size of the deque
    private Node front; // front node pointer
    private Node back;  // back node pointer


    // Helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }


    /**
     * Initializes an empty deque.
     */
    public Deque() {
        front = null;
        back = null;
        n = 0;
    }


    /**
     * Checks if the deque is empty.
     *
     * @return true if the deque is empty; false otherwise
     */
    public boolean isEmpty() {
        return (front == null || back == null);
    }


    /**
     * Returns the number of items on the deque.
     *
     * @return number of elements in deque
     */
    public int size() {
        return n;
    }


    /**
     * Adds the item to the front
     *
     * @param item the item to add
     */
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Item connot be null.");
        Node oldfront = front;
        front = new Node();
        front.item = item;
        front.next = oldfront;
        front.prev = null;
        if (isEmpty()) {
            // If we are inserting the first node in an empty deque, this block will be executed
            back = front;
        }
        else if (oldfront != null) {
            // We could have also used a simple "else" since `oldfront != null` is always satisfied
            // when the deque is non-empty.
            oldfront.prev = front;
        }
        n++; // increase the number-of-elements-counter of the deque
    }

    /**
     * Adds the item to the back.
     *
     * @param item the item to add to the back
     */
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Item connot be null.");
        Node oldback = back;                        // Save the pointer to the back node.
        back = new Node();                          // Create a new back node ...
        back.item = item;                           // ... and fill it with the item ...
        back.next = null;                           // ... and point its next to null.
        if (oldback != null) oldback.next = back;   // connect oldback to back
        back.prev = oldback;
        if (isEmpty()) front = back;
        n++;                                        // increase the number-of-elements-counter of the deque
    }

    /**
     * Removes and return the item from the front
     *
     * @return the item from the front
     * @throws java.util.NoSuchElementException if the deque is empty
     */
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = front.item; // save the front item to return it
        front = front.next;     // delete the front item
        if (front != null) front.prev = null;
        if (isEmpty()) {
            front = null;
            back = null;
        }
        n--;
        return item;            // return the saved item
    }


    /**
     * Removes and teturn the item from the back
     *
     * @return item from the back of the deque
     * @throws java.util.NoSuchElementException if the deque is empty
     */
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = back.item;
        if (n > 1) {
            back.prev.next = null;
            back = back.prev;
        }
        else {
            front = null;
            back = null;
        }
        n--;
        return item;
    }


    /**
     * Returns an iterator over item in order from front to back
     *
     * @return iterator
     */
    public Iterator<Item> iterator() {
        return new FrontToBackIterator();
    }

    private class FrontToBackIterator implements Iterator<Item> {
        private Node current = front;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // Some simple unit tests
    public static void main(String[] args) {
        // As an example for Deque, we know that if we call addFirst() with the numbers 1 through n
        // in ascending order, then call removeLast() n times, we should see the numbers 1 through
        // n in ascending order.

        int n = 5;

        Deque<Integer> deque = new Deque<Integer>();
        for (int i = 1; i <= n; i++) {
            deque.addFirst(i);
        }

        // Make it more interesting
        deque.addLast(7);

        StdOut.println(deque.size());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeFirst());

        // Example 2:
        Deque<Integer> deque2 = new Deque<>();
        deque2.addFirst(1);
        deque2.iterator();      // ==> [1]
        deque2.addFirst(2);
        deque2.iterator();      // ==> [2, 1]
        deque2.addFirst(3);
        deque2.iterator();      // ==> [3, 2, 1]
        deque2.removeLast();    // ==> 1
        deque2.iterator();      // ==> [3, 2]
        deque2.addFirst(5);
        deque2.iterator();      // ==> [5, 3, 2]
        for (Integer e : deque2) {
            System.out.println(e);
        }
        deque2.removeLast();    // ==> 1


    }
}
