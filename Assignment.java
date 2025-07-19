import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class Assignment {
    //variables:
    private String name;
    private String deadline;
    private boolean isActive;
    private Course course;
    public FileWriter assignmentInfo;

    //constractors:

    public Assignment(String name, Course course) throws IOException {
        this.name = name;
        this.isActive = false;
        this.course = course;
        this.assignmentInfo = new FileWriter("D:\\University\\AP\\project\\project files\\assignements"
                + name + ".txt");
        this.assignmentInfo.write("name: "+ name + "\ncourse: " + course.getName() + "\ndeadline: " + deadline + ", active");
        course.infoWriter.write("\nassignment: " + name + " deadline: " + deadline + ", active" );
    }
    public Assignment(String name, String  deadline, Course course) throws IOException {
        this.name = name;
        this.deadline = deadline;
        this.isActive = true;
        this.course = course;
        this.assignmentInfo = new FileWriter("D:\\University\\AP\\project\\project files\\assignements\\"
                + name + ".txt", true);
        assignmentInfo.write("name: "+ name + "\ncourse: " + course.getName() + "\ndeadline: " + deadline + ", active");

    }

    //gettres:

    public String getName() {
        return name;
    }

    public String  getDeadline() {
        return deadline;
    }

    public Course getCourse() {
        return course;
    }

    //setters:

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setName(String name) {
        this.name = name;
    }

    //methods:

    public void changeDeadline(String newDeadline){
        this.deadline = newDeadline;
    }
}