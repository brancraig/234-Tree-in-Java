import java.io.*;

public class Tree234 {
    private static Tree234 tree234 = new Tree234();
    public static void main(String [] args){
        String filename = "/usr/share/dict/american-english";
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
                        tree234.insertSubstrings(temp);
                    }
                }while (temp != null);
                milliSeconds = System.currentTimeMillis() - milliSeconds;
                seconds = ((double)milliSeconds)/((double)1000);
            } catch(IOException e){
                System.err.println("**Exception " + e);
            }


            System.out.println(seconds + " seconds to load");

        }
        System.out.println(tree234.count() + " entries added.");
        System.out.println(tree234.size() + " nodes created.");
        System.out.println(tree234.height() + " levels deep.");
        if(tree234.verifyBalance()){
            System.out.println("The tree is balanced.");
        } else {
            System.out.println("The tree is not balanced.");
        }


        System.exit(0);
    }
    private Node root;
    private Tree234(){

        root = new RootLeaf1();
    }
    public void add(String toAdd){
        root = root.add(toAdd);
    }


    public void displayInOrder(){
        root.displayInOrder();
        System.out.println();
    }

    public int count(){
        return root.count();
    }

    public int size(){ return root.size();}

    public int height() { return root.height();}

    public boolean verifyBalance(){

        return 0 != root.verifyBalance();
    }

    public boolean insertSubstrings(String string){
        return !string.trim().isEmpty() && insertSubstrings(string.toLowerCase(), string.length(),1);
    }

    private boolean insertSubstrings(String string, int stringLength, int substringSize){
        if(substringSize == stringLength){
            tree234.add(string);
            return true;
        }else{
            for(int i = 0, j = substringSize + 1; j <= stringLength; ++i, ++j){
                tree234.add(string.substring(i,j));
            }
            return insertSubstrings(string, stringLength, ++substringSize);
        }
    }

    private enum NodeType {
        ROOTLEAF1, ROOTLEAF2, ROOTLEAF3, ROOT1, ROOT2, ROOT3, LEAF1, LEAF2, LEAF3, BRANCH1, BRANCH2, BRANCH3, RETURN
    }

    private interface Node {
        Node add(String toAdd);

        Node remove(String toRemove);

        boolean contains(String toFind);

        NodeType getEnumeration();

        int count();

        int size();

        int height();

        int verifyBalance();

        void displayInOrder();


    }

    private class ReturnNode implements Node {
        String toAdd;
        String pushUpData;
        Node leftChild;
        Node rightChild;
        ReturnNode(){}
        ReturnNode(String pushUpData, Node leftChild, Node rightChild){
            this.pushUpData = pushUpData;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        @Override
        public Node add(String toAdd) {
            return this;
        }


        @Override
        public Node remove(String toRemove) {
            if(pushUpData == null){
                return this;
            }
            if(pushUpData.compareTo(toRemove) == 0){
                return null;
            }
            return this;
        }
        @Override
        public boolean contains(String toFind) {
            return pushUpData.matches(toFind);}

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

    private class RootLeaf1 implements Node {
        private String middleData;
        RootLeaf1(){
            middleData = null;
        }
        RootLeaf1(String string){
            middleData = string;
        }

        @Override
        public Node add(String toAdd) {
            if(middleData == null){
                middleData = toAdd;
                return this;
            }
            int compare = middleData.compareTo(toAdd);
            if(compare < 0) { return new RootLeaf2(middleData, toAdd);}
            else if(compare > 0) { return new RootLeaf2(toAdd, middleData); }
            return this;
        }

        @Override
        public Node remove(String toRemove) {
            if(middleData == null){
                return this;
            }
            if(middleData.compareTo(toRemove) == 0){
                return new RootLeaf1();
            }
            return this;
        }

        @Override
        public boolean contains(String toFind) {
            return middleData != null && middleData.compareTo(toFind) == 0;
        }

        @Override
        public void displayInOrder() {
            System.out.print(middleData);
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
        public NodeType getEnumeration() { return NodeType.ROOTLEAF1; }


    }

    private class RootLeaf2 implements Node{
        private String leftData;
        private String rightData;
        RootLeaf2(String leftData, String rightData){
            this.leftData = leftData;
            this.rightData = rightData;
        }

        @Override
        public Node add(String toAdd) {
            int compare = this.rightData.compareTo(toAdd);
            if(compare < 0){ return new RootLeaf3(leftData, rightData, toAdd); }
            if(compare == 0) { return this; }
            compare = this.leftData.compareTo(toAdd);
            if(compare < 0){ return new RootLeaf3(leftData, toAdd, rightData); }
            if(compare > 0){ return new RootLeaf3(toAdd, leftData, rightData); }
            return this;
        }

        @Override
        public Node remove(String toRemove) {
            if(leftData.compareTo(toRemove) == 0){
                return new RootLeaf1(rightData);
            }
            if(rightData.compareTo(toRemove) == 0){
                return new RootLeaf1(leftData);
            }
            return this;
        }
        @Override
        public boolean contains(String toFind) {
            return rightData.compareTo(toFind) == 0 || leftData.compareTo(toFind) == 0;
        }

        @Override
        public void displayInOrder() {
            System.out.print(leftData + " " + rightData);
        }

        @Override
        public int count() {
            return 2;
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
            return NodeType.ROOTLEAF2;
        }

    }

    private class RootLeaf3 implements Node {
        private String leftData;
        private String middleData;
        private String rightData;
        RootLeaf3(){

        }
        RootLeaf3(String leftData, String middleData, String rightData){
            this.leftData = leftData;
            this.middleData = middleData;
            this.rightData = rightData;
        }

        @Override
        public Node add(String toAdd) {
            return new Root1(middleData, new Leaf1(leftData), new Leaf1(rightData)).add(toAdd);
        }

        @Override
        public Node remove(String toRemove) {
            if(leftData.compareTo(toRemove) == 0){
                return new RootLeaf2(middleData, rightData);
            }
            if(middleData.compareTo(toRemove) == 0){
                return new RootLeaf2(leftData, rightData);
            }
            if(rightData.compareTo(toRemove) == 0){
                return new RootLeaf2(leftData, middleData);
            }
            return this;
        }

        @Override
        public boolean contains(String toFind) {
            return leftData.compareTo(toFind) == 0 || middleData.compareTo(toFind) == 0 || rightData.compareTo(toFind) == 0;
        }

        @Override
        public void displayInOrder() {
            System.out.print(leftData + " " + middleData + " " + rightData);
        }

        @Override
        public int count() {
            return 3;
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
        public NodeType getEnumeration() { return NodeType.ROOTLEAF3; }
    }

    private class Root1 implements Node {
        private String middleData;
        private Node leftChild;
        private Node rightChild;
        Root1(){}
        Root1(String middleData, Node leftChild, Node rightChild){
            this.middleData = middleData;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        @Override
        public Node add(String toAdd) {
            Node returnNode;
            ReturnNode cast;
            int compare = middleData.compareTo(toAdd);
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
        public Node remove(String toRemove) {
            int compare = middleData.compareTo(toRemove);
            Node returnNode;
            /*if(compare < 0){
                returnNode = rightChild.remove(toRemove);
                if(returnNode.getEnumeration() == NodeType.return)
            }*/
            return null;
        }

        @Override
        public boolean contains(String toFind) {
            int compare = middleData.compareTo(toFind);
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
            System.out.print(middleData + " ");
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

    private class Root2 implements Node {
        private String leftData;
        private String rightData;
        private Node leftChild;
        private Node middleChild;
        private Node rightChild;
        Root2(){}
        Root2(String leftData, String rightData, Node leftChild, Node middleChild, Node rightChild){
            this.leftData = leftData;
            this.rightData = rightData;
            this.leftChild = leftChild;
            this.middleChild = middleChild;
            this.rightChild = rightChild;
        }

        @Override
        public Node add(String toAdd) {
            Node returnNode;
            ReturnNode cast;
            int compare = this.rightData.compareTo(toAdd);
            if(compare < 0) {
                returnNode = rightChild.add(toAdd);
                if (returnNode.getEnumeration() == NodeType.RETURN) {
                    cast = (ReturnNode) returnNode;
                    return new Root3(leftData, rightData, cast.pushUpData, leftChild, middleChild, cast.leftChild, cast.rightChild).add(toAdd);
                }
                rightChild = returnNode;
                return this;
            }
            if(compare == 0) { return this; }
            compare = this.leftData.compareTo(toAdd);
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
        public Node remove(String toRemove) {
            return null;
        }


        @Override
        public boolean contains(String toFind) {
            int compareLeft = leftData.compareTo(toFind);
            int compareRight = rightData.compareTo(toFind);
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
            System.out.print(leftData + " ");
            middleChild.displayInOrder();
            System.out.print(rightData + " ");
            rightChild.displayInOrder();
        }

        @Override
        public int count() {
            return 2 + leftChild.count() + middleChild.count() + rightChild.count();
        }


        @Override
        public int size() { return 1 + leftChild.size() + middleChild.size() + rightChild.size();}

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

    private class Root3 implements Node {
        private String leftData;
        private String middleData;
        private String rightData;
        private Node leftChild;
        private Node leftMiddleChild;
        private Node rightMiddleChild;
        private Node rightChild;
        Root3(){}
        Root3(String leftData, String middleData, String rightData, Node leftChild, Node leftMiddleChild, Node
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
        public Node add(String toAdd) {
            return new Root1(middleData, new Branch1(leftData, leftChild, leftMiddleChild),
                    new Branch1(rightData, rightMiddleChild, rightChild)).add(toAdd);
        }

        @Override
        public Node remove(String toRemove) {
            return null;
        }

        @Override
        public boolean contains(String toFind) {
            int compareLeft = leftData.compareTo(toFind);
            int compareMiddle = middleData.compareTo(toFind);
            int compareRight = rightData.compareTo(toFind);
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
            return 3 + leftChild.count() + leftMiddleChild.count() + rightMiddleChild.count() + rightChild.count();
        }

        @Override
        public int size() {
            return 1 + leftChild.size() + leftMiddleChild.size() +  rightMiddleChild.size() + rightChild.size();}

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

    private class Branch1 implements Node {
        private String middleData;
        private Node leftChild;
        private Node rightChild;
        Branch1(){}
        Branch1(String middleData, Node leftChild, Node rightChild){
            this.middleData = middleData;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        @Override
        public Node add(String toAdd) {
            Node returnNode;
            ReturnNode cast;
            int compare = middleData.compareTo(toAdd);
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
        public Node remove(String toRemove) {
            return null;
        }

        @Override
        public boolean contains(String toFind) {
            int compare = middleData.compareTo(toFind);
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
            System.out.print(middleData + " ");
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

    private class Branch2 implements Node {
        private String leftData;
        private String rightData;
        private Node leftChild;
        private Node middleChild;
        private Node rightChild;
        Branch2(){}
        Branch2(String leftData, String rightData, Node leftChild, Node middleChild, Node rightChild){
            this.leftData = leftData;
            this.rightData = rightData;
            this.leftChild = leftChild;
            this.middleChild = middleChild;
            this.rightChild = rightChild;
        }

        @Override
        public Node add(String toAdd) {
            Node returnNode;
            ReturnNode cast;
            int compare = this.rightData.compareTo(toAdd);
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
            compare = this.leftData.compareTo(toAdd);
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
        public Node remove(String toRemove) {
            return null;
        }

        @Override
        public boolean contains(String toFind) {
            int compareLeft = leftData.compareTo(toFind);
            int compareRight = rightData.compareTo(toFind);
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
            System.out.print(leftData + " ");
            middleChild.displayInOrder();
            System.out.print(rightData + " ");
            rightChild.displayInOrder();
        }

        @Override
        public int count() {
            return 2 + leftChild.count() + middleChild.count() + rightChild.count();
        }

        @Override
        public int size() { return 1 + leftChild.size() + middleChild.size() + rightChild.size();}

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

    private class Branch3 implements Node {
        private String leftData;
        private String middleData;
        private String rightData;
        private Node leftChild;
        private Node leftMiddleChild;
        private Node rightMiddleChild;
        private Node rightChild;
        Branch3(){}
        Branch3(String leftData, String middleData, String rightData, Node leftChild, Node leftMiddleChild, Node
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
        public Node add(String toAdd) {
            return new ReturnNode(middleData, new Branch1(leftData, leftChild, leftMiddleChild),
                    new Branch1(rightData, rightMiddleChild, rightChild));
        }

        @Override
        public Node remove(String toRemove) {
            return null;
        }

        @Override
        public boolean contains(String toFind) {
            int compareLeft = leftData.compareTo(toFind);
            int compareMiddle = middleData.compareTo(toFind);
            int compareRight = rightData.compareTo(toFind);
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
            return 3 + leftChild.count() + leftMiddleChild.count() + rightMiddleChild.count() + rightChild.count();
        }

        @Override
        public int size() { return 1 + leftChild.size() + leftMiddleChild.size() +  rightMiddleChild.size() + rightChild.size();}

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

    private class Leaf1 implements Node {
        private String middleData;
        Leaf1(){}
        Leaf1(String middleData){
            this.middleData = middleData;
        }

        @Override
        public Node add(String toAdd) {
            int compare = middleData.compareTo(toAdd);
            if(compare < 0) { return new Leaf2(middleData, toAdd);}
            else if(compare > 0) { return new Leaf2(toAdd, middleData); }
            return this;
        }

        @Override
        public Node remove(String toRemove) {
            return null;
        }

        @Override
        public boolean contains(String toFind) {
            return middleData.compareTo(toFind) == 0;
        }

        @Override
        public void displayInOrder() {
            System.out.print(middleData + " ");
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

    private class Leaf2 implements Node {
        private String leftData;
        private String rightData;

        Leaf2(){}
        Leaf2(String leftData, String rightData){
            this.leftData = leftData;
            this.rightData = rightData;
        }

        @Override
        public Node add(String toAdd) {
            int compare = this.rightData.compareTo(toAdd);
            if(compare < 0){ return new Leaf3(leftData, rightData, toAdd); }
            if(compare == 0) { return this; }
            compare = this.leftData.compareTo(toAdd);
            if(compare < 0){ return new Leaf3(leftData, toAdd, rightData); }
            if(compare > 0){ return new Leaf3(toAdd, leftData, rightData); }
            return this;
        }

        @Override
        public Node remove(String toRemove) {
            return null;
        }

        @Override
        public boolean contains(String toFind) {
            return leftData.compareTo(toFind) == 0 || rightData.compareTo(toFind) == 0;
        }

        @Override
        public void displayInOrder() {
            System.out.print(leftData + " " + rightData + " ");
        }

        @Override
        public int count() {
            return 2;
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
            return NodeType.LEAF2;
        }
    }

    private class Leaf3 implements Node {
        private String leftData;
        private String middleData;
        private String rightData;
        Leaf3(){}
        Leaf3(String leftData, String middleData, String rightData){
            this.leftData = leftData;
            this.middleData = middleData;
            this.rightData = rightData;
        }

        @Override
        public Node add(String toAdd) {
            return new ReturnNode(middleData, new Leaf1(leftData), new Leaf1(rightData));
        }

        @Override
        public Node remove(String toRemove) {
            return null;
        }

        @Override
        public boolean contains(String toFind) {
            return leftData.compareTo(toFind) == 0 || middleData.compareTo(toFind) == 0 || rightData.compareTo(toFind) == 0;
        }

        @Override
        public void displayInOrder() {
            System.out.print(leftData + " " + middleData + " " + rightData + " ");
        }

        @Override
        public int count() {
            return 3;
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
            return NodeType.LEAF3;
        }
    }

}


