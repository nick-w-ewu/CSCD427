import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by nicho_000 on 4/16/2016.
 */
public class BPlusTree
{
    private BTNode root;
    private int order = 3;
    private int nextNodeId = 1;
    private Hashtable<Integer, BTNode> quickAccess;

    public class BTNode
    {
        ArrayList<BTElement> elements;
        boolean leaf;
        ArrayList<Integer> frequencies;
        BTNode farRightChild;
        BTNode leftLeafSibling;
        int nodeID;
        BTNode parent;

        public BTNode()
        {
            this.elements = new ArrayList<>();
            this.leaf = false;
            this.farRightChild = null;
            this.nodeID = nextNodeId++;
        }

        public boolean isFull()
        {
            if (this.elements.size() < 3)
            {
                return false;
            }
            return true;
        }

        public void insertKeywordLeaf(String s)
        {
            BTElement temp1 = new BTElement(s);
            int index = this.elements.indexOf(temp1);
            if (index != -1)
            {
                int temp = this.frequencies.get(index);
                this.frequencies.remove(temp);
                temp++;
                this.frequencies.add(index, temp);
            }
            else
            {
                if (this.elements.isEmpty())
                {
                    BTElement temp = new BTElement(s);
                    this.elements.add(0, temp);
                    this.frequencies.add(0, 1);
                }
                else
                {
                    boolean inserted = false;
                    for (int i = 0; i < this.elements.size(); i++)
                    {
                        int compare = this.elements.get(i).keyword.compareToIgnoreCase(s);
                        if (compare > 0)
                        {
                            BTElement temp = new BTElement(s);
                            this.elements.add(i, temp);
                            this.frequencies.add(i, 1);
                            inserted = true;
                            break;
                        }
                    }
                    if (!inserted)
                    {
                        BTElement temp = new BTElement(s);
                        this.elements.add(this.elements.size(), temp);
                        this.frequencies.add(this.frequencies.size(), 1);
                    }
                }
            }
        }

        public void insertKeyword(String s)
        {

            if (this.elements.isEmpty())
            {
                BTElement temp = new BTElement(s);
                this.elements.add(0, temp);
            }
            else
            {
                boolean inserted = false;
                for (int i = 0; i < this.elements.size(); i++)
                {
                    int compare = this.elements.get(i).keyword.compareToIgnoreCase(s);
                    if (compare > 0)
                    {
                        BTElement temp = new BTElement(s);
                        this.elements.add(i, temp);
                        inserted = true;
                        break;
                    }
                }
                if (!inserted)
                {
                    BTElement temp = new BTElement(s);
                    this.elements.add(this.elements.size(), temp);
                }
            }
        }

        public boolean containsKey(String s)
        {
            BTElement temp = new BTElement(s);
            return this.elements.contains(temp);
        }

        public void makeLeaf()
        {
            this.leaf = true;
            this.frequencies = new ArrayList<>();
        }

        public BTNode[] splitNode()
        {
            if (this.elements.size() == 4)
            {
                BTNode new1 = new BTNode();
                BTNode new2 = new BTNode();
                if (this.leaf)
                {
                    new1.leftLeafSibling = this.leftLeafSibling;
                    if(new1.leftLeafSibling != null)
                    {
                        new1.leftLeafSibling.farRightChild = new1;
                    }
                    new2.leftLeafSibling = new1;
                    new1.makeLeaf();
                    new2.makeLeaf();
                    new1.elements.add(this.elements.get(0));
                    new1.elements.add(this.elements.get(1));
                    new1.frequencies.add(this.frequencies.get(0));
                    new1.frequencies.add(this.frequencies.get(1));
                    new1.farRightChild = new2;

                    new2.farRightChild = this.farRightChild;
                    new2.elements.add(this.elements.get(2));
                    new2.frequencies.add(this.frequencies.get(2));
                    new2.elements.add(this.elements.get(3));
                    new2.frequencies.add(this.frequencies.get(3));
                    quickAccess.put(new1.nodeID, new1);
                    quickAccess.put(new2.nodeID, new2);
                    quickAccess.remove(this.nodeID);
                    BTNode[] temp = {new1, new2};
                    return temp;
                }
            }
            return null;
        }
    }

