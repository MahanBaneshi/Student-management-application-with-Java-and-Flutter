//import java.io.IOException;
//import java.util.*;
//public class Main {
//    public static void main(String[] args) throws IOException {
//        Admin admin = Admin.getInstance();
//
//        Course c1 = new Course("Calculus1");
//        Course c2 = new Course("Calculus2");
//        Course c3 = new Course("Ode");
//        Course c4 = new Course("ITP");
//        Course c5 = new Course("IE");
//        Course c6 = new Course("SE");
//
//        Teacher t1 = new Teacher("Sadegh", "Aliakbari");
//        Teacher t2 = new Teacher("Dr", "Chopogloo", List.of(c1 , c2, c3) );
//
//        t1.setCourses(List.of(c4, c5, c6));
//
//        Student s1 = new Student(555243099);
//        Student s2 = new Student("Mahan");
//        Student s3 = new Student("Ali Alavi", 555243117);
//
//        s1.setName("Taghi Tagavi");
//        s2.setId(555243002);
//
//        Course c7 = new Course("Calculus1");
//        Course c8 = new Course("AP", t1, 3 );
//
//        t2.addCourse(c7);
//
//        s2.setCourses(List.of(c1, c2, c3, c6));
//        c1.setUnitsOfTheCourse(3);
//        c6.setUnitsOfTheCourse(4);
//        c2.setUnitsOfTheCourse(2);
//        c3.setUnitsOfTheCourse(1);
//        s2.printTotalUnits();
//
//        s2.addGradeInCourse(c1, 10);
//        s2.addGradeInCourse(c2, 20);
//        s2.addGradeInCourse(c3, 15);
//        s2.printTotalAverage();
//
//        c5.addStudent(s1);
//        c5.addStudent(s2);
//        c5.addStudent(s3);
//        s2.setId(555489452);
//        c5.printStudents();
//    }
//
//}