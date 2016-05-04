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
        HashJoin test = new HashJoin(12, 20);
        test.hash("input/takes.txt", "take");
        test.hash("input/students.txt", "student");
        for(int i = 0; i < 20; i++)
        {
            test.join(i, i);
        }
        test.flushBuffer();
    }
}
