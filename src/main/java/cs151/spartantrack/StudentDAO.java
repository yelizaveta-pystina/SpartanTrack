package cs151.spartantrack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentDAO {
    private final File file = new File("data/students.json");
    private final Type listType = new TypeToken<List<StudentData>>() {}.getType();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static class StudentData {
        String fullName;
        String academicStatus;
        boolean isEmployed;
        String jobDetails;
        List<String> programmingLanguages;
        List<String> databasesKnown;
        String preferredRole;
        List<CommentData> comments;
        boolean isWhitelisted;
        boolean isBlacklisted;

        StudentData(Student student) {
            this.fullName = student.getFullName();
            this.academicStatus = student.getAcademicStatus();
            this.isEmployed = student.isEmployed();
            this.jobDetails = student.getJobDetails();
            this.programmingLanguages = student.getProgrammingLanguages();
            this.databasesKnown = student.getDatabasesKnown();
            this.preferredRole = student.getPreferredRole();
            this.comments = new ArrayList<>();
            for (Comment comment : student.getComments()){
                this.comments.add(new CommentData(comment));
            }
            this.isWhitelisted = student.isWhitelisted();
            this.isBlacklisted = student.isBlacklisted();
        }

        private static class CommentData{
            String commentText;
            String dateCreated;

            CommentData(Comment comment) {
                this.commentText = comment.getCommentText();
                this.dateCreated = comment.getDateCreated().toString();
            }

            Comment toComment(){
                return new Comment(commentText, LocalDate.parse(dateCreated));
            }
        }

        Student toStudent() {
            String safeName = fullName != null ? fullName : "";
            String safeStatus = academicStatus != null ? academicStatus : "Freshman";
            String safeJobDetails = jobDetails != null ? jobDetails : "";
            List<String> safeLanguages = programmingLanguages != null ? programmingLanguages : new ArrayList<>();
            List<String> safeDatabases = databasesKnown != null ? databasesKnown : new ArrayList<>();
            String safeRole = preferredRole != null ? preferredRole : "";

            List<Comment> safeComments = new ArrayList<>();
            if (comments != null) {
                for (CommentData data : comments) {
                    safeComments.add(data.toComment());
                }
            }

            return new Student(safeName, safeStatus, isEmployed, safeJobDetails, safeLanguages,
                    safeDatabases, safeRole, safeComments, isWhitelisted, isBlacklisted);
        }
    }

    public StudentDAO() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

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

            students.sort(Comparator.comparing(s -> s.getFullName().toLowerCase()));

        } catch (IOException e) {
            System.err.println("Error loading students: " + e.getMessage());
            e.printStackTrace();
        }

        return students;
    }

    public boolean addStudent(Student student) {
        try {
            if (!student.isValid()) {
                System.err.println("Error adding student: Invalid student data");
                return false;
            }

            List<StudentData> existingStudents = readStudentData();

            for (StudentData data : existingStudents) {
                if (data.fullName.equalsIgnoreCase(student.getFullName())) {
                    System.err.println("Error adding student: Student with this name already exists");
                    return false;
                }
            }

            existingStudents.add(new StudentData(student));
            writeStudentData(existingStudents);
            return true;

        } catch (IOException e) {
            System.err.println("Error adding student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudent(String fullName, Student updatedStudent) {
        try {
            List<StudentData> existingStudents = readStudentData();
            boolean found = false;

            for (int i = 0; i < existingStudents.size(); i++) {
                if (existingStudents.get(i).fullName.equalsIgnoreCase(fullName)) {
                    existingStudents.set(i, new StudentData(updatedStudent));
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.err.println("Error updating student: Student not found");
                return false;
            }

            writeStudentData(existingStudents);
            return true;

        } catch (IOException e) {
            System.err.println("Error updating student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(String fullName) {
        try {
            List<StudentData> existingStudents = readStudentData();
            boolean removed = false;

            for (int i = 0; i < existingStudents.size(); i++) {
                if (existingStudents.get(i).fullName.equalsIgnoreCase(fullName)) {
                    existingStudents.remove(i);
                    removed = true;
                    break;
                }
            }

            if (!removed) {
                System.err.println("Error deleting student: Student not found");
                return false;
            }

            writeStudentData(existingStudents);
            return true;

        } catch (IOException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean studentExists(String fullName) {
        try {
            List<StudentData> existingStudents = readStudentData();

            for (StudentData data : existingStudents) {
                if (data.fullName.equalsIgnoreCase(fullName)) {
                    return true;
                }
            }

            return false;

        } catch (IOException e) {
            System.err.println("Error checking student: " + e.getMessage());
            return false;
        }
    }

    public Student getStudentByName(String fullName) {
        try {
            List<StudentData> existingStudents = readStudentData();

            for (StudentData data : existingStudents) {
                if (data.fullName.equalsIgnoreCase(fullName)) {
                    return data.toStudent();
                }
            }

            return null;

        } catch (IOException e) {
            System.err.println("Error getting student: " + e.getMessage());
            return null;
        }
    }

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

    private void writeStudentData(List<StudentData> students) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(students, writer);
        }
    }

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