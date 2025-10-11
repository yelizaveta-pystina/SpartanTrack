package cs151.spartantrack;

public class ProgrammingLanguage {
    private static int idCounter = 1;

    private int languageId;
    private String languageName;

    public ProgrammingLanguage(String languageName) {
        this.languageId = idCounter++;
        this.languageName = languageName;
    }

    public int getLanguageId() {
        return languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public boolean validateName() {
        return languageName != null && !languageName.trim().isEmpty();
    }

    @Override
    public String toString() {
        return languageName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProgrammingLanguage that = (ProgrammingLanguage) obj;
        return languageName.equalsIgnoreCase(that.languageName);
    }
}