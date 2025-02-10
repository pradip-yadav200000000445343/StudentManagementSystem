import java.io.*;
import java.util.*;

class Student {
    private String name;
    private int rollNumber;
    private String grade;

    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDetails() {
        return "Roll No: " + rollNumber + ", Name: " + name + ", Grade: " + grade;
    }

    public String toFileFormat() {
        return rollNumber + "," + name + "," + grade;
    }

    public static Student fromFileFormat(String line) {
        String[] parts = line.split(",");
        return new Student(parts[1], Integer.parseInt(parts[0]), parts[2]);
    }
}

class StudentManagementSystem {
    private List<Student> students;
    private static final String FILE_NAME = "students.txt";

    public StudentManagementSystem() {
        students = new ArrayList<>();
        loadStudentsFromFile();
    }

    public void addStudent(String name, int rollNumber, String grade) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                System.out.println("Error: Roll number already exists.");
                return;
            }
        }
        students.add(new Student(name, rollNumber, grade));
        System.out.println("Student added successfully!");
        saveStudentsToFile();
    }

    public void editStudent(int rollNumber, String newName, String newGrade) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                student.setName(newName);
                student.setGrade(newGrade);
                System.out.println("Student details updated!");
                saveStudentsToFile();
                return;
            }
        }
        System.out.println("Student not found.");
    }

    public void removeStudent(int rollNumber) {
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            Student student = iterator.next();
            if (student.getRollNumber() == rollNumber) {
                iterator.remove();
                System.out.println("Student removed successfully!");
                saveStudentsToFile();
                return;
            }
        }
        System.out.println("Student not found.");
    }

    public void searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                System.out.println("Student Found: " + student.getDetails());
                return;
            }
        }
        System.out.println("Student not found.");
    }

    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        for (Student student : students) {
            System.out.println(student.getDetails());
        }
    }

    private void saveStudentsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Student student : students) {
                writer.println(student.toFileFormat());
            }
        } catch (IOException e) {
            System.out.println("Error saving student data.");
        }
    }

    private void loadStudentsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                students.add(Student.fromFileFormat(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading student data.");
        }
    }
}

public class StudentManagementApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentManagementSystem sms = new StudentManagementSystem();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nStudent Management System:");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student");
            System.out.println("3. Remove Student");
            System.out.println("4. Search Student");
            System.out.println("5. Display All Students");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = getValidIntegerInput();
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    editStudent();
                    break;
                case 3:
                    removeStudent();
                    break;
                case 4:
                    searchStudent();
                    break;
                case 5:
                    sms.displayAllStudents();
                    break;
                case 6:
                    System.out.println("Exiting... Thank you!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Error: Name cannot be empty.");
            return;
        }

        System.out.print("Enter roll number: ");
        int rollNumber = getValidIntegerInput();

        System.out.print("Enter grade: ");
        String grade = scanner.nextLine().trim();
        if (grade.isEmpty()) {
            System.out.println("Error: Grade cannot be empty.");
            return;
        }

        sms.addStudent(name, rollNumber, grade);
    }

    private static void editStudent() {
        System.out.print("Enter roll number of student to edit: ");
        int rollNumber = getValidIntegerInput();

        System.out.print("Enter new name: ");
        String newName = scanner.nextLine().trim();
        if (newName.isEmpty()) {
            System.out.println("Error: Name cannot be empty.");
            return;
        }

        System.out.print("Enter new grade: ");
        String newGrade = scanner.nextLine().trim();
        if (newGrade.isEmpty()) {
            System.out.println("Error: Grade cannot be empty.");
            return;
        }

        sms.editStudent(rollNumber, newName, newGrade);
    }

    private static void removeStudent() {
        System.out.print("Enter roll number to remove: ");
        int rollNumber = getValidIntegerInput();
        sms.removeStudent(rollNumber);
    }

    private static void searchStudent() {
        System.out.print("Enter roll number to search: ");
        int rollNumber = getValidIntegerInput();
        sms.searchStudent(rollNumber);
    }

    private static int getValidIntegerInput() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value < 0) throw new NumberFormatException();
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a valid number: ");
            }
        }
    }
}
