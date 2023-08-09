import java.util.Comparator;

public class EnrComp implements Comparator<Course> {
    @Override
    public int compare(Course t1, Course t2) {
        return t2.number_of_students - t1.number_of_students;
    }
}
