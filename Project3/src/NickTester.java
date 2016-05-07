import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Created by nicho_000 on 4/30/2016.
 */
public class NickTester
{
    public static void main(String[] args) throws FileNotFoundException
    {
//        HashJoin test = new HashJoin(12, 20);
//        test.hash("input/takes.txt", "take");
//        test.hash("input/students.txt", "student");
//        for(int i = 0; i < 19; i++)
//        {
//            test.join(i, i);
//        }
//        test.flushBuffer();
        SortMergeJoin test2 = new SortMergeJoin(12, 20);
//        test2.sort("input/students.txt", "student", 244);
//        test2.sort("input/takes.txt", "take", 3694);
//        test2.merge("student", 244);
//        test2.merge("take", 3694);
        test2.join(244, 3694);
    }
}
