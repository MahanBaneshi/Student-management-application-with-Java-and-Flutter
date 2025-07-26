import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Database {
    public static int loginChecker(String studentId, String password) throws FileNotFoundException {
        File file = new File("D:\\University\\AP\\project\\project files\\Student info.txt");
        Scanner fileScanner = new Scanner(file);
        int found = 0;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(", Password: ");
            String usernameFromFile = parts[0].substring(10);
            String passwordFromFile = parts[1];

            if (usernameFromFile.equals(studentId)) {
                found++;
                if (passwordFromFile.equals(password)){
                    found++;
                    break;
                }
            }
        }
        return found;
    }

    public static int signInChecker(String studentId, String password) throws IOException {
        File file = new File("D:\\University\\AP\\project\\project files\\Student info.txt");
        FileWriter fw = new FileWriter(file, true);
        int found = 0;
        long username = Long.parseLong(studentId);
        if (cli.usernameValidator(username) && cli.passwordValidator(password)){
            found = 2;
            fw.write("Username: " + username + ", Password: " + password + "\n");
            fw.flush();
        } else if (cli.usernameValidator(username) && !cli.passwordValidator(password)) {
            found = 1;
        }
        fw.close();
        return found;
    }

    public static int takeCourseChecker(String id, String courseName){
        int found = 0;
        File courseFile = new File("D:\\University\\AP\\project\\project files\\courses\\courseInformations\\"
                + courseName + "_info.txt");
        if (courseFile.exists()){
            found = 1;
            try { // The name of the course must be written in the student's file
                FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\students\\studentCourses\\"
                        + id + "_courses.txt", true);
                writer.write("Course: " + courseName + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try { // The name of the student must be written in the course file
                FileWriter writer = new FileWriter("D:\\University\\AP\\project\\project files\\courses\\courseStudents\\"
                        + courseName + "_students.txt", true);
                writer.write("Student: " + id + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try { //Add the number of course units ti the number of student units
                File oldFile = new File("D:\\University\\AP\\project\\project files\\students\\studentInfos\\"
                        + id + "_info.txt");
                File courseInfo = new File("D:\\University\\AP\\project\\project files\\courses\\courseInformations\\"
                        + courseName + "_info.txt");
                //Find the number of course units
                String targetLinePrefix = "number of the units: ";
                BufferedReader reader = new BufferedReader(new FileReader(courseInfo));
                String line;
                int x = 0;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(targetLinePrefix)) {
                        String valueString = line.substring(targetLinePrefix.length()).trim();
                        x = Integer.parseInt(valueString);
                        break;
                    }
                }
                int numberToAdd = x;

                BufferedReader studentReader = new BufferedReader(new FileReader(oldFile));
                String firstLine = studentReader.readLine();
                studentReader.close();

                String[] parts = firstLine.split(": ");
                int currentNumber = Integer.parseInt(parts[1]);
                int newNumber = currentNumber + numberToAdd;
                String updatedFirstLine = "Number of units: " + newNumber;

                Files.write(Paths.get("D:\\University\\AP\\project\\project files\\students\\studentInfos\\"
                        + id + "_info.txt"), Collections.singleton(updatedFirstLine));

            } catch (IOException e){
                e.printStackTrace();
            }
        }

        return found;
    }


}
