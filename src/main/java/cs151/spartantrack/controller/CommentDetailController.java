package cs151.spartantrack.controller;

import cs151.spartantrack.Comment;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CommentDetailController {

    @FXML private Label dateLabel;
    @FXML private TextArea commentTextArea;
    @FXML private Button closeButton;

    public void setComment(Comment comment) {
        if (comment == null) {
            return;
        }

        dateLabel.setText("Date: " + comment.getFormattedDate());
        commentTextArea.setText(comment.getCommentText());
    }

    @FXML
    public void initialize() {
        commentTextArea.setWrapText(true);
        commentTextArea.setEditable(false);
    }

    @FXML
    private void onCloseClick() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}