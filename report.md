# Report for assignment 4: Issue resolution
This report covers Assignment 4: Issue Resolution, as part of the course DD2480 Software Engineering Fundamentals. The full assignment description can be found in the [task description](https://canvas.kth.se/courses/52568/assignments/320347). 

Group 3: Adam Åström, Hanna Sennerö, Janne Schyffert, Oskar Grönman och Pontus Filén.

## Project

Name: Stirling-PDF

URL: https://github.com/Stirling-Tools/Stirling-PDF 

Stirling-PDF is a web application (localy hosed) that can be used for various PDF operations. It offers a wide range of features such as converting, merging and compressing PDF-files.

## Onboarding experience
We chose to switch projects for this assignment from the project we had in assignment 3. We made this decision mainly because of the onboarding experience. The previous project was very difficult to get running, and we spent hours trying to understand how to execute it and fix error messages that prevented compilation.

This project was much more simple to get working than the last project. The documentation was extremely clear, with instuctions for both using the software and for different types of development. Step-by-step instructions were provided on which programs needed to be downloaded and how to build and run the program. It was a bit unclear for someone new to Docker that you need to build before you can use Compose (and that you needed to log in). In their user guide, they explained Compose first, followed by build, which might lead to some confusion for someone who is not familiar with Docker, but it doesn’t necessarily need to be updated in the documentation since it could be seen as basic knowledge that the user is expected to have.

## Effort spent
Below is a breakdown of the time each team member spent in different activities.

### Adam
1. plenary discussions/meetings;
2. discussions within parts of the group;
3. reading documentation;
4. configuration and setup; 
5. analyzing code/output;
6. writing documentation;
7. writing code;
8. running code;

### Hanna
1. plenary discussions/meetings;
2. discussions within parts of the group;
3. reading documentation;
4. configuration and setup; 
5. analyzing code/output;
6. writing documentation;
7. writing code;
8. running code?

### Janne
1. plenary discussions/meetings;
2. discussions within parts of the group;
3. reading documentation;
4. configuration and setup; 
5. analyzing code/output;
6. writing documentation;
7. writing code;
8. running code?

### Oskar
1. plenary discussions/meetings;
2. discussions within parts of the group;
3. reading documentation;
4. configuration and setup; 
5. analyzing code/output;
6. writing documentation;
7. writing code;
8. running code?

### Pontus
1. plenary discussions/meetings;
2. discussions within parts of the group;
3. reading documentation;
4. configuration and setup; 
5. analyzing code/output;
6. writing documentation;
7. writing code;
8. running code?

## Overview of issue(s) and work done.

### Issue 1
Title: "Color space changes #1249"

URL: https://github.com/Stirling-Tools/Stirling-PDF/issues/1249 

We are adding a new feature to the PDF tool that will convert PDFs from RGB to CMYK.

Scope (functionality and code affected)?

### Issue 2
Title: "[Bug]: Error message banner should disappear when a new file operation is performed  #2419"

URL: https://github.com/Stirling-Tools/Stirling-PDF/issues/2419

The bug occurs when you upload a broken PDF or a similar file (such as one with missing content). After selecting a random tool and uploading the corrupted PDF, an error will appear when the tool is run. Then, if you upload a new, correct PDF and run the tool again, the error message will still be visible. This is the issue that needs to be addressed.

Scope (functionality and code affected)?

## Requirements for the new feature or requirements affected by functionality being refactored

Optional (point 3): trace tests to requirements.

## Code changes

### Patch

(copy your changes or the add git command to show them)

git diff ...

Optional (point 4): the patch is clean.

Optional (point 5): considered for acceptance (passes all automated checks).

## Test results

Overall results with link to a copy or excerpt of the logs (before/after
refactoring).

## UML class diagram and its description

### Key changes/classes affected

Optional (point 1): Architectural overview.

Optional (point 2): relation to design pattern(s).

## Overall experience

What are your main take-aways from this project? What did you learn?

How did you grow as a team, using the Essence standard to evaluate yourself?

Optional (point 6): How would you put your work in context with best software engineering practice?

Optional (point 7): Is there something special you want to mention here?
