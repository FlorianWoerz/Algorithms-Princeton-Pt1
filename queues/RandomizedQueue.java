/* *****************************************************************************
 *  Name:              Florian Woerz
 *  Course:            Algorithms, Part I by Princeton University
 *  Submitted:         2022-03-24
 *  Score:             100/100
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements a randomized queue. A randomized queue is similar to a stack or queue, except that the
 * item removed is chosen uniformly at random among items in the data structure.
 * <p>
 * Iterator. Each iterator returns the items in uniformly random order. The order of two or more
 * iterators to the same randomized queue are mutually independent; each iterator maintains its own
 * random order.
 * <p>
 * Performance.  The randomized queue implementation supports each randomized queue operation
 * (besides creating an iterator) in constant amortized time. That is, any intermixed sequence
 * of m randomized queue operations (starting from an empty queue) takes at most cm steps in the
 * worst case, for some constant c. A randomized queue containing n items uses at most 48n + 192
 * bytes of memory. Additionally, the iterator implementation supports operations next() and
 * hasNext() in constant worst-case time; and construction in linear time; we have to use a linear
 * amount of extra memory per iterator.
 * (In the file Deque.java we will also implement a deque.)
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
public class RandomizedQueue<Item> implements Iterable<Item> {
    // specify initial capacity of the underlying resizing array
    private static final int INIT_CAPACITY = 8;

    private Item[] q;   // queue elements
    private int n;      // number of elements in queue


    /**
     * Initializes an empty randomized queue
     */
    public RandomizedQueue() {
        q = (Item[]) new Object[INIT_CAPACITY];
        n = 0;
    }


    /**
     * Is the randomized queue empty?
     *
     * @return true if the randomized queue is empty; false otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }


    /**
     * Returns the number of items in the randomized queue
     *
     * @return number of items in randomized queue
     */
    public int size() {
        return n;
    }


    // Resizes the underlying array to hold `newCapacity` many items
    private void resize(int newCapacity) {
        assert newCapacity >= n;
        Item[] copy = (Item[]) new Object[newCapacity];
        for (int i = 0; i < n; i++) {
            copy[i] = q[i];
        }
        q = copy;
    }


    /**
     * Adds the item to the randomized queue
     *
     * @param item the item to be added
     * @throws IllegalArgumentException if object is null
     */
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Object to add cannot be null.");

        // if necessary, double the size of the array and recopy the content to the front of new one
        if (n == q.length) resize(2 * q.length);    // size-adjustment of array
        q[n++] = item;                              // add item and increase last pointer
    }


    /**
     * Removes and returns a random item
     *
     * @return a random item from the randomized queue
     * @throws java.util.NoSuchElementException if the randomized queue is empty
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        // Get a random element
        int rand = StdRandom.uniform(n);
        Item item = q[rand];
        if (rand == n - 1) {
            q[rand] = null; // avoid loitering
        }
        else { // do not waste space at the end and fill the gap
            q[rand] = q[n - 1];
            q[n - 1] = null; // avoid loitering
        }
        // if necessary, shrink size of array
        if (n > 0 && n == q.length / 4) resize(q.length / 2);
        // return the randomly sampled item and reduce item counter
        n--;
        return item;
    }


    /**
     * Return a random item from the queue (but does not remove it!)
     *
     * @return random item
     */
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        // Get a random element
        int rand = StdRandom.uniform(n);
        Item item = q[rand];
        return item;
    }


    /**
     * Return an independent iterator over the items in the queue in random order
     *
     * @return independent iterator
     */
    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }

    private class RandomizedIterator implements Iterator<Item> {

        private final Item[] copy;
        private int count = 0;

        public RandomizedIterator() {
            copy = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                copy[i] = q[i];
            }
            StdRandom.shuffle(copy);
        }

        public boolean hasNext() {
            return count < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy[count++];
        }
    }

    // Some small and incomplete unit tests
    public static void main(String[] args) {

        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        StdOut.println(queue.isEmpty());    // ==> true
        queue.enqueue(23);
        for (int e : queue) {
            StdOut.println(e);
        }
        StdOut.println(queue.sample());     //==> 23
        for (int e : queue) {
            StdOut.println(e);
        }
        StdOut.println(queue.dequeue());    // ==> 23
        StdOut.println(queue.size());       // ==> 0
        StdOut.println(queue.isEmpty());    // ==> true
        queue.enqueue(531);
        StdOut.println(queue.sample());


        RandomizedQueue<Integer> queue2 = new RandomizedQueue<>();
        queue2.enqueue(890);
        queue2.size();                      // ==> 1
        queue2.enqueue(759);
        queue2.dequeue();                   // ==> 890
        queue2.enqueue(950);
        queue2.size();                      // ==> 2
        queue2.enqueue(490);
        StdOut.println(queue2.dequeue());
        
    }

}
