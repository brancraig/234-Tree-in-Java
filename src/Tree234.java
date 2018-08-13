/* Brandon Craig, brandonjcraig00@gmail.com */

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;


/**
 * An implementation of a Sorted Set Collection, done as a 2-3-4 self-balancing tree. The public class itself acts as a
 * set of methods controlling a set private Node-class objects internally that generate the behavior expected for each
 * of the given methods. Internal to the API-level function calls is a group of private node-class objects that
 * implement the Node-interface; where each type of node handles different methods based on its type. For example, on an
 * insertion of an object: Roots, Leaves, and Branches of each size (0, 2, 3, or 4 children) will handle that method
 * dependent on the Node's type--this type-specific behavior is what allows the tree behave cohesively. Behavior for
 * each method is for each node-type is described in that methods documentation.
 *
 * @param <E> A parametrized object that can be stored in this collection. Must have a toCompare function implemented as
 *           this set creates a total ordering through comparison.
 */

@SuppressWarnings("unchecked")
public class Tree234<E> implements SortedSet<E> {
    /**
     * Main parametrizes an instance of the 2-3-4 tree as a string-based collection, and then inserts all substrings
     * from the ubuntu-linux american-english dictionary--this is a remnant of an earlier usage of this tree that should
     * be changed when more creative test examples are created.
     * @param args command-line args not used.
     */
    public static void main(String [] args){
        Tree234<String> tree234 = new Tree234<String>(String::compareTo);
        String filename = "/usr/share/dict/american-english"; //must be linux/ubuntu
        File file = new File(filename);
        FileInputStream fileInputStream;
        long milliSeconds;
        double seconds = 0;
        try {
            fileInputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e){
            System.err.println("**Exception " + e);
            return;
        }
        if(file.canRead()){

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String temp;

            try {
                milliSeconds = System.currentTimeMillis();
                do {
                    temp = bufferedReader.readLine();
                    if(temp != null){
                       SubstringInserter.insertSubstrings(temp, tree234);
                    }
                }while (temp != null);
                milliSeconds = System.currentTimeMillis() - milliSeconds;
                seconds = ((double)milliSeconds)/((double)1000);
            } catch(IOException e){
                System.err.println("**Exception " + e);
            }


            System.out.println(seconds + " seconds to load");

        }
        //tree234.displayInOrder();
        System.out.println(tree234.size() + " entries added.");
        System.out.println(tree234.count() + " nodes created.");
        System.out.println(tree234.height() + " levels deep.");
        if(tree234.verifyBalance()){
            System.out.println("The tree is balanced.");
        } else {
            System.out.println("The tree is not balanced.");
        }


        System.exit(0);
    }
    private Node root;
    private Comparator comparator;

    /**
     * The 2-3-4 Tree class constructor. Upon instantiation creates a new "Seed1" that is an empty root-node.
     * @param comparator the comparator to be used in the absolute ordering of the set.
     */
    private Tree234(Comparator<? super E> comparator){
        this.root = new Seed1();
        this.comparator = comparator;
    }



    //Begin SortedSet Required Functions://///////

    /**
     * Inserts an object of type E into the collection through a total ordering.
     * @param toAdd the object to be added to the collection.
     * @return true if the add added a new item to the set; false if that item was already present.
     */
    @Override
    public boolean add(E toAdd) {
        Node oldRoot = this.root;
        this.root = root.add(toAdd);
        return oldRoot != this.root;
    }

    /**
     * Inserts a collection of objects of type E into this collection.
     * @param c the collection of type E to be added to this collection.
     * @return true if the addAll added at least one item not present in this collection.
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    /**
     * Empties the entire collection by re-instantiating the root-node as an empty "seed".
     */
    @Override
    public void clear() {
        this.root = new Seed1();
    }

    /**
     * Gets the comparator that will be used in the total-ordering of the collection/set.
     * @return the comparator to be used by the collection.
     */
    @Override
    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    /**
     * Determines whether or not an object is contained within the set.
     * @param o the object to be found in the set.
     * @return true if found, false if not found.
     */
    @Override
    public boolean contains(Object o) {
        return false;
    }

    /**
     * Determines whether or not a collection (in its entirety) is contained within the set.
     * @param c the collection to be found within this set.
     * @return true if the entire collection is found, false if even one item is not found.
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    /**
     * Retrieves the first element in the total ordering of objects contained within the set.
     * @return the first-order objects if present, null if the set is empty.
     */
    @Override
    public E first() {
        return null;
    }

