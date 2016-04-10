import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Created by nicho_000 on 4/3/2016.
 */
public class BufMgr
{
    private PageFrame[] frames;
    private BufHashTbl mappings;
    private Queue<Integer> freeFrames = new LinkedList<>();

    public BufMgr(int numFrames, BufHashTbl m)
    {
        this.frames = new PageFrame[numFrames];
        this.mappings = m;
        int i;
        for (i = 0; i < this.frames.length; i++)
        {
            this.frames[i] = new PageFrame(-1, -1);
        }
        for (i = 0; i < numFrames; i++)
        {
            freeFrames.add(i);
        }
    }

    public PageFrame pin(int page)
    {
        int frame = mappings.lookup(page);
        if (frame != -1)
        {
            if (this.freeFrames.contains(frame))
            {
                this.freeFrames.remove(frame);
            }
            PageFrame toReturn = frames[frame];
            toReturn.addPin();
            return toReturn;
        }
        else
        {
            frame = lru(page);
            if (frame != -1)
            {
                boolean read = readPage(frame);
                if (read)
                {
                    PageFrame toReturn = frames[frame];
                    toReturn.addPin();
                    return toReturn;
                }
                return null;
            }
        }
        return null;
    }

    public PageFrame getPage(int page)
    {
        int frame = mappings.lookup(page);
        if (this.freeFrames.contains(frame))
        {
            this.freeFrames.remove(frame);
        }
        PageFrame toReturn = frames[frame];
        toReturn.addPin();
        return toReturn;
    }

    public void unpin(int page)
    {
        int frame = mappings.lookup(page);
        if (frame != -1)
        {
            this.frames[frame].removePin();
            if (this.frames[frame].getPinCount() == 0)
            {
                this.freeFrames.add(frame);
            }
        }
    }

    private int lru(int page)
    {
        if (freeFrames.size() > 0)
        {
            int replace = freeFrames.remove();
            if (!frames[replace].getDirty())
            {
                PageFrame newFrame = new PageFrame(replace, page);
                this.frames[replace] = newFrame;
                this.mappings.insert(page, replace);
                return replace;
            }
            else
            {
                writePage(replace);
                this.mappings.remove(frames[replace].getPageNumber(), replace);
                PageFrame newFrame = new PageFrame(replace, page);
                this.frames[replace] = newFrame;
                this.mappings.insert(page, replace);
                return replace;
            }
        }
        return -1;
    }

    public void createPage(int page)
    {
        try
        {
            PrintWriter writer = new PrintWriter("files/" + page + ".txt");
            writer.println("This is Page " + page);
            writer.close();
        }
        catch (FileNotFoundException e)
        {

        }
    }

    private void writePage(int frame)
    {
        try
        {
            PrintWriter writer = new PrintWriter("files/" + this.frames[frame].getPageNumber() + ".txt");
            for (String s : this.frames[frame].getContents())
            {
                writer.println(s);
            }
            writer.close();
        }
        catch (FileNotFoundException e)
        {

        }
    }

    private boolean readPage(int frame)
    {
        try
        {
            Scanner file = new Scanner(new File("files/" + this.frames[frame].getPageNumber() + ".txt"));
            ArrayList<String> contents = new ArrayList<>();
            while (file.hasNext())
            {
                contents.add(file.nextLine());
            }
            this.frames[frame].setContents(contents);
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        return true;
    }

    public void cleanUp()
    {
        for (PageFrame frame : this.frames)
        {
            if (frame.getDirty())
            {
                writePage(frame.getFrameNum());
            }
        }
    }
}