    public BPlusTree()
    {
        this.root = new BTNode();
        this.quickAccess = new Hashtable<>();
        this.quickAccess.put(1, this.root);
        this.root.makeLeaf();
    }

    public BTNode getRoot()
    {
        return root;
    }

    public void insertWord(String s)
    {
        if (this.root.leaf)
        {
            if (!this.root.isFull())
            {
                this.root.insertKeywordLeaf(s);
            }
            else if (this.root.containsKey(s))
            {
                this.root.insertKeywordLeaf(s);
            }
            else if (this.root.isFull())
            {
                this.root.insertKeywordLeaf(s);
                BTNode[] split = this.root.splitNode();
                BTNode newRoot = new BTNode();
                newRoot.insertKeyword(split[1].elements.get(0).keyword);
                BTElement temp = newRoot.elements.get(0);
                split[0].parent = newRoot;
                split[1].parent = newRoot;
                temp.leftChild = split[0];
                newRoot.farRightChild = split[1];
                this.root = newRoot;
            }
        }
        else
        {
            insertOnePass(s);
        }
    }

    private void insertOnePass(String s)
    {
        //split on the way down?
        if (this.root.isFull())
        {
            BTNode new1 = new BTNode();
            BTNode right = new BTNode();
            BTNode left = new BTNode();
            right.parent = new1;
            left.parent = new1;
            new1.insertKeyword(this.root.elements.get(1).keyword);
            BTElement new2 = new1.elements.get(0);
            new2.leftChild = left;
            new1.farRightChild = right;
            left.elements.add(this.root.elements.get(0));
            left.farRightChild = this.root.elements.get(1).leftChild;
            right.elements.add(this.root.elements.get(2));
            right.farRightChild = this.root.farRightChild;
            this.quickAccess.remove(this.root.nodeID);
            this.root = new1;
            this.quickAccess.put(this.root.nodeID, this.root);
            this.quickAccess.put(left.nodeID, left);
            this.quickAccess.put(right.nodeID, right);
            remapParent(left);
            remapParent(right);
        }
        BTNode curr = this.root, prev;
        boolean found = false;
        while (!curr.leaf)
        {
            prev = curr;
            for (int i = 0; i < curr.elements.size(); i++)
            {
                if (curr.elements.get(i).keyword.compareToIgnoreCase(s) > 0)
                {
                    curr = curr.elements.get(i).leftChild;
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                curr = curr.farRightChild;
            }
            found = false;
            if (prev.isFull())
            {
                BTNode right = new BTNode();
                BTNode left = new BTNode();
                right.parent = prev.parent;
                left.parent = prev.parent;
                left.elements.add(prev.elements.get(0));
                left.farRightChild = prev.elements.get(1).leftChild;
                right.elements.add(prev.elements.get(2));
                right.farRightChild = prev.farRightChild;
                prev.parent.insertKeyword(prev.elements.get(1).keyword);
                BTElement temp1 = new BTElement(prev.parent.elements.get(0).keyword);
                int location = prev.parent.elements.indexOf(temp1);
                temp1 = prev.parent.elements.get(location);
                temp1.leftChild = left;
                if (location == prev.parent.elements.size() - 1)
                {
                    curr.parent.farRightChild = right;
                }
                else
                {
                    BTElement temp2 = prev.parent.elements.get(location + 1);
                    temp2.leftChild = right;
                }
                remapParent(left);
                remapParent(right);
                this.quickAccess.put(left.nodeID, left);
                this.quickAccess.put(right.nodeID, right);
                this.quickAccess.remove(prev.nodeID);
            }
        }
        if (!curr.isFull())
        {
            curr.insertKeywordLeaf(s);
        }
        else if (curr.containsKey(s))
        {
            this.root.insertKeywordLeaf(s);
        }
        else
        {
            curr.insertKeywordLeaf(s);
            BTNode[] split = curr.splitNode();
            BTNode parent = curr.parent;
            parent.insertKeyword(split[1].elements.get(0).keyword);
            split[0].parent = parent;
            split[1].parent = parent;
            BTElement temp1 = new BTElement(split[1].elements.get(0).keyword);
            int location = curr.parent.elements.indexOf(temp1);
            temp1 = parent.elements.get(location);
            temp1.leftChild = split[0];
            if (location == curr.parent.elements.size() - 1)
            {
                curr.parent.farRightChild = split[1];
            }
            else
            {
                BTElement temp2 = curr.parent.elements.get(location + 1);
                temp2.leftChild = split[1];
            }
        }
    }

    private void remapParent(BTNode node)
    {
        BTNode temp;
        for(int i = 0; i < node.elements.size(); i++)
        {
            temp = node.elements.get(i).leftChild;
            temp.parent = node;
        }
        temp = node.farRightChild;
        temp.parent = node;
    }

    public void search(String s)
    {
        boolean found = false;
        BTNode curr = this.root;
        while (!curr.leaf)
        {
            for (int i = 0; i < curr.elements.size(); i++)
            {
                if (curr.elements.get(i).keyword.compareToIgnoreCase(s) > 0)
                {
                    curr = curr.elements.get(i).leftChild;
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                curr = curr.farRightChild;
            }
            found = false;
        }
        for(int i = 0; i < curr.elements.size(); i++)
        {
            if(curr.elements.get(i).keyword.compareToIgnoreCase(s) == 0)
            {
                System.out.println(s + " Frequencey: " + curr.frequencies.get(i));
                return;
            }
        }
        System.out.println(s + " Not found\n");
    }

    public void rangeSearch(String s1, String s2)
    {
        boolean found = false;
        BTNode curr = this.root;
        while (!curr.leaf)
        {
            for (int i = 0; i < curr.elements.size(); i++)
            {
                if (curr.elements.get(i).keyword.compareToIgnoreCase(s1) > 0)
                {
                    curr = curr.elements.get(i).leftChild;
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                curr = curr.farRightChild;
            }
            found = false;
        }
        for(int i = 0; i < curr.elements.size(); i++)
        {
            if(curr.elements.get(i).keyword.compareToIgnoreCase(s1) >= 0)
            {
                System.out.println(curr.elements.get(i).keyword);
            }
        }
        curr = curr.farRightChild;
        boolean maxReached = false;
        while(curr != null && !maxReached)
        {
            for(int i = 0; i < curr.elements.size(); i++)
            {
                if(curr.elements.get(i).keyword.compareToIgnoreCase(s2) <= 0)
                {
                    System.out.println(curr.elements.get(i).keyword);
                }
                if(curr.elements.get(i).keyword.compareToIgnoreCase(s2) > 0)
                {
                    maxReached = true;
                    break;
                }
            }
            curr = curr.farRightChild;
        }
    }

    public void printNode(BTNode n)
    {
        if (n.leaf)
        {
            for (int i = 0; i < n.elements.size(); i++)
            {
                System.out.println(n.elements.get(i).keyword + " Frequency: " + n.frequencies.get(i));
            }
        }
        else
        {
            for (int i = 0; i < n.elements.size(); i++)
            {
                System.out.println(n.elements.get(i).keyword);
            }
        }
        System.out.println();
    }

    public BTNode getNode(int id)
    {
        return this.quickAccess.get(id);
    }

    public void printWords()
    {
        BTNode curr = this.root;
        while (!curr.leaf)
        {
            curr = curr.elements.get(0).leftChild;
        }
        while (curr != null)
        {
            System.out.println("Node " + curr.nodeID);
            for (int i = 0; i < curr.elements.size(); i++)
            {
                System.out.println(curr.elements.get(i).keyword);
            }
            curr = curr.farRightChild;
        }
    }

    public void printTreeLayout()
    {

    }
}
