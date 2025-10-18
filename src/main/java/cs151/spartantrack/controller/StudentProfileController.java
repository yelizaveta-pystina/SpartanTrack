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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentProfileController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField majorField;

    @FXML
    private ListView<String> languagesListView;

    @FXML
    private Label statusLabel;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button backButton;

    private final StudentDAO studentDAO = new StudentDAO();
    private final ProgrammingLanguageDAO languageDAO = new ProgrammingLanguageDAO();

    @FXML
    public void initialize() {
        loadProgrammingLanguages();

        // Enable multiple selection
        languagesListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
    }

    /**
     * Load programming languages dynamically from the database
     */
    private void loadProgrammingLanguages() {
        try {
            List<ProgrammingLanguage> languages = languageDAO.getAllLanguages();
            ObservableList<String> languageNames = FXCollections.observableArrayList();

            for (ProgrammingLanguage lang : languages) {
                languageNames.add(lang.getLanguageName());
            }

            languagesListView.setItems(languageNames);

            // Show message if no languages available
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

    /**
     * Handle save button click
     */
    @FXML
    private void onSaveStudentClick() {
        // Clear previous status message
        statusLabel.setText("");

        // Get form values
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String major = majorField.getText().trim();
        List<String> selectedLanguages = new ArrayList<>(languagesListView.getSelectionModel().getSelectedItems());

        // Validate required fields
        if (firstName.isEmpty()) {
            showError("First name is required.");
            firstNameField.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            showError("Last name is required.");
            lastNameField.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            showError("Email is required.");
            emailField.requestFocus();
            return;
        }

        if (major.isEmpty()) {
            showError("Major is required.");
            majorField.requestFocus();
            return;
        }

        // Create student object
        Student newStudent = new Student(firstName, lastName, email, major, selectedLanguages);

        // Validate email format
        if (!newStudent.validateEmail()) {
            showError("Invalid email format. Please enter a valid email address.");
            emailField.requestFocus();
            return;
        }

        // Check if email already exists
        if (studentDAO.studentExists(email)) {
            showError("A student with this email already exists.");
            emailField.requestFocus();
            return;
        }

        // Save student to database
        boolean success = studentDAO.addStudent(newStudent);

        if (success) {
            showSuccess("Student profile saved successfully!");
            clearForm();
        } else {
            showError("Failed to save student profile. Please try again.");
        }
    }

    /**
     * Handle clear form button click
     */
    @FXML
    private void onClearFormClick() {
        clearForm();
        statusLabel.setText("");
    }

    /**
     * Clear all form fields
     */
    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        majorField.clear();
        languagesListView.getSelectionModel().clearSelection();
        firstNameField.requestFocus();
    }

    /**
     * Handle back button click
     */
    @FXML
    private void onBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/cs151/spartantrack/view/MainView.fxml")
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SpartanTrack - Student Management");

        } catch (IOException e) {
            showError("Error navigating back to home: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #CC0000; -fx-font-weight: bold;");
    }

    /**
     * Show success message
     */
    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #00AA00; -fx-font-weight: bold;");
    }
}