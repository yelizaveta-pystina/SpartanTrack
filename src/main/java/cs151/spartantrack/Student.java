package cs151.spartantrack;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private static int idCounter = 1;

    private int studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String major;
    private List<String> programmingLanguages;

    // Constructor
    public Student(String firstName, String lastName, String email, String major, List<String> programmingLanguages) {
        this.studentId = idCounter++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.major = major;
        this.programmingLanguages = programmingLanguages != null ? new ArrayList<>(programmingLanguages) : new ArrayList<>();
    }

    // Getters
    public int getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMajor() {
        return major;
    }

    public List<String> getProgrammingLanguages() {
        return new ArrayList<>(programmingLanguages);
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setProgrammingLanguages(List<String> programmingLanguages) {
        this.programmingLanguages = programmingLanguages != null ? new ArrayList<>(programmingLanguages) : new ArrayList<>();
    }

    // Validation methods
    public boolean validateFirstName() {
        return firstName != null && !firstName.trim().isEmpty();
    }

    public boolean validateLastName() {
        return lastName != null && !lastName.trim().isEmpty();
    }

    public boolean validateEmail() {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Basic email validation
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public boolean validateMajor() {
        return major != null && !major.trim().isEmpty();
    }

    public boolean isValid() {
        return validateFirstName() && validateLastName() && validateEmail() && validateMajor();
    }

    // Get full name for display
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Get programming languages as comma-separated string
    public String getProgrammingLanguagesString() {
        return String.join(", ", programmingLanguages);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", major='" + major + '\'' +
                ", programmingLanguages=" + programmingLanguages +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return email.equalsIgnoreCase(student.email);
    }

    @Override
    public int hashCode() {
        return email.toLowerCase().hashCode();
    }
}