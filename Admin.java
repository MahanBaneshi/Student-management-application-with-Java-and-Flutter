
import java.util.Objects;
import java.util.Scanner;

public class Admin {
    private static Admin instance;
    private long id;
    public String password;
    static {
        Admin admin = new Admin();
    }
    private Admin(){
        this.id = 123456789;
        this.password = "sbu.admin";
    }
    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    public long getUsername() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void addStudentToApp(Student student){
        Scanner scanner = new Scanner(System.in);
        long studentId = scanner.nextLong();
        String studentPassword = scanner.next();
        if (String.valueOf(studentId) != String.valueOf(student.getId())){
            System.out.println("Invalid studentId");
        } else if (studentPassword != student.getPassword()) {
            System.out.println("Invalid password");
        }
        else {
            System.out.println("welcome!");
        }
    }
    public void addTeacherToApp(Teacher teacher){
        Scanner scanner = new Scanner(System.in);
        long teacherId = scanner.nextLong();
        String teacherPassword = scanner.next();
        if (teacherId != teacher.getId()){
            System.out.println("Invalid studentId");
        } else if (!Objects.equals(teacherPassword, teacher.getPassword())) {
            System.out.println("Invalid password");
        }
        else {
            System.out.println("welcome!");
        }
    }
    public void addCourseToApp(){

    }
}
