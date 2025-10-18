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
    private TableColumn<Student, String> fullNameColumn;

    @FXML
    private TableColumn<Student, String> academicStatusColumn;

    @FXML
    private TableColumn<Student, String> employmentColumn;

    @FXML
    private TableColumn<Student, String> jobDetailsColumn;

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
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        academicStatusColumn.setCellValueFactory(new PropertyValueFactory<>("academicStatus"));
        employmentColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmploymentStatus())
        );
        jobDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("jobDetails"));

        languagesColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProgrammingLanguagesString())
        );

        loadStudents();
    }


    private void loadStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            studentList.clear();
            studentList.addAll(students);
            studentsTableView.setItems(studentList);

            studentCountLabel.setText("Total Students: " + students.size());

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

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Student");
        confirmAlert.setHeaderText("Delete " + selectedStudent.getFullName() + "?");
        confirmAlert.setContentText("This action cannot be undone. Are you sure you want to delete this student profile?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = studentDAO.deleteStudent(selectedStudent.getFullName());

            if (success) {
                showSuccess("Student profile deleted successfully.");
                loadStudents(); // Reload table
            } else {
                showError("Failed to delete student profile.");
            }
        }
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