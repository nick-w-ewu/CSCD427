import java.util.ArrayList;

/**
 * Created by nicho on 3/31/2016.
 */
public class PageFrame
{
    private int frameNum;
    private int pageNumber;
    private int pinCount;
    private boolean dirty;
    private ArrayList<String> contents;

    public PageFrame(int frameNum, int pageNumber)
    {
        this.frameNum = frameNum;
        this.pageNumber = pageNumber;
        this.dirty = false;
        this.pinCount = 0;
        this.contents = new ArrayList<>();
    }

    public int getFrameNum()
    {
        return frameNum;
    }

    public void setFrameNum(int frameNum)
    {
        this.frameNum = frameNum;
    }

    public int getPageNumber()
    {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public int getPinCount()
    {
        return pinCount;
    }

    public void addPin()
    {
        this.pinCount++;
    }

    public void removePin()
    {
        this.pinCount--;
    }

    public boolean getDirty()
    {
        return dirty;
    }

    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
    }

    public ArrayList<String> getContents()
    {
        return contents;
    }

    public void addContents(String s)
    {
        this.dirty = true;
        this.contents.add(s);
    }

    public void setContents(ArrayList<String> contents)
    {
        this.contents = contents;
    }

    public String toString()
    {
        String toReturn = "Page: " + this.pageNumber + " Dirty: " + this.dirty + " Pin: " + this.pinCount;
        return toReturn;
    }
}
