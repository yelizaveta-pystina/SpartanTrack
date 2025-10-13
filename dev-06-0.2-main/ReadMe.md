# Name of application: SpartanTrack
# Version: 0.3

# who did what:
1. Liza Pystina- file cleaning, debugging, database setup
2. Troy Quach- file cleaning, debugging, 
3. Troy Paulus Asia-  file cleaning, debugging 



# Any other instruction that users need to know:

Prequisites: mySQL, (mySQL workbench recommended), Java 17 or higher
  **Initial Prerequisite Setup** 
Step 1: Verify Java is Installed
Open Terminal/Command Prompt and type:
bashjava -version
Should see: java version "17" or higher
If not installed:

Download from: https://www.oracle.com/java/technologies/downloads/
Install and restart terminal


Step 2: Verify Maven is Installed
In Terminal, type:
bashmvn -version
Should see: Apache Maven 3.x.x
If not installed:
Mac:
bashbrew install maven
Windows:

Download from: https://maven.apache.org/download.cgi
Extract to C:\Program Files\Maven
Add to PATH:

Search "Environment Variables"
Edit System PATH
Add: C:\Program Files\Maven\bin


Restart terminal and test: mvn -version


Step 3: Verify MySQL is Installed
In Terminal, type:
bashmysql --version
Should see: mysql Ver 8.0.x
If not installed:
Mac:
bashbrew install mysql
brew services start mysql
Windows:

Download MySQL Installer: https://dev.mysql.com/downloads/installer/
Run installer
Choose "Developer Default"
Set root password to: root (or remember your password)
Complete installation

**How to Run Application:**
IN INTELLIJ
How to Run Application:

1. Download your project zip file

2. Unzip the project.

IN INTELLIJ:
3. Open Intellij, select open project, select 'Open'
find file dev-06-03 in its path
such as 
/Users/lizapystina/Documents/dev-0.6-0.3, press ok.
4. Make sure SDK is defined within Intellij if not already-- select latest Zulu
5. Next, where it says "Current File" on top click the arrow next to it, and press Edit Configurations. 
6. Press "Add New" , select Maven, 

name: SpartanTrack
Run command: clean javafx:run
working directory: select dev-06-0.3, press OK, then press Apply and the blue "OK"

7. Press on SpartanTrackDataBase.sql in the hiearchy. If no Data source is configured to run the SQL, press "Configure data source" on the top
8. When this opens, press the '+' and select mySQL
enter
user: root
password: root
database: spartantrack
9. Go to Main.class--> press play 
10. Should direct you to a popup screen. Press Manage Languages, Add Language
Press Apply, then the blue "OK"
11. TEST:
Test the Application
In the application window:

Click "Manage Languages" button

New window opens: "Program Language Management"


Add a language:

Type "Java" in text field
Click "Add Language"
See success message
"Java" will appear in table


Add more languages:
for example, 
Add "Python"
Add "C++"
Add "JavaScript"
All appear sorted alphabetically


Test Edit:

Click on "Java" to select it
Click "Edit Selected"
Change to "Java SE"
Click OK
Name updates in table


Test Delete:

Click on a language
Click "Delete Selected"
Language will disappear



12.  Verify Database Storage

Check Database Has Your Data
Keep application running, open MySQL Workbench:

Connect to local instance
Run this query:

sql   USE spartantrack;
      SELECT * FROM programming_languages;

Click lightning bolt to run. 
You should see all languages you added before. 

Example output:
| language_id | language_name |
|-------------|---------------|
| 1           | C++           |
| 2           | Java SE       |
| 3           | JavaScript    |
| 4           | Python        |
 Data is in database. 

13. Test Persistence of Data
This proves permanent storage works:

Close the JavaFX application (click X)
In Terminal, run :

bash   mvn clean javafx:run
 OR
 press play again, languages previosuly entered will remain
 
Click "Manage Languages"
All your languages should still be there! 

14. Can view Database in IntelliJ

Configure IntelliJ Database Tool
If using IntelliJ IDEA:

Right side: Click "Database" tab
Click '+' → Data Source → MySQL
Fill in:

Host: localhost
Port: 3306
Database: spartantrack
User: root
Password: root


Click "Download Drivers" (if needed)
Click "Test Connection" → should see green checkmark
Click OK

Now you can:

Browse tables in IntelliJ
Right-click table → View Data
Run SQL queries directly



TROUBLESHOOT:
Configure Database Credentials (If Needed)

**Default settings work with the provided SQL setup script.**
user = 'root', password = 'root'

If your MySQL has different credentials, you can change them:

1. **Open:** `src/main/resources/database.properties`

2. **Edit the values:**
```properties
   database.username=your_mysql_username
   database.password=your_mysql_password

