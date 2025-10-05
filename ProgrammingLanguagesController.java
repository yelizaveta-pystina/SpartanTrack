package cs151.spartantrack.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class ProgrammingLanguagesController {
    @FXML
    private TextField languageNameField;
    
    @FXML
    private ListView<String> existingLanguagesList;
    
    @FXML
    private Button addLanguageButton;
    
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Pre-populate with some common languages for demo
        existingLanguagesList.getItems().addAll(
            "Java", "Python", "C++", "JavaScript"
        );
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
        
        // Check for duplicates
        if (existingLanguagesList.getItems().contains(languageName)) {
            showAlert(
                Alert.AlertType.WARNING,
                "Duplicate Entry",
                "This programming language already exists"
            );
            return;
        }
        
        // Add language to list
        existingLanguagesList.getItems().add(languageName);
        languageNameField.clear();
        
        showAlert(
            Alert.AlertType.INFORMATION,
            "Success",
            "Programming language added successfully!"
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
