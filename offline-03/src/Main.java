import java.io.*;
import java.util.*;

public class Main {
    static ArrayList<Course> courses;
    static int max_time_slot;

    static final String PENALTY_TYPE = "EXPONENTIAL";
//    static final String PENALTY_TYPE = "LINEAR";

    static void satisfyHardConstraints(String heuristics) {
        if("HighestDegree".equalsIgnoreCase(heuristics)) {
            satisfyHardConstraintsWithComparator(new DegreeComp());
        } else if ("HighestEnrollment".equalsIgnoreCase(heuristics)) {
            satisfyHardConstraintsWithComparator(new EnrComp());
        } else if ("DSatur".equalsIgnoreCase(heuristics)) {
            satisfyHardConstraintsWithDSatur();
        } else if ("Random".equalsIgnoreCase(heuristics)) {
            satisfyHardConstraintsWithRandom();
        } else {
            System.out.println("Invalid heuristics!");
            System.exit(0);
        }
    }

    static void satisfyHardConstraintsWithRandom() {
        ArrayList<Course> pq = new ArrayList<>();
        for(int i = 0; i < courses.size(); i++) {
            if(courses.get(i).time_slot == -1) pq.add(courses.get(i));
        }
        Collections.shuffle(pq);
        while(!pq.isEmpty()) {
            Course course = pq.get(0);
            pq.remove(0);
            if(course.time_slot != -1) continue;
            int time_slot = -1;
            course.conflictingCourses.sort(new TSlotComp());
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//            System.out.println("SIZE "+course.conflictingCourses.size());
//            for(Course course1: course.conflictingCourses) System.out.println(course1.courseID+"->"+course1.time_slot);
//            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");



            for(int i = 0; i < course.conflictingCourses.size(); i++) {
                if(course.conflictingCourses.get(i).time_slot == -1) continue;
                if(course.conflictingCourses.get(i).time_slot != time_slot) {
                    if(course.conflictingCourses.get(i).time_slot - time_slot > 1) {
                        break;
                    } else if (course.conflictingCourses.get(i).time_slot - time_slot == 1) {
                        time_slot = course.conflictingCourses.get(i).time_slot;
                    } else {
                        System.out.println("THIS SHOULD NOT HAPPEN");
                    }
                }
            }

            time_slot++;
//            System.out.println("Course "+course.courseID+" got timeslot "+time_slot);
            course.time_slot = time_slot;
            if(max_time_slot < time_slot)
                max_time_slot = time_slot;
        }
    }

    static void satisfyHardConstraintsWithDSatur() {
        ArrayList<Course> pq = new ArrayList<>();
        for(int i = 0; i < courses.size(); i++) {
            if(courses.get(i).time_slot == -1) pq.add(courses.get(i));
        }
        while(!pq.isEmpty()) {
            pq.sort(new SatComp());
            Course course = pq.get(0);
            pq.remove(0);
            if(course.time_slot != -1) continue;
            int time_slot = -1;
            course.conflictingCourses.sort(new TSlotComp());
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//            System.out.println("SIZE "+course.conflictingCourses.size());
//            for(Course course1: course.conflictingCourses) System.out.println(course1.courseID+"->"+course1.time_slot);
//            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");



            for(int i = 0; i < course.conflictingCourses.size(); i++) {
                if(course.conflictingCourses.get(i).time_slot == -1) continue;
                if(course.conflictingCourses.get(i).time_slot != time_slot) {
                    if(course.conflictingCourses.get(i).time_slot - time_slot > 1) {
                        break;
                    } else if (course.conflictingCourses.get(i).time_slot - time_slot == 1) {
                        time_slot = course.conflictingCourses.get(i).time_slot;
                    } else {
                        System.out.println("THIS SHOULD NOT HAPPEN");
                    }
                }
            }

            time_slot++;
//            System.out.println("Course "+course.courseID+" got timeslot "+time_slot);
            course.time_slot = time_slot;
            if(max_time_slot < time_slot)
                max_time_slot = time_slot;
        }
    }

