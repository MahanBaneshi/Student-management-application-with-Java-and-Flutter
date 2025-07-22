import javax.sound.midi.Soundbank;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class cli { //Based on working with text files

    public static void main(String[] args) throws IOException {
        System.out.println("Hello! welcome to Sbu!!");
        login();
    }
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    public static void login() throws IOException {
        System.out.println("Please specify your role");
        System.out.println("1: student");
        System.out.println("2: teacher");
        System.out.println("3: admin");
        System.out.println("4: Sign up"+ "(have no account)");
        // Before the login you should sign up
        Scanner scanner = new Scanner(System.in);
        int answer = scanner.nextInt();
        switch (answer) {
            case 1:
                studentLogin();
                break;
            case 2:
                teacherLogin();
                break;
            case 3:
                adminLogin();
                break;
            case 4:
                signUp();
                break;
            default:
                System.out.println("Please enter a valid number");
                login();
        }
    }
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void adminLogin () throws IOException {
        //In this method, we check the validity of the entered username and password for the admin
        //For admin, we have only one username and password
        //That specification is stored in a text file

        System.out.println("Here is admin login");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your username and password:");
        System.out.print("username: ");
        String username = scanner.nextLine();

        System.out.print("password: ");
        String password = scanner.nextLine();
        try {
            File file = new File("D:\\University\\AP\\project\\project files\\Admin info.txt");
            Scanner fileScanner = new Scanner(file);

            boolean found = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.contains(", Password: ")) {
                    String[] parts = line.split(", Password: ");
                    String usernameFromFile = parts[0].substring(10);
                    String passwordFromFile = parts[1];
                    if (username.equals(usernameFromFile) && password.equals(passwordFromFile)) {
                        found = true;
                        break;
                    }
                } else {
                    System.out.println("Invalid line in file: " + line);
                }
            }
            if (found) {
                System.out.println("Login successful");
                long l = Long.parseLong(username);
                Admin admin = Admin.getInstance();
                adminAfterLog();
            } else {
                System.out.println("Invalid username or password");
                login();
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void adminAfterLog() throws IOException {
        //In this method, we show the tasks that the admin can do

        Scanner scanner = new Scanner(System.in);
        System.out.println("What are you going to do?");
        System.out.println("1: Define the course");
        System.out.println("2: delete the course");
        System.out.println("3: Student registration");
        System.out.println("4: delete the student");
        System.out.println("5: Teacher registration");
        System.out.println("6: delete the teacher");
        System.out.println("7: Do some teacher works!");
        System.out.println("8: Do some student works!");
        // We always provide a way back for the user!!
        System.out.println("9: Back to login page");


        int ans = scanner.nextInt();
        switch (ans){
            case 1:
                difineCourse();
                break;
            case 2:
                deleteCourse();
                break;
            case 3:
                studentRegis();
                break;
            case 4:
                deleteStudent();
                break;
            case 5:
                teacherRegis();
                break;
            case 6:
                deleteTeacher();
                break;
            case 7:
                teacherLogin();
                break;
            case 8:
                studentLogin();
                break;
            case 9:
                login();
                break;
            default:
                System.out.println("Please enter a valid number");
                adminAfterLog();
                break;
        }
    }

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void difineCourse() throws IOException {
        //In this method, the admin can define a course and perform tasks for that course
        /*
        Each course that is born as an object creates two files.
        One for maintaining the students of that course and the other for maintaining other details
         */
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please print the course name");
        String courseName = scanner.next();
        Course course = new Course(courseName);

        System.out.println("What do you want to do with this course?");
        System.out.println("1: Choose a teacher");
        System.out.println("2: Add student");
        System.out.println("3: Determine the number of units");
        System.out.println("4: Back");

        int x = scanner.nextInt();
        switch (x){
            case 1:
                // Both the teacher ID is written in the course file and the course name is written in the teacher file
                // These two tasks are done in the methods of teacher and course classes
                System.out.println("Please enter the teacher id");
                long teacherId = scanner.nextLong();
                Teacher teacher = new Teacher(teacherId);
                course.setTeacher(teacher);
                teacher.addCourse(course);
                System.out.println("Done");
                adminAfterLog();
                break;
            case 2:
                // Both the student ID is written in the course file and the course name is written in the student file
                System.out.println("Please enter the student id");
                long studentIdForAdd = scanner.nextLong();
                Student student = new Student(studentIdForAdd);
                course.addStudent(student);
                student.getCourses().add(course);

                student.courseWriter.write("Course: " + course.getName() + "\n");
                student.courseWriter.flush();

                course.studentsWriter.write("Student: " + student.getId() + "\n");
                course.studentsWriter.flush();
                System.out.println("Done");
                adminLogin();
                break;
            case 3:
                System.out.println("Please enter the number of units");
                int unitsNum = scanner.nextInt();
                course.infoWriter.write("number of the units: " + unitsNum + "\n");
                course.infoWriter.flush();
                break;
            case 4:
                adminAfterLog();
                break;
            default:
                System.out.println("Please enter a valid number");
                difineCourse();
                break;
        }
    }

    public static void deleteCourse() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the course name:");
        String courseName = scanner.nextLine();

        // Delete the name of the course from the student file
        String courseStudentsPath = "D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                + courseName + "_students.txt";
        List<String> studentIds = new ArrayList<>();
        try {
            File file = new File(courseStudentsPath);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String studentId = line.split(": ")[1].split(", grade")[0];
                studentIds.add(studentId);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file");
            e.printStackTrace();
        }

        for (String studentId : studentIds) {
            String studentCoursesPath = "D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                    + studentId + "_courses.txt";
            try {
                List<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(studentCoursesPath)));
                lines.removeIf(line -> line.contains(courseName));
                Files.write(Paths.get(studentCoursesPath), lines);
            } catch (IOException e) {
                System.out.println("An error occurred while updating the student's courses");
                e.printStackTrace();
            }
        }

        // Delete the name of the course from the teacher's file of that course
        String courseInfoPath = "D:\\University\\AP\\project\\project files\\courses\\courseInformations\\"
                + courseName + "_info.txt";
        String teacherId = "";
        try {
            File file = new File(courseInfoPath);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.startsWith("Teacher ID: ")) {
                    teacherId = line.split(": ")[1];
                    break;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file");
            e.printStackTrace();
        }

        String teacherCoursesPath = "D:\\University\\AP\\project\\project files\\teachers\\teacherCourses\\"
                + teacherId + "_courses.txt";
        try {
            List<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(teacherCoursesPath)));
            lines.removeIf(line -> line.contains(courseName));
            Files.write(Paths.get(teacherCoursesPath), lines);
        } catch (IOException e) {
            System.out.println("An error occurred while updating the teacher's courses");
            e.printStackTrace();
        }

        // Delete all assignments related to that course
        String assignmentsPath = "D:\\University\\AP\\project\\project files\\assignments\\";
        try {
            File file = new File(courseInfoPath);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.startsWith("Assignment: ")) {
                    String assignmentName = line.split(": ")[1].split(", Deadline")[0];
                    Files.deleteIfExists(Paths.get(assignmentsPath + assignmentName + ".txt"));
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while deleting the assignments");
            e.printStackTrace();
        }

        // Delete course files
        try {
            Files.deleteIfExists(Paths.get(courseStudentsPath));
            Files.deleteIfExists(Paths.get(courseInfoPath));
        } catch (IOException e) {
            System.out.println("An error occurred while deleting the course files");
            e.printStackTrace();
        }
        System.out.println("Done");
        adminAfterLog();
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


    public static void studentRegis() throws IOException {
        //In this method, the administrator defines a new student

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the student id:");
        System.out.print("student id: ");
        long studentId = scanner.nextLong();
        //Username verification
        while(!usernameValidator(studentId)) {
            System.out.println("This student id is not valid");
            System.out.println("Be careful that the student id is an 8-digit number (it is your ID).");
            System.out.println("Please enter the student id");
            System.out.print("student id: ");
            studentId = scanner.nextLong();
        }

        System.out.println("Please enter the password:");
        System.out.print("password: ");
        String password = scanner.next();
        //password verification
        while (!passwordValidator(password)) {
            System.out.println("This password in not valid");
            System.out.println("Be careful that the length of the password must be at least 8 and " +
                    "the password must consist of uppercase and lowercase letters as well as numbers");
            System.out.println("Please enter your password");
            System.out.print("password: ");
            password = scanner.next();
        }

        try { // Username and password in a specified file
            Student student = new Student(studentId);
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\" +
                    "Student info.txt", true);
            writer.write("Username: " + studentId + ", Password: " + password + "\n");
            writer.close();

            System.out.println("A new student has been registered with id: " + studentId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done");
        adminAfterLog();
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void deleteStudent() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the student ID:");
        String studentId = scanner.nextLine();

        String studentCoursesPath = "D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                + studentId + "_courses.txt";
        List<String> courseNames = new ArrayList<>();
        try {
            File file = new File(studentCoursesPath);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String courseName = line.split(": ")[1];
                courseNames.add(courseName);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file");
            e.printStackTrace();
        }

        // Deleting the student's name in his courses file
        String courseStudentsPath = "D:\\University\\AP\\project\\project files\\courses\\courseStudents\\";
        for (String courseName : courseNames) {
            try {
                File file = new File(courseStudentsPath + courseName + "_students.txt");
                List<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(file.getPath())));
                lines.removeIf(line -> line.contains(studentId));
                Files.write(Paths.get(file.getPath()), lines);
            } catch (IOException e) {
                System.out.println("An error occurred while updating the course's students");
                e.printStackTrace();
            }
        }

        // Deleting the student's files
        try {
            Files.deleteIfExists(Paths.get(studentCoursesPath));
            Files.deleteIfExists(Paths.get("D:\\University\\AP\\project\\project files\\students\\studentInfos\\"
                    + studentId + "_info.txt"));
        } catch (IOException e) {
            System.out.println("An error occurred while deleting the student's files");
            e.printStackTrace();
        }
        System.out.println("Done");
        adminAfterLog();
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void teacherRegis() throws IOException {
        //In this method, the administrator defines a new teacher

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the teacher id:");
        System.out.print("teacher id: ");
        long teacherId = scanner.nextLong();
        //Username verification
        while(!usernameValidator(teacherId)) {
            System.out.println("This techer id is not valid");
            System.out.println("Be careful that the teacher id is an 8-digit number (it is your ID).");
            System.out.println("Please enter the teacher id");
            System.out.print("teacher id: ");
            teacherId = scanner.nextLong();
        }

        System.out.println("Please enter the password:");
        System.out.print("password: ");
        String password = scanner.next();
        //password verification
        while (!passwordValidator(password)) {
            System.out.println("This password in not valid");
            System.out.println("Be careful that the length of the password must be at least 8 and " +
                    "the password must consist of uppercase and lowercase letters as well as numbers");
            System.out.println("Please enter your password");
            System.out.print("password: ");
            password = scanner.next();
        }

        try { // Username and password in a specified file
            Teacher teacher = new Teacher(teacherId);
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\" +
                    "Teacher info.txt", true);
            writer.write("Username: " + teacherId + ", Password: " + password + "\n");
            writer.close();

            System.out.println("A new teacher has been registered with id: " + teacherId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adminAfterLog();
    }

    public static void deleteTeacher() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the teacher ID:");
        String teacherId = scanner.nextLine();

        String teacherCoursesPath = "D:\\University\\AP\\project\\project files\\teachers\\teacherCourses\\" +
                teacherId + "_courses.txt";
        List<String> courseNames = new ArrayList<>();
        try {
            File file = new File(teacherCoursesPath);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String courseName = line.split(": ")[1];
                courseNames.add(courseName);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file");
            e.printStackTrace();
        }

        // Deleting the teacher's name in his courses file

        String courseInfosPath = "D:\\University\\AP\\project\\project files\\courses\\courseInformations\\";
        for (String courseName : courseNames) {
            try {
                File file = new File(courseInfosPath + courseName + "_info.txt");
                List<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(file.getPath())));
                lines.removeIf(line -> line.contains(teacherId));
                Files.write(Paths.get(file.getPath()), lines);
            } catch (IOException e) {
                System.out.println("An error occurred while updating the course's teachers");
                e.printStackTrace();
            }
        }

        // Deleting the teacher's files
        try {
            Files.deleteIfExists(Paths.get(teacherCoursesPath));
            Files.deleteIfExists(Paths.get("D:\\University\\AP\\project\\project files\\teachers\\teacherInfo\\"
                    + teacherId + "_info.txt"));
        } catch (IOException e) {
            System.out.println("An error occurred while deleting the teacher's files");
            e.printStackTrace();
        }
        System.out.println("Done");
        adminAfterLog();
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void teacherLogin () {
        //In this method, professors enter the application by entering valid ID and password.
        //Username and password must match what is in the database

        System.out.println("Here is teacher login");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your username and password:" + "username is your id");
        System.out.print("username: ");
        String username = scanner.nextLine();

        System.out.print("password: ");
        String password = scanner.nextLine();
        try {
            File file = new File("D:\\University\\AP\\project\\project files\\Teacher info.txt");
            Scanner fileScanner = new Scanner(file);

            boolean found = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(", Password: ");
                String usernameFromFile = parts[0].substring(10);
                String passwordFromFile = parts[1];

                if (username.equals(usernameFromFile) && password.equals(passwordFromFile)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                System.out.println("Login successful");
                long l = Long.parseLong(username);
                // When each teacher logs in, an object of the teacher's type is created
                Teacher teacher = new Teacher(l);
                teacherAfterLog(teacher);
            } else {
                System.out.println("Invalid username or password");
                login();
            }

            fileScanner.close();

        } catch (FileNotFoundException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void teacherAfterLog(Teacher teacher) throws IOException {
        //This method displays what the master can do
        /*
        With the birth of each teacher as an object, two text files are created.
        One for maintaining the teacher's courses and the other for maintaining other teacher's information
         */

        Scanner scanner = new Scanner(System.in);
        System.out.println("What are you going to do?");
        System.out.println("1: Complete the profile");
        System.out.println("2: View profile");
        System.out.println("3: View courses");
        System.out.println("4: Add a course");
        System.out.println("5: Add student to course");
        System.out.println("6: Remove student to course");
        System.out.println("7: Add assignment to course");
        System.out.println("8: Remove assignment to course");
        System.out.println("9: Grade student in course");
        System.out.println("10: Back to login page");
        int ans = scanner.nextInt();
        switch (ans){
            case 1:
                teacherCompleteProf(teacher);
                break;
            case 2:
                teacherViewProf(teacher);
                break;
            case 3:
                teacherViewCourses(teacher);
                break;
            case 4:
                teacherAddCourse(teacher);
                break;
            case 5:
                teacherAddStudent(teacher);
                break;
            case 6:
                teacherRemoveStudent(teacher);
                break;
            case 7:
                teacherAddAssignment(teacher);
                break;
            case 8:
                teacherRemoveAssignment(teacher);
                break;
            case 9:
                teacherGradeStudent(teacher);
                break;
            case 10:
                login();
            default:
                System.out.println("Please enter a valid number");
                teacherAfterLog(teacher);
                break;
        }
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void teacherCompleteProf(Teacher teacher) throws IOException {
        // Here the professor can complete his personal information
        // This information is stored in the teacher's file
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your first name");
        System.out.print("Firstname: ");
        String teacherFirstName = scanner.next();

        System.out.println("Please enter your last name");
        System.out.print("Lastname: ");
        String teacherLastName = scanner.next();

        FileWriter teacherFile = new FileWriter("D:\\University\\AP\\project\\project files\\teachers\\teacherInfo\\"
                + teacher.getId() + "_info.txt" , true);

        teacher.setFirstName(teacherFirstName);
        teacherFile.write("firstname: " + teacher.getFirstName() + "\n");
        teacher.setLastName(teacherLastName);
        teacherFile.write("lastname: " + teacher.getLastName() + "\n");
        teacherFile.close();
        System.out.println("done successfully");
        teacherAfterLog(teacher);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void teacherViewProf(Teacher teacher) throws IOException {
        // This is done by reading the teacher file
        String filePath = "D:\\University\\AP\\project\\project files\\teachers\\teacherInfo\\"
                + teacher.getId() + "_info.txt";
        try {
            File file = new File(filePath);
            Scanner reader = new Scanner(file);
            String firstName = reader.nextLine().split(": ")[1];
            String lastName = reader.nextLine().split(": ")[1];
            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);
            System.out.println("ID: " + teacher.getId());
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file");
            e.printStackTrace();
        }
        System.out.println("done successfully");
        teacherAfterLog(teacher);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void teacherViewCourses(Teacher teacher) throws IOException {
        // Here the professor can see the courses he is the professor of
        // This is done by reading the teacher file
        File teacherFile = new File("D:\\University\\AP\\project\\project files\\teachers\\teacherCourses\\"
                + teacher.getId() + "_courses.txt");

        try {
            Scanner fileReader = new Scanner(teacherFile);
            System.out.println("Courses for " + teacher.getId() + ":");

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                if (line.startsWith("Course: ")) {
                    System.out.println(line.substring(8));  // Print the course name
                }
            }

            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        teacherAfterLog(teacher);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void teacherAddCourse(Teacher teacher) {
        //Here the teacher can take a new course
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the course name:");
        String courseName = scanner.nextLine();

        File courseInfoFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseInformations\\"
                + courseName + "_info.txt");
        File teacherFile = new File("D:\\University\\AP\\project\\project files\\teachers\\teacherCourses\\"
                + teacher.getId() + "_courses.txt");
        File courseStuFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                + courseName + "_students.txt");

        try {
            //If this course has not been defined before, we will define it ourselves
            if (!courseInfoFile.exists()) {
                courseInfoFile.createNewFile();
                courseStuFile.createNewFile();
            }

            // Write the teacher's ID in the course file
            FileWriter courseWriter = new FileWriter(courseInfoFile);
            courseWriter.write("Teacher ID: " + teacher.getId() + "\n");
            courseWriter.close();

            if (!teacherFile.exists()) {
                teacherFile.createNewFile();
            }

            // Write the name of the course in the teacher's file
            FileWriter teacherWriter = new FileWriter(teacherFile, true);
            teacherWriter.write("Course: " + courseName + "\n");
            teacherWriter.close();

            System.out.println("The course has been added successfully!");
            teacherAfterLog(teacher);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void teacherAddStudent(Teacher teacher) throws IOException {
        //Here the professor can add students to the course he has taken
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the student ID:");
        System.out.print("student id: ");
        String studentId = scanner.nextLine();
        System.out.println("Please enter the course name:");
        System.out.print("course name: ");
        String courseName = scanner.nextLine();

        File studentCourseFile = new File("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                + studentId + "_courses.txt");
        File studentInfoFile = new File("D:\\University\\AP\\project\\project files\\students\\studentInfos"
                + studentId + "_info.txt");
        File courseStuFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                + courseName + "_students.txt");

            //If the entered student or course does not exist, an appropriate error message will be printed
        try {
            if (!studentCourseFile.exists()) {
                System.out.println("This student does not exist.");
                teacherAfterLog(teacher);
                return;
            }

            if (!courseStuFile.exists()) {
                System.out.println("This course does not exist.");
                teacherAfterLog(teacher);
                return;
            }
            // Write the name of the course in the student file
            FileWriter studentWriter = new FileWriter(studentCourseFile, true);
            studentWriter.write( "Course: " + courseName + "\n");
            studentWriter.close();

            // Write the student ID in the course file
            FileWriter courseWriter = new FileWriter(courseStuFile, true);
            courseWriter.write("Student: " + studentId + "\n");
            courseWriter.close();

            System.out.println("The student has been added to the course successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
        teacherAfterLog(teacher);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void teacherRemoveStudent(Teacher teacher) throws IOException {
        //In this method, the professor can remove a student from his course
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the student id:");
        System.out.print("student id: ");
        String studentId = scanner.next();
        System.out.println("Please enter the course name:");
        System.out.print("course name: ");
        String courseName = scanner.next();

        // The course must belong to the teacher
        File teacherFile = new File("D:\\University\\AP\\project\\project files\\teachers\\teacherCourses\\"
                + teacher.getId() + "_courses.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(teacherFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("Course: " + courseName)) {

                    Course courseToRemove = null;
                    for (Course course : teacher.getCourses()) {
                        if (course.getName().equals(courseName)) {
                            courseToRemove = course;
                            break;
                        }
                    }
                    if (courseToRemove != null) {
                        Student studentToRemove = null;
                        for (Student student : courseToRemove.getStudents()) {
                            if (String.valueOf(student.getId()).equals(String.valueOf(studentId))) {
                                studentToRemove = student;
                                break;
                            }
                        }
                        if (studentToRemove != null) {
                            courseToRemove.getStudents().remove(studentToRemove);
                            studentToRemove.getCourses().remove(courseToRemove);
                        }
                    }
                    // Our method is to write a new file as requested and replace the previous file
                    // Replace the student file
                    File oldFile = new File("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                            + studentId + "_courses.txt");
                    File newFile = new File("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                            + "new.txt");

                    BufferedReader studentReader = new BufferedReader(new FileReader(oldFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));

                    String lineToRemove = "Course: " + courseName;
                    String currentLine;
                    while((currentLine = studentReader.readLine()) != null) {
                        if(currentLine.equals(lineToRemove)) continue;
                        writer.write(currentLine + System.getProperty("line.separator"));
                    }
                    writer.close();
                    studentReader.close();
                    oldFile.delete();
                    newFile.renameTo(oldFile);

                    // Replace the course file
                    oldFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                            + courseName + "_students.txt");
                    newFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                            + "temp.txt");


                    studentReader = new BufferedReader(new FileReader(oldFile));
                    writer = new BufferedWriter(new FileWriter(newFile));

                    lineToRemove = "Student: " + studentId;

                    while((currentLine = studentReader.readLine()) != null) {
                        if(currentLine.equals(lineToRemove)) continue;
                        writer.write(currentLine + System.getProperty("line.separator"));
                    }
                    writer.close();
                    studentReader.close();
                    oldFile.delete();
                    newFile.renameTo(oldFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done");
        teacherAfterLog(teacher);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void teacherAddAssignment(Teacher teacher) {
        //In this method, the professor can add an assignment to the course
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the assignment name:");
        String assignmentName = scanner.next();
        System.out.println("Please enter the course name:");
        String courseName = scanner.next();
        System.out.println("Please enter the deadline:");
        String deadline = scanner.next();

        // The course must belong to the teacher
        File teacherFile = new File("D:\\University\\AP\\project\\project files\\teachers\\teacherCourses\\"
                + teacher.getId() + "_courses.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(teacherFile))) {
            String line;
            boolean teacherHasCourse = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("Course: " + courseName)) {
                    teacherHasCourse = true;
                    break;
                }
            }
            if (!teacherHasCourse) {
                System.out.println("The teacher does not teach this course.");
                teacherAfterLog(teacher);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If the teacher has the course, create a new assignment and add it to the course
        try {
            Course course = new Course(courseName);
            Assignment assignment = new Assignment(assignmentName, deadline, course);
            course.getAssignments().add(assignment);

            // Write the assignment's name and deadline to the course's file
            File courseFile =
                    new File("D:\\University\\AP\\project\\project files\\courses\\courseInformations\\"
                    + courseName + "_info.txt");
            FileWriter courseWriter = new FileWriter(courseFile , true);
            courseWriter.write("Assignment: " + assignmentName + ", Deadline: " + deadline + ", Active" + "\n");
            courseWriter.close();

            // Write the assignment's details to the assignment's file
            FileWriter assignmentWriter = new FileWriter("D:\\University\\AP\\project\\project files\\assignments\\"
                    + assignmentName + ".txt", true);
            assignmentWriter.write("Name: " + assignmentName + "\nCourse: " + courseName + "\nActive" + "\n");
            assignmentWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void teacherRemoveAssignment(Teacher teacher) throws IOException {
        // In this method, the teacher can remove an assignment from the course
        // For this purpose, we only change "active" to "active: false" in the files
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the course name:");
        String courseName = scanner.next();
        System.out.println("Please enter the assignment name:");
        String assignmentName = scanner.next();

        // The course must belong to the teacher
        File teacherFile = new File("D:\\University\\AP\\project\\project files\\teachers\\teacherCourses\\"
                + teacher.getId() + "_courses.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(teacherFile))) {
            String line;
            boolean teacherHasCourse = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("Course: " + courseName)) {
                    teacherHasCourse = true;
                    break;
                }
            }
            if (!teacherHasCourse) {
                System.out.println("The teacher does not teach this course.");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check if the assignment is in the course
        File courseFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseInformations\\"
                + courseName + "_info.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(courseFile))) {
            String line;
            boolean courseHasAssignment = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Assignment: " + assignmentName)) {
                    courseHasAssignment = true;
                    break;
                }
            }
            if (!courseHasAssignment) {
                System.out.println("The assignment is not in this course.");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If the teacher has the course and the assignment is in the course, deactivate the assignment
        try {
            // Deactivate the assignment in the assignment's file
            File assignmentFile = new File("D:\\University\\AP\\project\\project files\\assignments\\"
                    + assignmentName + ".txt");
            BufferedReader reader = new BufferedReader(new FileReader(assignmentFile));
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter("D:\\University\\AP\\project\\project files\\assignments\\" +
                            "temp.txt"));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Active")) {
                    line = "Active: false";
                }
                writer.write(line + System.lineSeparator());
            }
            writer.close();
            reader.close();
            assignmentFile.delete();
            new File("D:\\University\\AP\\project\\project files\\assignments\\temp.txt").renameTo(assignmentFile);

            // Deactivate the assignment in the course's file
            reader = new BufferedReader(new FileReader(courseFile));
            writer = new BufferedWriter(new FileWriter("D:\\University\\AP\\project\\project files\\courses\\courseInformations\\"
                    + "temp.txt"));

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Assignment: " + assignmentName)) {
                    line += ": false";
                }
                writer.write(line + System.lineSeparator());
            }
            writer.close();
            reader.close();
            courseFile.delete();
            new File("D:\\University\\AP\\project\\project files\\courses\\courseInformations\\" +
                    "temp.txt").renameTo(courseFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done");
        teacherAfterLog(teacher);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void teacherGradeStudent(Teacher teacher) throws IOException {
        //In this method, the professor can give grades to the students
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the student id:");
        String studentId = scanner.next();
        System.out.println("Please enter the course name:");
        String courseName = scanner.next();
        System.out.println("Please enter the grade:");
        String grade = scanner.next();

        // The course must belong to the teacher so that the professor can grade the student in it
        File teacherFile = new File("D:\\University\\AP\\project\\project files\\teachers\\teacherCourses\\"
                + teacher.getId() + "_courses.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(teacherFile))) {
            String line;
            boolean teacherHasCourse = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("Course: " + courseName)) {
                    teacherHasCourse = true;
                    break;
                }
            }
            if (!teacherHasCourse) {
                System.out.println("The teacher does not teach this course.");
                teacherAfterLog(teacher);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // The student must be in that course so that the teacher can grade the student
        File courseFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                + courseName + "_students.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(courseFile))) {
            String line;
            boolean courseHasStudent = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("Student: " + studentId)) {
                    courseHasStudent = true;
                    break;
                }
            }
            if (!courseHasStudent) {
                System.out.println("The student is not in this course.");
                teacherAfterLog(teacher);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add the grade to the course's file
        try {
            File inputFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                    + courseName + "_students.txt");
            File tempFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                    + "temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String lineToReplace = "Student: " + studentId;
            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                if(currentLine.equals(lineToReplace)) {
                    currentLine += ", grade: " + grade;
                }
                writer.write(currentLine + System.lineSeparator());
            }
            writer.close();
            reader.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add the grade to the student's file
        try {
            File inputFile = new File("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                    + studentId + "_courses.txt");
            File tempFile = new File("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                    + "temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String lineToReplace = "Course: " + courseName;
            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                if(currentLine.equals(lineToReplace)) {
                    currentLine += ", grade: " + grade;
                }
                writer.write(currentLine + System.lineSeparator());
            }
            writer.close();
            reader.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            e.getCause();
        }
        System.out.println("Done");
        teacherAfterLog(teacher);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void studentLogin () {
        //This is for students to enter
        System.out.println("Here is student login");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your username and password: " + "(Username is your student id)");
        System.out.print("username: ");
        String username = scanner.nextLine();

        System.out.print("password: ");
        String password = scanner.nextLine();
        try { // This file is where students ID and password are stored
            File file = new File("D:\\University\\AP\\project\\project files\\Student info.txt");
            Scanner fileScanner = new Scanner(file);

            boolean found = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(", Password: ");
                String usernameFromFile = parts[0].substring(10);
                String passwordFromFile = parts[1];
                //If the username and password entered by the student are in the file, the student can enter the application
                if (username.equals(usernameFromFile) && password.equals(passwordFromFile)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                System.out.println("Login successful");
                long l = Long.parseLong(username);
                Student student = new Student(l);
                studentAfterLog(student);
            } else {
                System.out.println("Invalid username or password");
                login();
            }

            fileScanner.close();

        } catch (FileNotFoundException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void studentAfterLog(Student student) throws IOException {
        //This method displays what a student can do
        /*
        Two files are created for each student who is born as an object
        One for the courses it has and the other for other specifications
        All methods are implemented with these two files
         */
        Scanner scanner = new Scanner(System.in);
        System.out.println("What are you going to do?");
        System.out.println("1: Complete the profile");
        System.out.println("2: View profile");
        System.out.println("3: View courses");
        System.out.println("4: See the courses in detail (number of units and grades and teachers)");
        System.out.println("5: Taking a course");
        System.out.println("6: Back to login page");
        int ans = scanner.nextInt();
        switch (ans){
            case 1:
                studentCompleteProf(student);
                break;
            case 2:
                studentViewProf(student);
                break;
            case 3:
                studentViewCourses(student);
                break;
            case 4:
                studentViewFullCourse(student);
                break;
            case 5:
                studentTakeCourse(student);
                break;
            case 6:
                login();
                break;
            default:
                System.out.println("Please enter a valid number");
                studentAfterLog(student);
                break;
        }

    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void studentCompleteProf(Student student) throws IOException {
        // We write this information from the student's file
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the full name of the student:");
        String fullName = scanner.nextLine();

        try {
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\students\\studentInfos\\"
                    + student.getId() + "_info.txt");
            writer.write("Full Name: " + fullName + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done");
        studentAfterLog(student);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void studentViewProf(Student student) throws IOException {
        // We read this information from the student's file
        String filePath = "D:\\University\\AP\\project\\project files\\students\\studentInfos\\"
                + student.getId() + "_info.txt";
        try {
            File file = new File(filePath);
            Scanner reader = new Scanner(file);
            String fullName = reader.nextLine().split(": ")[1];
            System.out.println("Full Name: " + fullName);
            System.out.println("ID: " + student.getId());
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        studentAfterLog(student);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void studentViewCourses(Student student) throws IOException {
        try { // We read the courses from the student file
            File file = new File("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                    + student.getId() + "_courses.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            System.out.println("Courses for student " + student.getId() + ":");
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Course: ")) {
                    System.out.println(line.substring(8));  // Print the course name
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        // The program should not end
        studentAfterLog(student);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void studentViewFullCourse(Student student) throws IOException {

        List<String> studentCourses = new ArrayList<>();
        try {
            File file = new File("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                    + student.getId() + "_courses.txt");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String courseName = line.split(": ")[1];
                if (courseName.contains(", grade")) {
                    courseName = courseName.split(",")[0];
                }
                studentCourses.add(courseName);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file");
            e.printStackTrace();
        }
          System.out.println("Your courses: " + studentCourses);
//        studentCourses.removeIf("grade"::equals);
//        System.out.println(studentCourses);

        String courseInfosPath = "D:\\University\\AP\\project\\project files\\courses\\courseInformations\\";
        for (String course : studentCourses) {
                try {
                    System.out.println(course + ":");
                    File courseFile = new File(courseInfosPath + course + "_info.txt");
                    Scanner courseReader = new Scanner(courseFile);
                    while (courseReader.hasNextLine()) {
                        String line = courseReader.nextLine();
                        System.out.println(line);
                    }
                    courseReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred while reading the course file");
                    e.printStackTrace();
                }
            }
        studentAfterLog(student);
        }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void studentTakeCourse(Student student) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the course name to add:");
        String courseName = scanner.next();

        // This loop and condition is related to the discussion of objectism
        Course courseToAdd = null;
        for (Course course : student.getCourses()) {
            if (course.getName().equals(courseName)) {
                courseToAdd = course;
                break;
            }
        }
        if (courseToAdd != null) {
            student.getCourses().add(courseToAdd);
            courseToAdd.getStudents().add(student);
        }

        try { // The name of the course must be written in the student's file
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                    + student.getId() + "_courses.txt", true);
            writer.write("Course: " + courseName + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try { // The name of the student must be written in the course file
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                    + courseName + "_students.txt", true);
            writer.write("Student: " + student.getId() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        studentAfterLog(student);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void signUp () throws IOException {
        //This is for registration
        System.out.println("Choose the option you want");
        System.out.println("1: Sign up as a student");
        System.out.println("2: Sign up as a teacher");
        System.out.println("3: Sign up as an admin");
        Scanner scanner = new Scanner(System.in);
        int answer = scanner.nextInt();
        switch (answer) {
            case 1:
                studentSignIn();
                break;
            case 2:
                teacherSignIn();
                break;
            case 3:
                adminSignIn();
                break;
            default:
                //We leave this so that the program does not end with a mistake
                System.out.println("Please enter a valid number");
                signUp();
                break;
        }
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void adminSignIn() {
        //We assumed that there should be only one default admin and there is no need to implement this method.
    }

    public static void teacherSignIn() throws IOException {
        System.out.println("Here is teacher sign up");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your username" + "(your id)");
        System.out.print("username: ");
        long username = scanner.nextLong();

        while (!usernameValidator(username)) { //Username must be validated
            System.out.println("This username is not valid");
            System.out.println("Be careful that the username is an 8-digit number (it is your ID).");
            System.out.println("Please enter your username" + "(your id)");
            System.out.print("username: ");
            username = scanner.nextLong();
        }

        System.out.println("Please enter your password");
        System.out.print("password: ");
        String password = scanner.next();

        while (!passwordValidator(password)) { // Password must be validated
            System.out.println("This password in not valid");
            System.out.println("Be careful that the length of the password must be at least 8 and " +
                    "the password must consist of uppercase and lowercase letters as well as numbers");
            System.out.println("Please enter your password");
            System.out.print("password: ");
            password = scanner.next();
        }
        try { //Username and password are stored in the database for use
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\Teacher "
                    + "info.txt", true);
            writer.write("Username: " + username + ", Password: " + password + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }

        System.out.println("Sign up was successful");
        //The program should not end
        login();
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void studentSignIn() throws IOException {
        System.out.println("Here is student sign up");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your username" + "(your student id)");
        System.out.print("username: ");
        long username = scanner.nextLong();

        while (!usernameValidator(username)) { //Username must be validated
            System.out.println("This username is not valid");
            System.out.println("Be careful that the username is an 8-digit number (it is your ID).");
            System.out.println("Please enter your username" + "(your student id)");
            System.out.print("username: ");
            username = scanner.nextLong();
        }

        System.out.println("Please enter your password");
        System.out.print("password: ");
        String password = scanner.next();

        while (!passwordValidator(password)) { // Password must be validated
            System.out.println("This password in not valid");
            System.out.println("Be careful that the length of the password must be at least 8 and " +
                    "the password must consist of uppercase and lowercase letters as well as numbers");
            System.out.println("Please enter your password");
            System.out.print("password: ");
            password = scanner.next();
        }
        try {//Username and password are stored in the database for use
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\Student info.txt", true);
            writer.write("Username: " + username + ", Password: " + password + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error during work with file");
        }

        System.out.println("Sign up was successful");
        //The program should not end
        login();
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static boolean usernameValidator(long username){
        return username >= 100000000 && username <= 999999999;
    }
    public static boolean passwordValidator(String password){
        // Valid passwords have the terms that apply here
        if (password.length() < 8) {
            return false;
        }

        boolean hasUpper = false, hasLower = false, hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }

            if (hasUpper && hasLower && hasDigit) {
                return true;
            }
        }
        return false;
    }
}
