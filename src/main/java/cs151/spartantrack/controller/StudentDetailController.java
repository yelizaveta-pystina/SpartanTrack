package cs151.spartantrack.controller;

import cs151.spartantrack.Comment;
import cs151.spartantrack.Main;
import cs151.spartantrack.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class StudentDetailController {

    @FXML private Label fullNameLabel;
    @FXML private Label academicStatusLabel;
    @FXML private Label employmentLabel;
    @FXML private Label jobDetailsLabel;
    @FXML private Label languagesLabel;
    @FXML private Label databasesLabel;
    @FXML private Label preferredRoleLabel;
    @FXML private Label serviceFlagLabel;

    @FXML private TableView<CommentRow> commentsTableView;
    @FXML private TableColumn<CommentRow, String> dateColumn;
    @FXML private TableColumn<CommentRow, String> commentColumn;

    @FXML private Label commentCountLabel;
    @FXML private Button closeButton;

    private Student currentStudent;
    private ObservableList<CommentRow> commentsList = FXCollections.observableArrayList();

    public static class CommentRow {
        private final String date;
        private final String commentText;
        private final Comment originalComment;

        public CommentRow(String date, String commentText, Comment originalComment) {
            this.date = date;
            this.commentText = commentText;
            this.originalComment = originalComment;
        }

        public String getDate() {
            return date;
        }

        public String getCommentText() {
            return commentText;
        }

        public Comment getOriginalComment() {
            return originalComment;
        }
    }

    public void setStudent(Student student) {
        if (student == null) {
            return;
        }

        this.currentStudent = student;
        displayStudentInfo();
        loadComments();
    }

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate()));

        commentColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCommentText()));

        commentColumn.setCellFactory(tc -> {
            TableCell<CommentRow, String> cell = new TableCell<>() {
                private Label label = new Label();

                {
                    label.setWrapText(true);
                    label.setMaxWidth(450);
                    label.setStyle("-fx-padding: 5;");
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        String excerpt = item.length() > 100 ? item.substring(0, 97) + "..." : item;
                        label.setText(excerpt);
                        setGraphic(label);
                    }
                }
            };
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            return cell;
        });

        setupDoubleClickHandler();
    }

    private void setupDoubleClickHandler() {
        commentsTableView.setRowFactory(tv -> {
            TableRow<CommentRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    CommentRow selectedComment = row.getItem();
                    openCommentDetailView(selectedComment.getOriginalComment());
                }
            });
            return row;
        });
    }

    private void displayStudentInfo() {
        fullNameLabel.setText(currentStudent.getFullName());
        academicStatusLabel.setText(currentStudent.getAcademicStatus());
        employmentLabel.setText(currentStudent.getEmploymentStatus());
        jobDetailsLabel.setText(currentStudent.getJobDetails().isEmpty() ? "N/A" : currentStudent.getJobDetails());
        languagesLabel.setText(currentStudent.getProgrammingLanguagesString());
        databasesLabel.setText(currentStudent.getDatabasesKnownString());
        preferredRoleLabel.setText(currentStudent.getPreferredRole());
        serviceFlagLabel.setText(currentStudent.getServiceFlagStatus());

        if (currentStudent.isWhitelisted()) {
            serviceFlagLabel.setStyle("-fx-text-fill: #00AA00; -fx-font-weight: bold;");
        } else if (currentStudent.isBlacklisted()) {
            serviceFlagLabel.setStyle("-fx-text-fill: #CC0000; -fx-font-weight: bold;");
        }
    }

    private void loadComments() {
        commentsList.clear();

        if (currentStudent == null) {
            return;
        }

        List<Comment> comments = currentStudent.getComments();
        comments.sort((c1, c2) -> c2.getDateCreated().compareTo(c1.getDateCreated()));

        for (Comment comment : comments) {
            CommentRow row = new CommentRow(
                    comment.getFormattedDate(),
                    comment.getCommentText(),
                    comment
            );
            commentsList.add(row);
        }

        commentsTableView.setItems(commentsList);
        commentCountLabel.setText("Total Comments: " + comments.size());
    }

    private void openCommentDetailView(Comment comment) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/cs151/spartantrack/view/CommentDetailView.fxml")
            );
            Parent root = loader.load();

            CommentDetailController controller = loader.getController();
            controller.setComment(comment);

            Stage stage = new Stage();
            stage.setTitle("SpartanTrack - Comment Details");
            stage.setScene(new Scene(root));
            stage.setMinWidth(500);
            stage.setMinHeight(300);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error opening comment details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onCloseClick() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}