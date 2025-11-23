package cs151.spartantrack.controller;

import cs151.spartantrack.ProgrammingLanguage;
import cs151.spartantrack.ProgrammingLanguageDAO;
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
import java.util.Optional;

/**
 * Controller for Programming Languages Management screen
 */
public class ProgrammingLanguagesController {

    @FXML
    private TextField languageNameField;

    @FXML
    private Button addLanguageButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<ProgrammingLanguage> languagesTableView;

    @FXML
    private TableColumn<ProgrammingLanguage, String> nameColumn;

    private ProgrammingLanguageDAO languageDAO;
    private ObservableList<ProgrammingLanguage> languagesList;

    /**
     * Initialize the controller (called automatically by JavaFX)
     */
    @FXML
    private void initialize() {
        languageDAO = new ProgrammingLanguageDAO();

        // Setup table column
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("languageName"));

        // Load and display languages
        refreshLanguageTable();
    }

    /**
     * Refresh the table with current data from storage
     */
    private void refreshLanguageTable() {
        languagesList = FXCollections.observableArrayList(languageDAO.getAllLanguages());
        languagesTableView.setItems(languagesList);
    }

    /**
     * Add a new programming language
     */
    @FXML
    private void onAddLanguageClick() {
        String languageName = languageNameField.getText().trim();

        // Validation
        if (languageName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter a programming language name.");
            return;
        }

        // Check if already exists
        if (languageDAO.languageExists(languageName)) {
            showAlert(Alert.AlertType.WARNING, "Duplicate Entry",
                    "The language '" + languageName + "' already exists.");
            return;
        }

        // Add to storage
        if (languageDAO.addLanguage(languageName)) {
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Language '" + languageName + "' added successfully!");
            languageNameField.clear();
            refreshLanguageTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to add language. Please try again.");
        }
    }

    /**
     * Edit the selected programming language
     */
    @FXML
    private void onEditLanguageClick() {
        ProgrammingLanguage selected = languagesTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a language to edit.");
            return;
        }

        // Show input dialog for new name
        TextInputDialog dialog = new TextInputDialog(selected.getLanguageName());
        dialog.setTitle("Edit Language");
        dialog.setHeaderText("Edit Programming Language");
        dialog.setContentText("Enter new name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            newName = newName.trim();
            if (!newName.isEmpty()) {
                if (languageDAO.updateLanguage(selected.getLanguageName(), newName)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Language updated successfully!");
                    refreshLanguageTable();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error",
                            "Failed to update language.");
                }
            }
        });
    }

    /**
     * Delete the selected programming language
     */
    @FXML
    private void onDeleteLanguageClick() {
        ProgrammingLanguage selected = languagesTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a language to delete.");
            return;
        }

        // Confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete Language");
        confirmDialog.setContentText("Are you sure you want to delete '" +
                selected.getLanguageName() + "'?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (languageDAO.deleteLanguage(selected.getLanguageName())) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Language deleted successfully!");
                refreshLanguageTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to delete language.");
            }
        }
    }

    /**
     * Navigate back to home screen
     */
    @FXML
    private void onBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cs151/spartantrack/view/MainView.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();

            // Preserve window state
            boolean wasMaximized = stage.isMaximized();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            stage.setScene(new Scene(root));
            stage.setTitle("SpartanTrack - Student Management");

            // Restore window state
            if (wasMaximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(currentWidth);
                stage.setHeight(currentHeight);
            }

        } catch (IOException e) {
            System.err.println("Error loading Main view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to show alerts
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}