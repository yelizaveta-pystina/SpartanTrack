package cs151.spartantrack;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Comment {
    private String commentText;
    private LocalDate dateCreated;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public Comment(String commentText){
        this.commentText = commentText;
        this.dateCreated = LocalDate.now();
    }

    public Comment(String commentText, LocalDate dateCreated){
        this.commentText = commentText;
        this.dateCreated = dateCreated;
    }

    public String getCommentText(){
        return commentText;
    }

    public void setCommentText(String commentText){
        this.commentText = commentText;
    }

    public LocalDate getDateCreated(){
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated){
        this.dateCreated = dateCreated;
    }

    public String getFormattedDate(){
        return dateCreated.format(DATE_FORMATTER);
    }

    @Override
    public String toString(){
        return "[" + getFormattedDate() + "]" + commentText;
    }
}
