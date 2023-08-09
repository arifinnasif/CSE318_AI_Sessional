import java.util.Comparator;

public class TSlotComp implements Comparator<Course> {
    @Override
    public int compare(Course o1, Course o2) {
        return o1.time_slot - o2.time_slot;
    }
}

