/**
 * Created by nicho_000 on 4/17/2016.
 */
public class NickTester
{
    public static void main(String[] args)
    {
        BPlusTree tree = new BPlusTree();
        tree.insertWord("Cat");
        tree.insertWord("Bird");
        tree.insertWord("Apple");
        tree.insertWord("Apple");
        tree.insertWord("Fish");

        BPlusTree.BTNode root = tree.getRoot();
        tree.printNode(root);
    }
}
