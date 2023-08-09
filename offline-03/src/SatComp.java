import java.util.ArrayList;
import java.util.Comparator;

public class SatComp implements Comparator<Course> {
    @Override
    public int compare(Course o1, Course o2) {
        int o1_sat_count = 0;
        int o1_uncolored_count = 0;
        ArrayList<Course> o1_conflictingCourses = new ArrayList<>(o1.conflictingCourses);
        o1_conflictingCourses.sort(new TSlotComp());
        int o1_lastColor = -1;
        for(int i = 0; i < o1_conflictingCourses.size(); i++) {
            if(o1_conflictingCourses.get(i).time_slot == -1) {
                o1_uncolored_count++;
            }
            if(o1_conflictingCourses.get(i).time_slot != o1_lastColor) {
                o1_lastColor = o1_conflictingCourses.get(i).time_slot;
                o1_sat_count++;
            }
        }

        int o2_sat_count = 0;
        int o2_uncolored_count = 0;
        ArrayList<Course> o2_conflictingCourses = new ArrayList<>(o2.conflictingCourses);
        o2_conflictingCourses.sort(new TSlotComp());
        int o2_lastColor = -1;
        for(int i = 0; i < o2_conflictingCourses.size(); i++) {
            if(o2_conflictingCourses.get(i).time_slot == -1) {
                o2_uncolored_count++;
            }
            if(o2_conflictingCourses.get(i).time_slot != o2_lastColor) {
                o2_lastColor = o2_conflictingCourses.get(i).time_slot;
                o2_sat_count++;
            }
        }

        if(o1_sat_count == o2_sat_count) {
            return o2_uncolored_count - o1_uncolored_count;
        }

        return o2_sat_count - o1_sat_count;
    }
}


