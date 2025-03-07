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
8. running codeM

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

#### Issue 1

Since the patch mainly implemented new classes and files, it would be easiest to look at them directly. Since everything in them are new.

These can be found at: 
- [ChangeColorSpaceRequest.java](https://github.com/Soffan-Group-3/Stirling-PDF/blob/1249-color-space-changes/src/main/java/stirling/software/SPDF/model/api/misc/ChangeColorSpaceRequest.java)
- [ChangeColorSpaceController.java](https://github.com/Soffan-Group-3/Stirling-PDF/blob/1249-color-space-changes/src/main/java/stirling/software/SPDF/controller/api/misc/ChangeColorSpaceController.java)
- [ChangeColorSpaceService.java](https://github.com/Soffan-Group-3/Stirling-PDF/blob/1249-color-space-changes/src/main/java/stirling/software/SPDF/service/ChangeColorSpaceService.java)
- [ChangeColorSpace.java](https://github.com/Soffan-Group-3/Stirling-PDF/blob/1249-color-space-changes/src/main/java/stirling/software/SPDF/utils/ChangeColorSpace.java)
- [change-color-space.html](https://github.com/Soffan-Group-3/Stirling-PDF/blob/1249-color-space-changes/src/main/resources/templates/misc/change-color-space.html)


However, these are not the only changed we have made. There are some changes that are needed to be made in order for `Springboot`, `Thymeleaf` and the program in general to work as expected. The changes for these files are as follows:

##### EndpointConfiguration.java
![EndpointConfiguration changes](images/endpoint.PNG)
#### OtherWebController.java
![OtherWebController changes](images/webController.PNG)
#### messages_en_GB.properties
![messages_en_GB.properties changes](images/GBProperties.PNG)
#### messages_en_US.properties
![messages_en_US.properties changes](images/USProperties.PNG)
#### navElements.html
![navElements.html changes](images/navBar.PNG)

There is an automatic formatter that is applied when one runs the project. This is great, since it makes all of additions follow the correct specification. However, as an unfortunate side effect this formatter sometimes makes changes to methods, whitespace and code that we have not touched. This can easily be avoided for files we have not changed by not adding them to the `commit`. However, if there is a file we have changed it becomes a little harder to notice. 


#### Issue 2


For issue 2 the [pull request](https://github.com/Stirling-Tools/Stirling-PDF/pull/3114) have been merged and accepted into main!

## Test results

Add link or something to show the test results

## UML class diagram
![UML-klassdiagram](out/diagram/diagram.png)

### Key changes

#### Issue 1
The key methods added were the following:
- ChangeColorSpaceRequest – Uses ICC profiles.
- ChangeColorSpaceController – Handles HTTP requests.
- ChangeColorSpaceService – Responsible for changing the color space of images in a PDF.
- ChangeColorSpace – Performs the color conversion.
- PDDocument (from PDFBox) – Represents the PDF document.
- PDImageXObject – Handles image objects within the PDF.
- ICC_Profile and ICC_ColorSpace – Represent color profiles.

##### Relation to design pattern(s)

During our development, we tried our best to stick to the design patterns that are already used in the project. In order to get a MVP we started with copying other files, and using their code. This automatically leads to a lot of the code sharing a lot of similarities to the already existing one. There is as stated also an automatic formatter, which when building/running the project automatically formats much of the code to be correct. This formatter seems to be quite advanced since it has multiple times changed lines of code to another (newer) version, that does the same thing. These two things make it easier to keep the new changes to the already existing design pattern.

However, in this project it is in places quite obvious that it is a open source project. Naming conventions and file structures are routinely used differently in different places. In some places it is expected to have different naming conventions. For example, the Java code uses camelCase as is standard for Java, while the front-end code usually uses hyphen connected names (E.g `test-variable`). The problem in naming conventions arises when front-end variables will be used by the back-end. In this case the variables are most often written in camel case, but not always. This kind of confusion occurs at multiple places in the project, however, we have tried to keep with the most used and popular patterns.

Since we added a new `feature` we have closely followed the [Developer Guide](DeveloperGuide.md). This clearly describes how to add a feature (and a new front-end for that feature) in a way that is consistent with the already existing Architecture. 
 
## Overall experience

What are your main take-aways from this project? What did you learn?

How did you grow as a team, using the Essence standard to evaluate yourself?
Right now we are mostly in the Collaborating Stage rather than the Performing Stage because we’ve built a strong foundation open communication, and teamwork. We work well together as a unit, and everyone is aligned with the shared mission. Ideas are exchanged freely, and we support each other to get things done.

However, we still have some room to improve when it comes to efficiency in order to reach the Performing Stage. At times, we find ourselves not having divided the work clearcly enough, which may lead to two people working on the same thing at once. Thus we are still working on our ability to spot and eliminate wasted effort before it happens. While these things don’t hold us back too much, however, fine-tuning these areas will help us transition into the Performing Stage, where we could work at our highest level with minimal setbacks.


### Our work in context

