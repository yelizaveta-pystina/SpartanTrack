package cs151.spartantrack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentDAO {
    private final File file = new File("data/students.json");
    private final Type listType = new TypeToken<List<StudentData>>() {}.getType();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Inner class for JSON serialization
    private static class StudentData {
        String firstName;
        String lastName;
        String email;
        String major;
        List<String> programmingLanguages;

        StudentData(Student student) {
            this.firstName = student.getFirstName();
            this.lastName = student.getLastName();
            this.email = student.getEmail();
            this.major = student.getMajor();
            this.programmingLanguages = student.getProgrammingLanguages();
        }

        Student toStudent() {
            return new Student(firstName, lastName, email, major, programmingLanguages);
        }
    }

    public StudentDAO() {
        // Ensure data directory exists
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    /**
     * Get all students from the database
     * Returns students sorted alphabetically by last name, then first name (case-insensitive)
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        try {
            if (!file.exists()) {
                return students;
            }

            String content = readFile().trim();
            if (content.isEmpty() || content.equals("[]")) {
                return students;
            }

            List<StudentData> studentDataList = gson.fromJson(content, listType);
            if (studentDataList != null) {
                for (StudentData data : studentDataList) {
                    students.add(data.toStudent());
                }
            }

            // Sort alphabetically by last name, then first name (case-insensitive, A to Z)
            students.sort(Comparator
                    .comparing((Student s) -> s.getLastName().toLowerCase())
                    .thenComparing(s -> s.getFirstName().toLowerCase()));

        } catch (IOException e) {
            System.err.println("Error loading students: " + e.getMessage());
            e.printStackTrace();
        }

        return students;
    }

    /**
     * Add a new student to the database
     */
    public boolean addStudent(Student student) {
        try {
            // Validate student data
            if (!student.isValid()) {
                System.err.println("Error adding student: Invalid student data");
                return false;
            }

            List<StudentData> existingStudents = readStudentData();

            // Check if student with same email already exists
            for (StudentData data : existingStudents) {
                if (data.email.equalsIgnoreCase(student.getEmail())) {
                    System.err.println("Error adding student: Email already exists");
                    return false;
                }
            }

            // Add new student
            existingStudents.add(new StudentData(student));

            // Write back to file
            writeStudentData(existingStudents);
            return true;

        } catch (IOException e) {
            System.err.println("Error adding student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update an existing student
     */
    public boolean updateStudent(String email, Student updatedStudent) {
        try {
            List<StudentData> existingStudents = readStudentData();
            boolean found = false;

            // Find and update the student by email
            for (int i = 0; i < existingStudents.size(); i++) {
                if (existingStudents.get(i).email.equalsIgnoreCase(email)) {
                    existingStudents.set(i, new StudentData(updatedStudent));
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.err.println("Error updating student: Student not found");
                return false;
            }

            // Write back to file
            writeStudentData(existingStudents);
            return true;

        } catch (IOException e) {
            System.err.println("Error updating student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a student by email
     */
    public boolean deleteStudent(String email) {
        try {
            List<StudentData> existingStudents = readStudentData();
            boolean removed = false;

            // Remove the student with matching email (case-insensitive)
            for (int i = 0; i < existingStudents.size(); i++) {
                if (existingStudents.get(i).email.equalsIgnoreCase(email)) {
                    existingStudents.remove(i);
                    removed = true;
                    break;
                }
            }

            if (!removed) {
                System.err.println("Error deleting student: Student not found");
                return false;
            }

            // Write back to file
            writeStudentData(existingStudents);
            return true;

        } catch (IOException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a student with given email exists
     */
    public boolean studentExists(String email) {
        try {
            List<StudentData> existingStudents = readStudentData();

            for (StudentData data : existingStudents) {
                if (data.email.equalsIgnoreCase(email)) {
                    return true;
                }
            }

            return false;

        } catch (IOException e) {
            System.err.println("Error checking student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get a student by email
     */
    public Student getStudentByEmail(String email) {
        try {
            List<StudentData> existingStudents = readStudentData();

            for (StudentData data : existingStudents) {
                if (data.email.equalsIgnoreCase(email)) {
                    return data.toStudent();
                }
            }

            return null;

        } catch (IOException e) {
            System.err.println("Error getting student: " + e.getMessage());
            return null;
        }
    }

    // Helper method to read student data from file
    private List<StudentData> readStudentData() throws IOException {
        List<StudentData> students = new ArrayList<>();

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                List<StudentData> temp = gson.fromJson(reader, listType);
                if (temp != null) {
                    students = temp;
                }
            }
        }

        return students;
    }

    // Helper method to write student data to file
    private void writeStudentData(List<StudentData> students) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(students, writer);
        }
    }

    // Helper method to read file content as string
    private String readFile() throws IOException {
        try (FileReader reader = new FileReader(file)) {
            StringBuilder content = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                content.append((char) ch);
            }
            return content.toString();
        }
    }
}