# Report for assignment 4: Issue resolution
This report covers Assignment 4: Issue Resolution, as part of the course DD2480 Software Engineering Fundamentals. The full assignment description can be found in the [task description](https://canvas.kth.se/courses/52568/assignments/320347). 

Group 3: Adam Åström, Hanna Sennerö, Janne Schyffert, Oskar Grönman och Pontus Filén.

## Project

Name: Stirling-PDF

URL: https://github.com/Stirling-Tools/Stirling-PDF 

Stirling-PDF is a web application (localy hosed) that can be used for various PDF operations. It offers a wide range of features such as converting, merging and compressing PDF-files.

## Onboarding experience
We chose to switch projects for this assignment from the project we had in assignment 3. We made this decision mainly because of the onboarding experience. The previous project was very difficult to get running, and we spent hours trying to understand how to execute it and fix error messages that prevented compilation.

This project was much more simple to get working than the last project. The documentation was extremely clear, with instuctions for both using the software and for different types of development. Step-by-step instructions were provided on which programs needed to be downloaded and how to build and run the program. In their user guide, they explained Compose first, followed by build, which might lead to some confusion for someone who is not familiar with Docker, but it doesn’t necessarily need to be updated in the documentation since it could be seen as basic knowledge that the user is expected to have.

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

## Overview of issues and work done.

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

## Requirements for the new feature

### Functional requirements
- Color Space Conversion: The primary requirement is to add functionality that converts PDF files from RGB color space to CMYK color space. 
- ICC Profile Handling: The tool should support the use of ICC profiles for color conversion. The system should accept an ICC profile and apply this profile to convert the RGB images to CMYK within the PDF.
- Image Handling: The color space conversion should be applied to image objects within the PDF. All images should be processed and converted to CMYK while maintain high quality.
- Text and Background Conversion: The color of text and any background elements should also be converted.
- API Integration: The new feature should be integrated with the existing PDF program as a new button. The user should be able to send a PDF file throught the program, and the program will return the modified PDF with the color space conversion applied.

### Non-Functional Requirements:
- Should be able to handle large PDF documents.
- Error handling: ensure that issues such as missing ICC profiles, unsupported PDF formats, or invalid color values are managed with error messages.


## Code changes
The code changes made to solve Issue 1 is presented below.

### Patch

(copy your changes or the add git command to show them)
git diff ...


## Test results

Add link or something to show the test results

## UML class diagram
![UML-klassdiagram](out/diagram/diagram.png)

### Key changes
The key methods added were the following:
- ChangeColorSpaceRequest – Uses ICC profiles.
- ChangeColorSpaceController – Handles HTTP requests.
- ChangeColorSpaceService – Responsible for changing the color space of images in a PDF.
- ChangeColorSpace – Performs the color conversion.
- PDDocument (from PDFBox) – Represents the PDF document.
- PDImageXObject – Handles image objects within the PDF.
- ICC_Profile and ICC_ColorSpace – Represent color profiles.

## Overall experience

What are your main take-aways from this project? What did you learn?

How did you grow as a team, using the Essence standard to evaluate yourself?