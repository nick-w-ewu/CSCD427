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
        tree.insertWord("Fish");
        tree.insertWord("Dog");
        tree.insertWord("Hamster");
        tree.insertWord("Banana");
        tree.insertWord("Can");
        tree.insertWord("bad");
        tree.insertWord("bat");
        tree.insertWord("Amazon");
        tree.insertWord("ape");
        tree.insertWord("add");


        BPlusTree.BTNode root = tree.getRoot();
        tree.printWords();
    }
}
