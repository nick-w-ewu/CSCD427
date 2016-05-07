import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by nicho on 5/4/2016.
 */
public class SortMergeJoin
{
    int blockSize;
    int memorySize;
    ArrayList<Object> outputBuffer = new ArrayList<>();

    public SortMergeJoin(int blockSize, int memorySize)
    {
        this.blockSize = blockSize;
        this.memorySize = memorySize;
    }

    public void sort(String file, String type, int recordCount) throws FileNotFoundException
    {
        int numBlocks = recordCount / blockSize;
        int numPartitions = numBlocks / memorySize;
        if(numPartitions == 0)
        {
            numPartitions = 1;
        }
        int recordsPerPartition = recordCount / numPartitions;
        Scanner input = new Scanner(new File(file));
        if (type.equals("student"))
        {
            Student[] students = new Student[recordsPerPartition];
            String temp;
            int partition = 0;
            while (input.hasNext())
            {
                for (int i = 0; i < students.length; i++)
                {
                    try
                    {
                        temp = input.nextLine();
                        students[i] = new Student(temp);
                    }
                    catch (Exception e)
                    {
                        ArrayList<Student> students2 = new ArrayList<>();
                        for (int j = 0; j < students.length; j++)
                        {
                            if (students[j] == null)
                            {
                                break;
                            }
                            students2.add(students[j]);
                        }
                        Collections.sort(students2);
                        outputStudentPartition(students2.toArray(), partition);
                        return;
                    }
                }
                Arrays.sort(students);
                outputStudentPartition(students, partition);
                students = new Student[recordsPerPartition];
                partition++;
            }
        }
        else
        {
            Take[] takes = new Take[recordsPerPartition];
            String temp;
            int partition = 0;
            while (input.hasNext())
            {
                for (int i = 0; i < takes.length; i++)
                {
                    try
                    {
                        temp = input.nextLine();
                        takes[i] = new Take(temp);
                    }
                    catch (Exception e)
                    {
                        ArrayList<Take> takes2 = new ArrayList<>();
                        for (int j = 0; j < takes.length; j++)
                        {
                            if (takes[j] == null)
                            {
                                break;
                            }
                            takes2.add(takes[j]);
                        }
                        Collections.sort(takes2);
                        outputTakePartition(takes2.toArray(), partition);
                        return;
                    }
                }
                Arrays.sort(takes);
                outputTakePartition(takes, partition);
                takes = new Take[recordsPerPartition];
                partition++;
            }
        }
    }

    private void outputStudentPartition(Object[] students, int partition)
    {
        try
        {
            PrintWriter output = new PrintWriter(new File("files/smj/smj_student_" + partition + ".txt"));
            for (Object s : students)
            {
                output.println(s);
            }
            output.close();
        }
        catch (FileNotFoundException e)
        {

        }
    }

    private void outputTakePartition(Object[] takes, int partition)
    {
        try
        {
            PrintWriter output = new PrintWriter(new File("files/smj/smj_take_" + partition + ".txt"));
            for (Object t : takes)
            {
                output.println(t);
            }
            output.close();
        }
        catch (FileNotFoundException e)
        {

        }
    }

    public void merge(String type, int recordCount) throws FileNotFoundException
    {
        double numBlocks = recordCount / blockSize;
        double partitionCount = numBlocks / memorySize;
        String number = partitionCount+"";
        String[] split = number.split("\\.");
        int numPartitions = (int)partitionCount;
        if(split[1].length() == 1)
        {
            if(!split[1].equals("0"))
            {
                numPartitions++;
            }
        }
        else
        {
            numPartitions++;
        }
        Scanner[] partitions = new Scanner[numPartitions];
        if (type.equals("student"))
        {
            PrintWriter out = new PrintWriter(new File("files/smj/sorted_students.txt"));
            out.close();
            for (int i = 0; i < numPartitions; i++)
            {
                partitions[i] = new Scanner(new File("files/smj/smj_student_" + i + ".txt"));
            }
            ArrayList<Student>[] students = new ArrayList[numPartitions];
            readPartitionBlocks(partitions, students, "student");
            int smallest;
            while(hasRecords(students))
            {
                smallest = findSmallestStudents(students);
                addRecordBuffer(students[smallest].get(0), "files/smj/sorted_students.txt");
                removeRecord(smallest, students, partitions);
            }
            outputBuffer("files/smj/sorted_students.txt");
        }
        else
        {
            PrintWriter out = new PrintWriter(new File("files/smj/sorted_takes.txt"));
            out.close();
            for (int i = 0; i < numPartitions; i++)
            {
                partitions[i] = new Scanner(new File("files/smj/smj_take_" + i + ".txt"));
            }
            ArrayList<Take>[] takes = new ArrayList[numPartitions];
            readPartitionBlocks(partitions, takes, "take");
            int smallest;
            while(hasRecords(takes))
            {
                smallest = findSmallestTake(takes);
                addRecordBuffer(takes[smallest].get(0), "files/smj/sorted_takes.txt");
                removeTake(smallest, takes, partitions);
            }
            outputBuffer("files/smj/sorted_takes.txt");
        }
        for(Scanner s : partitions)
        {
            s.close();
        }
    }

