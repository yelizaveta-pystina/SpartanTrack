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
import java.util.List;

public class StudentProfileController {

    @FXML
    private TextField fullNameField;

    @FXML
    private ComboBox<String> academicStatusComboBox;

    @FXML
    private RadioButton employedRadio;

    @FXML
    private RadioButton notEmployedRadio;

    @FXML
    private ToggleGroup employmentGroup;

    @FXML
    private TextField jobDetailsField;

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
        ObservableList<String> academicStatuses = FXCollections.observableArrayList(
                "Freshman", "Sophomore", "Junior", "Senior", "Graduate"
        );
        academicStatusComboBox.setItems(academicStatuses);

        loadProgrammingLanguages();

        languagesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        employedRadio.selectedProperty().addListener((observable, oldValue, newValue) -> {
            jobDetailsField.setDisable(!newValue);
            if (!newValue) {
                jobDetailsField.clear();
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

            String fullName = fullNameField.getText().trim();
            String academicStatus = academicStatusComboBox.getValue();
            boolean isEmployed = employedRadio.isSelected();
            String jobDetails = jobDetailsField.getText().trim();
            List<String> selectedLanguages = new ArrayList<>(languagesListView.getSelectionModel().getSelectedItems());

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

            Student newStudent = new Student(fullName, academicStatus, isEmployed, jobDetails, selectedLanguages);

            if (studentDAO.studentExists(fullName)) {
                showError("A student with this name already exists.");
                fullNameField.requestFocus();
                return;
            }

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


    private void clearForm() {
        fullNameField.clear();
        academicStatusComboBox.setValue(null);
        notEmployedRadio.setSelected(true);
        jobDetailsField.clear();
        jobDetailsField.setDisable(true);
        languagesListView.getSelectionModel().clearSelection();
        fullNameField.requestFocus();
    }


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


    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #CC0000; -fx-font-weight: bold;");
    }


    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #00AA00; -fx-font-weight: bold;");
    }
}