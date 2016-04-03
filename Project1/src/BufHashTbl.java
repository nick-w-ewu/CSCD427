import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by nicho on 3/31/2016.
 */
public class BufHashTbl
{
    private LinkedList<PageMapper>[] mappings = new LinkedList[15];

    public void insert(int frame, int page)
    {
        int index = page % mappings.length;
        PageMapper temp = new PageMapper(frame, page);
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
        PageMapper temp = new PageMapper(frame, page);
        this.mappings[index].remove(temp);
    }

}
