# Name of application: SpartanTrack
# Version: 0.3

# who did what:
1. Liza Pystina- file cleaning, debugging, database
2. Troy Quach- file cleaning, debugging, .. add  more idk
3. Troy Paulus Asia-  file cleaning, debugging .. add  more idk



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

1. Download your project zip file
2. Extract to a location like:
Mac: /Users/yourname/Documents/dev-06-0.2-main
Windows: C:\Users\yourname\Documents\dev-06-0.2-main


3. Open Terminal and navigate there:
bash   cd /path/to/dev-06-0.2-main

4. Configure Maven
Maven configuration is in pom.xml to verify:
In Terminal, type:
bashmvn clean install
This--> 
Downloads JavaFX libraries
Downloads MySQL Connector
Compiles your Java code

should see:
[INFO] BUILD SUCCESS
Maven is now configured!

5. MySQL Database Setup
Start MySQL Server
Mac:
bashbrew services start mysql **OR** bashsudo /usr/local/mysql/support-files/mysql.server start

Windows:
Press Win + R
Type: services.msc
Find "MySQL80"
Right-click → Start

Verify mySQL is running:
bashmysql -u root -p
Enter password: root
If you see mysql> prompt, it's working! Type exit; to leave.

6. Create Database Using SQL Script
Make sure you're in the project directory:
bashcd /path/to/dev-06-0.2-main
Run the setup script:
bashmysql -u root -p < SpartanTrackDatabase.sql
Enter password: root
What this does:

Creates database called spartantrack
Creates table called programming_languages
Shows confirmation messages

Database is now set up!

7. Verify Database Was Created
Method A: MySQL Workbench 

Open MySQL Workbench
Click on Local instance 3306
Enter password: root
In query window, type:

  USE spartantrack;
   SHOW TABLES;

Click lightning bolt to run
You should then see programming_languages table

Method B: Command Line
bashmysql -u root -p
Enter password: root
sqlUSE spartantrack;
SHOW TABLES;
DESCRIBE programming_languages;
SELECT * FROM programming_languages;
exit;
Database is now verified.

8: Configure Database Credentials (If Needed)

**Default settings work with the provided SQL setup script.**
user = 'root', password = 'root'

If your MySQL has different credentials, you can change them:

1. **Open:** `src/main/resources/database.properties`

2. **Edit the values:**
```properties
   database.username=your_mysql_username
   database.password=your_mysql_password

**9. Running the Application**

Run with Maven
In Terminal (in project directory):
bashmvn clean javafx:run

Maven cleans old compiled files
Compiles fresh code
Launches JavaFX application
Window appears with "SpartanTrack" title

Application is running!

10: Test the Application
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




11.  Verify Database Storage

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

12. Test Persistence of Data
This proves permanent storage works:

Close the JavaFX application (click X)
In Terminal, run again:

bash   mvn clean javafx:run

Click "Manage Languages"
All your languages should still be there! 

13.Can view Database in IntelliJ

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




