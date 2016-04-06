import java.util.ArrayList;

/**
 * Created by nicho on 4/6/2016.
 */
public class BufMgrTester
{
    public static void main(String[] args)
    {
        BufHashTbl mappings = new BufHashTbl();
        BufMgr manager = new BufMgr(5, mappings);
        for(int i = 0; i < 7; i++)
        {
            manager.createPage(i);
        }
        PageFrame test = manager.pin(1);
        ArrayList<String> contents = test.getContents();
        for(String s : contents)
        {
            System.out.println(s);
        }
    }
}
