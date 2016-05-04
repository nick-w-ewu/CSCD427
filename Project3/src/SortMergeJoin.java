/**
 * Created by nicho on 5/4/2016.
 */
public class SortMergeJoin
{
    int blockSize;
    int memorySize;

    public SortMergeJoin(int blockSize, int memorySize)
    {
        this.blockSize = blockSize;
        this.memorySize = memorySize;
    }

    public void sort(String filename, String type, int recordCount)
    {
        int numBlocks = recordCount/blockSize;
        int numPartitions = numBlocks/memorySize;
        int blocksPerPartition = numBlocks/numPartitions;
    }
}
