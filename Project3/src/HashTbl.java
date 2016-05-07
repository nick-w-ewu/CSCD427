import sun.misc.JavaIOAccess;

import java.io.*;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by nicho on 3/31/2016.
 */
public class HashTbl<E>
{
    private LinkedList<E>[] mappings;
    int recordsOnBlock;
    String filePrefix;
    boolean inMemory;

    public HashTbl(String filePrefix, int recordsOnBlock, int numBuckets, boolean inMemory) throws FileNotFoundException
    {
        this.recordsOnBlock = recordsOnBlock;
        this.mappings = new LinkedList[numBuckets];
        this.filePrefix = filePrefix;
        this.inMemory = inMemory;
        for (int i = 0; i < mappings.length; i++)
        {
            mappings[i] = new LinkedList<E>();
        }
        if (!this.inMemory)
        {
            PrintWriter out;
            for (int i = 0; i < numBuckets; i++)
            {
                out = new PrintWriter(new File("files/hashjoin/" + filePrefix + "_" + i + ".txt"));
                out.close();
            }
        }
    }

    public void insert(int id, E e)
    {
        int index = id % mappings.length;
        if (!this.inMemory)
        {
            if (this.mappings[index].size() == this.recordsOnBlock)
            {
                writeBucket(index);
                this.mappings[index] = new LinkedList<>();
            }
        }
        this.mappings[index].addFirst(e);
    }

    private void writeBucket(int index)
    {
        try
        {
            FileWriter fw = new FileWriter(new File("files/hashjoin/" + filePrefix + "_" + index + ".txt"), true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter writer = new PrintWriter(bw);
            for (E e : this.mappings[index])
            {
                writer.println(e);
            }
            writer.close();
        }
        catch (IOException e)
        {

        }
    }

    public void flush()
    {
        for (int i = 0; i < this.mappings.length; i++)
        {
            writeBucket(i);
        }
    }

    public LinkedList<E> lookUp(int id)
    {
        int index = id % this.mappings.length;
        return this.mappings[index];
    }
}