    static void satisfyHardConstraintsWithComparator(Comparator<? super Course> comparator) {
        PriorityQueue<Course> pq = new PriorityQueue<>(comparator);
        for(int i = 0; i < courses.size(); i++) {
            if(courses.get(i).time_slot == -1) pq.add(courses.get(i));
        }
        while(!pq.isEmpty()) {
            Course course = pq.poll();
            if(course.time_slot != -1) continue;
            int time_slot = -1;
            course.conflictingCourses.sort(new TSlotComp());
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//            System.out.println("SIZE "+course.conflictingCourses.size());
//            for(Course course1: course.conflictingCourses) System.out.println(course1.courseID+"->"+course1.time_slot);
//            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");



            for(int i = 0; i < course.conflictingCourses.size(); i++) {
                if(course.conflictingCourses.get(i).time_slot == -1) continue;
                if(course.conflictingCourses.get(i).time_slot != time_slot) {
                    if(course.conflictingCourses.get(i).time_slot - time_slot > 1) {
                        break;
                    } else if (course.conflictingCourses.get(i).time_slot - time_slot == 1) {
                        time_slot = course.conflictingCourses.get(i).time_slot;
                    } else {
                        System.out.println("THIS SHOULD NOT HAPPEN");
                    }
                }
            }

            time_slot++;
//            System.out.println("Course "+course.courseID+" got timeslot "+time_slot);
            course.time_slot = time_slot;
            if(max_time_slot < time_slot)
                max_time_slot = time_slot;
        }
    }


    static double calculateAveragePenalty(ArrayList<Student> students) {

        double penalty = 0;
        for(int i = 0; i < students.size(); i++) {
            students.get(i).registeredCourses.sort(new TSlotComp());
            for(int j = 0; j < students.get(i).registeredCourses.size(); j++) {
                for(int k = j+1; k < students.get(i).registeredCourses.size(); k++) {
                    int interval = students.get(i).registeredCourses.get(k).time_slot - students.get(i).registeredCourses.get(j).time_slot;
                    if(interval < 6) {

                        if("EXPONENTIAL".equals(PENALTY_TYPE)) {
                            penalty += Math.pow(2, 5-interval);
                        } else if("LINEAR".equals(PENALTY_TYPE)) {
                            penalty += 2.0*(5-interval);
                        } else {
                            System.out.println("Invalid penalty type");
                            System.exit(-1);
                        }
                    }
                }
            }
        }
        return penalty/students.size();
    }

    static HashMap<Integer, Integer> isVisited;

    static void kempe_dfs(Course current, int neighbour_time_slot) {
        if(isVisited.getOrDefault(current.courseID,0) != 0) return;
        isVisited.put(current.courseID, 1);
        for(int i = 0; i < current.conflictingCourses.size(); i++) {
            Course neighbour = current.conflictingCourses.get(i);
            if(isVisited.getOrDefault(neighbour.courseID, 0) == 0 && neighbour.time_slot == neighbour_time_slot) {
//                neighbour.time_slot = current.time_slot;
//                current.time_slot = neighbour_time_slot;
//                System.out.println("++");
                kempe_dfs(neighbour, current.time_slot);
//                System.out.println("--");
            }
        }
        isVisited.put(current.courseID, 2);
    }

    static void kempe_dfs_undo(int current_time_slot, int neighbour_time_slot) {
        for(Course course: courses) {
            if(isVisited.getOrDefault(course.courseID, 0) != 0) {
                if(course.time_slot == current_time_slot) {
                    course.time_slot = neighbour_time_slot;
                } else if(course.time_slot == neighbour_time_slot) {
                    course.time_slot = current_time_slot;
                } else {
                    System.out.println("THIS SHOULD NOT HAPPEN 4: "+course.time_slot+":"+current_time_slot+":"+neighbour_time_slot);
                }
            }
        }
    }

