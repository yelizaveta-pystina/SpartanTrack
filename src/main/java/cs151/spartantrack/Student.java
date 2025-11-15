package cs151.spartantrack;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private static int idCounter = 1;

    private int studentId;
    private String fullName;
    private String academicStatus;
    private boolean isEmployed;
    private String jobDetails;
    private List<String> programmingLanguages;

    private List<String> databasesKnown;
    private String preferredRole;
    private List<Comment> comments;
    private boolean isWhitelisted;
    private boolean isBlacklisted;

    public Student(String fullName, String academicStatus, boolean isEmployed, String jobDetails,
                   List<String> programmingLanguages, List<String> databasesKnown,
                   String preferredRole, List<Comment> comments, boolean isWhitelisted, boolean isBlacklisted) {
        this.studentId = idCounter++;
        this.fullName = fullName;
        this.academicStatus = academicStatus;
        this.isEmployed = isEmployed;
        this.jobDetails = jobDetails != null ? jobDetails : "";
        this.programmingLanguages = programmingLanguages != null ? new ArrayList<>(programmingLanguages) : new ArrayList<>();
        this.databasesKnown = databasesKnown != null ? new ArrayList<>(databasesKnown) : new ArrayList<>();
        this.preferredRole = preferredRole != null ? preferredRole : "";
        this.comments = comments != null ? new ArrayList<>(comments) : new ArrayList<>();
        this.isWhitelisted = isWhitelisted;
        this.isBlacklisted = isBlacklisted;
    }

    public int getStudentId() { return studentId; }
    public String getFullName() { return fullName; }
    public String getAcademicStatus() { return academicStatus; }
    public boolean isEmployed() { return isEmployed; }
    public String getJobDetails() { return jobDetails; }
    public List<String> getProgrammingLanguages() { return new ArrayList<>(programmingLanguages); }
    public List<String> getDatabasesKnown() { return new ArrayList<>(databasesKnown); }
    public String getPreferredRole() { return preferredRole; }
    public List<Comment> getComments() { return new ArrayList<>(comments); }
    public boolean isWhitelisted() { return isWhitelisted; }
    public boolean isBlacklisted() { return isBlacklisted; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setAcademicStatus(String academicStatus) { this.academicStatus = academicStatus; }
    public void setEmployed(boolean employed) { isEmployed = employed; }
    public void setJobDetails(String jobDetails) { this.jobDetails = jobDetails; }
    public void setProgrammingLanguages(List<String> programmingLanguages) {
        this.programmingLanguages = programmingLanguages != null ? new ArrayList<>(programmingLanguages) : new ArrayList<>();
    }
    public void setDatabasesKnown(List<String> databasesKnown) {
        this.databasesKnown = databasesKnown != null ? new ArrayList<>(databasesKnown) : new ArrayList<>();
    }
    public void setPreferredRole(String preferredRole) { this.preferredRole = preferredRole; }
    public void setComments(List<Comment> comments) { this.comments = comments != null ? new ArrayList<>(comments) : new ArrayList<>(); }
    public void setWhitelisted(boolean whitelisted) { isWhitelisted = whitelisted; }
    public void setBlacklisted(boolean blacklisted) { isBlacklisted = blacklisted; }

    public void addComment(Comment comment) {
        if (comment != null) {
            this.comments.add(comment);
        }
    }

    public boolean validateFullName() {
        return fullName != null && !fullName.trim().isEmpty();
    }

    public boolean validateAcademicStatus() {
        return academicStatus != null && !academicStatus.trim().isEmpty();
    }

    public boolean validateJobDetails() {
        if (isEmployed) {
            return jobDetails != null && !jobDetails.trim().isEmpty();
        }
        return true;
    }

    public boolean isValid() {
        return validateFullName() && validateAcademicStatus() && validateJobDetails();
    }

    public String getEmploymentStatus() {
        return isEmployed ? "Employed" : "Not Employed";
    }

    public String getProgrammingLanguagesString() {
        return programmingLanguages.isEmpty() ? "None" : String.join(", ", programmingLanguages);
    }

    public String getDatabasesKnownString() {
        return databasesKnown.isEmpty() ? "None" : String.join(", ", databasesKnown);
    }

    public String getCommentsString() {
        if (comments.isEmpty()) {
            return "No comments";
        }
        return comments.size() + " comment(s)";
    }

    public String getServiceFlagStatus() {
        if (isWhitelisted) return "Whitelisted";
        if (isBlacklisted) return "Blacklisted";
        return "None";
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", fullName='" + fullName + '\'' +
                ", academicStatus='" + academicStatus + '\'' +
                ", isEmployed=" + isEmployed +
                ", jobDetails='" + jobDetails + '\'' +
                ", programmingLanguages=" + programmingLanguages +
                ", databasesKnown=" + databasesKnown +
                ", preferredRole='" + preferredRole + '\'' +
                ", comments='" + comments.size() + " entries" +
                ", isWhitelisted=" + isWhitelisted +
                ", isBlacklisted=" + isBlacklisted +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        if (fullName == null && student.fullName == null) return true;
        if (fullName == null || student.fullName == null) return false;
        return fullName.equalsIgnoreCase(student.fullName);
    }

    @Override
    public int hashCode() {
        return fullName != null ? fullName.toLowerCase().hashCode() : 0;
    }
}