import java.util.Comparator;

public class DegreeComp implements Comparator<Course> {
    @Override
    public int compare(Course t1, Course t2) {
        return t2.conflictingCourses.size() - t1.conflictingCourses.size();
    }
}