    static void kempe(Course current, int neighbour_time_slot, ArrayList<Course> courses, ArrayList<Student> students) {
        double old_penalty = calculateAveragePenalty(students);
        int current_time_slot = current.time_slot;
        isVisited.clear();
        kempe_dfs(current, neighbour_time_slot);
//        for(int c : isVisited.keySet()) System.out.println(c+"->"+isVisited.get(c));
        for(Course course: courses) {
            if(isVisited.getOrDefault(course.courseID, 0) != 0) {
                if(course.time_slot == current_time_slot) {
                    course.time_slot = neighbour_time_slot;
                } else if(course.time_slot == neighbour_time_slot) {
                    course.time_slot = current_time_slot;
                } else {
                    System.out.println("THIS SHOULD NOT HAPPEN 3: "+course.time_slot+":"+current_time_slot+":"+neighbour_time_slot);
                }
            }
        }
        double new_penalty = calculateAveragePenalty(students);
        if(new_penalty >= old_penalty) {
//            System.out.println("+KEMPE CHANGED FROM "+old_penalty+" TO "+new_penalty);
            // undoing the changes
            kempe_dfs_undo(current_time_slot, neighbour_time_slot);
        } else {
            System.out.println("-[KEMPE] CHANGED\t\tFROM "+old_penalty+"\tTO "+new_penalty);
        }
        isVisited.clear();

    }

    static void satisfySoftConstraintsWithKempe(ArrayList<Course> courses, ArrayList<Student> students, int iteration_count) {
        Random r = new Random();
        for(int i = 0; i < iteration_count; i++) {
            int course_index;
            Course chosen_course;
            do {
                course_index = r.nextInt(courses.size());
                chosen_course = courses.get(course_index);
            } while(chosen_course.conflictingCourses.size() == 0);
//            System.out.println("Chosen course "+chosen_course.courseID+" with time slot "+chosen_course.conflictingCourses.size());
            int neighbour_index = r.nextInt(chosen_course.conflictingCourses.size());
            ArrayList<Course> neighbours = chosen_course.conflictingCourses;
            Course random_neighbour = neighbours.get(neighbour_index);
            kempe(chosen_course, random_neighbour.time_slot, courses, students);
        }
    }

    static void satisfySoftConstraintsWithPairSwap(ArrayList<Course> courses, ArrayList<Student> students, int iteration_count) {
        Random r = new Random();
        for (int i = 0; i < iteration_count; i++) {
            int course1_index=r.nextInt(courses.size());
            Course course1 = courses.get(course1_index);
            int course1_time_slot = course1.time_slot;

            int course2_index=r.nextInt(courses.size());
            Course course2 = courses.get(course2_index);
            int course2_time_slot = course2.time_slot;

            if(course1_time_slot == course2_time_slot) continue;

            boolean is_swappable = true;

            for(Course course1_neighbour : course1.conflictingCourses) {
                if(course1_neighbour.time_slot == course2_time_slot) {
                    is_swappable = false;
                    break;
                }
            }
            if(!is_swappable) continue;

            for(Course course2_neighbour : course2.conflictingCourses) {
                if(course2_neighbour.time_slot == course1_time_slot) {
                    is_swappable = false;
                    break;
                }
            }
            if(!is_swappable) continue;

            double old_penalty = calculateAveragePenalty(students);
            course1.time_slot = course2_time_slot;
            course2.time_slot = course1_time_slot;
            double new_penalty = calculateAveragePenalty(students);

            if(new_penalty >= old_penalty) {
                course1.time_slot = course1_time_slot;
                course2.time_slot = course2_time_slot;
            } else {
                System.out.println("-[PAIR SWAP] CHANGED\tFROM "+old_penalty+"\tTO "+new_penalty);
            }

        }
    }


