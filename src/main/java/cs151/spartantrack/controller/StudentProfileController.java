package cs151.spartantrack.controller;

import cs151.spartantrack.Main;
import cs151.spartantrack.ProgrammingLanguage;
import cs151.spartantrack.ProgrammingLanguageDAO;
import cs151.spartantrack.Student;
import cs151.spartantrack.StudentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentProfileController {

    @FXML private TextField fullNameField;
    @FXML private ComboBox<String> academicStatusComboBox;
    @FXML private RadioButton employedRadio;
    @FXML private RadioButton notEmployedRadio;
    @FXML private ToggleGroup employmentGroup;
    @FXML private TextField jobDetailsField;
    @FXML private ListView<String> languagesListView;
    @FXML private ListView<String> databasesListView;
    @FXML private ComboBox<String> preferredRoleComboBox;
    @FXML private CheckBox whitelistCheckBox;
    @FXML private CheckBox blacklistCheckBox;
    @FXML private Label statusLabel;
    @FXML private Button saveButton;
    @FXML private Button clearButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;

    private final StudentDAO studentDAO = new StudentDAO();
    private final ProgrammingLanguageDAO languageDAO = new ProgrammingLanguageDAO();

    @FXML
    public void initialize() {
        // Setup Academic Status dropdown
        ObservableList<String> academicStatuses = FXCollections.observableArrayList(
                "Freshman", "Sophomore", "Junior", "Senior", "Graduate"
        );
        academicStatusComboBox.setItems(academicStatuses);

        // Setup Databases dropdown
        ObservableList<String> databases = FXCollections.observableArrayList(
                "MySQL", "PostgreSQL", "MongoDB", "SQLite", "Oracle", "Redis", "Cassandra"
        );
        databasesListView.setItems(databases);
        databasesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Setup Preferred Role dropdown
        ObservableList<String> roles = FXCollections.observableArrayList(
                "Software Engineer", "Web Developer", "Mobile Developer",
                "Data Scientist", "DevOps Engineer", "QA Engineer",
                "Full Stack Developer", "Backend Developer", "Frontend Developer",
                "Database Administrator", "System Administrator", "Security Engineer"
        );
        preferredRoleComboBox.setItems(roles);

        // Load programming languages from DAO
        loadProgrammingLanguages();

        // Setup multiple selection for languages
        languagesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Enable/disable job details field based on employment status
        employedRadio.selectedProperty().addListener((observable, oldValue, newValue) -> {
            jobDetailsField.setDisable(!newValue);
            if (!newValue) {
                jobDetailsField.clear();
            }
        });

        // Ensure only one checkbox can be selected at a time
        whitelistCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                blacklistCheckBox.setSelected(false);
            }
        });

        blacklistCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                whitelistCheckBox.setSelected(false);
            }
        });
    }

    private void loadProgrammingLanguages() {
        try {
            List<ProgrammingLanguage> languages = languageDAO.getAllLanguages();
            ObservableList<String> languageNames = FXCollections.observableArrayList();

            for (ProgrammingLanguage lang : languages) {
                languageNames.add(lang.getLanguageName());
            }

            languagesListView.setItems(languageNames);

            if (languageNames.isEmpty()) {
                statusLabel.setText("No programming languages available. Please add languages first.");
                statusLabel.setStyle("-fx-text-fill: #E5A823; -fx-font-weight: bold;");
            }

        } catch (Exception e) {
            statusLabel.setText("Error loading programming languages: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: #CC0000; -fx-font-weight: bold;");
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveStudentClick() {
        try {
            statusLabel.setText("");

            // Validate and collect data
            String fullName = fullNameField.getText().trim();
            String academicStatus = academicStatusComboBox.getValue();
            boolean isEmployed = employedRadio.isSelected();
            String jobDetails = jobDetailsField.getText().trim();
            List<String> selectedLanguages = new ArrayList<>(languagesListView.getSelectionModel().getSelectedItems());
            List<String> selectedDatabases = new ArrayList<>(databasesListView.getSelectionModel().getSelectedItems());
            String preferredRole = preferredRoleComboBox.getValue();
            boolean isWhitelisted = whitelistCheckBox.isSelected();
            boolean isBlacklisted = blacklistCheckBox.isSelected();

            // Validation
            if (fullName.isEmpty()) {
                showError("Full name is required.");
                fullNameField.requestFocus();
                return;
            }

            if (academicStatus == null || academicStatus.isEmpty()) {
                showError("Academic status is required.");
                academicStatusComboBox.requestFocus();
                return;
            }

            if (isEmployed && jobDetails.isEmpty()) {
                showError("Job details are required when employment status is 'Employed'.");
                jobDetailsField.requestFocus();
                return;
            }

            if (preferredRole == null || preferredRole.isEmpty()) {
                showError("Preferred professional role is required.");
                preferredRoleComboBox.requestFocus();
                return;
            }

            // Create student object
            Student newStudent = new Student(fullName, academicStatus, isEmployed, jobDetails,
                    selectedLanguages, selectedDatabases, preferredRole,
                    new ArrayList<>(), isWhitelisted, isBlacklisted);

            // Check if student already exists
            if (studentDAO.studentExists(fullName)) {
                showError("A student with this name already exists.");
                fullNameField.requestFocus();
                return;
            }

            // Save to database
            boolean success = studentDAO.addStudent(newStudent);

            if (success) {
                showSuccess("Student profile saved successfully!");
                clearForm();
            } else {
                showError("Failed to save student profile. Please try again.");
            }
        } catch (Exception e) {
            showError("Error saving student: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onClearFormClick() {
        clearForm();
        statusLabel.setText("");
    }

    @FXML
    private void onDeleteProfileClick() {
        String fullName = fullNameField.getText().trim();

        if (fullName.isEmpty()) {
            showError("Please enter a student name to delete.");
            return;
        }

        // Confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete Student Profile");
        confirmDialog.setContentText("Are you sure you want to delete the profile for '" + fullName + "'?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (studentDAO.deleteStudent(fullName)) {
                    showSuccess("Student profile deleted successfully!");
                    clearForm();
                } else {
                    showError("Failed to delete student profile. Student may not exist.");
                }
            }
        });
    }

    private void clearForm() {
        fullNameField.clear();
        academicStatusComboBox.setValue(null);
        notEmployedRadio.setSelected(true);
        jobDetailsField.clear();
        jobDetailsField.setDisable(true);
        languagesListView.getSelectionModel().clearSelection();
        databasesListView.getSelectionModel().clearSelection();
        preferredRoleComboBox.setValue(null);
        whitelistCheckBox.setSelected(false);
        blacklistCheckBox.setSelected(false);
        fullNameField.requestFocus();
    }

    @FXML
    private void onBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/cs151/spartantrack/view/MainView.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();

            // Preserve window state
            boolean wasMaximized = stage.isMaximized();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("SpartanTrack - Student Management");

            // Restore window state
            if (wasMaximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(currentWidth);
                stage.setHeight(currentHeight);
            }

        } catch (IOException e) {
            showError("Error navigating back to home: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #CC0000; -fx-font-weight: bold;");
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #00AA00; -fx-font-weight: bold;");
    }
}