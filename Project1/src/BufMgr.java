import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Created by nicho_000 on 4/3/2016.
 */
public class BufMgr
{
    private PageFrame[] frames;
    private BufHashTbl mappings;
    private Queue<Integer> freeFrames;

    public BufMgr(int numFrames, BufHashTbl m)
    {
        this.frames = new PageFrame[numFrames];
        this.mappings = m;
        for(int i = 0; i < numFrames; i++)
        {
            freeFrames.add(i);
        }
    }

    public PageFrame pin(int page)
    {
        int frame = mappings.lookup(page);
        if(frame != -1)
        {
            PageFrame toReturn = frames[frame];
            toReturn.addPin();
            return toReturn;
        }
        else
        {
            frame = lru(page);
            if(frame != -1)
            {
                readPage(frame);
                PageFrame toReturn = frames[frame];
                toReturn.addPin();
                return toReturn;
            }
        }
        return null;
    }

    public void unpin(int page)
    {
        int frame = mappings.lookup(page);
        this.frames[frame].removePin();
        if(this.frames[frame].getPinCount() == 0)
        {
            this.freeFrames.add(frame);
        }
    }

    private int lru(int page)
    {
        if(freeFrames.size() > 0)
        {
            int replace = freeFrames.remove();
            if(!frames[replace].getDirty())
            {
                PageFrame newFrame = new PageFrame(replace, page);
                this.mappings.insert(replace, page);
                return replace;
            }
            else
            {
                writePage(replace);
                this.mappings.remove(frames[replace].getPageNumber(), replace);
                PageFrame newFrame = new PageFrame(replace, page);
                this.frames[replace] = newFrame;
                this.mappings.insert(replace, page);
                return replace;
            }
        }
        return -1;
    }

    private void createPage(int page)
    {
        try
        {
            PrintWriter writer = new PrintWriter(page+".txt");
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
            PrintWriter writer = new PrintWriter(this.frames[frame].getPageNumber()+".txt");
            for(String s : this.frames[frame].getContents())
            {
                writer.println(s);
            }
            writer.close();
        }
        catch (FileNotFoundException e)
        {

        }
    }

    private void readPage(int frame)
    {
        try
        {
            Scanner file = new Scanner(new File(this.frames[frame].getPageNumber()+".txt"));
            ArrayList<String> contents = new ArrayList<>();
            while(file.hasNextLine())
            {
                contents.add(file.nextLine());
            }
            this.frames[frame].setContents(contents);
        }
        catch (FileNotFoundException e)
        {

        }
    }
}
