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

    @FXML
    private Button viewReportsButton;

    private final String LANGUAGES_VIEW_PATH = "/cs151/spartantrack/view/ProgrammingLanguagesView.fxml";
    private final String STUDENT_PROFILE_VIEW_PATH = "/cs151/spartantrack/view/StudentProfileView.fxml";
    private final String VIEW_STUDENTS_PATH = "/cs151/spartantrack/view/ViewStudentsView.fxml";
    private final String REPORTS_VIEW_PATH = "/cs151/spartantrack/view/ReportsView.fxml";

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

    @FXML
    private void onViewReportsClick() {
        navigateToView(REPORTS_VIEW_PATH, "SpartanTrack - Reports");
    }

    private void navigateToView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Parent root = loader.load();

            Button sourceButton = manageLanguagesButton != null ? manageLanguagesButton :
                    (createProfileButton != null ? createProfileButton :
                            (viewStudentsButton != null ? viewStudentsButton : viewReportsButton));
            Stage stage = (Stage) sourceButton.getScene().getWindow();

            // Get current window state
            boolean wasMaximized = stage.isMaximized();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);

            // Restore window state
            if (wasMaximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(currentWidth);
                stage.setHeight(currentHeight);
            }

        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlPath);
            e.printStackTrace();
        }
    }
}