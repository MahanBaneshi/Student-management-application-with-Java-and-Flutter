import java.util.*;
public class Assignment {
    //variables:
    private String name;
    private Date deadline;
    private boolean isActive;
    private Course course;

    //constractors:

    public Assignment(String name, Course course){
        this.name = name;
        this.isActive = false;
        this.course = course;
    }
    public Assignment(String name, Date deadline, Course course){
        this.name = name;
        this.deadline = deadline;
        this.isActive = true;
        this.course = course;
    }

    //gettres:

    public String getName() {
        return name;
    }

    public Date getDeadline() {
        return deadline;
    }

    public Course getCourse() {
        return course;
    }

    //setters:

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setName(String name) {
        this.name = name;
    }

    //methods:

    public void changeDeadline(Date newDeadline){
        this.deadline = newDeadline;
    }
}