import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class Course {
    //variables:
    private String name;
    private Teacher teacher;
    private int unitsOfTheCourse;
    private List<Student> students;
    private boolean isActive;
    private List<Assignment> assignments;
    private Date examTime;
    private int studentsCount;
    public FileWriter writer;

    //constractors:

    public Course(String name) throws IOException {
        this.name = name;
        this.students = new ArrayList<>();
        this.assignments = new ArrayList<>();
        writer = new FileWriter("D:\\University\\AP\\project\\project files\\courses\\" + name + ".txt",true);
    }
    public Course(String name, Teacher teacher, int unitsOfTheCourse){
        this.name = name;
        this.teacher = teacher;
        this.unitsOfTheCourse = unitsOfTheCourse;
        this.isActive = true;
        this.studentsCount = 0;
        this.students = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    //getters:

    public String getName() {
        return name;
    }
    public Teacher getTeacher() {
        return teacher;
    }
    public int getUnitsOfTheCourse() {
        return unitsOfTheCourse;
    }
    public List<Student> getStudents() {
        return students;
    }
    public List<Assignment> getAssignments() {
        return assignments;
    }
    public Date getExamTime() {
        return examTime;
    }

    //setters:

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        try {
            writer.write("Teacher id: " + teacher.getId());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUnitsOfTheCourse(int unitsOfTheCourse) {
        this.unitsOfTheCourse = unitsOfTheCourse;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }
    public void setExamTime(Date examTime) {
        this.examTime = examTime;
    }
    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void setStudentsCount(int studentsCount) {
        this.studentsCount = studentsCount;
    }

    //methods:

    public void printStudents() {
        System.out.println("This students are in the course: ");
        for (Student student : students) {
            System.out.println(student.getId());
        }
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public Student findTopScorer() {
        Student topScorer = null;
        double highestGrade = 0;

        for (Student student : students) {
            double grade = student.getThisCourseTotalAverage();
            if (grade > highestGrade) {
                highestGrade = grade;
                topScorer = student;
            }
        }
        return topScorer;
    }
}
