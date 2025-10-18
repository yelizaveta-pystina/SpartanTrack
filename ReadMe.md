# SpartanTrack
**Version:** 0.5

A Student Management System built with JavaFX for tracking student profiles and programming language skills.

## Team Members
1. Liza Pystina - file cleaning, Main, MainController, debugging
2. Troy Quach - Main, MainController, debugging  
3. Troy Paulus Asia - Main, MainController, debugging

## Features
**Manage Programming Languages**: Add, edit, and delete programming languages
**Create Student Profiles**: Store student information including name, email, major, and programming skills
**View All Students**: Display all student profiles in a sortable table (A-Z, case insensitive)
**Persistent Storage**: All data saved in JSON format

## Technologies Used
- JavaFX 17.0.6
- Gson 2.11.0 (JSON handling)
- Maven

## How to Run
1. Extract the zip file to a folder
2. Open the project in IntelliJ IDEA
3. Make sure to configure JDK if not done so already (Zulu 25)
4. Run `Main.java` (located in `src/main/java/cs151/spartantrack/Main.java`)
5. The application will launch with the home screen
   
## Project Structure
```
src/main/java/cs151/spartantrack/
├── Main.java
├── Student.java
├── StudentDAO.java
├── ProgrammingLanguage.java
├── ProgrammingLanguageDAO.java
└── controller/
    ├── MainController.java
    ├── StudentProfileController.java
    └── ViewStudentsController.java
```
