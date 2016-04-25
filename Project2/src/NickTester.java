import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by nicho_000 on 4/17/2016.
 */
public class NickTester
{
    public static void main(String[] args)
    {
//        BPlusTree tree = new BPlusTree();
//        tree.insertWord("Cat");
//        tree.insertWord("Bird");
//        tree.insertWord("Apple");
//        tree.insertWord("Fish");
//        tree.insertWord("Dog");
//        tree.insertWord("Hamster");
//        tree.insertWord("Banana");
//        tree.insertWord("Can");
//        tree.insertWord("bad");
//        tree.insertWord("bat");
//        tree.insertWord("Apple");
//        tree.insertWord("Amazon");
//        tree.insertWord("ape");
//        tree.insertWord("add");
//        tree.search("Apple");
//        tree.printWords();
//        System.out.println();
//        tree.rangeSearch("dish", "frog");
        String html = "http://portfolio.witmercomputing.com/wp/";
        try
        {
            Document doc = Jsoup.connect(html).get();
            String body = doc.body().text();
            System.out.println(body);
        }
        catch (IOException e)
        {

        }


//
//        BPlusTree.BTNode root = tree.getNode(1);
//        tree.printNode(root);
//        tree.printWords();
    }
}
