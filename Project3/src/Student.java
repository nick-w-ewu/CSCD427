/**
 * Created by nicho_000 on 4/30/2016.
 */
public class Student implements Comparable<Student>
{
    int id;
    String name;
    String department;
    String credits;

    public Student(String student)
    {
        String[] temp = student.split("\\s+");
        this.id = Integer.parseInt(temp[0]);
        this.name = temp[1];
        if(temp.length == 4)
        {
            this.department = temp[2];
            this.credits = temp[3];
        }
        else
        {
            this.department = temp[2] + " " + temp[3];
            this.credits = temp[4];
        }
    }

    public String toString()
    {
        return this.id + "\t" + this.name + "\t" + this.department + "\t" + this.credits;
    }

    public int compareTo(Student that)
    {
        return this.id - that.id;
    }

    public boolean equals(Object o)
    {
        if(o.getClass().getSimpleName().equals("Student"))
        {
            Student that = (Student)o;
            if(this.id == that.id && this.name.equals(that.name) && this.department.equals(that.department) &&
                    this.credits.equals(that.credits))
            {
                return true;
            }
        }
        return false;
    }
}
