import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Teacher {
    //variables:
    private String firstName;
    private String lastName;
    private List<Course> courses;
    private long id;
    private String password;
    public FileWriter teacherInfo;
    public FileWriter teacherCourses;


    //constractors:

    public Teacher(long id) throws IOException {
        this.id = id;
        this.courses = new ArrayList<>();
        teacherInfo = new FileWriter("D:\\University\\AP\\project\\project files\\teachers\\"
                + id + ".txt",true);
    }

    public Teacher(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.courses = new ArrayList<>();
    }
    public Teacher(String firstName, String lastName, List<Course> courses){
        this.firstName = firstName;
        this.lastName = lastName;
        this.courses = courses;
        this.courses = new ArrayList<>();
    }

    //getters:

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    //setters:

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void setFirstName(String firstName) throws IOException {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) throws IOException {
        this.lastName = lastName;
    }
    //methods:

    public void addCourse(Course course) throws IOException {
        if (!this.courses.contains(course)) {
            courses.add(course);
            teacherInfo.write("Course: " + course.getName() + "\n");
            teacherInfo.flush();
            course.infoWriter.write(this.getId() + " \n");

        } else {
            System.out.println("This lesson has already been added to the list.");
        }
    }
    public void addStudentToCourse(Student student, Course course) {
        course.addStudent(student);
        student.getCourses().add(course);
       // student.getCoursesAndGrades().put(course, null);
    }

    public void removeStudentFromCourse(Student student, Course course) {
        course.removeStudent(student);
        student.getCourses().remove(course);
        student.getCoursesAndGrades().remove(course);
    }

    public void addAssignmentToCourse(Assignment assignment, Course course) {
        course.getAssignments().add(assignment);
    }

    public void removeAssignmentFromCourse(Assignment assignment, Course course) {
        course.getAssignments().remove(assignment);
    }

    public void gradeStudentInCourse(Student student, Course course, double grade) {
        student.addGradeInCourse(course , grade);

    }
}
