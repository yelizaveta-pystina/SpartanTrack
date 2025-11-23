package cs151.spartantrack.controller;

import cs151.spartantrack.Comment;
import cs151.spartantrack.Main;
import cs151.spartantrack.Student;
import cs151.spartantrack.StudentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class StudentCommentsController {

    @FXML private Label studentNameLabel;
    @FXML private ListView<String> commentsListView;
    @FXML private TextArea newCommentTextArea;
    @FXML private Button addCommentButton;
    @FXML private Button backButton;
    @FXML private Label statusLabel;
    @FXML private Label commentCountLabel;

    private final StudentDAO studentDAO = new StudentDAO();
    private Student currentStudent;
    private ObservableList<String> commentsList = FXCollections.observableArrayList();

    public void setStudent(Student student) {
        if (student == null) {
            showError("No student data provided");
            return;
        }

        this.currentStudent = student;
        studentNameLabel.setText("Comments for: " + student.getFullName());
        loadComments();
    }

    @FXML
    public void initialize() {
        commentsListView.setItems(commentsList);

        // Enable text wrapping in ListView
        commentsListView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                        setWrapText(true);
                        setPrefWidth(0);
                    }
                }
            };
            return cell;
        });
    }

   //Load all comments for the current student
    private void loadComments() {
        commentsList.clear();

        if (currentStudent == null) {
            return;
        }

        List<Comment> comments = currentStudent.getComments();

        if (comments.isEmpty()) {
            commentsList.add("No comments yet. Add the first comment below.");
            commentCountLabel.setText("Total Comments: 0");
        } else {
            // Sort comments by date (most recent first)
            comments.sort((c1, c2) -> c2.getDateCreated().compareTo(c1.getDateCreated()));

            for (Comment comment : comments) {
                String formattedComment = String.format("[%s] %s",
                        comment.getFormattedDate(),
                        comment.getCommentText());
                commentsList.add(formattedComment);
            }

            commentCountLabel.setText("Total Comments: " + comments.size());
        }
    }

    @FXML
    private void onAddCommentClick() {
        String commentText = newCommentTextArea.getText().trim();

        if (commentText.isEmpty()) {
            showError("Please enter a comment before adding.");
            newCommentTextArea.requestFocus();
            return;
        }

        if (currentStudent == null) {
            showError("No student selected. Please go back and select a student.");
            return;
        }

        try {
            // Create new comment with current date
            Comment newComment = new Comment(commentText);

            // Add comment to student
            currentStudent.addComment(newComment);

            // Update student in database
            boolean success = studentDAO.updateStudent(
                    currentStudent.getFullName(),
                    currentStudent
            );

            if (success) {
                showSuccess("Comment added successfully!");
                newCommentTextArea.clear();
                loadComments(); // Refresh the list
            } else {
                showError("Failed to save comment. Please try again.");
            }

        } catch (Exception e) {
            showError("Error adding comment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navigate back to View Students page
     */
    @FXML
    private void onBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/cs151/spartantrack/view/ViewStudentsView.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();

            boolean wasMaximized = stage.isMaximized();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("SpartanTrack - View All Students");

            if (wasMaximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(currentWidth);
                stage.setHeight(currentHeight);
            }

        } catch (IOException e) {
            showError("Error navigating back: " + e.getMessage());
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
