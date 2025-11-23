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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ReportsController {

    @FXML private RadioButton whitelistRadio;
    @FXML private RadioButton blacklistRadio;
    @FXML private ToggleGroup filterGroup;
    @FXML private TableView<Student> studentsTableView;
    @FXML private TableColumn<Student, String> fullNameColumn;
    @FXML private TableColumn<Student, String> academicStatusColumn;
    @FXML private TableColumn<Student, String> employmentColumn;
    @FXML private TableColumn<Student, String> languagesColumn;
    @FXML private TableColumn<Student, String> preferredRoleColumn;
    @FXML private TableColumn<Student, String> commentsColumn;
    @FXML private Label studentCountLabel;
    @FXML private Label statusLabel;
    @FXML private Button backButton;

    private final StudentDAO studentDAO = new StudentDAO();
    private ObservableList<Student> filteredStudents = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        setupDoubleClickHandler();

        whitelistRadio.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                loadWhitelistedStudents();
            }
        });

        blacklistRadio.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                loadBlacklistedStudents();
            }
        });

        whitelistRadio.setSelected(true);
    }

    private void setupTableColumns() {
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        academicStatusColumn.setCellValueFactory(new PropertyValueFactory<>("academicStatus"));

        employmentColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmploymentStatus())
        );

        languagesColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProgrammingLanguagesString())
        );

        preferredRoleColumn.setCellValueFactory(new PropertyValueFactory<>("preferredRole"));

        commentsColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCommentsString())
        );
    }

    private void setupDoubleClickHandler() {
        studentsTableView.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Student selectedStudent = row.getItem();
                    openStudentDetailView(selectedStudent);
                }
            });
            return row;
        });
    }

    private void loadWhitelistedStudents() {
        try {
            List<Student> allStudents = studentDAO.getAllStudents();
            List<Student> whitelisted = allStudents.stream()
                    .filter(Student::isWhitelisted)
                    .collect(Collectors.toList());

            filteredStudents.clear();
            filteredStudents.addAll(whitelisted);
            studentsTableView.setItems(filteredStudents);

            studentCountLabel.setText("Whitelisted Students: " + whitelisted.size());
            statusLabel.setText("Showing whitelisted students");
            statusLabel.setStyle("-fx-text-fill: #00AA00; -fx-font-weight: bold;");

        } catch (Exception e) {
            showError("Error loading whitelisted students: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadBlacklistedStudents() {
        try {
            List<Student> allStudents = studentDAO.getAllStudents();
            List<Student> blacklisted = allStudents.stream()
                    .filter(Student::isBlacklisted)
                    .collect(Collectors.toList());

            filteredStudents.clear();
            filteredStudents.addAll(blacklisted);
            studentsTableView.setItems(filteredStudents);

            studentCountLabel.setText("Blacklisted Students: " + blacklisted.size());
            statusLabel.setText("Showing blacklisted students");
            statusLabel.setStyle("-fx-text-fill: #CC0000; -fx-font-weight: bold;");

        } catch (Exception e) {
            showError("Error loading blacklisted students: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openStudentDetailView(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/cs151/spartantrack/view/StudentDetailView.fxml")
            );
            Parent root = loader.load();

            StudentDetailController controller = loader.getController();
            controller.setStudent(student);

            Stage stage = new Stage();
            stage.setTitle("SpartanTrack - Student Details");
            stage.setScene(new Scene(root));
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();

        } catch (IOException e) {
            showError("Error opening student details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/cs151/spartantrack/view/MainView.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();

            boolean wasMaximized = stage.isMaximized();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("SpartanTrack - Student Management");

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
}