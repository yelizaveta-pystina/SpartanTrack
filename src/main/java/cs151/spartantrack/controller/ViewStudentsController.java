package cs151.spartantrack.controller;

import cs151.spartantrack.Main;
import cs151.spartantrack.ProgrammingLanguage;
import cs151.spartantrack.ProgrammingLanguageDAO;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewStudentsController {

    @FXML private TableView<Student> studentsTableView;
    @FXML private TableColumn<Student, String> fullNameColumn;
    @FXML private TableColumn<Student, String> academicStatusColumn;
    @FXML private TableColumn<Student, String> employmentColumn;
    @FXML private TableColumn<Student, String> jobDetailsColumn;
    @FXML private TableColumn<Student, String> languagesColumn;
    @FXML private TableColumn<Student, String> databasesColumn;
    @FXML private TableColumn<Student, String> preferredRoleColumn;
    @FXML private TableColumn<Student, String> commentsColumn;
    @FXML private TableColumn<Student, String> serviceFlagColumn;

    @FXML private TextField nameSearchField;
    @FXML private ComboBox<String> academicStatusSearchCombo;
    @FXML private ComboBox<String> programmingLanguageSearchCombo;
    @FXML private ComboBox<String> databaseSkillSearchCombo;
    @FXML private ComboBox<String> professionalRoleSearchCombo;

    @FXML private Label studentCountLabel;
    @FXML private Label statusLabel;
    @FXML private Button searchButton;
    @FXML private Button clearSearchButton;
    @FXML private Button refreshButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;

    private final StudentDAO studentDAO = new StudentDAO();
    private final ProgrammingLanguageDAO languageDAO = new ProgrammingLanguageDAO();
    private ObservableList<Student> allStudents = FXCollections.observableArrayList();
    private ObservableList<Student> filteredStudents = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        setupSearchFields();
        setupColumnWrapping();
        loadStudents();
    }

    private void setupTableColumns() {
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        academicStatusColumn.setCellValueFactory(new PropertyValueFactory<>("academicStatus"));

        employmentColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmploymentStatus())
        );

        jobDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("jobDetails"));

        languagesColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProgrammingLanguagesString())
        );

        databasesColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDatabasesKnownString())
        );

        preferredRoleColumn.setCellValueFactory(new PropertyValueFactory<>("preferredRole"));

        commentsColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCommentsString())
        );

        serviceFlagColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getServiceFlagStatus())
        );
    }

    private void setupSearchFields() {
        ObservableList<String> academicStatuses = FXCollections.observableArrayList(
                "Any Status", "Freshman", "Sophomore", "Junior", "Senior", "Graduate"
        );
        academicStatusSearchCombo.setItems(academicStatuses);
        academicStatusSearchCombo.setValue("Any Status");

        List<ProgrammingLanguage> languages = languageDAO.getAllLanguages();
        ObservableList<String> languageNames = FXCollections.observableArrayList("Any Language");
        languageNames.addAll(languages.stream()
                .map(ProgrammingLanguage::getLanguageName)
                .collect(Collectors.toList()));
        programmingLanguageSearchCombo.setItems(languageNames);
        programmingLanguageSearchCombo.setValue("Any Language");

        populateDatabaseSkillFilter();
        populateProfessionalRoleFilter();
    }

    private void populateDatabaseSkillFilter() {
        Set<String> uniqueDatabases = new HashSet<>();
        List<Student> students = studentDAO.getAllStudents();

        for (Student student : students) {
            uniqueDatabases.addAll(student.getDatabasesKnown());
        }

        ObservableList<String> databases = FXCollections.observableArrayList("Any Database");
        databases.addAll(uniqueDatabases.stream().sorted().collect(Collectors.toList()));
        databaseSkillSearchCombo.setItems(databases);
        databaseSkillSearchCombo.setValue("Any Database");
    }

    private void populateProfessionalRoleFilter() {
        Set<String> uniqueRoles = new HashSet<>();
        List<Student> students = studentDAO.getAllStudents();

        for (Student student : students) {
            if (student.getPreferredRole() != null && !student.getPreferredRole().trim().isEmpty()) {
                uniqueRoles.add(student.getPreferredRole());
            }
        }

        ObservableList<String> roles = FXCollections.observableArrayList("Any Role");
        roles.addAll(uniqueRoles.stream().sorted().collect(Collectors.toList()));
        professionalRoleSearchCombo.setItems(roles);
        professionalRoleSearchCombo.setValue("Any Role");
    }

    private void setupColumnWrapping() {
        jobDetailsColumn.setCellFactory(tc -> {
            TableCell<Student, String> cell = new TableCell<>();
            Label label = new Label();
            label.setWrapText(true);
            label.setPrefWidth(180);
            cell.setGraphic(label);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            label.textProperty().bind(cell.itemProperty());
            return cell;
        });

        commentsColumn.setCellFactory(tc -> {
            TableCell<Student, String> cell = new TableCell<>();
            Label label = new Label();
            label.setWrapText(true);
            label.setPrefWidth(200);
            cell.setGraphic(label);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            label.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    private void loadStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            allStudents.clear();
            allStudents.addAll(students);
            filteredStudents.clear();
            filteredStudents.addAll(students);
            studentsTableView.setItems(filteredStudents);

            updateStudentCount();

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
    private void onSearchClick() {
        String nameQuery = nameSearchField.getText().trim().toLowerCase();
        String academicStatus = academicStatusSearchCombo.getValue();
        String programmingLanguage = programmingLanguageSearchCombo.getValue();
        String databaseSkill = databaseSkillSearchCombo.getValue();
        String professionalRole = professionalRoleSearchCombo.getValue();

        List<Student> results = new ArrayList<>(allStudents);

        if (!nameQuery.isEmpty()) {
            results = results.stream()
                    .filter(s -> s.getFullName().toLowerCase().contains(nameQuery))
                    .collect(Collectors.toList());
        }

        if (academicStatus != null && !academicStatus.equals("Any Status")) {
            results = results.stream()
                    .filter(s -> s.getAcademicStatus().equalsIgnoreCase(academicStatus))
                    .collect(Collectors.toList());
        }

        if (programmingLanguage != null && !programmingLanguage.equals("Any Language")) {
            results = results.stream()
                    .filter(s -> s.getProgrammingLanguages().stream()
                            .anyMatch(lang -> lang.equalsIgnoreCase(programmingLanguage)))
                    .collect(Collectors.toList());
        }

        if (databaseSkill != null && !databaseSkill.equals("Any Database")) {
            results = results.stream()
                    .filter(s -> s.getDatabasesKnown().stream()
                            .anyMatch(db -> db.equalsIgnoreCase(databaseSkill)))
                    .collect(Collectors.toList());
        }

        if (professionalRole != null && !professionalRole.equals("Any Role")) {
            results = results.stream()
                    .filter(s -> s.getPreferredRole().equalsIgnoreCase(professionalRole))
                    .collect(Collectors.toList());
        }

        filteredStudents.clear();
        filteredStudents.addAll(results);
        updateStudentCount();

        showSuccess("Search completed. Found " + results.size() + " student(s).");
    }

    @FXML
    private void onClearSearchClick() {
        nameSearchField.clear();
        academicStatusSearchCombo.setValue("Any Status");
        programmingLanguageSearchCombo.setValue("Any Language");
        databaseSkillSearchCombo.setValue("Any Database");
        professionalRoleSearchCombo.setValue("Any Role");

        filteredStudents.clear();
        filteredStudents.addAll(allStudents);
        updateStudentCount();
        showSuccess("Search cleared. Showing all students.");
    }

    private void updateStudentCount() {
        int count = filteredStudents.size();
        int total = allStudents.size();

        if (count == total) {
            studentCountLabel.setText("Total Students: " + count);
        } else {
            studentCountLabel.setText("Showing " + count + " of " + total + " students");
        }
    }

    @FXML
    private void onRefreshClick() {
        loadStudents();
        populateDatabaseSkillFilter();
        populateProfessionalRoleFilter();
        onClearSearchClick();
        showSuccess("Student list refreshed.");
    }

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
        confirmAlert.setContentText("This action cannot be undone. Are you sure you want to permanently delete this student profile?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = studentDAO.deleteStudent(selectedStudent.getFullName());

            if (success) {
                allStudents.remove(selectedStudent);
                filteredStudents.remove(selectedStudent);
                updateStudentCount();

                populateDatabaseSkillFilter();
                populateProfessionalRoleFilter();

                showSuccess("Student profile deleted successfully.");
            } else {
                showError("Failed to delete student profile.");
            }
        }
    }

    @FXML
    private void onEditClick() {
        Student selectedStudent = studentsTableView.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            showError("Please select a student to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/cs151/spartantrack/view/EditStudentView.fxml")
            );
            Parent root = loader.load();

            // Get the controller and pass the student data
            EditStudentController controller = loader.getController();
            controller.setStudent(selectedStudent);

            Stage stage = (Stage) studentsTableView.getScene().getWindow();

            boolean wasMaximized = stage.isMaximized();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("SpartanTrack - Edit Student Profile");

            if (wasMaximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(currentWidth);
                stage.setHeight(currentHeight);
            }

        } catch (IOException e) {
            showError("Error navigating to edit page: " + e.getMessage());
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

    @FXML
    private void onViewCommentsClick() {
        Student selectedStudent = studentsTableView.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            showError("Please select a student to view comments.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/cs151/spartantrack/view/StudentCommentsView.fxml")
            );
            Parent root = loader.load();

            // Get the controller and pass the student data
            StudentCommentsController controller = loader.getController();
            controller.setStudent(selectedStudent);

            Stage stage = (Stage) studentsTableView.getScene().getWindow();

            boolean wasMaximized = stage.isMaximized();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("SpartanTrack - Student Comments");

            if (wasMaximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(currentWidth);
                stage.setHeight(currentHeight);
            }

        } catch (IOException e) {
            showError("Error navigating to comments page: " + e.getMessage());
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