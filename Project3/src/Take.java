/**
 * Created by nicho_000 on 4/30/2016.
 */
public class Take implements Comparable<Take>
{
    int id;
    String course_id;
    String sec_id;
    String semester;
    int year;
    String grade;

    public Take(String take)
    {
        String[] temp = take.split("\\s+");
        id = Integer.parseInt(temp[0]);
        course_id = temp[1];
        sec_id = temp[2];
        semester = temp[3];
        year = Integer.parseInt(temp[4]);
        grade = temp[5];
    }

    public String toString()
    {
        return this.id + "\t" + this.course_id + "\t" + this.sec_id + "\t" + this.semester + "\t" + this.year + "\t" + this.grade;
    }

    public int compareTo(Take that)
    {
        if(this.id == that.id)
        {
            return this.year - that.year;
        }
        return this.id - that.id;
    }
}
