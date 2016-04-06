/**
 * Created by nicho on 4/6/2016.
 */
public class HashTester
{
    public static void main(String[] args)
    {
        BufHashTbl test = new BufHashTbl();
        test.insert(1,1);
        test.insert(4,15);
        test.printTable();
        System.out.println(test.lookup(1));
        System.out.println(test.lookup(4));
        test.remove(4,15);
        test.printTable();
    }
}
