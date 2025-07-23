import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Student {
    //variables:
    private String name;
    private long id;
    private String password;
    private List<Course> courses;
    private Collection<Double> grades;
    private Map<Course, Double> coursesAndGrades;
    private double TotalAverage;
    private double thisCourseTotalAverage;
    private String specialAdjective;
    public FileWriter courseWriter;
    public FileWriter infoWriter;
    public FileReader infoReader;

    //constractors:
    public Student(long id) throws IOException {
        this.id = id;
        this.courses = new ArrayList<>();
        this.grades = new ArrayList<>();
        this.coursesAndGrades = new HashMap<>();
        this.courseWriter = new FileWriter("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                + id + "_courses.txt",true);
        this.infoWriter = new FileWriter("D:\\University\\AP\\project\\project files\\students\\studentInfos\\"
                + id + "_info.txt",true);
        this.infoReader = new FileReader("D:\\University\\AP\\project\\project files\\students\\studentInfos\\"
                + id + "_info.txt");
        BufferedReader br = new BufferedReader(infoReader);

        if (! br.readLine().equals("Number of units: 0")){
            infoWriter.write("Number of units: 0");
            infoWriter.flush();
        }
    }



    //gettres:

    public String getName() {
        return name;
    }
    public long getId() {
        return id;
    }
    public List<Course> getCourses() {
        return courses;
    }

    public List<Double> getGarades() {
        return (List<Double>) grades;
    }

    public double getTotalAverage() {
        return TotalAverage;
    }
    public double getThisCourseTotalAverage() {
        return thisCourseTotalAverage;
    }

    public String getSpecialAdjective() {
        return specialAdjective;
    }

    public Map<Course, Double> getCoursesAndGrades() {
        return coursesAndGrades;
    }

    public String getPassword() {
        return password;
    }
    //setters:

    public void setName(String name) {
        this.name = name;
    }
    public void setId(long id) {
        if (String.valueOf(id).length() == 9) {
            this.id = id;
        } else {
            System.out.println("The student number must be 9 digits");
        }
    }
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
    public void setGarades(List<Double> garades) {
        this.grades = garades;
    }

    public void setSpecialAdjective(String specialAdjective) {
        this.specialAdjective = specialAdjective;
    }

    public void setCoursesAndGrades(Map<Course, Double> coursesAndGrades) {
        this.coursesAndGrades = coursesAndGrades;
    }
    public void setThisCourseTotalAverage(double thisCourseTotalAverage, Course course) {
        this.thisCourseTotalAverage = thisCourseTotalAverage;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //methods:

    public void addToCourse(Course course) throws IOException {
        this.courses.add(course);
        courseWriter.write(course.getName());
    }
    public void addGradeInCourse(Course course, double grade){
        this.coursesAndGrades.put(course, grade);
    }

    public void printCourses() {
        for (Course course : courses) {
            System.out.println(course.getName());
        }
    }
    public double calculateTotalGrade(){
        float sum = 0;
        Collection<Double> allGrades = new ArrayList<>();
        allGrades =  this.coursesAndGrades.values();
       for (double grade : allGrades) {
           sum += grade;
        }
        return sum / allGrades.size();
    }
    public void printTotalAverage() {
        System.out.println(this.name + " total average is: " + this.calculateTotalGrade());
    }
    public void printTotalUnits() {
        System.out.print("Total units is:");
        int totalUnits = 0;
        for (Course course : courses) {
            totalUnits += course.getUnitsOfTheCourse();
        }
        System.out.println(totalUnits);
    }
    public void listAllGrades(){
        this.grades =  this.coursesAndGrades.values();
    }
    public double minGrade() {
        if (grades.isEmpty()){
            System.out.println("Not any grade yet");
            return 0;
        }
        double minimum = 20;
        for (double grade : grades){
            if (grade < minimum){
                minimum = grade;
            }
        }
        return minimum;
    }
    public double maxGrade() {
        if (grades.isEmpty()){
            System.out.println("Not any grade yet");
            return 0;
        }
        double maximum = 0;
        for (double grade : grades){
            if (grade > maximum){
                maximum = grade;
            }
        }
        return maximum;
    }
}
