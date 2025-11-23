# Name of application: SpartanTrack

# Who did What

## Version: 0.9
1. Liza Pystina: Developed ReportsController and ReportsView for whitelist/blacklist filtering
2. Troy Quach: Implemented StudentDetailController and StudentDetailView with profile form and comments table
3. Troy Paulus Asia: Created CommentDetailController and CommentDetailView for full comment text display
4. Liza Pystina: Updated MainController and MainView to add Reports navigation button
5. Troy Quach: Populated students.json with 6 student profiles (3 whitelisted, 3 blacklisted)
6. Troy Paulus Asia: Created meaningful 30+ word comments for all students
7. All Team Members: Testing, debugging, and final integration

## Version: 0.8
1. Liza Pystina: Developed StudentCommentsController for adding and viewing comments
2. Troy Quach: Created StudentCommentsView with comment list and date stamps
3. Troy Paulus Asia: Implemented Comment class and integrated with Student model
4. Liza Pystina: Updated StudentDAO to persist comments in JSON format
5. Troy Quach: Added navigation from ViewStudents to Comments page
6. All Team Members: Populated 5 student profiles with test comments

## Version: 0.7
1. Troy Paulus Asia: Developed EditStudentController for editing student profiles
2. Liza Pystina: Created EditStudentView with pre-populated form fields
3. Troy Quach: Implemented update functionality in StudentDAO
4. Troy Paulus Asia: Added Edit button and navigation in ViewStudentsController
5. Liza Pystina: Tested profile editing with data persistence
6. All Team Members: Bug fixes and validation improvements

## Version: 0.6
1. Troy Quach: Developed ViewStudentsController with search functionality
2. Liza Pystina: Created ViewStudentsView with TableView and search filters
3. Troy Paulus Asia: Implemented multi-criteria search (name, status, languages, databases, role)
4. Troy Quach: Added delete functionality with confirmation dialog
5. Liza Pystina: Populated database with 5 test student profiles
6. All Team Members: Testing search and delete features

## Version: 0.5
1. Liza Pystina: Developed StudentProfileController for creating student profiles
2. Troy Paulus Asia: Created StudentProfileView with form fields and validation
3. Troy Quach: Implemented Student class with all required attributes
4. Liza Pystina: Developed StudentDAO for JSON file persistence
5. Troy Paulus Asia: Integrated programming languages multi-select from version 0.3
6. All Team Members: Testing and data validation

## Version: 0.3
1. Troy Quach: Developed ProgrammingLanguageDAO for data persistence
2. Liza Pystina: Implemented JSON file storage for programming languages
3. Troy Paulus Asia: Added TableView display with alphabetical sorting
4. Troy Quach: Created edit and delete functionality for languages
5. All Team Members: Testing add, view, edit, and delete operations

## Version: 0.2
1. Troy Paulus Asia: Set up Maven JavaFX project structure
2. Liza Pystina: Developed MainController and MainView
3. Troy Quach: Created ProgrammingLanguagesController
4. Troy Paulus Asia: Designed ProgrammingLanguagesView FXML layout
5. Liza Pystina: Implemented ProgrammingLanguage model class
6. All Team Members: Project setup and initial testing

# Technical-Spec
1. Troy Quach: UML Class Diagram, Introduction
2. Troy Paulus Asia: UML Sequence Diagram
3. Liza Pystina: Software Overview, entities and attributes
4. All Team Members: Technical review and refinements

# Functional-Spec
1. Liza Pystina: Software Overview, Introduction
2. Troy Quach: Use Cases Section
3. Troy Paulus Asia: Mockups
4. All Team Members: Requirements gathering and specification review

# Instructions for Running the Application

## Prerequisites
- Java 23 (Zulu JDK recommended)
- Maven 3.6 or higher

## Running the Application
1. Import the project into your IDE as a Maven project
2. Ensure Maven dependencies are downloaded (JavaFX, Gson)
3. Run the `Main.java` class located in `cs151.spartantrack` package
4. The application will create a `data/` folder automatically for JSON storage

## Features
- Manage programming languages 
- Create and manage student profiles
- Search students by multiple criteria
- Edit student information
- Add timestamped comments to student profiles
- Generate reports filtered by whitelist/blacklist status
- View detailed student profiles with comment history

## Data Storage
- Programming languages stored in `data/languages.json`
- Student profiles stored in `data/students.json`
- All data persists between application runs