    /**
     * Retrieves a sorted set (of type Tree234) containing all elements from the head to the specified element toElement
     * @param toElement The element up to which all elements will be retrieved.
     * @return
     */
    @Override
    public SortedSet<E> headSet(E toElement) {
        return null;
    }

    /**
     * Determines whether the set is empty. Through the behavior of the internal classes; if the root-node is a SEED1
     * and is empty, then the set is empty.
     * @return true if empty, false is not-empty.
     */
    @Override
    public boolean isEmpty() {
        return root.getEnumeration() == NodeType.SEED1 && root.contains(null);
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }


    /**
     * Retrieves the last element in the total ordering of objects contained within the set.
     * @return the last-order objects if present, null if the set is empty.
     */
    @Override
    public E last() {
        return null;
    }

    /**
     * Removes an object from the set.
     * @param o the object to be removed.
     * @return true if the object to be removed was present, false if it was not found.
     */
    @Override
    public boolean remove(Object o) {
        Node newRoot = this.root.remove(o);
        if(newRoot == this.root) {
            return false;
        }
        this.root = newRoot;
        return true;
    }

    /**
     * Removes all object in a given collection from this set.
     * @param c the collection to be removed.
     * @return true if at least one object was removed, false if no objects were removed.
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public Spliterator<E> spliterator() {
        return null;
    }

    /**
     * Retrieves a subset of this set containing all objects including and between to objects determined by their
     * ordering.
     * @param fromElement the first item in the ordering to be returned in the sub-collection.
     * @param toElement the last item in the ordering to be returned in the sub-collection.
     * @return
     */
    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) { return null; }

    /**
     * Retrieves all elements from an element all the way to the last-ordered object in the set.
     * @param fromElement the element from which all other elements to the tail will be included in the returned
     *                    collection.
     * @return the Sorted Set containing all elements from the param to the tail, or null if the parameter was not
     * found within these set.
     */
    @Override
    public SortedSet<E> tailSet(E fromElement) { return null; }

    /**
     *
     * @param array the array of type T that will contain the contents of this set with respect to their total-ordering.
     * @param <T> The underlying parametrized type of the set to be returned in the array.
     * @return an array of type T containing all elements of the set.
     */
    @Override
    public <T> T[] toArray(T[] array) {
        int size = this.root.count();
        if(array.length < size){
            array = (T[]) Array.newInstance(array.getClass().getComponentType(), size);
        }
        else if(array.length > size)
            array[size] = null;
        int i = 0;
        for(E e : this) {
            array[i] = (T) e;
            ++i;
        }
        return array;
    }

    @Override
    public Object[] toArray() { return new Object[0]; }

    //END SortedSet Required Functions.////////////////

    /**
     * displays each element, in-order, by each elements given "toString" function.
     */
    private void displayInOrder(){
        root.displayInOrder();
        System.out.println();
    }

    /**
     * counts the number of nodes (not objects) contained within the set.
     * @return the number of nodes within the set.
     */
    private int count(){ return root.count();}

    /**
     * counts the number of elements within the set.
     * @return the number of elements within the set.
     */
    public int size(){ return root.size();}

    /**
     * determines the height/depth of the set.
     * @return the height/depth of the set.
     */
    private int height() { return root.height();}

    /**
     * checks to see if the tree is still balanced.
     * @return true if the tree is balanced (all leaves are at the same depth), false if not balanced.
     */
    private boolean verifyBalance(){

        return 0 != root.verifyBalance();
    }

    /**
     * The private enumerations that label each node as a certain type. Useful for certain functions when type-agnostic
     * solutions are not determined.
     */
    private enum NodeType {
        SEED1, SEED2, SEED3, ROOT1, ROOT2, ROOT3, LEAF1, LEAF2, LEAF3, BRANCH1, BRANCH2, BRANCH3, RETURN
    }

    /**
     * An interface describing the functions each node must have to fulfill its role.
     * @param <E> the type of the object contained as data members in each node.
     */
    private interface Node<E>{
        /**
         * Adds an element E to a node.
         * @param toAdd the element E to be added.
         * @return the Node that contains E, or the node whose children contain E, or the original node if E is already
         * present in the node or its children.
         */
        Node add(E toAdd);

        /**
         * Finds an element toFind within the tree in logarithmic-time. Comparisons are made at each node to direct the
         * path the algorithm takes.
         * @param toFind the object to be found in the sorted set.
         * @return true if found, false if not found.
         */
        boolean contains(E toFind);

        /**
         * counts the number of NODES in the tree, not the number of elements of param E.
         * @return the number of nodes in the tree.
         */
        int count();

        /**
         * an in-order traversal that displays each object "toString"
         */
        void displayInOrder();

        /**
         * Provided so that any node can give its type. The type itself is not stored as a data-member but as a function
         * call to a definition.
         * @return the enumeration of the node object.
         */
        NodeType getEnumeration();

        /**
         * presuming that balancing of the tree is enforced, returns the height of the tree (by counting each node down
         * the left side.)
         * @return the height of the tree.
         */
        int height();

        /**
         * Removal function. not yet described.
         * @param toRemove The object to be removed.
         * @return the new node no longer containing the thing to be removed, or removed in one of its children, or the
         * original node (unchanged) because toRemove was not found in it or any of its descendants.
         */
        Node remove(E toRemove);

        /**
         * counts the number of elements in a given node and calls the function on its children (by summing all nodes'
         * counts).
         * @return the count of elements in each node plus those of all its descendants.
         */
        int size();

        /**
         * validates the balance of the tree by comparing the height of each child by each parent node; null signals
         * failure.
         * @return the distance of a node from the leaves of the tree.
         */
        int verifyBalance();




    }

    /**
     * This type of node is meant to be passed back through the return statement when the tree is splitting nodes on an
     * insertion. This only happens when unresolved changes need to be communication towards the root of the tree,
     * otherwise a reference to a non-ReturnNode (data-carrying node) is returned signaling that no more "rootward"
     * adjustment is needed.
     */
    private class ReturnNode implements Node<E> {
        E pushUpData; //named because it is the data from the split node being "pushed up" the tree towards the root.
        Node leftChild;
        Node rightChild;
        ReturnNode(E pushUpData, Node leftChild, Node rightChild){
            this.pushUpData = pushUpData;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        /**
         * part of the node interface but not used in this nodetype. Rejects the addition of an element.
         * @param toAdd the element E to be added, but it will always be rejected.
         * @return always returns itself, unchanged.
         */
        @Override
        public Node add(E toAdd) {
            return this;
        }


        @Override
        public Node remove(E toRemove) {
            if(this.pushUpData == null){
                return this;
            }
            if(comparator.compare(this.pushUpData, toRemove) == 0){
                return null;
            }
            return this;
        }
        @Override
        public boolean contains(E toFind) {
            return comparator.compare(this.pushUpData, toFind) == 0;}

        @Override
        public void displayInOrder() {}

        @Override
        public int count() {
            return 1;
        }

        @Override
        public int height() { return 1; }

        @Override
        public int size() { return 1; }

        @Override
        public int verifyBalance() { return 0; }

        @Override
        public NodeType getEnumeration() {
            return NodeType.RETURN;
        }

    }

    private class Seed1 implements Node<E> {
        private E middleData;
        Seed1(){
            middleData = null;
        }
        Seed1(E e){
            middleData = e;
        }

        @Override
        public Node add(E toAdd) {
            if(this.middleData == null){
                this.middleData = toAdd;
                return this;
            }
            int compare = comparator.compare(this.middleData, toAdd);
            if(compare < 0) { return new Seed2(middleData, toAdd);}
            else if(compare > 0) { return new Seed2(toAdd, middleData); }
            return this;
        }

        @Override
        public Node remove(E toRemove) {
            if(middleData == null){
                return this;
            }
            if(comparator.compare(middleData, toRemove) == 0){
                return new Seed1();
            }
            return this;
        }

        @Override
        public boolean contains(E toFind) {
            return middleData != null && comparator.compare(this.middleData, toFind) == 0;
        }

        @Override
        public void displayInOrder() {
            System.out.print(middleData.toString());
        }

        @Override
        public int count() {
            return 1;
        }

        @Override
        public int size() { return 1; }

        @Override
        public int height() { return 1; }

        @Override
        public int verifyBalance(){return 1;}

        @Override
        public NodeType getEnumeration() { return NodeType.SEED1; }


    }

    private class Seed2 implements Node<E>{
        private E leftData;
        private E rightData;
        Seed2(E leftData, E rightData){
            this.leftData = leftData;
            this.rightData = rightData;
        }

        @Override
        public Node add(E toAdd) {
            int compare = comparator.compare(this.rightData, toAdd);
            if(compare < 0){ return new Seed3(leftData, rightData, toAdd); }
            if(compare == 0) { return this; }
            compare = comparator.compare(this.leftData, toAdd);
            if(compare < 0){ return new Seed3(leftData, toAdd, rightData); }
            if(compare > 0){ return new Seed3(toAdd, leftData, rightData); }
            return this;
        }

        @Override
        public Node remove(E toRemove) {
            if(comparator.compare(leftData, toRemove) == 0){
                return new Seed1(rightData);
            }
            if(comparator.compare(rightData, toRemove) == 0){
                return new Seed1(leftData);
            }
            return this;
        }
        @Override
        public boolean contains(E toFind) {
            return comparator.compare(rightData, toFind) == 0 || comparator.compare(leftData, toFind) == 0;
        }

        @Override
        public void displayInOrder() {
            System.out.print(leftData.toString() + " " + rightData.toString());
        }

        @Override
        public int count() {
            return 1;
        }

        @Override
        public int size() { return 2; }

        @Override
        public int height() { return 1; }

        @Override
        public int verifyBalance() {
            return 1;
        }

        @Override
        public NodeType getEnumeration() {
            return NodeType.SEED2;
        }

    }

    private class Seed3 implements Node<E> {
        private E leftData;
        private E middleData;
        private E rightData;
        Seed3(E leftData, E middleData, E rightData){
            this.leftData = leftData;
            this.middleData = middleData;
            this.rightData = rightData;
        }

        @Override
        public Node add(E toAdd) {
            return new Root1(middleData, new Leaf1(leftData), new Leaf1(rightData)).add(toAdd);
        }

        @Override
        public Node remove(E toRemove) {
            if(comparator.compare(leftData,toRemove) == 0){
                return new Seed2(middleData, rightData);
            }
            if(comparator.compare(middleData, toRemove) == 0){
                return new Seed2(leftData, rightData);
            }
            if(comparator.compare(rightData, toRemove) == 0){
                return new Seed2(leftData, middleData);
            }
            return this;
        }

        @Override
        public boolean contains(E toFind) {
            return comparator.compare(leftData, toFind) == 0 || comparator.compare(middleData, toFind) == 0 || comparator.compare(rightData, toFind) == 0;
        }

        @Override
        public void displayInOrder() {
            System.out.print(leftData.toString() + " " + middleData.toString() + " " + rightData.toString());
        }

        @Override
        public int count() {
            return 1;
        }

        @Override
        public int size() { return 3; }

        @Override
        public int height() { return 1; }

        @Override
        public int verifyBalance() {
            return 1;
        }

        @Override
        public NodeType getEnumeration() { return NodeType.SEED3; }
    }

    private class Root1 implements Node<E> {
        private E middleData;
        private Node leftChild;
        private Node rightChild;
        Root1(E middleData, Node leftChild, Node rightChild){
            this.middleData = middleData;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        @Override
        public Node add(E toAdd) {
            Node returnNode;
            ReturnNode cast;
            int compare = comparator.compare(middleData, toAdd);
            if(compare < 0){
                returnNode = rightChild.add(toAdd);
                if(returnNode.getEnumeration() == NodeType.RETURN){
                    cast = (ReturnNode) returnNode;
                    return new Root2(middleData, cast.pushUpData, leftChild, cast.leftChild, cast.rightChild).add(toAdd);
                }
                rightChild = returnNode;
                return this;
            }
            if(compare > 0){
                returnNode = leftChild.add(toAdd);
                if(returnNode.getEnumeration() == NodeType.RETURN){
                    cast = (ReturnNode) returnNode;
                    return new Root2(cast.pushUpData, middleData, cast.leftChild, cast.rightChild, rightChild).add(toAdd);
                }
                leftChild = returnNode;
                return this;
            }
            return this;
        }

        @Override
        public Node remove(E toRemove) {
            return null;
        }

        @Override
        public boolean contains(E toFind) {
            int compare = comparator.compare(middleData, toFind);
            if(compare < 0){
                return rightChild.contains(toFind);
            }
            if(compare > 0){
                return leftChild.contains(toFind);
            }
            return true;
        }

        @Override
        public void displayInOrder() {
            leftChild.displayInOrder();
            System.out.print(middleData.toString() + " ");
            rightChild.displayInOrder();
        }

        @Override
        public int count() {
            return 1 + leftChild.count() + rightChild.count();
        }

        @Override
        public int size() { return 1 + leftChild.size() + rightChild.size();}

        @Override
        public int height() { return 1 + leftChild.height();}

        @Override
        public int verifyBalance() {
            int left = leftChild.verifyBalance();
            int right = rightChild.verifyBalance();
            if(right == left){
                return ++left;
            }
            return 0;
        }

        @Override
        public NodeType getEnumeration() {
            return NodeType.ROOT1;
        }
    }

    private class Root2 implements Node<E> {
        private E leftData;
        private E rightData;
        private Node leftChild;
        private Node middleChild;
        private Node rightChild;
        Root2(E leftData, E rightData, Node leftChild, Node middleChild, Node rightChild){
            this.leftData = leftData;
            this.rightData = rightData;
            this.leftChild = leftChild;
            this.middleChild = middleChild;
            this.rightChild = rightChild;
        }

        @Override
        public Node add(E toAdd) {
            Node returnNode;
            ReturnNode cast;
            int compare = comparator.compare(this.rightData, toAdd);
            if(compare < 0) {
                returnNode = rightChild.add(toAdd);
                if (returnNode.getEnumeration() == NodeType.RETURN) {
                    cast = (ReturnNode) returnNode;
                    return new Root3(leftData, rightData, cast.pushUpData, leftChild, middleChild, cast.leftChild, cast.rightChild).add(toAdd);
                }
                rightChild = returnNode;
                return this;
            }
            if(compare == 0) {
                return this;
            }
            compare = comparator.compare(this.leftData, toAdd);
            if(compare < 0){
                returnNode = middleChild.add(toAdd);
                if(returnNode.getEnumeration() == NodeType.RETURN){
                    cast = (ReturnNode) returnNode;
                    return new Root3(leftData, cast.pushUpData, rightData, leftChild, cast.leftChild, cast.rightChild, rightChild).add(toAdd);
                }
                middleChild = returnNode;
                return this;
            }
            if(compare > 0){
                returnNode = leftChild.add(toAdd);
                if(returnNode.getEnumeration() == NodeType.RETURN){
                    cast = (ReturnNode) returnNode;
                    return new Root3(cast.pushUpData, leftData, rightData, cast.leftChild, cast.rightChild, middleChild, rightChild).add(toAdd);
                }
                leftChild = returnNode;
                return this;
            }
            return this;
        }

        @Override
        public Node remove(E toRemove) {
            return null;
        }


        @Override
        public boolean contains(E toFind) {
            int compareLeft = comparator.compare(leftData, toFind);
            int compareRight = comparator.compare(rightData, toFind);
            if(compareLeft == 0 || compareRight == 0){
                return true;
            }
            if(compareLeft < 0)
                return leftChild.contains(toFind);
            if(compareRight < 0)
                return middleChild.contains(toFind);
            return rightChild.contains(toFind);
        }

        @Override
        public void displayInOrder() {
            leftChild.displayInOrder();
            System.out.print(leftData.toString() + " ");
            middleChild.displayInOrder();
            System.out.print(rightData.toString() + " ");
            rightChild.displayInOrder();
        }

        @Override
        public int count() {
            return 1 + leftChild.count() + middleChild.count() + rightChild.count();
        }


        @Override
        public int size() { return 2 + leftChild.size() + middleChild.size() + rightChild.size();}

        @Override
        public int height() { return 1 + leftChild.height();}

        @Override
        public int verifyBalance() {
            int left = leftChild.verifyBalance();
            int middle = middleChild.verifyBalance();
            int right = rightChild.verifyBalance();
            if(right == left && left == middle){
                return ++left;
            }
            return 0;
        }

        @Override
        public NodeType getEnumeration() {
            return NodeType.ROOT2;
        }
    }

    private class Root3 implements Node<E> {
        private E leftData;
        private E middleData;
        private E rightData;
        private Node leftChild;
        private Node leftMiddleChild;
        private Node rightMiddleChild;
        private Node rightChild;
        Root3(E leftData, E middleData, E rightData, Node leftChild, Node leftMiddleChild, Node
                rightMiddleChild, Node rightChild){
            this.leftData = leftData;
            this.middleData = middleData;
            this.rightData = rightData;
            this.leftChild = leftChild;
            this.leftMiddleChild = leftMiddleChild;
            this.rightMiddleChild = rightMiddleChild;
            this.rightChild = rightChild;
        }

        @Override
        public Node add(E toAdd) {
            return new Root1(middleData, new Branch1(leftData, leftChild, leftMiddleChild),
                    new Branch1(rightData, rightMiddleChild, rightChild)).add(toAdd);
        }

        @Override
        public Node remove(E toRemove) {
            return null;
        }

        @Override
        public boolean contains(E toFind) {
            int compareLeft = comparator.compare(leftData, toFind);
            int compareMiddle = comparator.compare(middleData, toFind);
            int compareRight = comparator.compare(rightData, toFind);
            if(compareLeft == 0 || compareMiddle == 0 || compareRight == 0)
                return true;
            if(compareLeft < 0)
                return leftChild.contains(toFind);
            if(compareMiddle < 0)
                return leftMiddleChild.contains(toFind);
            if(compareRight < 0)
                return rightMiddleChild.contains(toFind);
            return rightChild.contains(toFind);
        }

        @Override
        public void displayInOrder() {
            leftChild.displayInOrder();
            System.out.print(leftData.toString() + " ");
            leftMiddleChild.displayInOrder();
            System.out.print(middleData.toString() + " ");
            rightMiddleChild.displayInOrder();
            System.out.print(rightData.toString() + " ");
            rightChild.displayInOrder();
        }

        @Override
        public int count() {
            return 1 + leftChild.count() + leftMiddleChild.count() + rightMiddleChild.count() + rightChild.count();
        }

        @Override
        public int size() {
            return 3 + leftChild.size() + leftMiddleChild.size() +  rightMiddleChild.size() + rightChild.size();}

        @Override
        public int height() { return 1 + leftChild.height();}

        @Override
        public int verifyBalance() {
            int left = leftChild.verifyBalance();
            int leftMiddle = leftMiddleChild.verifyBalance();
            int rightMiddle = rightMiddleChild.verifyBalance();
            int right = rightChild.verifyBalance();
            if(left == leftMiddle && leftMiddle == rightMiddle && rightMiddle == right){
                return ++left;
            }
            return 0;
        }

        @Override
        public NodeType getEnumeration() {
            return NodeType.ROOT3;
        }
    }

    private class Branch1 implements Node<E> {
        private E middleData;
        private Node leftChild;
        private Node rightChild;
        Branch1(E middleData, Node leftChild, Node rightChild){
            this.middleData = middleData;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        @Override
        public Node add(E toAdd) {
            Node returnNode;
            ReturnNode cast;
            int compare = comparator.compare(middleData, toAdd);
            if(compare < 0){
                returnNode = rightChild.add(toAdd);
                if(returnNode.getEnumeration() == NodeType.RETURN){
                    cast = (ReturnNode) returnNode;
                    return new Branch2(middleData, cast.pushUpData, leftChild, cast.leftChild, cast.rightChild).add(toAdd);
                }
                rightChild = returnNode;
                return this;
            }
            if(compare > 0){
                returnNode = leftChild.add(toAdd);
                if(returnNode.getEnumeration() == NodeType.RETURN){
                    cast = (ReturnNode) returnNode;
                    return new Branch2(cast.pushUpData, middleData, cast.leftChild, cast.rightChild, rightChild).add(toAdd);
                }
                leftChild = returnNode;
                return this;
            }
            return this;
        }

        @Override
        public Node remove(E toRemove) {
            return null;
        }

        @Override
        public boolean contains(E toFind) {
            int compare = comparator.compare(middleData, toFind);
            if(compare < 0){
                return rightChild.contains(toFind);
            }
            if(compare > 0){
                return leftChild.contains(toFind);
            }
            return true;
        }

        @Override
        public void displayInOrder() {
            leftChild.displayInOrder();
            System.out.print(middleData.toString() + " ");
            rightChild.displayInOrder();
        }

        @Override
        public int count() {
            return 1 + leftChild.count() + rightChild.count();
        }

        @Override
        public int size() { return 1 + leftChild.size() + rightChild.size();}

        @Override
        public int height() { return 1 + leftChild.height();}

        @Override
        public int verifyBalance() {
            int left = leftChild.verifyBalance();
            int right = rightChild.verifyBalance();
            if(right == left){
                return ++left;
            }
            return 0;
        }

        @Override
        public NodeType getEnumeration() {
            return NodeType.BRANCH1;
        }
    }

    private class Branch2 implements Node<E> {
        private E leftData;
        private E rightData;
        private Node leftChild;
        private Node middleChild;
        private Node rightChild;
        Branch2(E leftData, E rightData, Node leftChild, Node middleChild, Node rightChild){
            this.leftData = leftData;
            this.rightData = rightData;
            this.leftChild = leftChild;
            this.middleChild = middleChild;
            this.rightChild = rightChild;
        }

        @Override
        public Node add(E toAdd) {
            Node returnNode;
            ReturnNode cast;
            int compare = comparator.compare(this.rightData, toAdd);
            if(compare < 0) {
                returnNode = rightChild.add(toAdd);
                if (returnNode.getEnumeration() == NodeType.RETURN) {
                    cast = (ReturnNode) returnNode;
                    return new Branch3(leftData, rightData, cast.pushUpData, leftChild, middleChild, cast.leftChild, cast.rightChild).add(toAdd);
                }
                rightChild = returnNode;
                return this;
            }
            if(compare == 0) { return this; }
            compare = comparator.compare(this.leftData, toAdd);
            if(compare < 0){
                returnNode = middleChild.add(toAdd);
                if(returnNode.getEnumeration() == NodeType.RETURN){
                    cast = (ReturnNode) returnNode;
                    return new Branch3(leftData, cast.pushUpData, rightData, leftChild, cast.leftChild, cast.rightChild, rightChild).add(toAdd);
                }
                middleChild = returnNode;
                return this;
            }
            if(compare > 0){
                returnNode = leftChild.add(toAdd);
                if(returnNode.getEnumeration() == NodeType.RETURN){
                    cast = (ReturnNode) returnNode;
                    return new Branch3(cast.pushUpData, leftData, rightData, cast.leftChild, cast.rightChild, middleChild, rightChild).add(toAdd);
                }
                leftChild = returnNode;
                return this;
            }
            return this;
        }

        @Override
        public Node remove(E toRemove) {
            return null;
        }

        @Override
        public boolean contains(E toFind) {
            int compareLeft = comparator.compare(leftData, toFind);
            int compareRight = comparator.compare(rightData, toFind);
            if(compareLeft == 0 || compareRight == 0){
                return true;
            }
            if(compareLeft < 0)
                return leftChild.contains(toFind);
            if(compareRight < 0)
                return middleChild.contains(toFind);
            return rightChild.contains(toFind);
        }

        @Override
        public void displayInOrder() {
            leftChild.displayInOrder();
            System.out.print(leftData.toString() + " ");
            middleChild.displayInOrder();
            System.out.print(rightData.toString() + " ");
            rightChild.displayInOrder();
        }

        @Override
        public int count() {
            return 1 + leftChild.count() + middleChild.count() + rightChild.count();
        }

        @Override
        public int size() { return 2 + leftChild.size() + middleChild.size() + rightChild.size();}

        @Override
        public int height() { return 1 + leftChild.height();}

        @Override
        public int verifyBalance() {
            int left = leftChild.verifyBalance();
            int middle = middleChild.verifyBalance();
            int right = rightChild.verifyBalance();
            if(right == left && left == middle){
                return ++left;
            }
            return 0;
        }

        @Override
        public NodeType getEnumeration() {
            return NodeType.BRANCH2;
        }
    }

    private class Branch3 implements Node<E> {
        private E leftData;
        private E middleData;
        private E rightData;
        private Node leftChild;
        private Node leftMiddleChild;
        private Node rightMiddleChild;
        private Node rightChild;
        Branch3(E leftData, E middleData, E rightData, Node leftChild, Node leftMiddleChild, Node
                rightMiddleChild, Node rightChild){
            this.leftData = leftData;
            this.middleData = middleData;
            this.rightData = rightData;
            this.leftChild = leftChild;
            this.leftMiddleChild = leftMiddleChild;
            this.rightMiddleChild = rightMiddleChild;
            this.rightChild = rightChild;
        }

        @Override
        public Node add(E toAdd) {
            return new ReturnNode(middleData, new Branch1(leftData, leftChild, leftMiddleChild),
                    new Branch1(rightData, rightMiddleChild, rightChild));
        }

        @Override
        public Node remove(E toRemove) {
            return null;
        }

        @Override
        public boolean contains(E toFind) {
            int compareLeft = comparator.compare(leftData, toFind);
            int compareMiddle = comparator.compare(middleData, toFind);
            int compareRight = comparator.compare(rightData, toFind);
            if(compareLeft == 0 || compareMiddle == 0 || compareRight == 0)
                return true;
            if(compareLeft < 0)
                return leftChild.contains(toFind);
            if(compareMiddle < 0)
                return leftMiddleChild.contains(toFind);
            if(compareRight < 0)
                return rightMiddleChild.contains(toFind);
            return rightChild.contains(toFind);
        }

        @Override
        public void displayInOrder() {
            leftChild.displayInOrder();
            System.out.print(leftData + " ");
            leftMiddleChild.displayInOrder();
            System.out.print(middleData + " ");
            rightMiddleChild.displayInOrder();
            System.out.print(rightData + " ");
            rightChild.displayInOrder();
        }

        @Override
        public int count() {
            return 1 + leftChild.count() + leftMiddleChild.count() + rightMiddleChild.count() + rightChild.count();
        }

        @Override
        public int size() { return 3 + leftChild.size() + leftMiddleChild.size() +  rightMiddleChild.size() + rightChild.size();}

        @Override
        public int height() { return 1 + leftChild.height();}

        @Override
        public int verifyBalance() {
            int left = leftChild.verifyBalance();
            int leftMiddle = leftMiddleChild.verifyBalance();
            int rightMiddle = rightMiddleChild.verifyBalance();
            int right = rightChild.verifyBalance();
            if(left == leftMiddle && leftMiddle == rightMiddle && rightMiddle == right){
                return ++left;
            }
            return 0;
        }

        @Override
        public NodeType getEnumeration() {
            return NodeType.BRANCH3;
        }
    }

    private class Leaf1 implements Node<E> {
        private E middleData;
        Leaf1(E middleData){
            this.middleData = middleData;
        }

        @Override
        public Node add(E toAdd) {
            int compare = comparator.compare(middleData, toAdd);
            if(compare < 0) { return new Leaf2(middleData, toAdd);}
            else if(compare > 0) { return new Leaf2(toAdd, middleData); }
            return this;
        }

        @Override
        public Node remove(E toRemove) {
            if(0 == comparator.compare(middleData, toRemove)){
                return new ReturnNode(null, null, null);
            }
            return this;
        }

        @Override
        public boolean contains(E toFind) {
            return comparator.compare(middleData, toFind) == 0;
        }

        @Override
        public void displayInOrder() {
            System.out.print(middleData.toString() + " ");
        }

        @Override
        public int count() {
            return 1;
        }

        @Override
        public int size() { return 1; }

        @Override
        public int height() { return 1; }

        @Override
        public int verifyBalance() {
            return 1;
        }

        @Override
        public NodeType getEnumeration() {
            return NodeType.LEAF1;
        }
    }

    private class Leaf2 implements Node<E> {
        private E leftData;
        private E rightData;
        Leaf2(E leftData, E rightData){
            this.leftData = leftData;
            this.rightData = rightData;
        }

        @Override
        public Node add(E toAdd) {
            int compare = comparator.compare(this.rightData, toAdd);
            if(compare < 0){ return new Leaf3(leftData, rightData, toAdd); }
            if(compare == 0) { return this; }
            compare = comparator.compare(this.leftData, toAdd);
            if(compare < 0){ return new Leaf3(leftData, toAdd, rightData); }
            if(compare > 0){ return new Leaf3(toAdd, leftData, rightData); }
            return this;
        }

        @Override
        public Node remove(E toRemove) {
            if(comparator.compare(leftData, toRemove) == 0){
                return new Leaf1(rightData);
            }
            if(comparator.compare(rightData, toRemove) == 0){
                return new Leaf1(leftData);
            }
            return this;
        }

        @Override
        public boolean contains(E toFind) {
            return comparator.compare(leftData, toFind) == 0 || comparator.compare(rightData, toFind) == 0;
        }

        @Override
        public void displayInOrder() {
            System.out.print(leftData + " " + rightData + " ");
        }

        @Override
        public int count() {
            return 1;
        }

        @Override
        public int size() { return 2; }

        @Override
        public int height() { return 1; }

        @Override
        public int verifyBalance() {
            return 1;
        }

        @Override
        public NodeType getEnumeration() {
            return NodeType.LEAF2;
        }
    }

    private class Leaf3 implements Node<E> {
        private E leftData;
        private E middleData;
        private E rightData;
        Leaf3(E leftData, E middleData, E rightData){
            this.leftData = leftData;
            this.middleData = middleData;
            this.rightData = rightData;
        }

        @Override
        public Node add(E toAdd) {
            return new ReturnNode(middleData, new Leaf1(leftData), new Leaf1(rightData));
        }

        @Override
        public Node remove(E toRemove) {
            if(comparator.compare(leftData,toRemove) == 0){
                return new Leaf2(middleData, rightData);
            }
            if(comparator.compare(middleData, toRemove) == 0){
                return new Leaf2(leftData, rightData);
            }
            if(comparator.compare(rightData, toRemove) == 0){
                return new Leaf2(leftData, middleData);
            }
            return this;
        }

        @Override
        public boolean contains(E toFind) {
            return comparator.compare(leftData, toFind) == 0 || comparator.compare(middleData, toFind) == 0 || comparator.compare(rightData, toFind) == 0;
        }

        @Override
        public void displayInOrder() {
            System.out.print(leftData.toString() + " " + middleData.toString() + " " + rightData.toString() + " ");
        }

        @Override
        public int count() {
            return 1;
        }

        @Override
        public int size() { return 3; }

        @Override
        public int height() { return 1; }

        @Override
        public int verifyBalance() {
            return 1;
        }

        @Override
        public NodeType getEnumeration() {
            return NodeType.LEAF3;
        }
    }

}


