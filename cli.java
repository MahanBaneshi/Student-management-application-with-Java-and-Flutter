import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class cli {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello! welcome to Sbu!!");
        login();
    }

    public static void login() throws IOException {
        System.out.println("Please specify your role");
        System.out.println("1: student");
        System.out.println("2: teacher");
        System.out.println("3: admin");
        System.out.println("4: Sign up"+ "(have no account)");
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
        }
    }

    public static void adminLogin () throws IOException {
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
                    //System.out.println(usernameFromFile);
                    String passwordFromFile = parts[1];
                    //System.out.println(passwordFromFile);
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

    public static void adminAfterLog() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What are you going to do?");
        System.out.println("1: Define the course");
        System.out.println("2: Student registration");
        System.out.println("3: Teacher registration");
        System.out.println("4: Back to login page");

        int ans = scanner.nextInt();
        switch (ans){
            case 1:
                difineCourse();
                break;
            case 2:
                studentRegis();
                break;
            case 3:
                teacherRegis();
                break;
            case 4:
                login();
                break;
        }
    }


    public static void difineCourse() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please print the course name");
        String courseName = scanner.next();
        Course course = new Course(courseName);

        System.out.println("What do you want to do with this course?");
        System.out.println("1: Choose a teacher");
        System.out.println("2: Add student");
        System.out.println("3: Remove student");
        System.out.println("4: Back");

        int x = scanner.nextInt();
        switch (x){
            case 1:
                System.out.println("Please enter the teacher id");
                long teacherId = scanner.nextLong();
                Teacher teacher = new Teacher(teacherId);
                course.setTeacher(teacher);
                teacher.addCourse(course);
                System.out.println("Done");
                adminAfterLog();
                break;
            case 2:
                System.out.println("Please enter the student id");
                long studentIdForAdd = scanner.nextLong();
                Student student = new Student(studentIdForAdd);
                course.addStudent(student);
                student.getCourses().add(course);

                student.courseWriter.write("\nCourse: " + course.getName());
                student.courseWriter.flush();

                course.studentsWriter.write("\nStudent: " + student.getId());
                course.studentsWriter.flush();
                System.out.println("Done");
                adminLogin();
                break;
            case 3:
                System.out.println("Please enter the student id");
                long studentIdForRemove = scanner.nextLong();
            case 4:
                adminAfterLog();
                break;
        }
    }

    public static void studentRegis() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the student id:");
        System.out.print("student id: ");
        long studentId = scanner.nextLong();
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
        while (!passwordValidator(password)) {
            System.out.println("This password in not valid");
            System.out.println("Be careful that the length of the password must be at least 8 and " +
                    "the password must consist of uppercase and lowercase letters as well as numbers");
            System.out.println("Please enter your password");
            System.out.print("password: ");
            password = scanner.next();
        }

        try {
            Student student = new Student(studentId);
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\Student info.txt", true);
            writer.write("\nUsername: " + studentId + ", Password: " + password);
            writer.close();

            System.out.println("A new student has been registered with id: " + studentId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adminAfterLog();
    }
    public static void teacherRegis() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the teacher id:");
        System.out.print("teacher id: ");
        long teacherId = scanner.nextLong();
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
        while (!passwordValidator(password)) {
            System.out.println("This password in not valid");
            System.out.println("Be careful that the length of the password must be at least 8 and " +
                    "the password must consist of uppercase and lowercase letters as well as numbers");
            System.out.println("Please enter your password");
            System.out.print("password: ");
            password = scanner.next();
        }

        try {
            Teacher teacher = new Teacher(teacherId);
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\Teacher info.txt", true);
            writer.write("\nUsername: " + teacherId + ", Password: " + password);
            writer.close();

            System.out.println("A new teacher has been registered with id: " + teacherId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adminAfterLog();
    }

    public static void teacherLogin () {
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


    public static void teacherAfterLog(Teacher teacher) throws IOException {
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
        }
    }
    public static void teacherCompleteProf(Teacher teacher) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your first name");
        System.out.print("Firstname: ");
        String teacherFirstName = scanner.next();

        System.out.println("Please enter your last name");
        System.out.print("Lastname: ");
        String teacherLastName = scanner.next();

        FileWriter teacherFile = new FileWriter("D:\\University\\AP\\project\\project files\\teachers\\"
        + teacher.getId() + ".txt" , true);

        teacher.setFirstName(teacherFirstName);
        teacherFile.write("\nfirstname: " + teacher.getFirstName());
        teacher.setLastName(teacherLastName);
        teacherFile.write("\nlastname: " + teacher.getLastName());
        teacherFile.close();
        System.out.println("done successfully");
        teacherAfterLog(teacher);
    }
    public static void teacherViewProf(Teacher teacher) throws IOException {
        String filePath = "D:\\University\\AP\\project\\project files\\teachers\\" + teacher.getId() + ".txt";
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

    }

    public static void teacherViewCourses(Teacher teacher) throws IOException {
        File teacherFile = new File("D:\\University\\AP\\project\\project files\\teachers\\" + teacher.getId() + ".txt");

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

    public static void teacherAddCourse(Teacher teacher) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the course name:");
        String courseName = scanner.nextLine();

        File courseInfoFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseInformations\\"
                + courseName + "_info.txt");
        File teacherFile = new File("D:\\University\\AP\\project\\project files\\teachers\\" + teacher.getId() + ".txt");
        File courseStuFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                + courseName + "_students.txt");

        try {

            if (!courseInfoFile.exists()) {
                courseInfoFile.createNewFile();
                courseStuFile.createNewFile();
            }

            FileWriter courseWriter = new FileWriter(courseInfoFile);
            courseWriter.write("Teacher ID: " + teacher.getId() + "\n");
            courseWriter.close();

            if (!teacherFile.exists()) {
                teacherFile.createNewFile();
            }

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

    public static void teacherAddStudent(Teacher teacher) throws IOException {
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

            FileWriter studentWriter = new FileWriter(studentCourseFile, true);
            studentWriter.write("\n" + "Course: " + courseName);
            studentWriter.close();

            FileWriter courseWriter = new FileWriter(courseStuFile, true);
            courseWriter.write("Student ID: " + studentId + "\n");
            courseWriter.close();

            System.out.println("The student has been added to the course successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            teacherAfterLog(teacher);
        }
    }

    public static void teacherRemoveStudent(Teacher teacher) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the student id:");
        System.out.print("stusent id: ");
        String studentId = scanner.next();
        System.out.println("Please enter the course name:");
        System.out.print("course name: ");
        String courseName = scanner.next();

        File teacherFile = new File("D:\\University\\AP\\project\\project files\\teachers\\"
                + teacher.getId() + ".txt");
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
                        writer.write(currentLine);
                        writer.newLine();
                    }
                    writer.close();
                    studentReader.close();
                    oldFile.delete();
                    newFile.renameTo(oldFile);

                    oldFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                            + courseName + "_students.txt");
                    newFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                            + "temp.txt");


                    studentReader = new BufferedReader(new FileReader(oldFile));
                    writer = new BufferedWriter(new FileWriter(newFile));

                    lineToRemove = "Student: " + studentId;

                    while((currentLine = studentReader.readLine()) != null) {
                        if(currentLine.equals(lineToRemove)) continue;
                        writer.write(currentLine);
                        writer.newLine();
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

    public static void teacherAddAssignment(Teacher teacher) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the assignment name:");
        String assignmentName = scanner.next();
        System.out.println("Please enter the course name:");
        String courseName = scanner.next();
        System.out.println("Please enter the deadline:");
        String deadline = scanner.next();

        // Check if the teacher is the teacher of the course
        File teacherFile = new File("D:\\University\\AP\\project\\project files\\teachers\\"
                + teacher.getId() + ".txt");
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
            FileWriter courseWriter =
                    new FileWriter("D:\\University\\AP\\project\\project files\\courses\\courseInformations\\"
                    + courseName + "_info.txt", true);
            courseWriter.write("\nAssignment: " + assignmentName + ", Deadline: " + deadline + "Active");
            courseWriter.close();

            // Write the assignment's details to the assignment's file
            FileWriter assignmentWriter = new FileWriter("D:\\University\\AP\\project\\project files\\assignments\\"
                    + assignmentName + ".txt", true);
            assignmentWriter.write("Name: " + assignmentName + "\nCourse: " + courseName + "\nActive");
            assignmentWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void teacherRemoveAssignment(Teacher teacher) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the course name:");
        String courseName = scanner.next();
        System.out.println("Please enter the assignment name:");
        String assignmentName = scanner.next();

        // Check if the teacher is the teacher of the course
        File teacherFile = new File("D:\\University\\AP\\project\\project files\\teachers\\" + teacher.getId() + ".txt");
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
                    new BufferedWriter(new FileWriter("D:\\University\\AP\\project\\project files\\assignments\\temp.txt"));

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
            writer = new BufferedWriter(new FileWriter("D:\\University\\AP\\project\\project files\\courses\\courseInformations\\temp.txt"));

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Assignment: " + assignmentName)) {
                    line += ", Active: false";
                }
                writer.write(line + System.lineSeparator());
            }
            writer.close();
            reader.close();
            courseFile.delete();
            new File("D:\\University\\AP\\project\\project files\\courses\\courseInformations\\temp.txt").renameTo(courseFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void teacherGradeStudent(Teacher teacher) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the student id:");
        String studentId = scanner.next();
        System.out.println("Please enter the course name:");
        String courseName = scanner.next();
        System.out.println("Please enter the grade:");
        String grade = scanner.next();

        // Check if the teacher is the teacher of the course
        File teacherFile = new File("D:\\University\\AP\\project\\project files\\teachers\\"
                + teacher.getId() + ".txt");
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
            e.printStackTrace();
        }
        teacherAfterLog(teacher);
    }

    public static void studentLogin () {
        System.out.println("Here is student login");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your username and password:" + "Username is your student id");
        System.out.print("username: ");
        String username = scanner.nextLine();

        System.out.print("password: ");
        String password = scanner.nextLine();
        try {
            File file = new File("D:\\University\\AP\\project\\project files\\Student info.txt");
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
    public static void studentAfterLog(Student student) throws IOException {
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
        }

    }

    public static void studentCompleteProf(Student student) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name");
        System.out.print("Name: ");
        String studentName = scanner.next();
        student.setName(studentName);
        System.out.println("done successfully");
        studentAfterLog(student);
    }

    public static void studentViewProf(Student student) throws IOException {
        System.out.println("Student name: " + student.getName());
        System.out.println("Student id: " + student.getId());
        studentAfterLog(student);
    }

    public static void studentViewCourses(Student student) {
        try {
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
            e.printStackTrace();
        }

    }

    public static void studentViewFullCourse(Student student) {

    }

    public static void studentTakeCourse(Student student) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the course name to add:");
        String courseName = scanner.next();


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

        try {
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                    + student.getId() + "_courses.txt", true);
            writer.write("\nCourse: " + courseName);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                    + courseName + "_students.txt", true);
            writer.write("\nStudent: " + student.getId());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void signUp () throws IOException {
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
                System.out.println("Please enter a valid number");
                signUp();
        }
    }

    public static void adminSignIn() {

    }

    public static void teacherSignIn() throws IOException {
        System.out.println("Here is teacher sign up");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your username" + "(your id)");
        System.out.print("username: ");
        long username = scanner.nextLong();

        while (!usernameValidator(username)) {
            System.out.println("This username is not valid");
            System.out.println("Be careful that the username is an 8-digit number (it is your ID).");
            System.out.println("Please enter your username" + "(your id)");
            System.out.print("username: ");
            username = scanner.nextLong();
        }

        System.out.println("Please enter your password");
        System.out.print("password: ");
        String password = scanner.next();

        while (!passwordValidator(password)) {
            System.out.println("This password in not valid");
            System.out.println("Be careful that the length of the password must be at least 8 and " +
                    "the password must consist of uppercase and lowercase letters as well as numbers");
            System.out.println("Please enter your password");
            System.out.print("password: ");
            password = scanner.next();
        }
        try {
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\Teacher "
                    + "info.txt", true);
            writer.write("Username: " + username + ", Password: " + password + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }

        System.out.println("Sign up was successful");
        login();
    }

    public static void studentSignIn() throws IOException {
        System.out.println("Here is student sign up");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your username" + "(your student id)");
        System.out.print("username: ");
        long username = scanner.nextLong();

        while (!usernameValidator(username)) {
            System.out.println("This username is not valid");
            System.out.println("Be careful that the username is an 8-digit number (it is your ID).");
            System.out.println("Please enter your username" + "(your student id)");
            System.out.print("username: ");
            username = scanner.nextLong();
        }

        System.out.println("Please enter your password");
        System.out.print("password: ");
        String password = scanner.next();

        while (!passwordValidator(password)) {
            System.out.println("This password in not valid");
            System.out.println("Be careful that the length of the password must be at least 8 and " +
                    "the password must consist of uppercase and lowercase letters as well as numbers");
            System.out.println("Please enter your password");
            System.out.print("password: ");
            password = scanner.next();
        }
        try {
            FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\Student info.txt", true);
            writer.write("Username: " + username + ", Password: " + password + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error during work with file");
        }

        System.out.println("Sign up was successful");
        login();
    }

    public static boolean usernameValidator(long username){
        return username >= 100000000 && username <= 999999999;
    }
    public static boolean passwordValidator(String password){
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
