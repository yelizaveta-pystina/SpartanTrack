package cs151.spartantrack;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgrammingLanguageDAO {

    public List<ProgrammingLanguage> getAllLanguages() {
        List<ProgrammingLanguage> languages = new ArrayList<>();
        String query = "SELECT * FROM programming_languages ORDER BY language_name ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("language_name");
                languages.add(new ProgrammingLanguage(name));
            }

        } catch (SQLException e) {
            System.err.println("Error loading languages: " + e.getMessage());
            e.printStackTrace();
        }

        return languages;
    }

    public boolean addLanguage(String languageName) {
        String query = "INSERT INTO programming_languages (language_name) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, languageName);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error adding language: " + e.getMessage());
            return false;
        }
    }

    public boolean updateLanguage(String oldName, String newName) {
        String query = "UPDATE programming_languages SET language_name = ? WHERE language_name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newName);
            pstmt.setString(2, oldName);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error updating language: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteLanguage(String languageName) {
        String query = "DELETE FROM programming_languages WHERE language_name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, languageName);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error deleting language: " + e.getMessage());
            return false;
        }
    }

    public boolean languageExists(String languageName) {
        String query = "SELECT COUNT(*) FROM programming_languages WHERE language_name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, languageName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking language: " + e.getMessage());
        }

        return false;
    }
}