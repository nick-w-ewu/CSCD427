import java.util.ArrayList;

/**
 * Created by nicho_000 on 4/16/2016.
 */
public class BPlusTree
{
    private BTNode root;
    private int order = 3;
    private int nextNodeId = 1;

    public class BTNode
    {
        ArrayList<BTElement> elements;
        boolean leaf;
        ArrayList<Integer> frequencies;
        BTNode farRightChild;
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
            BTNode curr = this.root;
            while(!curr.leaf)
            {
                for(int i = 0; i < curr.elements.size()-1; i++)
                {
                    if(curr.elements.get(i).keyword.compareToIgnoreCase(s) < 0)
                    {
                        curr = curr.elements.get(i).leftChild;
                        break;
                    }
                }
            }
            if(!curr.isFull() || curr.containsKey(s))
            {
                curr.insertKeywordLeaf(s);
            }
            else if(curr.isFull() && !curr.parent.isFull())
            {
                this.root.insertKeywordLeaf(s);
                BTNode[] split = curr.splitNode();
                curr.parent.insertKeyword(split[1].elements.get(0).keyword);
                split[0].parent = curr.parent;
                split[1].parent = curr.parent;
                BTElement temp1 = new BTElement(split[1].elements.get(0).keyword);
                int location = curr.parent.elements.indexOf(temp1);
                temp1 = curr.parent.elements.get(location);
                temp1.leftChild = split[0];
                if(location == curr.parent.elements.size()-1)
                {
                    curr.parent.farRightChild = split[1];
                }
                else
                {
                    BTElement temp2 = curr.parent.elements.get(location+1);
                    temp2.leftChild = split[2];
                }
            }
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
    }
}
