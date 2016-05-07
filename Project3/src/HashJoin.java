import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by nicho_000 on 4/30/2016.
 */
public class HashJoin
{
    int blockSize;
    int numBuckets;
    ArrayList<String> outputBuffer = new ArrayList<>();

    public HashJoin(int b, int m) throws FileNotFoundException
    {
        this.blockSize = b;
        this.numBuckets = m-1;
        PrintWriter out = new PrintWriter(new File("files/hashjoin/hj.txt"));
        out.close();
    }

    public void hash(String file, String type)
    {
        try
        {
            Scanner input = new Scanner(new File(file));
            if(type.equals("student"))
            {
                HashTbl<Student> hashTable = new HashTbl<>("hj_student", this.blockSize, this.numBuckets, false);
                Student[] students = new Student[blockSize];
                String temp;
                while(input.hasNext())
                {
                    for(int i = 0; i < students.length; i++)
                    {
                        try
                        {
                            temp = input.nextLine();
                            students[i] = new Student(temp);
                        }
                        catch (Exception e)
                        {
                            hashStudentBlock(students, hashTable);
                            hashTable.flush();
                            return;
                        }
                    }
                    hashStudentBlock(students, hashTable);
                    students = new Student[blockSize];
                }
            }
            else
            {
                HashTbl<Take> hashTable = new HashTbl<>("hj_takes", this.blockSize, this.numBuckets, false);
                Take[] takes = new Take[blockSize];
                String temp;
                while(input.hasNext())
                {
                    for(int i = 0; i < takes.length; i++)
                    {
                        try
                        {
                            temp = input.nextLine();
                            takes[i] = new Take(temp);
                        }
                        catch(Exception e)
                        {
                            hashTakeBlock(takes, hashTable);
                            hashTable.flush();
                            return;
                        }
                    }
                    hashTakeBlock(takes, hashTable);
                    takes = new Take[blockSize];
                }
            }
        }
        catch (FileNotFoundException e)
        {
        }
    }

    public void join(int buildIndex, int probeIndex) throws FileNotFoundException
    {
        HashTbl<Student> build = new HashTbl<>(null, blockSize, numBuckets, true);
        String temp;
        Scanner input = new Scanner(new File("files/hashjoin/hj_student_" + buildIndex + ".txt"));
        Student[] students = new Student[blockSize];
        while(input.hasNext())
        {
            for(int i = 0; i < students.length; i++)
            {
                try
                {
                    temp = input.nextLine();
                    students[i] = new Student(temp);
                }
                catch (Exception e)
                {
                    hashStudentBlock(students, build);
                    students = new Student[blockSize];
                    break;
                }
            }
            hashStudentBlock(students, build);
            students = new Student[blockSize];
        }
        Scanner input2 = new Scanner(new File("files/hashjoin/hj_takes_" + probeIndex + ".txt"));
        Take[] takes = new Take[blockSize];
        while(input2.hasNext())
        {
            for(int i = 0; i < takes.length; i++)
            {
                try
                {
                    temp = input2.nextLine();
                    takes[i] = new Take(temp);
                }
                catch(Exception e)
                {
                    processJoin(build, takes);
                    return;
                }
            }
            processJoin(build, takes);
            takes = new Take[blockSize];
        }
    }

    public void flushBuffer()
    {
        writeBuffer();
    }

    private void processJoin(HashTbl<Student> build, Take[] takes)
    {
        LinkedList<Student> partition = build.lookUp(takes[0].id);

        for(int i = 0; i < takes.length; i++)
        {
            if(takes[i] == null)
            {
                return;
            }
            for(Student s : partition)
            {
                if(s.id == takes[i].id)
                {
                    storeJoined(s, takes[i]);
                    break;
                }
            }
        }
    }

    private void printTakes(Take[] takes)
    {
        for(Take t : takes)
        {
            System.out.println(t);
        }
        System.out.println();
    }

    private void storeJoined(Student s, Take take)
    {
        if(this.outputBuffer.size() == this.blockSize)
        {
            writeBuffer();
            this.outputBuffer = new ArrayList<>();
        }
        this.outputBuffer.add(s.toString() + "\t" + take.toStringJoined());
    }

    private void writeBuffer()
    {
        try
        {
            FileWriter fw = new FileWriter(new File("files/hashjoin/hj.txt"), true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter writer = new PrintWriter(bw);
            for (String s : this.outputBuffer)
            {
                writer.println(s);
            }
            writer.close();
        }
        catch (IOException e)
        {

        }
    }

    private void hashStudentBlock(Student[] students, HashTbl table)
    {
        for(Student s : students)
        {
            if(s == null)
            {
                break;
            }
            table.insert(s.id, s);
        }
    }

    private void hashTakeBlock(Take[] takes, HashTbl table)
    {
        for(Take t : takes)
        {
            if(t == null)
            {
                break;
            }
            table.insert(t.id, t);
        }
    }
}