    public void join(int records_s, int records_t) throws FileNotFoundException
    {
        PrintWriter out = new PrintWriter(new File("files/smj/smj.txt"));
        out.close();
        Scanner outer = new Scanner(new File("files/smj/sorted_students.txt"));
        Scanner inner = new Scanner(new File(("files/smj/sorted_takes.txt")));
        Student[] students = readStudentBlock(outer);
        Take[] takes = readTakeBlock(inner);
        boolean processing = true;
        int i = 0, j = 0, processed_s = 0, processed_t = 0;
        while(processed_s != records_s && processed_t != records_t)
        {
            if(students[i].id > takes[j].id)
            {
                j++;
                processed_t++;
                if(j == takes.length)
                {
                    takes = readTakeBlock(inner);
                    j = 0;
                }
            }
            else if(students[i].id < takes[j].id)
            {
                i++;
                processed_s++;
                if(i == students.length)
                {
                    students = readStudentBlock(outer);
                    i = 0;
                }
            }
            else
            {
                addRecordBuffer(students[i].toString() + "\t" + takes[j].toStringJoined(), "files/smj/smj.txt");
                j++;
                processed_t++;
                if(j == takes.length)
                {
                    takes = readTakeBlock(inner);
                    j = 0;
                }
                try
                {
                    while (students[i].id == takes[j].id)
                    {
                        addRecordBuffer(students[i].toString() + "\t" + takes[j].toStringJoined(), "files/smj/smj.txt");
                        j++;
                        processed_t++;
                        if (j == takes.length)
                        {
                            takes = readTakeBlock(inner);
                            j = 0;
                        }
                    }
                }
                catch(Exception e)
                {
                    outputBuffer("files/smj/smj.txt");
                    return;
                }
            }
        }
    }

    private Student[] readStudentBlock(Scanner fin)
    {
        Student[] toReturn = new Student[this.blockSize];
        String temp;
        for(int i = 0; i < toReturn.length; i++)
        {
            try
            {
                temp = fin.nextLine();
                toReturn[i] = new Student(temp);
            }
            catch (Exception e)
            {
                break;
            }
        }
        return toReturn;
    }

    private Take[] readTakeBlock(Scanner fin)
    {
        Take[] toReturn = new Take[this.blockSize];
        String temp;
        for(int i = 0; i < toReturn.length; i++)
        {
            try
            {
                temp = fin.nextLine();
                toReturn[i] = new Take(temp);
            }
            catch (Exception e)
            {
                break;
            }
        }
        return toReturn;
    }

    private void removeTake(int smallest, ArrayList<Take>[] takes, Scanner[] partitions)
    {
        takes[smallest].remove(0);
        if(takes[smallest].size() == 0)
        {
            ArrayList<Take> temp = new ArrayList<>();
            String temp2;
            for(int i = 0; i < this.blockSize; i++)
            {
                try
                {
                    temp2 = partitions[smallest].nextLine();
                    temp.add(new Take(temp2));
                }
                catch(Exception e)
                {
                    break;
                }
            }
            takes[smallest] = temp;
        }
    }

    private int findSmallestTake(ArrayList<Take>[] takes)
    {
        int smallestLocation = -1;
        Take smallestValue = new Take(Integer.MAX_VALUE + "\ttemp\ttmep\ttemp\t"+Integer.MAX_VALUE+"\ttemp");
        for(int i = 0; i < takes.length; i++)
        {
            if(takes[i].size() != 0)
            {
                if(takes[i].get(0).compareTo(smallestValue) < 0)
                {
                    smallestLocation = i;
                    smallestValue = takes[i].get(0);
                }
            }
        }
        return smallestLocation;
    }

    private void removeRecord(int smallest, ArrayList<Student>[] students, Scanner[] partitions)
    {
        students[smallest].remove(0);
        if(students[smallest].size() == 0)
        {
            ArrayList<Student> temp = new ArrayList<>();
            String temp2;
            for(int i = 0; i < this.blockSize; i++)
            {
                try
                {
                    temp2 = partitions[smallest].nextLine();
                    temp.add(new Student(temp2));
                }
                catch(Exception e)
                {
                    break;
                }
            }
            students[smallest] = temp;
        }
    }

    private void addRecordBuffer(Object o, String fname)
    {
        if(this.outputBuffer.size() == this.blockSize)
        {
            outputBuffer(fname);
            this.outputBuffer = new ArrayList<>();
        }
        this.outputBuffer.add(o);
    }

    private void outputBuffer(String fileName)
    {
        try
        {
            FileWriter fw = new FileWriter(new File(fileName), true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter writer = new PrintWriter(bw);
            for (Object s : this.outputBuffer)
            {
                writer.println(s);
            }
            writer.close();
        }
        catch (IOException e)
        {

        }
    }

    private int findSmallestStudents(ArrayList<Student>[] students)
    {
        int smallestLocation = -1;
        Student smallestValue = new Student(Integer.MAX_VALUE + "\ttemp\ttmep\ttemp");
        for(int i = 0; i < students.length; i++)
        {
            if(students[i].size() != 0)
            {
                if (students[i].get(0).compareTo(smallestValue) < 0)
                {
                    smallestLocation = i;
                    smallestValue = students[i].get(0);
                }
            }
        }
        return smallestLocation;
    }

    private boolean hasRecords(ArrayList[] partitions)
    {
        boolean toReturn = false;
        for(int i = 0; i < partitions.length; i++)
        {
            if(partitions[i].size() > 0)
            {
                toReturn = true;
            }
        }
        return toReturn;
    }

    public void readPartitionBlocks(Scanner[] partitions, ArrayList[] records, String type)
    {
        String str;
        for (int i = 0; i < partitions.length; i++)
        {
            ArrayList temp = new ArrayList();
            for (int j = 0; j < blockSize; j++)
            {
                try
                {
                    str = partitions[i].nextLine();
                    if (type.equals("student"))
                    {
                        temp.add(new Student(str));
                    }
                    else
                    {
                        temp.add(new Take(str));
                    }
                }
                catch (Exception e)
                {
                    break;
                }
            }
            records[i] = temp;
        }
    }
}
