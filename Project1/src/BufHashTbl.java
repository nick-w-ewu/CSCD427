import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by nicho on 3/31/2016.
 */
public class BufHashTbl
{
    private LinkedList<PageMapper>[] mappings = new LinkedList[4];

    public BufHashTbl()
    {
        for(int i = 0; i < mappings.length; i++)
        {
            mappings[i] = new LinkedList<PageMapper>();
        }
    }

    public void insert(int page, int frame)
    {
        int index = page % mappings.length;
        PageMapper temp = new PageMapper(page, frame);
        if (!this.mappings[index].contains(temp))
        {
            this.mappings[index].addFirst(temp);
        }
    }

    public int lookup(int page)
    {
        int i = page % mappings.length;
        LinkedList<PageMapper> row = this.mappings[i];
        for (PageMapper map : row)
        {
            if (map.getPage() == page)
            {
                return map.getFrame();
            }
        }
        return -1;
    }

    public void remove(int page, int frame)
    {
        int index = page % mappings.length;
        PageMapper temp = new PageMapper(page, frame);
        this.mappings[index].remove(temp);
    }

    public void printTable()
    {
        for(LinkedList<PageMapper> list : mappings)
        {
            for(PageMapper p : list)
            {
                System.out.println(p.getPage() + ", " + p.getFrame());
            }
        }
    }

}
