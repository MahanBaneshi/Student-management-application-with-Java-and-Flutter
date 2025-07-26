import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the server");
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true){
            System.out.println("Waiting for client...");
            new ClientHandler(serverSocket.accept()).start();
        }
    }
}
class ClientHandler extends Thread {
    Socket socket;
    DataOutputStream dos;
    DataInputStream dis;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
        System.out.println("Connected to server");
    }

    public String listener() throws IOException {
        System.out.println("listener");
        StringBuilder sb = new StringBuilder();
        int index = dis.read();
        while (index != 0) {
            sb.append((char) index);
            index = dis.read();
        }
        System.out.println("listener2");
        return sb.toString();
    }

    public void writer(String text) throws IOException {
        dos.writeBytes(text);
        dos.flush();
        dos.close();
        dis.close();
        socket.close();
        System.out.println(text);
        System.out.println("command finished and response sent to server");
    }

    @Override
    public void run() {
        super.run();
        String command;
        try {
            command = listener();
            System.out.println("command received: { " + command + " }");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] split = command.split("~");//login$402243000$fg1999
        for (String s : split) {
            System.out.println(s);
        }
        try {
            switch (split[0]) {
                case "login":
                    logJava(split); //login~studentId~password
                    break;
                case "sign in":
                    signJava(split); //sign in~studentId~password~fullName
                    break;
                case "takeCourse":
                    takeCourse(split); //takeCourse~Id~courseName
                    break;
                case "viewCourses": //viewCourses~Id
                    viewCourses(split);
                    break;
                case "viewProf": //viewProf~ID
                    viewProf(split);
                    break;
                case "assignments":
                    seeAssignments(split);
                    break;
                case "allCourses": //allCourses
                    allCourses();
                    break;
                case "stInfo": //stInfo~username
                    stInfo(split);
                    break;
                case "changePass": //changePass~ID~newPass
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void logJava(String[] split) {
        boolean logIn = false;
        int responseOfDatabase = 100;
        try {
            responseOfDatabase = Database.loginChecker(split[1], split[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (responseOfDatabase == 2) {
            logIn = true;
            System.out.println("status code is 200");
            System.out.println("Successfully logged in!");
            try {
                writer("200");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (responseOfDatabase == 1) {
            System.out.println("status code is 401");
            System.out.println("Password is incorrect!");
            try {
                writer("401");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("status code is 404");
            System.out.println("username not found!");
            try {
                writer("404");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (logIn) {
            long l = Long.parseLong(split[1]);
            Student student = null;
            try {
                student = new Student(l);
                cli.studentAfterLog(student);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void signJava(String[] split) throws IOException {
        boolean signIn = false;
        int responseOfDatabase = 100;
        String fullName = split[3];
        responseOfDatabase = Database.signInChecker(split[1], split[2]);

        if (responseOfDatabase == 2) {
            signIn = true;
            System.out.println("status code is 200");
            System.out.println("Successfully signed in!");
            try {
                Student st = new Student(Long.parseLong(split[1]));
                FileWriter fw = new FileWriter("D:\\University\\AP\\project\\project files\\students\\studentInfos\\"
                        + split[1] + "_info.txt", true);
                fw.write("Full Name: " + fullName + "\n");
                fw.flush();
                fw.close();
                writer("200");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (responseOfDatabase == 1) {
            System.out.println("status code is 401");
            System.out.println("Password is incorrect!");
            try {
                writer("401");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("status code is 404");
            System.out.println("username is incorrect");
            try {
                writer("404");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void takeCourse(String[] split) throws IOException {
        boolean done = false;
        int responseOfDatabase = 100;
        responseOfDatabase = Database.takeCourseChecker(split[1], split[2]);
        if (responseOfDatabase == 0){
            System.out.println("this course does not exist");
            writer("404");
        } else if (responseOfDatabase == 1) {
            done = true;
            System.out.println("status code is 200");
            System.out.println("Successfully take course");
            try {
                writer("200");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void viewCourses (String[] split) throws IOException {
        List<String> studentCourses = new ArrayList<>();
        String studentId = split[1];

        try {
            File file = new File("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                    + studentId + "_courses.txt");
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
            e.printStackTrace();
        }

        String courseInfosPath = "D:\\University\\AP\\project\\project files\\courses\\courseInformations\\";
        for (String course : studentCourses) {
            try {
                System.out.println(course + ":");
                File courseFile = new File(courseInfosPath + course + "_info.txt");
                Scanner courseReader = new Scanner(courseFile);
                while (courseReader.hasNextLine()) {
                    String line = courseReader.nextLine();
                    writer(line);
                }
                courseReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void viewProf (String[] split) throws IOException {
        String studentId = split[1];
        String filePath = "D:\\University\\AP\\project\\project files\\students\\studentInfos\\"
                + studentId + "_info.txt";
        Scanner scanner = new Scanner(new File(filePath));
        String units = null;
        String fullName = null;
        double average = 0;

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if (line.startsWith("Number")){
                units = line.substring(17);
            }
            if (line.startsWith("Full")){
                fullName = line.substring(11);
            }
        }

        BufferedReader reader2 = new BufferedReader(new FileReader("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                + studentId + "_courses.txt"));
        String line;
        double totalGrade = 0.0;
        int courseCount = 0;

        while ((line = reader2.readLine()) != null) {
            if (line.contains("grade:")) {
                String gradeString = line.split("grade: ")[1].trim();
                double grade = Double.parseDouble(gradeString);
                totalGrade += grade;
                courseCount++;
            }
        }
        reader2.close();
        if (courseCount > 0) {
            average = totalGrade / courseCount;
        }

        writer(fullName + "~" + units + "~" + average);

    }

    public void allCourses() throws IOException {
        String courseInformationsPath = "D:\\University\\AP\\project\\project files\\courses\\courseInformations\\";
        StringBuilder courseNames = new StringBuilder();

        File directory = new File(courseInformationsPath);
        File[] files = directory.listFiles();
        for (File f : files) {
            courseNames.append(f.getName());
        }
        String newCourses = courseNames.toString().replaceAll("_info\\.txt", "~");
        String newCoursesX = newCourses.substring(0, newCourses.length() - 1);

        writer(newCoursesX);
    }

    public void seeAssignments(String[] split) {
        String studentId = split[1];
        String studentCoursesFilePath = "D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                + studentId + "_courses.txt";
        String courseInformationsPath = "D:\\University\\AP\\project\\project files\\courses\\courseInformations\\";
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader studentCoursesReader = new BufferedReader(new FileReader(studentCoursesFilePath));
            String line;
            while ((line = studentCoursesReader.readLine()) != null) {
                String[] parts = line.split(",");
                String courseName = parts[0].substring(8).trim();

                String assignmentFilePath = courseInformationsPath + courseName + "_info.txt";
                BufferedReader assignmentReader = new BufferedReader(new FileReader(assignmentFilePath));
                String assignmentLine;
                while ((assignmentLine = assignmentReader.readLine()) != null) {
                    if (assignmentLine.contains("Assignment")) {
                        stringBuilder.append("Course: ").append(courseName);
                        stringBuilder.append(assignmentLine);
                    }
                }
                assignmentReader.close();
            }
            studentCoursesReader.close();


            String result = convertToDesiredFormat(String.valueOf(stringBuilder));
            writer(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String convertToDesiredFormat(String input) {
        String[] lines = input.split("\n");
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < lines.length; i += 2) {
            String course = lines[i].split(": ")[1];
            String[] assignmentParts = lines[i + 1].split(", ");

            String assignment = assignmentParts[0].substring(12);
            String deadline = assignmentParts[1].split(": ")[1];
            String status = assignmentParts[2];

            output.append(course).append("~").append(assignment).append("~").append(deadline).append("~").append(status);
            if (i < lines.length - 2) {
                output.append("~");
            }
        }

        return output.toString();
    }

    public void stInfo(String[] split) {
        String studentId = split[1];

        String filePath = "D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                + studentId + "_courses.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            double highestGrade = 0;
            double lowestGrade = 20;

            while ((line = reader.readLine()) != null) {
                if (line.contains("grade:")) {
                    String gradeString = line.split(": ")[2].trim();
                    double grade = Double.parseDouble(gradeString);

                    highestGrade = Math.max(highestGrade, grade);
                    lowestGrade = Math.min(lowestGrade, grade);
                }
            }
            reader.close();


            String courseInformationsPath = "D:\\University\\AP\\project\\project files\\courses\\courseInformations\\";
            BufferedReader reader2= new BufferedReader(new FileReader(filePath));
            int asNum = 0;
            String courseName;
            String lineX;
            while ((lineX = reader2.readLine()) != null) {
                String[] parts = lineX.split(",");
                courseName = parts[0].substring(8).trim();


                String assignmentFilePath = courseInformationsPath + courseName + "_info.txt";
                BufferedReader assignmentReader = new BufferedReader(new FileReader(assignmentFilePath));
                String assignmentLine;
                while ((assignmentLine = assignmentReader.readLine()) != null) {
                    if (assignmentLine.contains("Assignment")) {
                        asNum++;
                    }
                }
                assignmentReader.close();
            }
            reader2.close();

                writer(asNum + "~" + highestGrade + "~" + lowestGrade);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changePass(String [] split){
        String studentId = split[1];
        String newPass = split[2];
        String filePath = "D:\\University\\AP\\project\\project files\\Student info.txt";
        if (!cli.passwordValidator(newPass)){
            return;
        }
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            StringBuilder fileContent = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("Username: " + studentId)) {
                    line = line.replaceFirst("Password: .*", "Password: " + newPass);
                }
                fileContent.append(line).append("\n");
            }

            bufferedReader.close();

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fileContent.toString());
            fileWriter.close();
            writer("200");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