    public static void main(String[] args) throws IOException {
        max_time_slot = 0;
        isVisited = new HashMap<>();
        FileWriter output_file = null;
        try {
            output_file = new FileWriter("output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // read from file
        String fileName = "car-f-92";
//        String fileName = "car-s-91";
//        String fileName = "kfu-s-93";
//        String fileName = "tre-s-92";
//        String fileName = "yor-f-83";
        File course_file = new File("dataset/"+fileName+".crs");
        courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(course_file))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] temp = line.split(" ");
                courses.add(new Course(Integer.parseInt(temp[0]), Integer.parseInt(temp[1])));
            }
        } catch (IOException e) {
            System.out.println("PLEASE ADD THE .crs FILE TO THE FOLDER \"dataset/\" IN THE PROJECT DIRECTORY...\n\n\n");
            e.printStackTrace();
            System.exit(-1);
        }

        File student_file = new File("dataset/"+fileName+".stu");
        ArrayList<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(student_file))) {
            String line;
//            int line_num = 0;
            while((line = br.readLine()) != null) {
//                line_num++;
                if(line.isEmpty()) continue;
                String[] temp = line.split(" ");
                Student student = new Student();
                for (int i = 0; i < temp.length; i++) {
                    Course course = courses.get(courses.indexOf(new Course(Integer.parseInt(temp[i]), 0)));
                    if(student.registeredCourses.contains(course)) System.out.println("THIS SHOULD NOT HAPPEN2");
                    student.registeredCourses.add(course);
                }
                for (int i = 0; i < student.registeredCourses.size() - 1; i++) {
                    for (int j = i + 1; j < student.registeredCourses.size(); j++) {
                        if(!student.registeredCourses.get(i).conflictingCourses.contains(student.registeredCourses.get(j))) student.registeredCourses.get(i).conflictingCourses.add(student.registeredCourses.get(j));
                        if(!student.registeredCourses.get(j).conflictingCourses.contains(student.registeredCourses.get(i))) student.registeredCourses.get(j).conflictingCourses.add(student.registeredCourses.get(i));

                    }

                }
                students.add(student);
            }

        } catch (IOException e) {
            System.out.println("PLEASE ADD THE .stu FILE TO THE FOLDER \"dataset/\" IN THE PROJECT DIRECTORY...\n\n\n");
            e.printStackTrace();
            System.exit(-1);
        }

        // assume everything is populated by this line
//        satisfyHardConstraints("HighestDegree");
        satisfyHardConstraints("DSatur");
//        satisfyHardConstraints("HighestEnrollment");
//        satisfyHardConstraints("Random");
        double penalty1 = calculateAveragePenalty(students);

        satisfySoftConstraintsWithKempe(courses, students, 3000);

        double penalty2 = calculateAveragePenalty(students);

        satisfySoftConstraintsWithPairSwap(courses, students, 3000);

        double penalty3 = calculateAveragePenalty(students);

        courses.sort(new TSlotComp());

        for(int i = 0; i < courses.size(); i++) {
            output_file.write("COURSE ID: "+courses.get(i).courseID +"\tTIMESLOT: "+ courses.get(i).time_slot+"\n");
        }

        output_file.write("\n\n\n");

        int id=0;
        for(Student student: students) {
            student.registeredCourses.sort(new TSlotComp());
            output_file.write(id+" : "+"\n");
            id++;
            for(Course course : student.registeredCourses) {
                output_file.write("COURSE ID: "+course.courseID+"\tTIMESLOT: "+course.time_slot+"\n");

            }
            output_file.write("\n");
        }
        output_file.flush();
        System.out.println("\nDONE!\n");
        System.out.println("timeslots : "+(max_time_slot+1));
        System.out.println("penalty1 : "+penalty1);
        System.out.println("penalty2 : "+penalty2);
        System.out.println("penalty3 : "+penalty3);

        // check inconsistency
//        id=0;
//        for(Student student: students) {
//            int temp = -1;
//            student.registeredCourses.sort(new TSlotComp());
//            for(Course course : student.registeredCourses) {
//                if(temp != course.time_slot) {
//                    temp = course.time_slot;
//                } else {
//                    System.out.println(id+" PANIC: temp="+temp+", courseID="+course.courseID+", timeslot="+course.time_slot);
//                    break;
//                }
//
//            }
//            id++;
//        }
    }
}
