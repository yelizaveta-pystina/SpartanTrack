package cs151.spartantrack.controller;

import cs151.spartantrack.ProgrammingLanguage;
import cs151.spartantrack.ProgrammingLanguageDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class ProgrammingLanguagesController {
    @FXML
    private TextField languageNameField;

    @FXML
    private TableView<ProgrammingLanguage> languagesTableView;

    @FXML
    private TableColumn<ProgrammingLanguage, String> nameColumn;

    @FXML
    private Button addLanguageButton;

    @FXML
    private Button backButton;

    private ObservableList<ProgrammingLanguage> languages;
    private ProgrammingLanguageDAO dao;  // NEW: Database access object

    @FXML
    public void initialize() {
        // NEW: Initialize DAO
        dao = new ProgrammingLanguageDAO();

        // Set up table column
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("languageName"));

        // NEW: Load data from database instead of hardcoded values
        loadLanguagesFromDatabase();

        // Set up sorting
        nameColumn.setSortType(TableColumn.SortType.ASCENDING);
        languagesTableView.getSortOrder().add(nameColumn);
        languagesTableView.sort();
    }

    // NEW: Load languages from database
    private void loadLanguagesFromDatabase() {
        languages = FXCollections.observableArrayList(dao.getAllLanguages());
        languagesTableView.setItems(languages);
    }

    @FXML
    protected void onAddLanguageClick() {
        String languageName = languageNameField.getText().trim();

        // Validation: Check if field is empty
        if (languageName.isEmpty()) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Programming Language Name is required"
            );
            return;
        }

        // NEW: Check for duplicates in database
        if (dao.languageExists(languageName)) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Duplicate Entry",
                    "This programming language already exists"
            );
            return;
        }

        // NEW: Add to database
        if (dao.addLanguage(languageName)) {
            languageNameField.clear();
            loadLanguagesFromDatabase();  // Refresh table from database
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    languageName + " added successfully!"
            );
        } else {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Failed to add language to database"
            );
        }
    }

    @FXML
    protected void onEditLanguageClick() {
        ProgrammingLanguage selectedLanguage = languagesTableView.getSelectionModel().getSelectedItem();

        if (selectedLanguage == null) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Selection Error",
                    "Please select a language to edit"
            );
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedLanguage.getLanguageName());
        dialog.setTitle("Edit Programming Language");
        dialog.setHeaderText("Edit Language Name");
        dialog.setContentText("Language Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            if (!newName.trim().isEmpty()) {
                // NEW: Update in database
                if (dao.updateLanguage(selectedLanguage.getLanguageName(), newName.trim())) {
                    loadLanguagesFromDatabase();  // Refresh table from database
                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Language updated successfully!"
                    );
                } else {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            "Failed to update language"
                    );
                }
            }
        });
    }

    @FXML
    protected void onDeleteLanguageClick() {
        ProgrammingLanguage selectedLanguage = languagesTableView.getSelectionModel().getSelectedItem();

        if (selectedLanguage == null) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Selection Error",
                    "Please select a language to delete"
            );
            return;
        }

        // NEW: Delete from database
        if (dao.deleteLanguage(selectedLanguage.getLanguageName())) {
            loadLanguagesFromDatabase();  // Refresh table from database
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    selectedLanguage.getLanguageName() + " deleted successfully!"
            );
        } else {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Failed to delete language"
            );
        }
    }

    @FXML
    protected void onBackClick() {
        navigateToHome();
    }

    private void navigateToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cs151/spartantrack/view/MainView.fxml")
            );
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SpartanTrack - Student Management");

        } catch (IOException e) {
            System.err.println("Error returning to home: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}