package cs151.spartantrack.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button manageLanguagesButton;

    @FXML
    private Button createProfileButton;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to SpartanTrack!");
    }

    @FXML
    protected void onManageLanguagesClick() {
        navigateToView(
                "/cs151/spartantrack/view/ProgrammingLanguagesView.fxml",
                "SpartanTrack - Program Language Management"
        );
    }

    @FXML
    protected void onCreateProfileClick() {
        // NEED TO DO LATER- Navigate to student profile creation
        welcomeText.setText("Create Profile (coming soon)");
    }

    private void navigateToView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) manageLanguagesButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);

        } catch (IOException e) {
            System.err.println("Error loading view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
