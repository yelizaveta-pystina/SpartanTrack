package cs151.spartantrack;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProgrammingLanguageDAO {
    private final File file = new File("data/languages.json");
    private final Type listType = new TypeToken<List<String>>() {}.getType();
    private final Gson gson = new Gson();

    public ProgrammingLanguageDAO() {
        // Ensure data directory exists
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public List<ProgrammingLanguage> getAllLanguages() {
        List<ProgrammingLanguage> languages = new ArrayList<>();

        try {
            if (!file.exists()) {
                return languages;
            }

            String content = readFile().trim();
            if (content.isEmpty() || content.equals("[]")) {
                return languages;
            }

            List<String> names = gson.fromJson(content, listType);
            if (names != null) {
                for (String name : names) {
                    languages.add(new ProgrammingLanguage(name));
                }
            }

            // Sort alphabetically
            languages.sort(Comparator.comparing(l -> l.getLanguageName().toLowerCase()));

        } catch (IOException e) {
            System.err.println("Error loading languages: " + e.getMessage());
            e.printStackTrace();
        }

        return languages;
    }

    public boolean addLanguage(String languageName) {
        try {
            List<String> existingLanguages = readLanguageNames();

            // Check if language already exists (case-insensitive)
            for (String lang : existingLanguages) {
                if (lang.equalsIgnoreCase(languageName)) {
                    System.err.println("Error adding language: Language already exists");
                    return false;
                }
            }

            // Add new language
            existingLanguages.add(languageName);

            // Write back to file
            writeLanguageNames(existingLanguages);
            return true;

        } catch (IOException e) {
            System.err.println("Error adding language: " + e.getMessage());
            return false;
        }
    }

    public boolean updateLanguage(String oldName, String newName) {
        try {
            List<String> existingLanguages = readLanguageNames();
            boolean found = false;

            // Find and update the language
            for (int i = 0; i < existingLanguages.size(); i++) {
                if (existingLanguages.get(i).equalsIgnoreCase(oldName)) {
                    existingLanguages.set(i, newName);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.err.println("Error updating language: Language not found");
                return false;
            }

            // Write back to file
            writeLanguageNames(existingLanguages);
            return true;

        } catch (IOException e) {
            System.err.println("Error updating language: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteLanguage(String languageName) {
        try {
            List<String> existingLanguages = readLanguageNames();
            boolean removed = false;

            // Remove the language (case-insensitive)
            for (int i = 0; i < existingLanguages.size(); i++) {
                if (existingLanguages.get(i).equalsIgnoreCase(languageName)) {
                    existingLanguages.remove(i);
                    removed = true;
                    break;
                }
            }

            if (!removed) {
                System.err.println("Error deleting language: Language not found");
                return false;
            }

            // Write back to file
            writeLanguageNames(existingLanguages);
            return true;

        } catch (IOException e) {
            System.err.println("Error deleting language: " + e.getMessage());
            return false;
        }
    }

    public boolean languageExists(String languageName) {
        try {
            List<String> existingLanguages = readLanguageNames();

            for (String lang : existingLanguages) {
                if (lang.equalsIgnoreCase(languageName)) {
                    return true;
                }
            }

            return false;

        } catch (IOException e) {
            System.err.println("Error checking language: " + e.getMessage());
            return false;
        }
    }

    // Helper method to read language names from file
    private List<String> readLanguageNames() throws IOException {
        List<String> languages = new ArrayList<>();

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                List<String> temp = gson.fromJson(reader, listType);
                if (temp != null) {
                    languages = temp;
                }
            }
        }

        return languages;
    }

    // Helper method to write language names to file
    private void writeLanguageNames(List<String> languages) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(languages, writer);
        }
    }

    // Helper method to read file content as string
    private String readFile() throws IOException {
        try (FileReader reader = new FileReader(file)) {
            StringBuilder content = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                content.append((char) ch);
            }
            return content.toString();
        }
    }
}