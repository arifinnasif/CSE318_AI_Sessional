import java.util.ArrayList;
import java.util.Objects;

public class Course {
    int courseID;
    int number_of_students;
    int time_slot;
    ArrayList<Course> conflictingCourses;
    Course(int courseID, int number_of_students) {
        this.courseID = courseID;
        this.number_of_students = number_of_students;
        time_slot = -1;
        conflictingCourses = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseID == course.courseID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseID);
    }
}
