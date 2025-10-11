package cs151.spartantrack.controller;

import cs151.spartantrack.ProgrammingLanguage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Comparator;
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

    // Store actual ProgrammingLanguage objects
    private ObservableList<ProgrammingLanguage> languages;

    @FXML
    public void initialize() {
        // Initialize the languages list
        languages = FXCollections.observableArrayList();

        // Pre-populate with some common languages for demo
        languages.add(new ProgrammingLanguage("Java"));
        languages.add(new ProgrammingLanguage("Python"));
        languages.add(new ProgrammingLanguage("C++"));
        languages.add(new ProgrammingLanguage("JavaScript"));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("languageName"));

        languagesTableView.setItems(languages);

        nameColumn.setSortType(TableColumn.SortType.ASCENDING);
        languagesTableView.getSortOrder().add(nameColumn);

        languagesTableView.sort();
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

        // Check for duplicates (case-insensitive)
        for (ProgrammingLanguage lang : languages) {
            if (lang.getLanguageName().equalsIgnoreCase(languageName)) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Duplicate Entry",
                        "This programming language already exists"
                );
                return;
            }
        }

        // Create and add new language
        ProgrammingLanguage newLanguage = new ProgrammingLanguage(languageName);
        languages.add(newLanguage);
        languages.sort(Comparator.comparing(ProgrammingLanguage::getLanguageName, String.CASE_INSENSITIVE_ORDER));
        languageNameField.clear();

        showAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                languageName + " added successfully!"
        );
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
                selectedLanguage.setLanguageName(newName.trim());
                languages.sort(Comparator.comparing(ProgrammingLanguage::getLanguageName, String.CASE_INSENSITIVE_ORDER));

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Language updated successfully!"
                );
            }
        });
    }

    @FXML
    protected void onDeleteLanguageClick() {
        ProgrammingLanguage selectedLanguage = languagesTableView.getSelectionModel().getSelectedItem();

        if (selectedLanguage == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error",
                    "Please select a language to delete"
            );
            return;
        }

        languages.remove(selectedLanguage);

        showAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                selectedLanguage.getLanguageName() + " deleted successfully!"
        );
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