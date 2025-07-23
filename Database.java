import java.io.*;
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

            if (usernameFromFile.equals(studentId) && passwordFromFile.equals(password)) {
                found = 2;
                break;
            } else if (usernameFromFile.equals(studentId) && (!passwordFromFile.equals(password))) {
                found = 1;
                break;
            }
        }
        return found;
    }

}
