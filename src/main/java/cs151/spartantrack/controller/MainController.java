package cs151.spartantrack.controller;

import cs151.spartantrack.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    private Button viewStudentsButton;

    private final String LANGUAGES_VIEW_PATH = "/cs151/spartantrack/view/ProgrammingLanguagesView.fxml";
    private final String STUDENT_PROFILE_VIEW_PATH = "/cs151/spartantrack/view/StudentProfileView.fxml";
    private final String VIEW_STUDENTS_PATH = "/cs151/spartantrack/view/ViewStudentsView.fxml";

    @FXML
    public void initialize() {
    }


    @FXML
    private void onManageLanguagesClick() {
        navigateToView(LANGUAGES_VIEW_PATH, "SpartanTrack - Manage Programming Languages");
    }


    @FXML
    private void onCreateProfileClick() {
        navigateToView(STUDENT_PROFILE_VIEW_PATH, "SpartanTrack - Create Student Profile");
    }


    @FXML
    private void onViewStudentsClick() {
        navigateToView(VIEW_STUDENTS_PATH, "SpartanTrack - View All Students");
    }


    private void navigateToView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Button sourceButton = manageLanguagesButton != null ? manageLanguagesButton :
                    (createProfileButton != null ? createProfileButton : viewStudentsButton);
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);

        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlPath);
            e.printStackTrace();
        }
    }
}