package cs151.spartantrack.controller;

import cs151.spartantrack.Main;
import cs151.spartantrack.Student;
import cs151.spartantrack.StudentDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ViewStudentsController {

    @FXML
    private TableView<Student> studentsTableView;

    @FXML
    private TableColumn<Student, String> firstNameColumn;

    @FXML
    private TableColumn<Student, String> lastNameColumn;

    @FXML
    private TableColumn<Student, String> emailColumn;

    @FXML
    private TableColumn<Student, String> majorColumn;

    @FXML
    private TableColumn<Student, String> languagesColumn;

    @FXML
    private Label studentCountLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button refreshButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;

    private final StudentDAO studentDAO = new StudentDAO();
    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));

        // Custom cell value factory for programming languages (display as comma-separated string)
        languagesColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProgrammingLanguagesString())
        );

        // Load students
        loadStudents();
    }

    /**
     * Load all students from database and display in table
     * Students are sorted alphabetically (A to Z, case insensitive) by the DAO
     */
    private void loadStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            studentList.clear();
            studentList.addAll(students);
            studentsTableView.setItems(studentList);

            // Update student count
            studentCountLabel.setText("Total Students: " + students.size());

            // Show message if no students
            if (students.isEmpty()) {
                statusLabel.setText("No student profiles found. Create a profile to get started.");
                statusLabel.setStyle("-fx-text-fill: #666; -fx-font-weight: normal;");
            } else {
                statusLabel.setText("");
            }

        } catch (Exception e) {
            showError("Error loading students: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle refresh button click
     */
    @FXML
    private void onRefreshClick() {
        loadStudents();
        showSuccess("Student list refreshed.");
    }

    /**
     * Handle delete button click
     */
    @FXML
    private void onDeleteClick() {
        Student selectedStudent = studentsTableView.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            showError("Please select a student to delete.");
            return;
        }

        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Student");
        confirmAlert.setHeaderText("Delete " + selectedStudent.getFullName() + "?");
        confirmAlert.setContentText("This action cannot be undone. Are you sure you want to delete this student profile?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete student
            boolean success = studentDAO.deleteStudent(selectedStudent.getEmail());

            if (success) {
                showSuccess("Student profile deleted successfully.");
                loadStudents(); // Reload table
            } else {
                showError("Failed to delete student profile.");
            }
        }
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