/**
 * Created by nicho_000 on 4/3/2016.
 */
public class PageMapper
{
    private int page;
    private int frame;

    public PageMapper(int p, int f)
    {
        this.frame = f;
        this.page = p;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getFrame()
    {
        return frame;
    }

    public void setFrame(int frame)
    {
        this.frame = frame;
    }

    public boolean equals(Object o)
    {
        if(o.getClass().getSimpleName().equals("PageMapper"))
        {
            PageMapper that = (PageMapper)o;
            if(this.page == that.getPage() && this.frame == that.getFrame())
            {
                return true;
            }
        }
        return false;
    }
}
