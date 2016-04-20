/**
 * Created by nicho_000 on 4/16/2016.
 */
public class BTElement
{
    String keyword;
    BPlusTree.BTNode leftChild;

    public BTElement(String s)
    {
        this.keyword = s;
        this.leftChild = null;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o.getClass().getSimpleName().equals("BTElement"))
        {
            BTElement that = (BTElement)o;
            return this.keyword.equals(that.keyword);
        }
        return false;
    }
}
