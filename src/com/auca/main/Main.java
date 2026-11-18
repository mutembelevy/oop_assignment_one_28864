package com.auca.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.auca.administrator.Administrator;
import com.auca.assessment.Assessment;
import com.auca.bookloan.BookLoan;
import com.auca.classroom.Classroom;
import com.auca.course.Course;
import com.auca.department.Department;
import com.auca.enrollment.Enrollment;
import com.auca.exceptions.BookUnavailableException;
import com.auca.exceptions.DuplicateRecordException;
import com.auca.exceptions.PrerequisiteMissingException;
import com.auca.exceptions.RecordNotFoundException;
import com.auca.exceptions.ValidationException;
import com.auca.feepayment.FeePayment;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import com.auca.teacher.Teacher;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.abstracts.Person;
import com.auca.abstracts.AcademicMember;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main { 

    // ONE shared Scanner for the whole program (all input uses nextLine())
    private static final Scanner scanner = new Scanner(System.in);

    // Typed in-memory stores: one list per kind of record
    private static final List<Department> departments = new ArrayList<>();
    private static final List<Student> students = new ArrayList<>();
    private static final List<Teacher> teachers = new ArrayList<>();
    private static final List<Administrator> administrators = new ArrayList<>();
    private static final List<Course> courses = new ArrayList<>();
    private static final List<Classroom> classrooms = new ArrayList<>();
    private static final List<Enrollment> enrollments = new ArrayList<>();
    private static final List<Assessment> assessments = new ArrayList<>();
    private static final List<FeePayment> payments = new ArrayList<>();
    private static final List<LibraryBook> books = new ArrayList<>();
    private static final List<BookLoan> loans = new ArrayList<>();

    // ---------------------------------------------------------------
    // Entry point: the menu loops until the user selects 0 (Exit)
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readMenuChoice();

            try {
                running = handleChoice(choice);
            } catch (ValidationException | DuplicateRecordException
                     | RecordNotFoundException | PrerequisiteMissingException
                     | BookUnavailableException e) {
                // Business-rule failure: show the reason, return to the menu
                System.out.println("! " + e.getMessage());
            } catch (RuntimeException e) {
                // Anything unexpected: no stack trace for the user
                System.out.println("! Something went wrong: " + e.getMessage());
                System.out.println("  Returning to the main menu.");
            }
        }

        System.out.println("Goodbye.");
    }

    // ---------------------------------------------------------------
    // Menu display (layout required by section 12.1)
    // ---------------------------------------------------------------
    private static void printMenu() {
        System.out.println();
        System.out.println("============== AUCA_ID SCHOOL MANAGEMENT ==============");
        System.out.println(" 1. Register Department       11. Borrow Library Book");
        System.out.println(" 2. Register Student          12. Return Library Book");
        System.out.println(" 3. Register Teacher          13. View All Records");
        System.out.println(" 4. Register Administrator    14. Search Record by ID");
        System.out.println(" 5. Register Course           15. Update Existing Record");
        System.out.println(" 6. Register Classroom        16. Student Academic Summary");
        System.out.println(" 7. Enroll Student            17. Student Payment History");
        System.out.println(" 8. Record Assessment         18. View Available Books");
        System.out.println(" 9. Record Fee Payment        19. Demonstrate Polymorphism");
        System.out.println("10. Register Library Book      0. Exit");
    }

    // ---------------------------------------------------------------
    // Safe menu input: whole line, parsed with try/catch, range 0-19.
    // Repeats until valid. If the input ends (no more lines), exit.
    // ---------------------------------------------------------------
    private static int readMenuChoice() {
        while (true) {
            System.out.print("Select an option: ");

            if (!scanner.hasNextLine()) {
                return 0;                       // input closed: leave safely
            }
            String line = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(line);
                if (choice >= 0 && choice <= 19) {
                    return choice;
                }
                System.out.println("! Invalid selection. Enter a number from 0 to 19.");
            } catch (NumberFormatException e) {
                System.out.println("! Please enter a number from 0 to 19.");
            }
        }
    }

    // ---------------------------------------------------------------
    // Route the menu choice. Returns false only when the user exits.
    // ---------------------------------------------------------------
    private static boolean handleChoice(int choice) throws ValidationException {
        switch (choice) {
            case 1 -> registerDepartment();
            case 2 -> registerStudent();
            case 3 -> registerTeacher();
            case 4 -> registerAdministrator();
            case 5 -> registerCourse();
            case 6 -> registerClassroom();
            case 7 -> registerEnrollment();
            case 8 -> registerAssessment();
            case 9 -> registerFeePayment();
            case 10 -> registerLibraryBook();
            case 11 -> borrowLibraryBook();
            case 12 -> returnLibraryBook();
            case 13 -> viewAllRecords();
            case 14 -> searchRecordById();
            case 15 -> updateExistingRecord();
            case 16 -> studentAcademicSummary();
            case 17 -> studentPaymentHistory();
            case 18 -> viewAvailableBooks();
            case 19 -> demonstratePolymorphism();
            case 0 -> {
                return false;
            }
            default -> System.out.println("! Invalid selection.");
        }
        return true;
    }

    private static void registerDepartment() throws ValidationException {
        String departmentId = null;
        String departmentName = null;
        String departmentCode = null;
        String officeLocation = null;
        String contactEmail = null;
        int step = 1;

        try {
            while (step <= 4) {
                try {
                    if (step == 1) {
                        printRegistrationStep("Department", step,
                                "Department identity", "Department code and office",
                                "Contact information");
                        if (departmentId == null) {
                            departmentId = readStudentWizardValue(
                                    "Department ID (4-30 characters): ",
                                    input -> requireText(
                                            input, "Department ID", 4, 30));
                        }
                        if (departmentName == null) {
                            departmentName = readStudentWizardValue(
                                    "Department name (2-60 characters): ",
                                    input -> requireText(
                                            input, "Department name", 2, 60));
                        }
                        step = readRegistrationStepAction(step);
                    } else if (step == 2) {
                        printRegistrationStep("Department", step,
                                "Department identity", "Department code and office",
                                "Contact information");
                        if (departmentCode == null) {
                            departmentCode = readStudentWizardValue(
                                    "Department code (2-20 characters): ",
                                    input -> requireText(
                                            input, "Department code", 2, 20));
                        }
                        if (officeLocation == null) {
                            officeLocation = readStudentWizardValue(
                                    "Office location (2-60 characters): ",
                                    input -> requireText(
                                            input, "Office location", 2, 60));
                        }
                        step = readRegistrationStepAction(step);
                    } else if (step == 3) {
                        printRegistrationStep("Department", step,
                                "Department identity", "Department code and office",
                                "Contact information");
                        if (contactEmail == null) {
                            contactEmail = readStudentWizardValue(
                                    "Contact email: ",
                                    input -> requireEmail(input, "Contact email"));
                        }
                        step = readRegistrationStepAction(step);
                    } else {
                        printRegistrationStep("Department", 4,
                                "Department identity", "Department code and office",
                                "Contact information");
                        System.out.println("\n--- Review Department ---");
                        System.out.println("1. Department ID: " + departmentId);
                        System.out.println("2. Department name: " + departmentName);
                        System.out.println("3. Department code: " + departmentCode);
                        System.out.println("4. Office location: " + officeLocation);
                        System.out.println("5. Contact email: " + contactEmail);
                        System.out.println("6. Save department");
                        System.out.println("0. Cancel registration");

                        String choice = readLine(
                                "Choose a field to edit or an action: ");
                        if (choice.equals(CMD_BACK)) {
                            step = 3;
                            continue;
                        }
                        if (choice.equals(CMD_CANCEL) || choice.equals("0")) {
                            System.out.println("Department registration canceled.");
                            return;
                        }

                        try {
                            switch (choice) {
                                case "1" -> departmentId = readStudentEditValue(
                                        "Department ID (4-30 characters): ",
                                        input -> requireText(
                                                input, "Department ID", 4, 30));
                                case "2" -> departmentName = readStudentEditValue(
                                        "Department name (2-60 characters): ",
                                        input -> requireText(
                                                input, "Department name", 2, 60));
                                case "3" -> departmentCode = readStudentEditValue(
                                        "Department code (2-20 characters): ",
                                        input -> requireText(
                                                input, "Department code", 2, 20));
                                case "4" -> officeLocation = readStudentEditValue(
                                        "Office location (2-60 characters): ",
                                        input -> requireText(
                                                input, "Office location", 2, 60));
                                case "5" -> contactEmail = readStudentEditValue(
                                        "Contact email: ",
                                        input -> requireEmail(
                                                input, "Contact email"));
                                case "6" -> {
                                    Department department = new Department(
                                            departmentId, departmentName,
                                            departmentCode, officeLocation,
                                            contactEmail, null);
                                    department.validate();
                                    ensureUniqueUpdatedRecord(department);
                                    if (confirm("Save this department?")) {
                                        departments.add(department);
                                        System.out.println(
                                                "Department "
                                                        + department.getId()
                                                        + " registered successfully.");
                                        return;
                                    }
                                }
                                default -> System.out.println(
                                        "! Choose a number from 0 to 6.");
                            }
                        } catch (FormBackException e) {
                            System.out.println(
                                    "Returning to the department review.");
                        } catch (ValidationException | DuplicateRecordException e) {
                            System.out.println("! Could not save department: "
                                    + e.getMessage());
                            System.out.println(
                                    "  Review the details and try again.");
                        }
                    }
                } catch (StudentBackException e) {
                    if (step > 1) {
                        step--;
                        System.out.println("Returning to the previous step.");
                    } else {
                        System.out.println("Already at the first step.");
                    }
                }
            }
        } catch (FormCancelledException e) {
            System.out.println("Department registration canceled.");
        }
    }

    private static String readDepartmentText(
        String prompt, String label, int min, int max) {
    while (true) {
        try {
            return requireText(formInput(prompt), label, min, max);
        } catch (ValidationException e) {
            System.out.println("! " + e.getMessage());
        }
    }
}

private static String readDepartmentEmail(String prompt) {
    while (true) {
        try {
            return requireEmail(formInput(prompt), "Contact email");
        } catch (ValidationException e) {
            System.out.println("! " + e.getMessage());
        }
    }
}


private static void registerStudent() throws ValidationException {

    // Store the information entered by the user.
    String personId = null;
    String firstName = null;
    String lastName = null;
    String email = null;
    String phone = null;
    LocalDate dateOfBirth = null;

    String memberNumber = null;
    Department department = null;
    LocalDate joinDate = null;
    String academicStatus = null;
    String institutionalEmail = null;
    String advisorName = null;

    String studentNumber = null;
    String programName = null;
    LocalDate admissionDate = null;
    Integer studyYear = null;
    String guardianPhone = null;
    Boolean scholarship = null;


    int step = 1;

    try {
        // Continue until the user saves or cancels.
        while (true) {

            try {

                // STEP 1: PERSONAL DETAILS
                if (step == 1) {

                    printStudentStepHeader(1);

                    if (personId == null) personId = readStudentWizardValue(
                            "Person ID (4-30 characters): ",
                            input -> requireText(input, "Person ID", 4, 30));

                    if (firstName == null) firstName = readStudentWizardValue(
                            "First name (2-60 characters): ",
                            input -> requireText(input, "First name", 2, 60));

                    if (lastName == null) lastName = readStudentWizardValue(
                            "Last name (2-60 characters): ",
                            input -> requireText(input, "Last name", 2, 60));

                    if (email == null) email = readStudentWizardValue(
                            "Email: ",
                            input -> requireEmail(input, "Email"));

                    if (phone == null) phone = readStudentWizardValue(
                            "Phone (10-15 digits): ",
                            input -> requirePhone(input, "Phone"));

                    if (dateOfBirth == null) dateOfBirth = readStudentWizardValue(
                            "Date of birth (YYYY-MM-DD): ",
                            input -> parsePastDate(input, "Date of birth"));

                    step = readRegistrationStepAction(step);
                }

                // STEP 2: ACADEMIC DETAILS
                else if (step == 2) {

                    printStudentStepHeader(2);

                    if (memberNumber == null) memberNumber = readStudentWizardValue(
                            "Member number (4-30 characters): ",
                            input -> requireText(input, "Member number", 4, 30));

                   if (department == null) department = chooseDepartment();

                    if (department == null) {
                            throw new StudentBackException();
                    }
                    if (joinDate == null) joinDate = readStudentWizardValue(
                            "Join date (YYYY-MM-DD): ",
                            input -> parseNotFutureDate(input, "Join date"));

                    if (academicStatus == null) academicStatus = readStudentWizardValue(
                            "Academic status (1 ACTIVE / 2 INACTIVE): ",
                            input -> choose(
                                    input, "Academic status", "ACTIVE", "INACTIVE"));

                    if (institutionalEmail == null) institutionalEmail = readStudentWizardValue(
                            "Institutional email: ",
                            input -> requireEmail(input, "Institutional email"));

                    if (advisorName == null) advisorName = readStudentWizardValue(
                            "Advisor name (2-120 characters): ",
                            input -> requireText(input, "Advisor name", 2, 120));

                    step = readRegistrationStepAction(step);
                }

                // STEP 3: STUDENT DETAILS
                else if (step == 3) {

                    printStudentStepHeader(3);

                    if (studentNumber == null) studentNumber = readStudentWizardValue(
                            "Student number (4-30 characters): ",
                            input -> requireText(input, "Student number", 4, 30));

                    if (programName == null) programName = readStudentWizardValue(
                            "Program name (2-120 characters): ",
                            input -> requireText(input, "Program name", 2, 120));

                    if (admissionDate == null) admissionDate = readStudentWizardValue(
                            "Admission date (YYYY-MM-DD): ",
                            input -> parseNotFutureDate(input, "Admission date"));

                    if (studyYear == null) studyYear = readStudentWizardValue(
                            "Study year (1-8): ",
                            input -> parseInt(input, "Study year", 1, 8));

                    if (guardianPhone == null) guardianPhone = readStudentWizardValue(
                            "Guardian phone (10-15 digits): ",
                            input -> requirePhone(input, "Guardian phone"));

                    if (scholarship == null) scholarship = readStudentWizardValue(
                            "Scholarship (1 YES / 2 NO): ",
                            input -> choose(input, "Scholarship", "YES", "NO")
                                    .equals("YES"));

                    step = readRegistrationStepAction(step);
                }

                // STEP 4: REVIEW AND CONFIRMATION
                else if (step == 4) {

                    printStudentStepHeader(4);

                    System.out.println("\n========== REVIEW STUDENT ==========");

                    System.out.println("\n--- Personal Details ---");
                    System.out.println("Person ID: " + personId);
                    System.out.println("First name: " + firstName);
                    System.out.println("Last name: " + lastName);
                    System.out.println("Email: " + email);
                    System.out.println("Phone: " + phone);
                    System.out.println("Date of birth: " + dateOfBirth);

                    System.out.println("\n--- Academic Details ---");
                    System.out.println("Member number: " + memberNumber);
                    System.out.println("Department: " + department.getDisplayName());
                    System.out.println("Join date: " + joinDate);
                    System.out.println("Academic status: " + academicStatus);
                    System.out.println("Institutional email: " + institutionalEmail);
                    System.out.println("Advisor name: " + advisorName);

                    System.out.println("\n--- Student Details ---");
                    System.out.println("Student number: " + studentNumber);
                    System.out.println("Program name: " + programName);
                    System.out.println("Admission date: " + admissionDate);
                    System.out.println("Study year: " + studyYear);
                    System.out.println("Guardian phone: " + guardianPhone);
                    System.out.println("Scholarship: " +
                            (scholarship ? "YES" : "NO"));

                    System.out.println("\n========== ACTIONS ==========");
                    System.out.println("1. Edit Personal Details");
                    System.out.println("2. Edit Academic Details");
                    System.out.println("3. Edit Student Details");
                    System.out.println("4. Save Student");
                    System.out.println("0. Cancel Registration");

                    String choice = readLine("Choose an option: ");
                    if (choice.equals(CMD_BACK)) {
                        step = 3;
                        continue;
                    }
                    if (choice.equals(CMD_CANCEL)) {
                        System.out.println("Student registration canceled.");
                        return;
                    }

                    switch (choice) {

                        
case "1" -> {

    while (true) {

        System.out.println("\n--- EDIT PERSONAL DETAILS ---");
        System.out.println("1. Person ID: " + personId);
        System.out.println("2. First name: " + firstName);
        System.out.println("3. Last name: " + lastName);
        System.out.println("4. Email: " + email);
        System.out.println("5. Phone: " + phone);
        System.out.println("6. Date of birth: " + dateOfBirth);

        System.out.println("\n0. Return to full review");

        String field = readLine("Select the field you want to edit: ");

        try {
        switch (field) {

            case "1" -> personId = readStudentEditValue(
                    "New Person ID: ",
                    input -> requireText(input, "Person ID", 4, 30));

            case "2" -> firstName = readStudentEditValue(
                    "New first name: ",
                    input -> requireText(input, "First name", 2, 60));

            case "3" -> lastName = readStudentEditValue(
                    "New last name: ",
                    input -> requireText(input, "Last name", 2, 60));

            case "4" -> email = readStudentEditValue(
                    "New email: ",
                    input -> requireEmail(input, "Email"));

            case "5" -> phone = readStudentEditValue(
                    "New phone: ",
                    input -> requirePhone(input, "Phone"));

            case "6" -> dateOfBirth = readStudentEditValue(
                    "New date of birth (YYYY-MM-DD): ",
                    input -> parsePastDate(input, "Date of birth"));

            case "0" -> {
                break;
            }

            default -> {
                System.out.println("Invalid selection.");
                continue;
            }
        }
        } catch (FormBackException e) {
            System.out.println("Returning to the personal-details field list.");
            continue;
        }

        if (field.equals("0")) {
            break;
        }

        System.out.println("\nPersonal detail updated.");
        System.out.println("The updated information will appear in the full review.");
    }
}


                        case "2" -> {
                            while (true) {
                                System.out.println(
                                        "\n--- EDIT ACADEMIC DETAILS ---");
                                System.out.println("1. Member number: " + memberNumber);
                                System.out.println("2. Department: "
                                        + department.getDisplayName());
                                System.out.println("3. Join date: " + joinDate);
                                System.out.println("4. Academic status: "
                                        + academicStatus);
                                System.out.println("5. Institutional email: "
                                        + institutionalEmail);
                                System.out.println("6. Advisor name: " + advisorName);
                                System.out.println("0. Return to full review");

                                String field = readLine(
                                        "Select the field you want to edit: ");
                                if (field.equals(CMD_BACK) || field.equals("0")) {
                                    break;
                                }
                                if (field.equals(CMD_CANCEL)) {
                                    throw new FormCancelledException();
                                }

                                try {
                                    switch (field) {
                                        case "1" -> memberNumber =
                                                readStudentEditValue(
                                                        "New member number: ",
                                                        input -> requireText(input,
                                                                "Member number",
                                                                4, 30));
                                        case "2" -> {
                                            Department selected =
                                                    chooseDepartment();
                                            if (selected != null) {
                                                department = selected;
                                            } else {
                                                System.out.println(
                                                        "Department unchanged.");
                                                continue;
                                            }
                                        }
                                        case "3" -> joinDate =
                                                readStudentEditValue(
                                                        "New join date (YYYY-MM-DD): ",
                                                        input -> parseNotFutureDate(
                                                                input, "Join date"));
                                        case "4" -> academicStatus =
                                                readStudentEditValue(
                                                        "Academic status "
                                                                + "(1 ACTIVE / 2 INACTIVE): ",
                                                        input -> choose(input,
                                                                "Academic status",
                                                                "ACTIVE",
                                                                "INACTIVE"));
                                        case "5" -> institutionalEmail =
                                                readStudentEditValue(
                                                        "New institutional email: ",
                                                        input -> requireEmail(input,
                                                                "Institutional email"));
                                        case "6" -> advisorName =
                                                readStudentEditValue(
                                                        "New advisor name: ",
                                                        input -> requireText(input,
                                                                "Advisor name",
                                                                2, 120));
                                        default -> {
                                            System.out.println(
                                                    "! Choose a number from 0 to 6.");
                                            continue;
                                        }
                                    }
                                    System.out.println(
                                            "Academic detail updated.");
                                } catch (FormBackException e) {
                                    System.out.println(
                                            "Returning to the academic-details field list.");
                                }
                            }
                        }

                        case "3" -> {
                            while (true) {
                                System.out.println(
                                        "\n--- EDIT STUDENT DETAILS ---");
                                System.out.println("1. Student number: "
                                        + studentNumber);
                                System.out.println("2. Program name: " + programName);
                                System.out.println("3. Admission date: "
                                        + admissionDate);
                                System.out.println("4. Study year: " + studyYear);
                                System.out.println("5. Guardian phone: "
                                        + guardianPhone);
                                System.out.println("6. Scholarship: "
                                        + (scholarship ? "YES" : "NO"));
                                System.out.println("0. Return to full review");

                                String field = readLine(
                                        "Select the field you want to edit: ");
                                if (field.equals(CMD_BACK) || field.equals("0")) {
                                    break;
                                }
                                if (field.equals(CMD_CANCEL)) {
                                    throw new FormCancelledException();
                                }

                                try {
                                    switch (field) {
                                        case "1" -> studentNumber =
                                                readStudentEditValue(
                                                        "New student number: ",
                                                        input -> requireText(input,
                                                                "Student number",
                                                                4, 30));
                                        case "2" -> programName =
                                                readStudentEditValue(
                                                        "New program name: ",
                                                        input -> requireText(input,
                                                                "Program name",
                                                                2, 120));
                                        case "3" -> admissionDate =
                                                readStudentEditValue(
                                                        "New admission date "
                                                                + "(YYYY-MM-DD): ",
                                                        input -> parseNotFutureDate(
                                                                input,
                                                                "Admission date"));
                                        case "4" -> studyYear =
                                                readStudentEditValue(
                                                        "New study year (1-8): ",
                                                        input -> parseInt(input,
                                                                "Study year",
                                                                1, 8));
                                        case "5" -> guardianPhone =
                                                readStudentEditValue(
                                                        "New guardian phone: ",
                                                        input -> requirePhone(input,
                                                                "Guardian phone"));
                                        case "6" -> scholarship =
                                                readStudentEditValue(
                                                        "Scholarship "
                                                                + "(1 YES / 2 NO): ",
                                                        input -> choose(input,
                                                                "Scholarship",
                                                                "YES", "NO")
                                                                .equals("YES"));
                                        default -> {
                                            System.out.println(
                                                    "! Choose a number from 0 to 6.");
                                            continue;
                                        }
                                    }
                                    System.out.println(
                                            "Student detail updated.");
                                } catch (FormBackException e) {
                                    System.out.println(
                                            "Returning to the student-details field list.");
                                }
                            }
                        }

                        case "4" -> {

                            Student student = new Student(
                                    personId, firstName, lastName, email, phone,
                                    dateOfBirth, memberNumber, department, joinDate,
                                    academicStatus, institutionalEmail, advisorName,
                                    studentNumber, programName, admissionDate,
                                    studyYear, guardianPhone, scholarship);

                            try {
                                student.validate();
                                ensureUniqueUpdatedRecord(student);
                            } catch (ValidationException
                                     | DuplicateRecordException e) {
                                System.out.println("! Could not save student: "
                                        + e.getMessage());
                                System.out.println(
                                        "  Review the details and try again.");
                                continue;
                            }

                            if (confirm("Save this student?")) {
                                students.add(student);

                                System.out.println(
                                        student.getRegistrationSummary()
                                                + " registered successfully.");

                                return;
                            }
                        }

                        case "0", CMD_CANCEL -> {
                            System.out.println("Student registration canceled.");
                            return;
                        }

                        default -> System.out.println(
                                "! Choose a number from 0 to 4.");
                    }
                }

            } catch (StudentBackException e) {

                // Return to the previous section.
                if (step > 1) {
                    step--;
                    System.out.println(
                            "\nReturning to the previous registration step...");
                } else {
                    System.out.println(
                            "\nYou are already at the first step.");
                }
            }
        }

    } catch (FormCancelledException e) {
        System.out.println("Student registration canceled.");
    }
}
        

@FunctionalInterface
private interface StudentValueParser<T> {
    T parse(String input) throws ValidationException;
}

private static <T> T readStudentValue(
        String prompt, StudentValueParser<T> parser) {
    while (true) {
        try {
            return parser.parse(formInput(prompt));
        } catch (ValidationException e) {
            System.out.println("! " + e.getMessage());
            System.out.println("  Please correct this value and try again.");
        }
    }
}

private static <T> T readStudentEditValue(
        String prompt, StudentValueParser<T> parser) {
    while (true) {
        String input = readLine(prompt);
        if (input.equals(CMD_BACK)) {
            throw new FormBackException();
        }
        if (input.equals(CMD_CANCEL)) {
            throw new FormCancelledException();
        }
        try {
            return parser.parse(input);
        } catch (ValidationException e) {
            System.out.println("! " + e.getMessage());
            System.out.println("  Please correct this value and try again.");
        }
    }
}


private static String studentWizardInput(String prompt) {

    String input = readLine(prompt);

    if (input.equalsIgnoreCase("BACK")) {
        throw new StudentBackException();
    }

    if (input.equalsIgnoreCase("CANCEL")) {
        throw new FormCancelledException();
    }

    return input;
}


private static <T> T readStudentWizardValue(
        String prompt, StudentValueParser<T> parser) {

    while (true) {
        try {
            return parser.parse(studentWizardInput(prompt));
        } catch (ValidationException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}


    private static String formInput(String prompt) {
        String input = readLine(prompt);
        if (input.equals(CMD_BACK) || input.equals(CMD_CANCEL)) {
            throw new FormCancelledException();
        }
        return input;
    }

    private static Department chooseDepartment()
            throws ValidationException {
        while (true) {
            System.out.println("\n--- Choose Department ---");
            for (int i = 0; i < departments.size(); i++) {
                Department department = departments.get(i);
                System.out.println((i + 1) + ". [" + department.getId() + "] "
                        + department.getDisplayName());
            }

            int registerOption = departments.size() + 1;
            System.out.println(registerOption + ". Register a new Department");
            System.out.println("0. Cancel registration");

            String input = readLine("Select an option: ");
            if (input.equals(CMD_BACK)) {
                return null;
            }
            if (input.equals(CMD_CANCEL) || input.equals("0")) {
                throw new FormCancelledException();
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("! Enter one of the listed numbers.");
                continue;
            }

            if (choice >= 1 && choice <= departments.size()) {
                return departments.get(choice - 1);
            }
            if (choice == registerOption) {
                registerDepartment();
                continue;
            }

            System.out.println("! Enter one of the listed numbers.");
        }
    }

    private static class FormCancelledException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private static class FormBackException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

private static class StudentBackException extends RuntimeException {
    private static final long serialVersionUID = 1L;
}


private static void printRegistrationStep(
        String formName, int step, String... sections) {
    System.out.println();
    System.out.println("=== " + formName + " Registration: Step "
            + step + " of 4 ===");
    for (int i = 0; i < sections.length; i++) {
        System.out.println((i + 1 == step ? " > " : "   ")
                + (i + 1) + ". " + sections[i]);
    }
    System.out.println("   4. Review and confirm");
    System.out.println("Type BACK to return to the previous step, or CANCEL "
            + "to discard this registration.");
}

private static int readRegistrationStepAction(int step) {
    while (true) {
        String input = readLine(
                "Press ENTER to continue, BACK for the previous step, or CANCEL: ");
        if (input.isEmpty()) {
            return step + 1;
        }
        if (input.equals(CMD_BACK)) {
            if (step > 1) {
                return step - 1;
            }
            System.out.println("Already at the first step.");
            continue;
        }
        if (input.equals(CMD_CANCEL)) {
            throw new FormCancelledException();
        }
        System.out.println("! Press ENTER, BACK, or CANCEL.");
    }
}

private static <T> T readRegistrationValue(
        T currentValue, String prompt, StudentValueParser<T> parser) {
    if (currentValue != null) {
        return currentValue;
    }
    return readStudentWizardValue(prompt, parser);
}

private static void returnToRegistrationReview(
        String formName, String... sections) {
    int step = 3;
    while (step <= 3) {
        printRegistrationStep(formName, step, sections);
        step = readRegistrationStepAction(step);
    }
}


private static void printStudentStepHeader(int step) {

    String[] steps = {
            "Personal details",
            "Academic details",
            "Student details",
            "Review & confirm"
    };

    System.out.println();
    System.out.println("+------------------------------------------------------------+");
    System.out.println("| AUCA_ID / SCHOOL MANAGEMENT                                |");
    System.out.println("| STUDENT REGISTRATION                 Step " + step + " of 4       |");
    System.out.println("+------------------------------------------------------------+");

    System.out.println("| [" + (step == 1 ? "*" : " ") + "] Personal details"
            + "    [" + (step == 2 ? "*" : " ") + "] Academic details       |");

    System.out.println("| [" + (step == 3 ? "*" : " ") + "] Student details"
            + "    [" + (step == 4 ? "*" : " ") + "] Review & confirm       |");

    System.out.println("+------------------------------------------------------------+");
    System.out.println("| Type BACK to return to the previous step.                  |");
    System.out.println("| Type CANCEL to discard the unsaved student.                |");
    System.out.println("+------------------------------------------------------------+");
    System.out.println();

    System.out.println("CURRENT SECTION: " + steps[step - 1]);
}


        private static void registerTeacher() throws ValidationException {
    try {
        System.out.println("\n--- Register Teacher ---");

        String personId = null, firstName = null, lastName = null;
        String email = null, phone = null;
        LocalDate dateOfBirth = null;
        String memberNumber = null;
        Department department = null;
        LocalDate joinDate = null;
        String academicStatus = null, institutionalEmail = null;
        String advisorName = null, employeeNumber = null, specialization = null;
        LocalDate hireDate = null;
        BigDecimal salary = null;
        String officeNumber = null, employmentType = null;
        int step = 1;
        while (step <= 3) {
            try {
                if (step == 1) {
                    printRegistrationStep("Teacher", step,
                            "Personal details", "Academic details",
                            "Teacher employment details");
                    personId = readRegistrationValue(personId,
                            "Person ID (4-30 characters): ",
                            input -> requireText(input, "Person ID", 4, 30));
                    firstName = readRegistrationValue(firstName,
                            "First name (2-60 characters): ",
                            input -> requireText(input, "First name", 2, 60));
                    lastName = readRegistrationValue(lastName,
                            "Last name (2-60 characters): ",
                            input -> requireText(input, "Last name", 2, 60));
                    email = readRegistrationValue(email, "Email: ",
                            input -> requireEmail(input, "Email"));
                    phone = readRegistrationValue(phone,
                            "Phone (10-15 digits): ",
                            input -> requirePhone(input, "Phone"));
                    dateOfBirth = readRegistrationValue(dateOfBirth,
                            "Date of birth (YYYY-MM-DD): ",
                            input -> parsePastDate(input, "Date of birth"));
                } else if (step == 2) {
                    printRegistrationStep("Teacher", step,
                            "Personal details", "Academic details",
                            "Teacher employment details");
                    memberNumber = readRegistrationValue(memberNumber,
                            "Member number (4-30 characters): ",
                            input -> requireText(input, "Member number", 4, 30));
                    if (department == null) {
                        department = chooseDepartment();
                        if (department == null) {
                            throw new StudentBackException();
                        }
                    }
                    joinDate = readRegistrationValue(joinDate,
                            "Join date (YYYY-MM-DD): ",
                            input -> parseNotFutureDate(input, "Join date"));
                    academicStatus = readRegistrationValue(academicStatus,
                            "Academic status (1 ACTIVE / 2 INACTIVE): ",
                            input -> choose(input, "Academic status",
                                    "ACTIVE", "INACTIVE"));
                    institutionalEmail = readRegistrationValue(
                            institutionalEmail, "Institutional email: ",
                            input -> requireEmail(input, "Institutional email"));
                    advisorName = readRegistrationValue(advisorName,
                            "Advisor name (2-120 characters): ",
                            input -> requireText(input, "Advisor name", 2, 120));
                } else {
                    printRegistrationStep("Teacher", step,
                            "Personal details", "Academic details",
                            "Teacher employment details");
                    employeeNumber = readRegistrationValue(employeeNumber,
                            "Employee number (4-30 characters): ",
                            input -> requireText(input, "Employee number", 4, 30));
                    specialization = readRegistrationValue(specialization,
                            "Specialization (2-120 characters): ",
                            input -> requireText(input, "Specialization", 2, 120));
                    hireDate = readRegistrationValue(hireDate,
                            "Hire date (YYYY-MM-DD): ",
                            input -> parseNotFutureDate(input, "Hire date"));
                    salary = readRegistrationValue(salary, "Salary: ",
                            input -> parseMoney(input, "Salary"));
                    officeNumber = readRegistrationValue(officeNumber,
                            "Office number (1-30 characters): ",
                            input -> requireText(input, "Office number", 1, 30));
                    employmentType = readRegistrationValue(employmentType,
                            "Employment type (1 FULL-TIME / 2 PART-TIME): ",
                            input -> choose(input, "Employment type",
                                    "FULL-TIME", "PART-TIME"));
                }
                step = readRegistrationStepAction(step);
            } catch (StudentBackException e) {
                if (step > 1) {
                    step--;
                    System.out.println("Returning to the previous step.");
                } else {
                    System.out.println("Already at the first step.");
                }
            }
        }

        while (true) {
            printRegistrationStep("Teacher", 4, "Personal details",
                    "Academic details", "Teacher employment details");
            System.out.println("\n--- Review Teacher ---");
            System.out.println("\n--- Personal Details ---");
            System.out.println("1. Person ID: " + personId);
            System.out.println("2. First name: " + firstName);
            System.out.println("3. Last name: " + lastName);
            System.out.println("4. Email: " + email);
            System.out.println("5. Phone: " + phone);
            System.out.println("6. Date of birth: " + dateOfBirth);
            System.out.println("\n--- Academic Details ---");
            System.out.println("7. Member number: " + memberNumber);
            System.out.println("8. Department: " + department.getDisplayName());
            System.out.println("9. Join date: " + joinDate);
            System.out.println("10. Academic status: " + academicStatus);
            System.out.println("11. Institutional email: " + institutionalEmail);
            System.out.println("12. Advisor name: " + advisorName);
            System.out.println("\n--- Teacher-specific Details ---");
            System.out.println("13. Employee number: " + employeeNumber);
            System.out.println("14. Specialization: " + specialization);
            System.out.println("15. Hire date: " + hireDate);
            System.out.println("16. Salary: " + salary.toPlainString());
            System.out.println("17. Office number: " + officeNumber);
            System.out.println("18. Employment type: " + employmentType);
            System.out.println("19. Save teacher");
            System.out.println("0. Cancel");

            String choice = readLine("Choose a field to edit or an action: ");
            if (choice.equals(CMD_BACK)) {
                returnToRegistrationReview("Teacher", "Personal details",
                        "Academic details", "Teacher employment details");
                continue;
            }
            if (choice.equals(CMD_CANCEL) || choice.equals("0")) {
                System.out.println("Teacher registration canceled.");
                return;
            }

            try {
            switch (choice) {
                case "1" -> personId = readStudentEditValue(
                        "Person ID (4-30 characters): ",
                        input -> requireText(input, "Person ID", 4, 30));
                case "2" -> firstName = readStudentEditValue(
                        "First name (2-60 characters): ",
                        input -> requireText(input, "First name", 2, 60));
                case "3" -> lastName = readStudentEditValue(
                        "Last name (2-60 characters): ",
                        input -> requireText(input, "Last name", 2, 60));
                case "4" -> email = readStudentEditValue(
                        "Email: ", input -> requireEmail(input, "Email"));
                case "5" -> phone = readStudentEditValue(
                        "Phone (10-15 digits): ",
                        input -> requirePhone(input, "Phone"));
                case "6" -> dateOfBirth = readStudentEditValue(
                        "Date of birth (YYYY-MM-DD): ",
                        input -> parsePastDate(input, "Date of birth"));
                case "7" -> memberNumber = readStudentEditValue(
                        "Member number (4-30 characters): ",
                        input -> requireText(input, "Member number", 4, 30));
                case "8" -> {
                    Department selected = chooseDepartment();
                    if (selected != null) {
                        department = selected;
                    } else {
                        System.out.println("Department unchanged.");
                    }
                }
                case "9" -> joinDate = readStudentEditValue(
                        "Join date (YYYY-MM-DD): ",
                        input -> parseNotFutureDate(input, "Join date"));
                case "10" -> academicStatus = readStudentEditValue(
                        "Academic status (1 ACTIVE / 2 INACTIVE): ",
                        input -> choose(
                                input, "Academic status", "ACTIVE", "INACTIVE"));
                case "11" -> institutionalEmail = readStudentEditValue(
                        "Institutional email: ",
                        input -> requireEmail(input, "Institutional email"));
                case "12" -> advisorName = readStudentEditValue(
                        "Advisor name (2-120 characters): ",
                        input -> requireText(input, "Advisor name", 2, 120));
                case "13" -> employeeNumber = readStudentEditValue(
                        "Employee number (4-30 characters): ",
                        input -> requireText(input, "Employee number", 4, 30));
                case "14" -> specialization = readStudentEditValue(
                        "Specialization (2-120 characters): ",
                        input -> requireText(input, "Specialization", 2, 120));
                case "15" -> hireDate = readStudentEditValue(
                        "Hire date (YYYY-MM-DD): ",
                        input -> parseNotFutureDate(input, "Hire date"));
                case "16" -> salary = readStudentEditValue(
                        "Salary: ", input -> parseMoney(input, "Salary"));
                case "17" -> officeNumber = readStudentEditValue(
                        "Office number (1-30 characters): ",
                        input -> requireText(input, "Office number", 1, 30));
                case "18" -> employmentType = readStudentEditValue(
                        "Employment type (1 FULL-TIME / 2 PART-TIME): ",
                        input -> choose(
                                input, "Employment type",
                                "FULL-TIME", "PART-TIME"));
                case "19" -> {
                    Teacher teacher = new Teacher(
                            personId, firstName, lastName, email, phone,
                            dateOfBirth, memberNumber, department, joinDate,
                            academicStatus, institutionalEmail, advisorName,
                            employeeNumber, specialization, hireDate, salary,
                            officeNumber, employmentType);

                    try {
                        teacher.validate();
                        ensureUniqueUpdatedRecord(teacher);
                    } catch (ValidationException | DuplicateRecordException e) {
                        System.out.println("! Could not save teacher: "
                                + e.getMessage());
                        System.out.println(
                                "  Review the details and try again.");
                        continue;
                    }

                    if (confirm("Save this teacher?")) {
                        teachers.add(teacher);
                        System.out.println(teacher.getRegistrationSummary()
                                + " registered successfully.");
                        return;
                    }
                }
                default -> System.out.println("! Choose a number from 0 to 19.");
            }
            } catch (FormBackException e) {
                System.out.println("Returning to the teacher review.");
            } catch (ValidationException | DuplicateRecordException e) {
                System.out.println("! Could not save teacher: " + e.getMessage());
                System.out.println("  Review the details and try again.");
            }
        }
    } catch (FormCancelledException e) {
        System.out.println("Teacher registration canceled.");
    }
}
    private static void registerAdministrator() throws ValidationException {
        try {
            System.out.println("\n--- Register Administrator ---");

            String personId = null, firstName = null, lastName = null;
            String email = null, phone = null;
            LocalDate dateOfBirth = null;
            String staffNumber = null, jobTitle = null, assignedOffice = null;
            LocalDate employmentDate = null;
            Integer accessLevel = null;
            Boolean active = null;
            int step = 1;
            while (step <= 3) {
                try {
                    if (step == 1) {
                        printRegistrationStep("Administrator", step,
                                "Identity", "Contact details",
                                "Administrator details");
                        personId = readRegistrationValue(personId,
                                "Person ID (4-30 characters): ",
                                input -> requireText(input, "Person ID", 4, 30));
                        firstName = readRegistrationValue(firstName,
                                "First name (2-60 characters): ",
                                input -> requireText(input, "First name", 2, 60));
                        lastName = readRegistrationValue(lastName,
                                "Last name (2-60 characters): ",
                                input -> requireText(input, "Last name", 2, 60));
                    } else if (step == 2) {
                        printRegistrationStep("Administrator", step,
                                "Identity", "Contact details",
                                "Administrator details");
                        email = readRegistrationValue(email, "Email: ",
                                input -> requireEmail(input, "Email"));
                        phone = readRegistrationValue(phone,
                                "Phone (10-15 digits): ",
                                input -> requirePhone(input, "Phone"));
                        dateOfBirth = readRegistrationValue(dateOfBirth,
                                "Date of birth (YYYY-MM-DD): ",
                                input -> parsePastDate(input, "Date of birth"));
                    } else {
                        printRegistrationStep("Administrator", step,
                                "Identity", "Contact details",
                                "Administrator details");
                        staffNumber = readRegistrationValue(staffNumber,
                                "Staff number (4-30 characters): ",
                                input -> requireText(input, "Staff number", 4, 30));
                        jobTitle = readRegistrationValue(jobTitle,
                                "Job title (2-120 characters): ",
                                input -> requireText(input, "Job title", 2, 120));
                        assignedOffice = readRegistrationValue(assignedOffice,
                                "Assigned office (1-30 characters): ",
                                input -> requireText(input, "Assigned office", 1, 30));
                        employmentDate = readRegistrationValue(employmentDate,
                                "Employment date (YYYY-MM-DD): ",
                                input -> parseNotFutureDate(
                                        input, "Employment date"));
                        accessLevel = readRegistrationValue(accessLevel,
                                "Access level (1-5): ",
                                input -> parseInt(input, "Access level", 1, 5));
                        active = readRegistrationValue(active,
                                "Active (1 YES / 2 NO): ",
                                input -> choose(input, "Active status",
                                        "YES", "NO").equals("YES"));
                    }
                    step = readRegistrationStepAction(step);
                } catch (StudentBackException e) {
                    if (step > 1) {
                        step--;
                        System.out.println("Returning to the previous step.");
                    } else {
                        System.out.println("Already at the first step.");
                    }
                }
            }

            while (true) {
                printRegistrationStep("Administrator", 4, "Identity",
                        "Contact details", "Administrator details");
                System.out.println("\n--- Review Administrator ---");
                System.out.println("\n--- Personal Details ---");
                System.out.println("1. Person ID: " + personId);
                System.out.println("2. First name: " + firstName);
                System.out.println("3. Last name: " + lastName);
                System.out.println("4. Email: " + email);
                System.out.println("5. Phone: " + phone);
                System.out.println("6. Date of birth: " + dateOfBirth);
                System.out.println("\n--- Administrator Details ---");
                System.out.println("7. Staff number: " + staffNumber);
                System.out.println("8. Job title: " + jobTitle);
                System.out.println("9. Assigned office: " + assignedOffice);
                System.out.println("10. Employment date: " + employmentDate);
                System.out.println("11. Access level: " + accessLevel);
                System.out.println("12. Active: " + (active ? "YES" : "NO"));
                System.out.println("13. Save administrator");
                System.out.println("0. Cancel");

                String choice = readLine("Choose a field to edit or an action: ");
                if (choice.equals(CMD_BACK)) {
                    returnToRegistrationReview("Administrator", "Identity",
                            "Contact details", "Administrator details");
                    continue;
                }
                if (choice.equals(CMD_CANCEL) || choice.equals("0")) {
                    System.out.println("Administrator registration canceled.");
                    return;
                }

                try {
                switch (choice) {
                    case "1" -> personId = readStudentEditValue(
                            "Person ID (4-30 characters): ",
                            input -> requireText(input, "Person ID", 4, 30));
                    case "2" -> firstName = readStudentEditValue(
                            "First name (2-60 characters): ",
                            input -> requireText(input, "First name", 2, 60));
                    case "3" -> lastName = readStudentEditValue(
                            "Last name (2-60 characters): ",
                            input -> requireText(input, "Last name", 2, 60));
                    case "4" -> email = readStudentEditValue(
                            "Email: ", input -> requireEmail(input, "Email"));
                    case "5" -> phone = readStudentEditValue(
                            "Phone (10-15 digits): ",
                            input -> requirePhone(input, "Phone"));
                    case "6" -> dateOfBirth = readStudentEditValue(
                            "Date of birth (YYYY-MM-DD): ",
                            input -> parsePastDate(input, "Date of birth"));
                    case "7" -> staffNumber = readStudentEditValue(
                            "Staff number (4-30 characters): ",
                            input -> requireText(input, "Staff number", 4, 30));
                    case "8" -> jobTitle = readStudentEditValue(
                            "Job title (2-120 characters): ",
                            input -> requireText(input, "Job title", 2, 120));
                    case "9" -> assignedOffice = readStudentEditValue(
                            "Assigned office (1-30 characters): ",
                            input -> requireText(input, "Assigned office", 1, 30));
                    case "10" -> employmentDate = readStudentEditValue(
                            "Employment date (YYYY-MM-DD): ",
                            input -> parseNotFutureDate(input, "Employment date"));
                    case "11" -> accessLevel = readStudentEditValue(
                            "Access level (1-5): ",
                            input -> parseInt(input, "Access level", 1, 5));
                    case "12" -> active = readStudentEditValue(
                            "Active (1 YES / 2 NO): ",
                            input -> choose(input, "Active status", "YES", "NO")
                                    .equals("YES"));
                    case "13" -> {
                        Administrator administrator = new Administrator(
                                personId, firstName, lastName, email, phone,
                                dateOfBirth, staffNumber, jobTitle,
                                assignedOffice, employmentDate, accessLevel,
                                active);
                        try {
                            administrator.validate();
                            ensureUniqueUpdatedRecord(administrator);
                        } catch (ValidationException
                                 | DuplicateRecordException e) {
                            System.out.println(
                                    "! Could not save administrator: "
                                            + e.getMessage());
                            System.out.println(
                                    "  Review the details and try again.");
                            continue;
                        }
                        if (confirm("Save this administrator?")) {
                            administrators.add(administrator);
                            System.out.println(
                                    administrator.getRegistrationSummary()
                                            + " registered successfully.");
                            return;
                        }
                    }
                    default -> System.out.println("! Choose a number from 0 to 13.");
                }
                } catch (FormBackException e) {
                    System.out.println("Returning to the administrator review.");
                }
            }
        } catch (FormCancelledException e) {
            System.out.println("Administrator registration canceled.");
        }
    }

    private static void registerCourse() throws ValidationException {
        try {
            System.out.println("\n--- Register Course ---");
            String courseId = null, courseCode = null, courseTitle = null;
            Integer creditHours = null;
            Department department = null;
            Teacher teacher = null;
            int step = 1;
            while (step <= 3) {
                try {
                    if (step == 1) {
                        printRegistrationStep("Course", step,
                                "Course identity", "Academic placement",
                                "Assigned teacher");
                        courseId = readRegistrationValue(courseId,
                                "Course ID (4-30 characters): ",
                                input -> requireText(input, "Course ID", 4, 30));
                        courseCode = readRegistrationValue(courseCode,
                                "Course code (2-20 characters): ",
                                input -> requireText(input, "Course code", 2, 20));
                        courseTitle = readRegistrationValue(courseTitle,
                                "Course title (2-120 characters): ",
                                input -> requireText(input, "Course title", 2, 120));
                    } else if (step == 2) {
                        printRegistrationStep("Course", step,
                                "Course identity", "Academic placement",
                                "Assigned teacher");
                        creditHours = readRegistrationValue(creditHours,
                                "Credit hours (1-6): ",
                                input -> parseInt(input, "Credit hours", 1, 6));
                        if (department == null) {
                            department = chooseDepartment();
                            if (department == null) {
                                throw new StudentBackException();
                            }
                        }
                    } else {
                        printRegistrationStep("Course", step,
                                "Course identity", "Academic placement",
                                "Assigned teacher");
                        if (teacher == null) {
                            teacher = chooseCourseTeacher();
                            if (teacher == null) {
                                throw new StudentBackException();
                            }
                        }
                    }
                    step = readRegistrationStepAction(step);
                } catch (StudentBackException e) {
                    if (step > 1) {
                        step--;
                        System.out.println("Returning to the previous step.");
                    } else {
                        System.out.println("Already at the first step.");
                    }
                }
            }

            while (true) {
                printRegistrationStep("Course", 4, "Course identity",
                        "Academic placement", "Assigned teacher");
                System.out.println("\n--- Review Course ---");
                System.out.println("1. Course ID: " + courseId);
                System.out.println("2. Course code: " + courseCode);
                System.out.println("3. Course title: " + courseTitle);
                System.out.println("4. Credit hours: " + creditHours);
                System.out.println("5. Department: " + department.getDisplayName());
                System.out.println("6. Assigned teacher: " + teacher.getDisplayName());
                System.out.println("7. Save course");
                System.out.println("0. Cancel");

                String choice = readLine("Choose a field to edit or an action: ");
                if (choice.equals(CMD_BACK)) {
                    returnToRegistrationReview("Course", "Course identity",
                            "Academic placement", "Assigned teacher");
                    continue;
                }
                if (choice.equals(CMD_CANCEL) || choice.equals("0")) {
                    System.out.println("Course registration canceled.");
                    return;
                }
                try {
                switch (choice) {
                    case "1" -> courseId = readStudentEditValue(
                            "Course ID (4-30 characters): ",
                            input -> requireText(input, "Course ID", 4, 30));
                    case "2" -> courseCode = readStudentEditValue(
                            "Course code (2-20 characters): ",
                            input -> requireText(input, "Course code", 2, 20));
                    case "3" -> courseTitle = readStudentEditValue(
                            "Course title (2-120 characters): ",
                            input -> requireText(input, "Course title", 2, 120));
                    case "4" -> creditHours = readStudentEditValue(
                            "Credit hours (1-6): ",
                            input -> parseInt(input, "Credit hours", 1, 6));
                    case "5" -> {
                        Department selected = chooseDepartment();
                        if (selected != null) {
                            department = selected;
                        }
                    }
                    case "6" -> {
                        Teacher selected = chooseCourseTeacher();
                        if (selected != null) {
                            teacher = selected;
                        }
                    }
                    case "7" -> {
                        Course course = new Course(courseId, courseCode,
                                courseTitle, creditHours, department, teacher);
                        try {
                            course.validate();
                            ensureUniqueUpdatedRecord(course);
                        } catch (ValidationException
                                 | DuplicateRecordException e) {
                            System.out.println("! Could not save course: "
                                    + e.getMessage());
                            System.out.println(
                                    "  Review the details and try again.");
                            continue;
                        }
                        if (confirm("Save this course?")) {
                            courses.add(course);
                            System.out.println(course.getRegistrationSummary()
                                    + " registered successfully.");
                            return;
                        }
                    }
                    default -> System.out.println("! Choose a number from 0 to 7.");
                }
                } catch (FormBackException e) {
                    System.out.println("Returning to the course review.");
                } catch (ValidationException | DuplicateRecordException e) {
                    System.out.println("! Could not save course: "
                            + e.getMessage());
                    System.out.println("  Review the details and try again.");
                }
            }
        } catch (FormCancelledException e) {
            System.out.println("Course registration canceled.");
        }
    }

    private static Teacher chooseCourseTeacher() throws ValidationException {
        while (true) {
            System.out.println("\n--- Choose Active Teacher ---");
            int activeTeacherCount = 0;
            for (Teacher teacher : teachers) {
                if (teacher.isActive()) {
                    activeTeacherCount++;
                    System.out.println(activeTeacherCount + ". ["
                            + teacher.getId() + "] " + teacher.getDisplayName());
                }
            }

            int registerOption = activeTeacherCount + 1;
            if (activeTeacherCount == 0) {
                System.out.println("No active teachers are registered yet.");
            }
            System.out.println(registerOption + ". Register a new Teacher");
            System.out.println("0. Cancel course registration");

            String input = readLine("Select an option: ");
            if (input.equals(CMD_BACK)) {
                return null;
            }
            if (input.equals(CMD_CANCEL) || input.equals("0")) {
                throw new FormCancelledException();
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("! Enter one of the listed numbers.");
                continue;
            }

            if (choice >= 1 && choice <= activeTeacherCount) {
                int index = 0;
                for (Teacher teacher : teachers) {
                    if (teacher.isActive() && ++index == choice) {
                        return teacher;
                    }
                }
            } else if (choice == registerOption) {
                registerTeacher();
            } else {
                System.out.println("! Enter one of the listed numbers.");
            }
        }
    }

    private static void registerClassroom() throws ValidationException {
        try {
            System.out.println("\n--- Register Classroom ---");
            String classroomId = null, buildingName = null, roomNumber = null;
            Integer capacity = null, floorNumber = null;
            Boolean available = null;
            int step = 1;
            while (step <= 3) {
                try {
                    if (step == 1) {
                        printRegistrationStep("Classroom", step,
                                "Classroom identity", "Room location",
                                "Capacity and availability");
                        classroomId = readRegistrationValue(classroomId,
                                "Classroom ID (4-30 characters): ",
                                input -> requireText(input, "Classroom ID", 4, 30));
                        buildingName = readRegistrationValue(buildingName,
                                "Building name (2-60 characters): ",
                                input -> requireText(input, "Building name", 2, 60));
                    } else if (step == 2) {
                        printRegistrationStep("Classroom", step,
                                "Classroom identity", "Room location",
                                "Capacity and availability");
                        roomNumber = readRegistrationValue(roomNumber,
                                "Room number (1-20 characters): ",
                                input -> requireText(input, "Room number", 1, 20));
                    } else {
                        printRegistrationStep("Classroom", step,
                                "Classroom identity", "Room location",
                                "Capacity and availability");
                        capacity = readRegistrationValue(capacity,
                                "Capacity (positive whole number): ",
                                input -> parseInt(
                                        input, "Capacity", 1, Integer.MAX_VALUE));
                        floorNumber = readRegistrationValue(floorNumber,
                                "Floor number (0 or greater): ",
                                input -> parseInt(
                                        input, "Floor number", 0, Integer.MAX_VALUE));
                        available = readRegistrationValue(available,
                                "Available (1 YES / 2 NO): ",
                                input -> choose(input, "Availability",
                                        "YES", "NO").equals("YES"));
                    }
                    step = readRegistrationStepAction(step);
                } catch (StudentBackException e) {
                    if (step > 1) {
                        step--;
                        System.out.println("Returning to the previous step.");
                    } else {
                        System.out.println("Already at the first step.");
                    }
                }
            }

            while (true) {
                printRegistrationStep("Classroom", 4, "Classroom identity",
                        "Room location", "Capacity and availability");
                System.out.println("\n--- Review Classroom ---");
                System.out.println("1. Classroom ID: " + classroomId);
                System.out.println("2. Building name: " + buildingName);
                System.out.println("3. Room number: " + roomNumber);
                System.out.println("4. Capacity: " + capacity);
                System.out.println("5. Floor number: " + floorNumber);
                System.out.println("6. Available: " + (available ? "YES" : "NO"));
                System.out.println("7. Save classroom");
                System.out.println("0. Cancel");

                String choice = readLine("Choose a field to edit or an action: ");
                if (choice.equals(CMD_BACK)) {
                    returnToRegistrationReview("Classroom", "Classroom identity",
                            "Room location", "Capacity and availability");
                    continue;
                }
                if (choice.equals(CMD_CANCEL) || choice.equals("0")) {
                    System.out.println("Classroom registration canceled.");
                    return;
                }
                try {
                switch (choice) {
                    case "1" -> classroomId = readStudentEditValue(
                            "Classroom ID (4-30 characters): ",
                            input -> requireText(input, "Classroom ID", 4, 30));
                    case "2" -> buildingName = readStudentEditValue(
                            "Building name (2-60 characters): ",
                            input -> requireText(input, "Building name", 2, 60));
                    case "3" -> roomNumber = readStudentEditValue(
                            "Room number (1-20 characters): ",
                            input -> requireText(input, "Room number", 1, 20));
                    case "4" -> capacity = readStudentEditValue(
                            "Capacity (positive whole number): ",
                            input -> parseInt(input, "Capacity", 1, Integer.MAX_VALUE));
                    case "5" -> floorNumber = readStudentEditValue(
                            "Floor number (0 or greater): ",
                            input -> parseInt(input, "Floor number", 0, Integer.MAX_VALUE));
                    case "6" -> available = readStudentEditValue(
                            "Available (1 YES / 2 NO): ",
                            input -> choose(input, "Availability", "YES", "NO")
                                    .equals("YES"));
                    case "7" -> {
                        Classroom classroom = new Classroom(classroomId,
                                buildingName, roomNumber, capacity, floorNumber,
                                available);
                        try {
                            classroom.validate();
                            ensureUniqueUpdatedRecord(classroom);
                        } catch (ValidationException
                                 | DuplicateRecordException e) {
                            System.out.println("! Could not save classroom: "
                                    + e.getMessage());
                            System.out.println(
                                    "  Review the details and try again.");
                            continue;
                        }
                        if (confirm("Save this classroom?")) {
                            classrooms.add(classroom);
                            System.out.println(classroom.getRegistrationSummary()
                                    + " registered successfully.");
                            return;
                        }
                    }
                    default -> System.out.println("! Choose a number from 0 to 7.");
                }
                } catch (FormBackException e) {
                    System.out.println("Returning to the classroom review.");
                }
            }
        } catch (FormCancelledException e) {
            System.out.println("Classroom registration canceled.");
        }
    }

    private static void registerEnrollment() throws ValidationException {
        try {
            System.out.println("\n--- Enroll Student ---");
            String enrollmentId = null, semester = null, status = null;
            Student student = null;
            Course course = null;
            LocalDate enrollmentDate = null;
            int step = 1;
            while (step <= 3) {
                try {
                    if (step == 1) {
                        printRegistrationStep("Enrollment", step,
                                "Enrollment identity", "Student and course",
                                "Enrollment details");
                        enrollmentId = readRegistrationValue(enrollmentId,
                                "Enrollment ID (4-30 characters): ",
                                input -> requireText(
                                        input, "Enrollment ID", 4, 30));
                    } else if (step == 2) {
                        printRegistrationStep("Enrollment", step,
                                "Enrollment identity", "Student and course",
                                "Enrollment details");
                        if (student == null) {
                            student = chooseStudent(true);
                            if (student == null) {
                                throw new StudentBackException();
                            }
                        }
                        if (course == null) {
                            course = chooseEnrollmentCourse(true);
                            if (course == null) {
                                throw new StudentBackException();
                            }
                        }
                    } else {
                        printRegistrationStep("Enrollment", step,
                                "Enrollment identity", "Student and course",
                                "Enrollment details");
                        enrollmentDate = readRegistrationValue(enrollmentDate,
                                "Enrollment date (YYYY-MM-DD): ",
                                input -> parseNotFutureDate(
                                        input, "Enrollment date"));
                        semester = readRegistrationValue(semester,
                                "Semester (1 SEMESTER_1 / 2 SEMESTER_2 / "
                                        + "3 SEMESTER_3): ",
                                input -> choose(input, "Semester",
                                        "SEMESTER_1", "SEMESTER_2",
                                        "SEMESTER_3"));
                        status = readRegistrationValue(status,
                                "Status (1 ACTIVE / 2 DROPPED / 3 COMPLETED): ",
                                input -> choose(input, "Enrollment status",
                                        "ACTIVE", "DROPPED", "COMPLETED"));
                    }
                    step = readRegistrationStepAction(step);
                } catch (StudentBackException e) {
                    if (step > 1) {
                        step--;
                        System.out.println("Returning to the previous step.");
                    } else {
                        System.out.println("Already at the first step.");
                    }
                }
            }

            while (true) {
                printRegistrationStep("Enrollment", 4,
                        "Enrollment identity", "Student and course",
                        "Enrollment details");
                System.out.println("\n--- Review Enrollment ---");
                System.out.println("1. Enrollment ID: " + enrollmentId);
                System.out.println("2. Student: " + student.getDisplayName());
                System.out.println("3. Course: " + course.getDisplayName());
                System.out.println("4. Enrollment date: " + enrollmentDate);
                System.out.println("5. Semester: " + semester);
                System.out.println("6. Status: " + status);
                System.out.println("7. Save enrollment");
                System.out.println("0. Cancel");

                String choice = readLine("Choose a field to edit or an action: ");
                if (choice.equals(CMD_BACK)) {
                    returnToRegistrationReview("Enrollment",
                            "Enrollment identity", "Student and course",
                            "Enrollment details");
                    continue;
                }
                if (choice.equals(CMD_CANCEL) || choice.equals("0")) {
                    System.out.println("Enrollment registration canceled.");
                    return;
                }
                try {
                switch (choice) {
                    case "1" -> enrollmentId = readStudentEditValue(
                            "Enrollment ID (4-30 characters): ",
                            input -> requireText(input, "Enrollment ID", 4, 30));
                    case "2" -> {
                        Student selected = chooseStudent(true);
                        if (selected != null) {
                            student = selected;
                        }
                    }
                    case "3" -> {
                        Course selected = chooseEnrollmentCourse(true);
                        if (selected != null) {
                            course = selected;
                        }
                    }
                    case "4" -> enrollmentDate = readStudentEditValue(
                            "Enrollment date (YYYY-MM-DD): ",
                            input -> parseNotFutureDate(input, "Enrollment date"));
                    case "5" -> semester = readStudentEditValue(
                            "Semester (1 SEMESTER_1 / 2 SEMESTER_2 / 3 SEMESTER_3): ",
                            input -> choose(input, "Semester",
                                    "SEMESTER_1", "SEMESTER_2", "SEMESTER_3"));
                    case "6" -> status = readStudentEditValue(
                            "Status (1 ACTIVE / 2 DROPPED / 3 COMPLETED): ",
                            input -> choose(input, "Enrollment status",
                                    "ACTIVE", "DROPPED", "COMPLETED"));
                    case "7" -> {
                        Enrollment enrollment = new Enrollment(enrollmentId,
                                student, course, enrollmentDate, semester, status);
                        try {
                            enrollment.validate();
                            ensureUniqueUpdatedRecord(enrollment);
                        } catch (ValidationException | DuplicateRecordException e) {
                            System.out.println("! " + e.getMessage());
                            continue;
                        }
                        if (confirm("Save this enrollment?")) {
                            enrollments.add(enrollment);
                            System.out.println(enrollment.getRegistrationSummary()
                                    + " registered successfully.");
                            return;
                        }
                    }
                    default -> System.out.println("! Choose a number from 0 to 7.");
                }
                } catch (FormBackException e) {
                    System.out.println("Returning to the enrollment review.");
                }
            }
        } catch (FormCancelledException e) {
            System.out.println("Enrollment registration canceled.");
        }
    }

    private static Student chooseStudent() throws ValidationException {
        return chooseStudent(false);
    }

    private static Student chooseStudent(boolean cancelThrows)
            throws ValidationException {
        while (true) {
            System.out.println("\n--- Choose Student ---");
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                System.out.println((i + 1) + ". [" + student.getStudentNumber()
                        + "] " + student.getDisplayName());
            }

            int registerOption = students.size() + 1;
            if (students.isEmpty()) {
                System.out.println("No students are registered yet.");
            }
            System.out.println(registerOption + ". Register a new Student");
            System.out.println("0. Cancel");

            String input = readLine("Select an option: ");
            if (input.equals(CMD_BACK)) {
                return null;
            }
            if (input.equals(CMD_CANCEL) || input.equals("0")) {
                if (cancelThrows) {
                    throw new FormCancelledException();
                }
                return null;
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("! Enter one of the listed numbers.");
                continue;
            }

            if (choice >= 1 && choice <= students.size()) {
                return students.get(choice - 1);
            }
            if (choice == registerOption) {
                registerStudent();
                continue;
            }
            System.out.println("! Enter one of the listed numbers.");
        }
    }

    private static Course chooseEnrollmentCourse() throws ValidationException {
        return chooseEnrollmentCourse(false);
    }

    private static Course chooseEnrollmentCourse(boolean cancelThrows)
            throws ValidationException {
        while (true) {
            System.out.println("\n--- Choose Course ---");
            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);
                System.out.println((i + 1) + ". [" + course.getCourseCode()
                        + "] " + course.getCourseTitle());
            }

            int registerOption = courses.size() + 1;
            if (courses.isEmpty()) {
                System.out.println("No courses are registered yet.");
            }
            System.out.println(registerOption + ". Register a new Course");
            System.out.println("0. Cancel");

            String input = readLine("Select an option: ");
            if (input.equals(CMD_BACK)) {
                return null;
            }
            if (input.equals(CMD_CANCEL) || input.equals("0")) {
                if (cancelThrows) {
                    throw new FormCancelledException();
                }
                return null;
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("! Enter one of the listed numbers.");
                continue;
            }

            if (choice >= 1 && choice <= courses.size()) {
                return courses.get(choice - 1);
            }
            if (choice == registerOption) {
                registerCourse();
                continue;
            }
            System.out.println("! Enter one of the listed numbers.");
        }
    }

    private static void registerAssessment() throws ValidationException {
        try {
            System.out.println("\n--- Record Assessment ---");
            String assessmentId = null, assessmentType = null;
            Enrollment enrollment = null;
            Double maximumScore = null, obtainedScore = null;
            LocalDate assessmentDate = null;
            int step = 1;
            while (step <= 3) {
                try {
                    if (step == 1) {
                        printRegistrationStep("Assessment", step,
                                "Assessment identity", "Enrollment",
                                "Score and date");
                        assessmentId = readRegistrationValue(assessmentId,
                                "Assessment ID (4-30 characters): ",
                                input -> requireText(
                                        input, "Assessment ID", 4, 30));
                    } else if (step == 2) {
                        printRegistrationStep("Assessment", step,
                                "Assessment identity", "Enrollment",
                                "Score and date");
                        if (enrollment == null) {
                            enrollment = chooseActiveEnrollment(true);
                            if (enrollment == null) {
                                throw new StudentBackException();
                            }
                        }
                        assessmentType = readRegistrationValue(assessmentType,
                                "Type (1 QUIZ / 2 ASSIGNMENT / 3 MIDTERM / "
                                        + "4 FINAL_EXAM): ",
                                input -> choose(input, "Assessment type",
                                        "QUIZ", "ASSIGNMENT", "MIDTERM",
                                        "FINAL_EXAM"));
                    } else {
                        printRegistrationStep("Assessment", step,
                                "Assessment identity", "Enrollment",
                                "Score and date");
                        maximumScore = readRegistrationValue(maximumScore,
                                "Maximum score (greater than 0): ",
                                input -> parseScore(input, "Maximum score"));
                        obtainedScore = readRegistrationValue(obtainedScore,
                                "Obtained score (0 to maximum): ",
                                input -> parseScore(input, "Obtained score"));
                        assessmentDate = readRegistrationValue(assessmentDate,
                                "Assessment date (YYYY-MM-DD): ",
                                input -> parseNotFutureDate(
                                        input, "Assessment date"));
                    }
                    step = readRegistrationStepAction(step);
                } catch (StudentBackException e) {
                    if (step > 1) {
                        step--;
                        System.out.println("Returning to the previous step.");
                    } else {
                        System.out.println("Already at the first step.");
                    }
                }
            }

            while (true) {
                printRegistrationStep("Assessment", 4,
                        "Assessment identity", "Enrollment", "Score and date");
                System.out.println("\n--- Review Assessment ---");
                System.out.println("1. Assessment ID: " + assessmentId);
                System.out.println("2. Enrollment: " + enrollment.getDisplayName());
                System.out.println("3. Type: " + assessmentType);
                System.out.println("4. Maximum score: " + maximumScore);
                System.out.println("5. Obtained score: " + obtainedScore);
                System.out.println("6. Assessment date: " + assessmentDate);
                System.out.println("7. Save assessment");
                System.out.println("0. Cancel");

                String choice = readLine("Choose a field to edit or an action: ");
                if (choice.equals(CMD_BACK)) {
                    returnToRegistrationReview("Assessment",
                            "Assessment identity", "Enrollment", "Score and date");
                    continue;
                }
                if (choice.equals(CMD_CANCEL) || choice.equals("0")) {
                    System.out.println("Assessment registration canceled.");
                    return;
                }
                try {
                switch (choice) {
                    case "1" -> assessmentId = readStudentEditValue(
                            "Assessment ID (4-30 characters): ",
                            input -> requireText(input, "Assessment ID", 4, 30));
                    case "2" -> {
                        Enrollment selected = chooseActiveEnrollment(true);
                        if (selected != null) {
                            enrollment = selected;
                        }
                    }
                    case "3" -> assessmentType = readStudentEditValue(
                            "Type (1 QUIZ / 2 ASSIGNMENT / 3 MIDTERM / 4 FINAL_EXAM): ",
                            input -> choose(input, "Assessment type",
                                    "QUIZ", "ASSIGNMENT", "MIDTERM", "FINAL_EXAM"));
                    case "4" -> maximumScore = readStudentEditValue(
                            "Maximum score (greater than 0): ",
                            input -> parseScore(input, "Maximum score"));
                    case "5" -> obtainedScore = readStudentEditValue(
                            "Obtained score (0 to maximum): ",
                            input -> parseScore(input, "Obtained score"));
                    case "6" -> assessmentDate = readStudentEditValue(
                            "Assessment date (YYYY-MM-DD): ",
                            input -> parseNotFutureDate(input, "Assessment date"));
                    case "7" -> {
                        Assessment assessment = new Assessment(assessmentId,
                                enrollment, assessmentType, maximumScore,
                                obtainedScore, assessmentDate);
                        try {
                            assessment.validate();
                            ensureUniqueUpdatedRecord(assessment);
                        } catch (ValidationException | DuplicateRecordException e) {
                            System.out.println("! " + e.getMessage());
                            continue;
                        }
                        if (confirm("Save this assessment?")) {
                            assessments.add(assessment);
                            System.out.println(assessment.getRegistrationSummary()
                                    + " recorded successfully.");
                            return;
                        }
                    }
                    default -> System.out.println("! Choose a number from 0 to 7.");
                }
                } catch (FormBackException e) {
                    System.out.println("Returning to the assessment review.");
                }
            }
        } catch (FormCancelledException e) {
            System.out.println("Assessment registration canceled.");
        }
    }

    private static Enrollment chooseActiveEnrollment()
            throws ValidationException {
        return chooseActiveEnrollment(false);
    }

    private static Enrollment chooseActiveEnrollment(boolean cancelThrows)
            throws ValidationException {
        while (true) {
            System.out.println("\n--- Choose Active Enrollment ---");
            int activeEnrollmentCount = 0;
            for (Enrollment enrollment : enrollments) {
                if (enrollment.isActive()) {
                    activeEnrollmentCount++;
                    System.out.println(activeEnrollmentCount + ". ["
                            + enrollment.getId() + "] "
                            + enrollment.getDisplayName() + " ("
                            + enrollment.getSemester() + ")");
                }
            }

            int registerOption = activeEnrollmentCount + 1;
            if (activeEnrollmentCount == 0) {
                System.out.println("No active enrollments are available.");
            }
            System.out.println(registerOption + ". Create an enrollment");
            System.out.println("0. Cancel");

            String input = readLine("Select an option: ");
            if (input.equals(CMD_BACK)) {
                return null;
            }
            if (input.equals(CMD_CANCEL) || input.equals("0")) {
                if (cancelThrows) {
                    throw new FormCancelledException();
                }
                return null;
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("! Enter one of the listed numbers.");
                continue;
            }

            if (choice >= 1 && choice <= activeEnrollmentCount) {
                int index = 0;
                for (Enrollment enrollment : enrollments) {
                    if (enrollment.isActive() && ++index == choice) {
                        return enrollment;
                    }
                }
            }
            if (choice == registerOption) {
                registerEnrollment();
                continue;
            }
            System.out.println("! Enter one of the listed numbers.");
        }
    }

    private static void registerFeePayment() throws ValidationException {
        try {
            System.out.println("\n--- Record Fee Payment ---");
            String paymentId = null, paymentMethod = null, receiptNumber = null;
            Student student = null;
            BigDecimal amount = null;
            LocalDate paymentDate = null;
            int step = 1;
            while (step <= 3) {
                try {
                    if (step == 1) {
                        printRegistrationStep("Fee payment", step,
                                "Payment identity", "Student",
                                "Payment details");
                        paymentId = readRegistrationValue(paymentId,
                                "Payment ID (4-30 characters): ",
                                input -> requireText(input, "Payment ID", 4, 30));
                    } else if (step == 2) {
                        printRegistrationStep("Fee payment", step,
                                "Payment identity", "Student",
                                "Payment details");
                        if (student == null) {
                            student = chooseStudent(true);
                            if (student == null) {
                                throw new StudentBackException();
                            }
                        }
                    } else {
                        printRegistrationStep("Fee payment", step,
                                "Payment identity", "Student",
                                "Payment details");
                        amount = readRegistrationValue(amount, "Amount: ",
                                input -> parseMoney(input, "Amount"));
                        paymentDate = readRegistrationValue(paymentDate,
                                "Payment date (YYYY-MM-DD): ",
                                input -> parseNotFutureDate(
                                        input, "Payment date"));
                        paymentMethod = readRegistrationValue(paymentMethod,
                                "Method (1 CASH / 2 MOBILE_MONEY / "
                                        + "3 BANK_TRANSFER / 4 CARD): ",
                                input -> choose(input, "Payment method",
                                        "CASH", "MOBILE_MONEY",
                                        "BANK_TRANSFER", "CARD"));
                        receiptNumber = readRegistrationValue(receiptNumber,
                                "Receipt number (1-30 characters): ",
                                input -> requireText(
                                        input, "Receipt number", 1, 30));
                    }
                    step = readRegistrationStepAction(step);
                } catch (StudentBackException e) {
                    if (step > 1) {
                        step--;
                        System.out.println("Returning to the previous step.");
                    } else {
                        System.out.println("Already at the first step.");
                    }
                }
            }

            while (true) {
                printRegistrationStep("Fee payment", 4,
                        "Payment identity", "Student", "Payment details");
                System.out.println("\n--- Review Fee Payment ---");
                System.out.println("1. Payment ID: " + paymentId);
                System.out.println("2. Student: " + student.getDisplayName());
                System.out.println("3. Amount: " + amount.toPlainString());
                System.out.println("4. Payment date: " + paymentDate);
                System.out.println("5. Payment method: " + paymentMethod);
                System.out.println("6. Receipt number: " + receiptNumber);
                System.out.println("7. Save payment");
                System.out.println("0. Cancel");

                String choice = readLine("Choose a field to edit or an action: ");
                if (choice.equals(CMD_BACK)) {
                    returnToRegistrationReview("Fee payment",
                            "Payment identity", "Student", "Payment details");
                    continue;
                }
                if (choice.equals(CMD_CANCEL) || choice.equals("0")) {
                    System.out.println("Fee payment canceled.");
                    return;
                }
                try {
                switch (choice) {
                    case "1" -> paymentId = readStudentEditValue(
                            "Payment ID (4-30 characters): ",
                            input -> requireText(input, "Payment ID", 4, 30));
                    case "2" -> {
                        Student selected = chooseStudent(true);
                        if (selected != null) {
                            student = selected;
                        }
                    }
                    case "3" -> amount = readStudentEditValue(
                            "Amount: ", input -> parseMoney(input, "Amount"));
                    case "4" -> paymentDate = readStudentEditValue(
                            "Payment date (YYYY-MM-DD): ",
                            input -> parseNotFutureDate(input, "Payment date"));
                    case "5" -> paymentMethod = readStudentEditValue(
                            "Method (1 CASH / 2 MOBILE_MONEY / "
                                    + "3 BANK_TRANSFER / 4 CARD): ",
                            input -> choose(input, "Payment method", "CASH",
                                    "MOBILE_MONEY", "BANK_TRANSFER", "CARD"));
                    case "6" -> receiptNumber = readStudentEditValue(
                            "Receipt number (1-30 characters): ",
                            input -> requireText(input, "Receipt number", 1, 30));
                    case "7" -> {
                        FeePayment payment = new FeePayment(paymentId,
                                student, amount, paymentDate, paymentMethod,
                                receiptNumber);
                        try {
                            payment.validate();
                            ensureUniqueUpdatedRecord(payment);
                        } catch (ValidationException | DuplicateRecordException e) {
                            System.out.println("! " + e.getMessage());
                            continue;
                        }
                        if (confirm("Save this payment?")) {
                            payments.add(payment);
                            System.out.println(payment.getRegistrationSummary()
                                    + " recorded successfully.");
                            return;
                        }
                    }
                    default -> System.out.println("! Choose a number from 0 to 7.");
                }
                } catch (FormBackException e) {
                    System.out.println("Returning to the fee payment review.");
                }
            }
        } catch (FormCancelledException e) {
            System.out.println("Fee payment canceled.");
        }
    }

    private static void registerLibraryBook() throws ValidationException {
        try {
            System.out.println("\n--- Register Library Book ---");
            String bookId = null, isbn = null, title = null, author = null;
            Integer totalCopies = null;
            int step = 1;
            while (step <= 3) {
                try {
                    if (step == 1) {
                        printRegistrationStep("Library book", step,
                                "Book identity", "Bibliographic details",
                                "Inventory");
                        bookId = readRegistrationValue(bookId,
                                "Book ID (4-30 characters): ",
                                input -> requireText(input, "Book ID", 4, 30));
                        isbn = readRegistrationValue(isbn,
                                "ISBN (10 or 13 digits): ",
                                input -> {
                                    String value = requireText(input, "ISBN", 10, 17);
                                    String normalized = value.replace("-", "")
                                            .replace(" ", "");
                                    if (!normalized.matches("\\d{9}[\\dX]")
                                            && !normalized.matches("\\d{13}")) {
                                        throw new ValidationException(
                                                "ISBN must have 10 or 13 digits.");
                                    }
                                    return value;
                                });
                    } else if (step == 2) {
                        printRegistrationStep("Library book", step,
                                "Book identity", "Bibliographic details",
                                "Inventory");
                        title = readRegistrationValue(title,
                                "Title (2-120 characters): ",
                                input -> requireText(input, "Title", 2, 120));
                        author = readRegistrationValue(author,
                                "Author (2-120 characters): ",
                                input -> requireText(input, "Author", 2, 120));
                    } else {
                        printRegistrationStep("Library book", step,
                                "Book identity", "Bibliographic details",
                                "Inventory");
                        totalCopies = readRegistrationValue(totalCopies,
                                "Total copies (0 or greater): ",
                                input -> parseInt(
                                        input, "Total copies", 0, Integer.MAX_VALUE));
                    }
                    step = readRegistrationStepAction(step);
                } catch (StudentBackException e) {
                    if (step > 1) {
                        step--;
                        System.out.println("Returning to the previous step.");
                    } else {
                        System.out.println("Already at the first step.");
                    }
                }
            }

            while (true) {
                printRegistrationStep("Library book", 4,
                        "Book identity", "Bibliographic details", "Inventory");
                System.out.println("\n--- Review Library Book ---");
                System.out.println("1. Book ID: " + bookId);
                System.out.println("2. ISBN: " + isbn);
                System.out.println("3. Title: " + title);
                System.out.println("4. Author: " + author);
                System.out.println("5. Total copies: " + totalCopies);
                System.out.println("6. Available copies: " + totalCopies);
                System.out.println("7. Save library book");
                System.out.println("0. Cancel");

                String choice = readLine("Choose a field to edit or an action: ");
                if (choice.equals(CMD_BACK)) {
                    returnToRegistrationReview("Library book",
                            "Book identity", "Bibliographic details", "Inventory");
                    continue;
                }
                if (choice.equals(CMD_CANCEL) || choice.equals("0")) {
                    System.out.println("Library book registration canceled.");
                    return;
                }
                try {
                switch (choice) {
                    case "1" -> bookId = readStudentEditValue(
                            "Book ID (4-30 characters): ",
                            input -> requireText(input, "Book ID", 4, 30));
                    case "2" -> isbn = readStudentEditValue(
                            "ISBN (10 or 13 digits): ",
                            input -> {
                                String value = requireText(input, "ISBN", 10, 17);
                                String normalized = value.replace("-", "")
                                        .replace(" ", "");
                                if (!normalized.matches("\\d{9}[\\dX]")
                                        && !normalized.matches("\\d{13}")) {
                                    throw new ValidationException(
                                            "ISBN must have 10 or 13 digits.");
                                }
                                return value;
                            });
                    case "3" -> title = readStudentEditValue(
                            "Title (2-120 characters): ",
                            input -> requireText(input, "Title", 2, 120));
                    case "4" -> author = readStudentEditValue(
                            "Author (2-120 characters): ",
                            input -> requireText(input, "Author", 2, 120));
                    case "5" -> totalCopies = readStudentEditValue(
                            "Total copies (0 or greater): ",
                            input -> parseInt(input, "Total copies",
                                    0, Integer.MAX_VALUE));
                    case "6" -> System.out.println(
                            "Available copies are initialized to the total copies "
                                    + "when the book is registered.");
                    case "7" -> {
                        LibraryBook book = new LibraryBook(bookId, isbn, title,
                                author, totalCopies, totalCopies);
                        try {
                            book.validate();
                            ensureUniqueUpdatedRecord(book);
                        } catch (ValidationException | DuplicateRecordException e) {
                            System.out.println("! " + e.getMessage());
                            continue;
                        }
                        if (confirm("Save this library book?")) {
                            books.add(book);
                            System.out.println(book.getRegistrationSummary()
                                    + " registered successfully.");
                            return;
                        }
                    }
                    default -> System.out.println("! Choose a number from 0 to 7.");
                }
                } catch (FormBackException e) {
                    System.out.println("Returning to the library book review.");
                }
            }
        } catch (FormCancelledException e) {
            System.out.println("Library book registration canceled.");
        }
    }

    private static void borrowLibraryBook() throws ValidationException {
        try {
            System.out.println("\n--- Borrow Library Book ---");
            String loanId = null;
            Student student = null;
            LibraryBook book = null;
            LocalDate borrowedDate = null, dueDate = null;
            int step = 1;
            while (step <= 3) {
                try {
                    if (step == 1) {
                        printRegistrationStep("Book loan", step,
                                "Loan identity", "Borrower and book",
                                "Loan dates");
                        loanId = readRegistrationValue(loanId,
                                "Loan ID (4-30 characters): ",
                                input -> {
                                    String value = requireText(
                                            input, "Loan ID", 4, 30);
                                    for (BookLoan existing : loans) {
                                        if (existing.getLoanId()
                                                .equalsIgnoreCase(value)) {
                                            throw new ValidationException(
                                                    "Loan ID already exists. "
                                                            + "Enter a unique loan ID.");
                                        }
                                    }
                                    return value;
                                });
                    } else if (step == 2) {
                        printRegistrationStep("Book loan", step,
                                "Loan identity", "Borrower and book",
                                "Loan dates");
                        if (student == null) {
                            student = chooseStudent(true);
                            if (student == null) {
                                throw new StudentBackException();
                            }
                        }
                        if (book == null) {
                            book = chooseAvailableBook(true);
                            if (book == null) {
                                throw new StudentBackException();
                            }
                        }
                    } else {
                        printRegistrationStep("Book loan", step,
                                "Loan identity", "Borrower and book",
                                "Loan dates");
                        borrowedDate = readRegistrationValue(borrowedDate,
                                "Borrowed date (YYYY-MM-DD): ",
                                input -> parseNotFutureDate(
                                        input, "Borrowed date"));
                        LocalDate currentBorrowedDate = borrowedDate;
                        dueDate = readRegistrationValue(dueDate,
                                "Due date (YYYY-MM-DD): ",
                                input -> parseLoanDueDate(
                                        input, currentBorrowedDate));
                    }
                    step = readRegistrationStepAction(step);
                } catch (StudentBackException e) {
                    if (step > 1) {
                        step--;
                        System.out.println("Returning to the previous step.");
                    } else {
                        System.out.println("Already at the first step.");
                    }
                }
            }

            while (true) {
                printRegistrationStep("Book loan", 4,
                        "Loan identity", "Borrower and book", "Loan dates");
                BookLoan loan = new BookLoan(
                        loanId, student, book, borrowedDate, dueDate, null);
                loan.printDetails();
                System.out.println("7. Save loan");
                System.out.println("0. Cancel");
                String choice = readLine(
                        "Choose a field to edit or an action: ");
                if (choice.equals(CMD_BACK)) {
                    returnToRegistrationReview("Book loan",
                            "Loan identity", "Borrower and book", "Loan dates");
                    continue;
                }
                if (choice.equals(CMD_CANCEL) || choice.equals("0")) {
                    System.out.println("Book borrowing canceled.");
                    return;
                }

                LocalDate currentBorrowedDate = borrowedDate;
                try {
                    switch (choice) {
                        case "1" -> loanId = readStudentEditValue(
                                "Loan ID (4-30 characters): ",
                                input -> {
                                    String value = requireText(
                                            input, "Loan ID", 4, 30);
                                    for (BookLoan existing : loans) {
                                        if (existing.getLoanId()
                                                .equalsIgnoreCase(value)) {
                                            throw new ValidationException(
                                                    "Loan ID already exists. "
                                                            + "Enter a unique loan ID.");
                                        }
                                    }
                                    return value;
                                });
                        case "2" -> {
                            Student selected = chooseStudent(true);
                            if (selected != null) {
                                student = selected;
                            }
                        }
                        case "3" -> {
                            LibraryBook selected = chooseAvailableBook(true);
                            if (selected != null) {
                                book = selected;
                            }
                        }
                        case "4" -> borrowedDate = readStudentEditValue(
                                "Borrowed date (YYYY-MM-DD): ",
                                input -> parseNotFutureDate(
                                        input, "Borrowed date"));
                        case "5" -> dueDate = readStudentEditValue(
                                "Due date (YYYY-MM-DD): ",
                                input -> parseLoanDueDate(
                                        input, currentBorrowedDate));
                        case "6" -> System.out.println(
                                "Returned date is assigned only by the "
                                        + "book return workflow.");
                        case "7" -> {
                            for (BookLoan existing : loans) {
                                if (existing.getLoanId()
                                        .equalsIgnoreCase(loanId)) {
                                    throw new DuplicateRecordException(
                                            "Loan ID " + loanId
                                                    + " already exists.");
                                }
                            }
                            if (book.getAvailableCopies() <= 0) {
                                throw new BookUnavailableException(
                                        "No copies of " + book.getTitle()
                                                + " are available.");
                            }
                            loan = new BookLoan(loanId, student, book,
                                    borrowedDate, dueDate, null);
                            loan.validate();
                            if (confirm("Confirm borrowing this book?")) {
                                loans.add(loan);
                                book.setAvailableCopies(
                                        book.getAvailableCopies() - 1);
                                System.out.println(
                                        loan.getRegistrationSummary()
                                                + " recorded successfully.");
                                System.out.println("Available copies remaining: "
                                        + book.getAvailableCopies());
                                return;
                            }
                        }
                        default -> System.out.println(
                                "! Choose a number from 0 to 7.");
                    }
                } catch (FormBackException e) {
                    System.out.println("Returning to the book loan review.");
                } catch (ValidationException | DuplicateRecordException
                         | BookUnavailableException e) {
                    System.out.println("! Could not save book loan: "
                            + e.getMessage());
                    System.out.println("  Review the details and try again.");
                }
            }
        } catch (FormCancelledException e) {
            System.out.println("Book borrowing canceled.");
        }
    }

    private static LibraryBook chooseAvailableBook()
            throws ValidationException {
        return chooseAvailableBook(false);
    }

    private static LibraryBook chooseAvailableBook(boolean cancelThrows)
            throws ValidationException {
        while (true) {
            System.out.println("\n--- Choose Available Book ---");
            int availableBookCount = 0;
            for (LibraryBook book : books) {
                if (book.getAvailableCopies() > 0) {
                    availableBookCount++;
                    System.out.println(availableBookCount + ". ["
                            + book.getBookId() + "] " + book.getTitle()
                            + " (" + book.getAvailableCopies() + " available)");
                }
            }

            int registerOption = availableBookCount + 1;
            if (availableBookCount == 0) {
                System.out.println("No books with available copies are registered.");
            }
            System.out.println(registerOption + ". Register a new Library Book");
            System.out.println("0. Cancel");

            String input = readLine("Select an option: ");
            if (input.equals(CMD_BACK)) {
                return null;
            }
            if (input.equals(CMD_CANCEL) || input.equals("0")) {
                if (cancelThrows) {
                    throw new FormCancelledException();
                }
                return null;
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("! Enter one of the listed numbers.");
                continue;
            }

            if (choice >= 1 && choice <= availableBookCount) {
                int index = 0;
                for (LibraryBook book : books) {
                    if (book.getAvailableCopies() > 0 && ++index == choice) {
                        return book;
                    }
                }
            }
            if (choice == registerOption) {
                registerLibraryBook();
                continue;
            }
            System.out.println("! Enter one of the listed numbers.");
        }
    }

    private static void returnLibraryBook() throws ValidationException {
        BookLoan loan = chooseActiveLoan();
        if (loan == null) {
            System.out.println("Book return canceled.");
            return;
        }

        LocalDate returnedDate;
        try {
            returnedDate = readStudentValue(
                    "Return date (YYYY-MM-DD): ",
                    input -> {
                        LocalDate value = parseNotFutureDate(input, "Return date");
                        if (value.isBefore(loan.getBorrowedDate())) {
                            throw new ValidationException(
                                    "Return date must not be before the borrowed "
                                            + "date. Enter the same date or a later date.");
                        }
                        return value;
                    });
        } catch (FormCancelledException e) {
            System.out.println("Book return canceled.");
            return;
        }

        loan.printDetails();
        System.out.println("Return date: " + returnedDate);
        if (confirm("Confirm return of this book?")) {
            loan.setReturnedDate(returnedDate);
            loan.getBook().setAvailableCopies(
                    loan.getBook().getAvailableCopies() + 1);
            System.out.println("Book returned successfully.");
            System.out.println("Available copies: "
                    + loan.getBook().getAvailableCopies());
        } else {
            System.out.println("Book return canceled.");
        }
    }

    private static BookLoan chooseActiveLoan() {
        while (true) {
            System.out.println("\n--- Choose Active Book Loan ---");
            int activeLoanCount = 0;
            for (BookLoan loan : loans) {
                if (loan.isActive()) {
                    activeLoanCount++;
                    System.out.println(activeLoanCount + ". ["
                            + loan.getLoanId() + "] " + loan.getDisplayName()
                            + " (due " + loan.getDueDate() + ")");
                }
            }
            if (activeLoanCount == 0) {
                System.out.println("There are no books currently on loan.");
                return null;
            }

            System.out.println("0. Cancel");
            String input = readLine("Select a loan: ");
            if (input.equals(CMD_BACK) || input.equals(CMD_CANCEL)
                    || input.equals("0")) {
                return null;
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("! Enter one of the listed numbers.");
                continue;
            }
            if (choice >= 1 && choice <= activeLoanCount) {
                int index = 0;
                for (BookLoan loan : loans) {
                    if (loan.isActive() && ++index == choice) {
                        return loan;
                    }
                }
            }
            System.out.println("! Enter one of the listed numbers.");
        }
    }

    private static List<Registrable> allRecords() {
        List<Registrable> records = new ArrayList<>();
        records.addAll(departments);
        records.addAll(students);
        records.addAll(teachers);
        records.addAll(administrators);
        records.addAll(courses);
        records.addAll(classrooms);
        records.addAll(enrollments);
        records.addAll(assessments);
        records.addAll(payments);
        records.addAll(books);
        records.addAll(loans);
        return records;
    }

    private static void studentAcademicSummary() throws ValidationException {
        Student student = chooseStudent();
        if (student == null) {
            System.out.println("Academic summary canceled.");
            return;
        }

        System.out.println("\n--- Academic Summary ---");
        System.out.println(student.getRegistrationSummary());
        System.out.println("Program: " + student.getProgramName());
        System.out.println("Study year: " + student.getStudyYear());

        boolean foundEnrollment = false;
        double totalObtained = 0;
        double totalMaximum = 0;
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent() != student) {
                continue;
            }
            foundEnrollment = true;
            System.out.println("\n" + enrollment.getRegistrationSummary());
            boolean foundAssessment = false;
            for (Assessment assessment : assessments) {
                if (assessment.getEnrollment() == enrollment) {
                    foundAssessment = true;
                    totalObtained += assessment.getObtainedScore();
                    totalMaximum += assessment.getMaximumScore();
                    System.out.printf("  %s: %.2f / %.2f%n",
                            assessment.getAssessmentType(),
                            assessment.getObtainedScore(),
                            assessment.getMaximumScore());
                }
            }
            if (!foundAssessment) {
                System.out.println("  No assessments recorded.");
            }
        }
        if (!foundEnrollment) {
            System.out.println("No enrollments recorded for this student.");
        }
        if (totalMaximum > 0) {
            System.out.printf("Overall assessed score: %.2f / %.2f (%.2f%%)%n",
                    totalObtained, totalMaximum,
                    totalObtained * 100.0 / totalMaximum);
        } else {
            System.out.println("No assessment scores are available to summarize.");
        }
    }

    private static void studentPaymentHistory() throws ValidationException {
        Student student = chooseStudent();
        if (student == null) {
            System.out.println("Payment history canceled.");
            return;
        }

        System.out.println("\n--- Payment History ---");
        System.out.println(student.getRegistrationSummary());
        BigDecimal total = BigDecimal.ZERO;
        boolean foundPayment = false;
        for (FeePayment payment : payments) {
            if (payment.getStudent() == student) {
                foundPayment = true;
                System.out.println(payment.getRegistrationSummary()
                        + " (" + payment.getPaymentDate() + ", "
                        + payment.getPaymentMethod() + ")");
                total = total.add(payment.getAmount());
            }
        }
        if (!foundPayment) {
            System.out.println("No payments are recorded for this student.");
        }
        System.out.println("Total paid: " + total.toPlainString());
    }

    private static void viewAvailableBooks() {
        System.out.println("\n--- Available Library Books ---");
        boolean foundBook = false;
        for (LibraryBook book : books) {
            if (book.getAvailableCopies() > 0) {
                foundBook = true;
                System.out.println(book.getRegistrationSummary());
            }
        }
        if (!foundBook) {
            System.out.println("No library books are currently available.");
        }
    }

    private static void demonstratePolymorphism() {
        System.out.println("\n--- Person Polymorphism ---");
        List<Person> people = new ArrayList<>();
        people.addAll(students);
        people.addAll(teachers);
        people.addAll(administrators);
        if (people.isEmpty()) {
            System.out.println("No people are registered to demonstrate.");
        } else {
            for (Person person : people) {
                System.out.println(person.getRoleDescription() + ": "
                        + person.getRegistrationSummary()
                        + " | active=" + person.isActive());
            }
        }

        System.out.println("\n--- Identifiable Polymorphism ---");
        List<Identifiable> identifiableRecords = new ArrayList<>();
        identifiableRecords.addAll(departments);
        identifiableRecords.addAll(students);
        identifiableRecords.addAll(teachers);
        identifiableRecords.addAll(administrators);
        identifiableRecords.addAll(courses);
        identifiableRecords.addAll(classrooms);
        identifiableRecords.addAll(enrollments);
        identifiableRecords.addAll(assessments);
        identifiableRecords.addAll(payments);
        identifiableRecords.addAll(books);
        identifiableRecords.addAll(loans);

        if (identifiableRecords.isEmpty()) {
            System.out.println("No identifiable records are registered.");
        } else {
            for (Identifiable record : identifiableRecords) {
                System.out.println(record.getId() + " | "
                        + record.getDisplayName() + " | active="
                        + record.isActive());
            }
        }
    }

    private static void viewAllRecords() {
        List<Registrable> records = allRecords();
        if (records.isEmpty()) {
            System.out.println("No records have been registered yet.");
            return;
        }
        for (Registrable record : records) {
            System.out.println(record.getRegistrationSummary());
        }
    }

    private static void searchRecordById() {
        String input = readLine("Enter record ID to search (or CANCEL): ");
        if (input.equals(CMD_BACK) || input.equals(CMD_CANCEL)) {
            return;
        }

        List<Registrable> matches = new ArrayList<>();
        for (Registrable record : allRecords()) {
            if (((Identifiable) record).getId().equalsIgnoreCase(input)) {
                matches.add(record);
            }
        }
        if (matches.isEmpty()) {
            System.out.println("No record found with ID " + input + ".");
            return;
        }
        for (Registrable record : matches) {
            record.printDetails();
        }
    }

    private static void updateExistingRecord() throws ValidationException {
        try {
            String input = readLine(
                    "Enter the ID of the record to update (or CANCEL): ");
            if (input.equals(CMD_BACK) || input.equals(CMD_CANCEL)) {
                return;
            }

            List<Registrable> matches = new ArrayList<>();
            for (Registrable record : allRecords()) {
                if (((Identifiable) record).getId().equalsIgnoreCase(input)) {
                    matches.add(record);
                }
            }
            if (matches.isEmpty()) {
                System.out.println("No record found with ID " + input + ".");
                return;
            }

            Registrable record;
            if (matches.size() == 1) {
                record = matches.get(0);
            } else {
                System.out.println("More than one record has that ID:");
                for (int i = 0; i < matches.size(); i++) {
                    Registrable match = matches.get(i);
                    System.out.println((i + 1) + ". "
                            + match.getRegistrationSummary());
                }
                int choice = readStudentValue(
                        "Choose the record number (0 to cancel): ",
                        value -> parseInt(
                                value, "Record choice", 0, matches.size()));
                if (choice == 0) {
                    return;
                }
                record = matches.get(choice - 1);
            }

            while (true) {
                try {
                    if (!editRecord(record)) {
                        return;
                    }
                } catch (ValidationException | DuplicateRecordException e) {
                    System.out.println("! " + e.getMessage());
                    System.out.println(
                            "  The update was not saved. Select a field and try again.");
                } catch (FormCancelledException e) {
                    System.out.println(
                            "Editing session canceled. Previously confirmed changes were kept.");
                    return;
                }
            }
        } catch (FormCancelledException e) {
            System.out.println("Update canceled.");
        }
    }

    private static boolean editRecord(Registrable record)
            throws ValidationException {
        record.printDetails();
        System.out.println("Select a field number (the record ID cannot be changed).");
        System.out.println("0. Finish editing and return to the main menu");
        System.out.println("Type BACK to finish or CANCEL to end this session.");

        String selection = readLine("Field number (2-18): ");
        if (selection.equals(CMD_BACK) || selection.equals("0")) {
            System.out.println(
                    "Editing finished. Previously confirmed changes were kept.");
            return false;
        }
        if (selection.equals(CMD_CANCEL)) {
            System.out.println(
                    "Editing session canceled. Previously confirmed changes were kept.");
            return false;
        }

        int field;
        try {
            field = parseInt(selection, "Field number", 2, 18);
        } catch (ValidationException e) {
            System.out.println("! " + e.getMessage());
            return true;
        }

        try {
            if (record instanceof Person person && field <= 6) {
                editPersonField(person, field);
                return true;
            }
            if (record instanceof AcademicMember member && field <= 12) {
                editAcademicMemberField(member, field);
                return true;
            }
            if (record instanceof Student student) {
                editStudentField(student, field);
            } else if (record instanceof Teacher teacher) {
                editTeacherField(teacher, field);
            } else if (record instanceof Administrator administrator) {
                editAdministratorField(administrator, field);
            } else if (record instanceof Department department) {
                editDepartmentField(department, field);
            } else if (record instanceof Course course) {
                editCourseField(course, field);
            } else if (record instanceof Classroom classroom) {
                editClassroomField(classroom, field);
            } else if (record instanceof Enrollment enrollment) {
                editEnrollmentField(enrollment, field);
            } else if (record instanceof Assessment assessment) {
                editAssessmentField(assessment, field);
            } else if (record instanceof FeePayment payment) {
                editFeePaymentField(payment, field);
            } else if (record instanceof LibraryBook book) {
                editLibraryBookField(book, field);
            } else if (record instanceof BookLoan loan) {
                editBookLoanField(loan, field);
            }
        } catch (FormBackException e) {
            System.out.println(
                    "Field edit canceled. The record was not changed; choose another field.");
        }
        return true;
    }

    private static void editPersonField(Person person, int field)
            throws ValidationException {
        switch (field) {
            case 2 -> {
                String value = readStudentEditValue("New first name: ",
                        input -> requireText(input, "First name", 2, 60));
                String old = person.getFirstName();
                commitUpdate(person, () -> person.setFirstName(value),
                        () -> person.setFirstName(old));
            }
            case 3 -> {
                String value = readStudentEditValue("New last name: ",
                        input -> requireText(input, "Last name", 2, 60));
                String old = person.getLastName();
                commitUpdate(person, () -> person.setLastName(value),
                        () -> person.setLastName(old));
            }
            case 4 -> {
                String value = readStudentEditValue("New email: ",
                        input -> requireEmail(input, "Email"));
                String old = person.getEmail();
                commitUpdate(person, () -> person.setEmail(value),
                        () -> person.setEmail(old));
            }
            case 5 -> {
                String value = readStudentEditValue("New phone: ",
                        input -> requirePhone(input, "Phone"));
                String old = person.getPhoneNumber();
                commitUpdate(person, () -> person.setPhoneNumber(value),
                        () -> person.setPhoneNumber(old));
            }
            case 6 -> {
                LocalDate value = readStudentEditValue(
                        "New date of birth (YYYY-MM-DD): ",
                        input -> parsePastDate(input, "Date of birth"));
                LocalDate old = person.getDateOfBirth();
                commitUpdate(person, () -> person.setDateOfBirth(value),
                        () -> person.setDateOfBirth(old));
            }
            default -> System.out.println("That field is not editable here.");
        }
    }

    private static void editAcademicMemberField(AcademicMember member, int field)
            throws ValidationException {
        switch (field) {
            case 7 -> {
                String value = readStudentEditValue("New member number: ",
                        input -> requireText(input, "Member number", 4, 30));
                String old = member.getMemberNumber();
                commitUpdate(member, () -> member.setMemberNumber(value),
                        () -> member.setMemberNumber(old));
            }
            case 8 -> {
                Department value = chooseDepartment();
                if (value != null) {
                    Department old = member.getDepartment();
                    commitUpdate(member, () -> member.setDepartment(value),
                            () -> member.setDepartment(old));
                }
            }
            case 9 -> {
                LocalDate value = readStudentEditValue(
                        "New join date (YYYY-MM-DD): ",
                        input -> parseNotFutureDate(input, "Join date"));
                LocalDate old = member.getJoinDate();
                commitUpdate(member, () -> member.setJoinDate(value),
                        () -> member.setJoinDate(old));
            }
            case 10 -> {
                String value = readStudentEditValue(
                        "New academic status (1 ACTIVE / 2 INACTIVE): ",
                        input -> choose(input, "Academic status",
                                "ACTIVE", "INACTIVE"));
                String old = member.getAcademicStatus();
                commitUpdate(member, () -> member.setAcademicStatus(value),
                        () -> member.setAcademicStatus(old));
            }
            case 11 -> {
                String value = readStudentEditValue("New institutional email: ",
                        input -> requireEmail(input, "Institutional email"));
                String old = member.getInstitutionalEmail();
                commitUpdate(member,
                        () -> member.setInstitutionalEmail(value),
                        () -> member.setInstitutionalEmail(old));
            }
            case 12 -> {
                String value = readStudentEditValue("New advisor name: ",
                        input -> requireText(input, "Advisor name", 2, 120));
                String old = member.getAdvisorName();
                commitUpdate(member, () -> member.setAdvisorName(value),
                        () -> member.setAdvisorName(old));
            }
            default -> System.out.println("That field is not editable here.");
        }
    }

    private static void editStudentField(Student student, int field)
            throws ValidationException {
        switch (field) {
            case 13 -> {
                String value = readStudentEditValue("New student number: ",
                        input -> requireText(input, "Student number", 4, 30));
                String old = student.getStudentNumber();
                commitUpdate(student, () -> student.setStudentNumber(value),
                        () -> student.setStudentNumber(old));
            }
            case 14 -> {
                String value = readStudentEditValue("New program name: ",
                        input -> requireText(input, "Program name", 2, 120));
                String old = student.getProgramName();
                commitUpdate(student, () -> student.setProgramName(value),
                        () -> student.setProgramName(old));
            }
            case 15 -> {
                LocalDate value = readStudentEditValue(
                        "New admission date (YYYY-MM-DD): ",
                        input -> parseNotFutureDate(input, "Admission date"));
                LocalDate old = student.getAdmissionDate();
                commitUpdate(student, () -> student.setAdmissionDate(value),
                        () -> student.setAdmissionDate(old));
            }
            case 16 -> {
                int value = readStudentEditValue("New study year (1-8): ",
                        input -> parseInt(input, "Study year", 1, 8));
                int old = student.getStudyYear();
                commitUpdate(student, () -> student.setStudyYear(value),
                        () -> student.setStudyYear(old));
            }
            case 17 -> {
                String value = readStudentEditValue("New guardian phone: ",
                        input -> requirePhone(input, "Guardian phone"));
                String old = student.getGuardianPhone();
                commitUpdate(student, () -> student.setGuardianPhone(value),
                        () -> student.setGuardianPhone(old));
            }
            case 18 -> {
                String value = readStudentEditValue(
                        "Scholarship (1 YES / 2 NO): ",
                        input -> choose(input, "Scholarship", "YES", "NO"));
                boolean old = student.isScholarshipStatus();
                commitUpdate(student,
                        () -> student.setScholarshipStatus(value.equals("YES")),
                        () -> student.setScholarshipStatus(old));
            }
            default -> System.out.println("That field is not editable here.");
        }
    }

    private static void editTeacherField(Teacher teacher, int field)
            throws ValidationException {
        switch (field) {
            case 13 -> {
                String value = readStudentEditValue("New employee number: ",
                        input -> requireText(input, "Employee number", 4, 30));
                String old = teacher.getEmployeeNumber();
                commitUpdate(teacher, () -> teacher.setEmployeeNumber(value),
                        () -> teacher.setEmployeeNumber(old));
            }
            case 14 -> {
                String value = readStudentEditValue("New specialization: ",
                        input -> requireText(input, "Specialization", 2, 120));
                String old = teacher.getSpecialization();
                commitUpdate(teacher, () -> teacher.setSpecialization(value),
                        () -> teacher.setSpecialization(old));
            }
            case 15 -> {
                LocalDate value = readStudentEditValue(
                        "New hire date (YYYY-MM-DD): ",
                        input -> parseNotFutureDate(input, "Hire date"));
                LocalDate old = teacher.getHireDate();
                commitUpdate(teacher, () -> teacher.setHireDate(value),
                        () -> teacher.setHireDate(old));
            }
            case 16 -> {
                BigDecimal value = readStudentEditValue("New salary: ",
                        input -> parseMoney(input, "Salary"));
                BigDecimal old = teacher.getSalary();
                commitUpdate(teacher, () -> teacher.setSalary(value),
                        () -> teacher.setSalary(old));
            }
            case 17 -> {
                String value = readStudentEditValue("New office number: ",
                        input -> requireText(input, "Office number", 1, 30));
                String old = teacher.getOfficeNumber();
                commitUpdate(teacher, () -> teacher.setOfficeNumber(value),
                        () -> teacher.setOfficeNumber(old));
            }
            case 18 -> {
                String value = readStudentEditValue(
                        "Employment type (1 FULL-TIME / 2 PART-TIME): ",
                        input -> choose(input, "Employment type",
                                "FULL-TIME", "PART-TIME"));
                String old = teacher.getEmploymentType();
                commitUpdate(teacher, () -> teacher.setEmploymentType(value),
                        () -> teacher.setEmploymentType(old));
            }
            default -> System.out.println("That field is not editable here.");
        }
    }

    private static void editAdministratorField(Administrator administrator,
                                               int field)
            throws ValidationException {
        switch (field) {
            case 7 -> {
                String value = readStudentEditValue("New staff number: ",
                        input -> requireText(input, "Staff number", 4, 30));
                String old = administrator.getStaffNumber();
                commitUpdate(administrator,
                        () -> administrator.setStaffNumber(value),
                        () -> administrator.setStaffNumber(old));
            }
            case 8 -> {
                String value = readStudentEditValue("New job title: ",
                        input -> requireText(input, "Job title", 2, 120));
                String old = administrator.getJobTitle();
                commitUpdate(administrator,
                        () -> administrator.setJobTitle(value),
                        () -> administrator.setJobTitle(old));
            }
            case 9 -> {
                String value = readStudentEditValue("New assigned office: ",
                        input -> requireText(input, "Assigned office", 1, 30));
                String old = administrator.getAssignedOffice();
                commitUpdate(administrator,
                        () -> administrator.setAssignedOffice(value),
                        () -> administrator.setAssignedOffice(old));
            }
            case 10 -> {
                LocalDate value = readStudentEditValue(
                        "New employment date (YYYY-MM-DD): ",
                        input -> parseNotFutureDate(input, "Employment date"));
                LocalDate old = administrator.getEmploymentDate();
                commitUpdate(administrator,
                        () -> administrator.setEmploymentDate(value),
                        () -> administrator.setEmploymentDate(old));
            }
            case 11 -> {
                int value = readStudentEditValue("New access level (1-5): ",
                        input -> parseInt(input, "Access level", 1, 5));
                int old = administrator.getAccessLevel();
                commitUpdate(administrator,
                        () -> administrator.setAccessLevel(value),
                        () -> administrator.setAccessLevel(old));
            }
            case 12 -> {
                String value = readStudentEditValue("Active (1 YES / 2 NO): ",
                        input -> choose(input, "Active status", "YES", "NO"));
                boolean old = administrator.isActive();
                commitUpdate(administrator,
                        () -> administrator.setActive(value.equals("YES")),
                        () -> administrator.setActive(old));
            }
            default -> System.out.println("That field is not editable here.");
        }
    }

    private static void editDepartmentField(Department department, int field)
            throws ValidationException {
        switch (field) {
            case 2 -> {
                String value = readStudentEditValue("New department name: ",
                        input -> requireText(input, "Department name", 2, 60));
                String old = department.getDepartmentName();
                commitUpdate(department,
                        () -> department.setDepartmentName(value),
                        () -> department.setDepartmentName(old));
            }
            case 3 -> {
                String value = readStudentEditValue("New department code: ",
                        input -> requireText(input, "Department code", 2, 20));
                String old = department.getDepartmentCode();
                commitUpdate(department,
                        () -> department.setDepartmentCode(value),
                        () -> department.setDepartmentCode(old));
            }
            case 4 -> {
                String value = readStudentEditValue("New office location: ",
                        input -> requireText(input, "Office location", 2, 60));
                String old = department.getOfficeLocation();
                commitUpdate(department,
                        () -> department.setOfficeLocation(value),
                        () -> department.setOfficeLocation(old));
            }
            case 5 -> {
                String value = readStudentEditValue("New contact email: ",
                        input -> requireEmail(input, "Contact email"));
                String old = department.getContactEmail();
                commitUpdate(department,
                        () -> department.setContactEmail(value),
                        () -> department.setContactEmail(old));
            }
            case 6 -> {
                Teacher value = chooseCourseTeacher();
                if (value != null) {
                    Teacher old = department.getHeadTeacher();
                    commitUpdate(department,
                            () -> department.setHeadTeacher(value),
                            () -> department.setHeadTeacher(old));
                }
            }
            default -> System.out.println("That field is not editable here.");
        }
    }

    private static void editCourseField(Course course, int field)
            throws ValidationException {
        switch (field) {
            case 2 -> {
                String value = readStudentEditValue("New course code: ",
                        input -> requireText(input, "Course code", 2, 20));
                String old = course.getCourseCode();
                commitUpdate(course, () -> course.setCourseCode(value),
                        () -> course.setCourseCode(old));
            }
            case 3 -> {
                String value = readStudentEditValue("New course title: ",
                        input -> requireText(input, "Course title", 2, 120));
                String old = course.getCourseTitle();
                commitUpdate(course, () -> course.setCourseTitle(value),
                        () -> course.setCourseTitle(old));
            }
            case 4 -> {
                int value = readStudentEditValue("New credit hours (1-6): ",
                        input -> parseInt(input, "Credit hours", 1, 6));
                int old = course.getCreditHours();
                commitUpdate(course, () -> course.setCreditHours(value),
                        () -> course.setCreditHours(old));
            }
            case 5 -> {
                Department value = chooseDepartment();
                if (value != null) {
                    Department old = course.getDepartment();
                    commitUpdate(course, () -> course.setDepartment(value),
                            () -> course.setDepartment(old));
                }
            }
            case 6 -> {
                Teacher value = chooseCourseTeacher();
                if (value != null) {
                    Teacher old = course.getAssignedTeacher();
                    commitUpdate(course,
                            () -> course.setAssignedTeacher(value),
                            () -> course.setAssignedTeacher(old));
                }
            }
            default -> System.out.println("That field is not editable here.");
        }
    }

    private static void editClassroomField(Classroom classroom, int field)
            throws ValidationException {
        switch (field) {
            case 2 -> {
                String value = readStudentEditValue("New building name: ",
                        input -> requireText(input, "Building name", 2, 60));
                String old = classroom.getBuildingName();
                commitUpdate(classroom, () -> classroom.setBuildingName(value),
                        () -> classroom.setBuildingName(old));
            }
            case 3 -> {
                String value = readStudentEditValue("New room number: ",
                        input -> requireText(input, "Room number", 1, 20));
                String old = classroom.getRoomNumber();
                commitUpdate(classroom, () -> classroom.setRoomNumber(value),
                        () -> classroom.setRoomNumber(old));
            }
            case 4 -> {
                int value = readStudentEditValue(
                        "New capacity (positive whole number): ",
                        input -> parseInt(
                                input, "Capacity", 1, Integer.MAX_VALUE));
                int old = classroom.getCapacity();
                commitUpdate(classroom, () -> classroom.setCapacity(value),
                        () -> classroom.setCapacity(old));
            }
            case 5 -> {
                int value = readStudentEditValue(
                        "New floor number (0 or greater): ",
                        input -> parseInt(
                                input, "Floor number", 0, Integer.MAX_VALUE));
                int old = classroom.getFloorNumber();
                commitUpdate(classroom, () -> classroom.setFloorNumber(value),
                        () -> classroom.setFloorNumber(old));
            }
            case 6 -> {
                String value = readStudentEditValue(
                        "Available (1 YES / 2 NO): ",
                        input -> choose(input, "Availability", "YES", "NO"));
                boolean old = classroom.isAvailable();
                commitUpdate(classroom,
                        () -> classroom.setAvailable(value.equals("YES")),
                        () -> classroom.setAvailable(old));
            }
            default -> System.out.println("That field is not editable here.");
        }
    }

    private static void editEnrollmentField(Enrollment enrollment, int field)
            throws ValidationException {
        switch (field) {
            case 2 -> {
                Student value = chooseStudent();
                if (value != null) {
                    Student old = enrollment.getStudent();
                    commitUpdate(enrollment,
                            () -> enrollment.setStudent(value),
                            () -> enrollment.setStudent(old));
                }
            }
            case 3 -> {
                Course value = chooseEnrollmentCourse();
                if (value != null) {
                    Course old = enrollment.getCourse();
                    commitUpdate(enrollment,
                            () -> enrollment.setCourse(value),
                            () -> enrollment.setCourse(old));
                }
            }
            case 4 -> {
                LocalDate value = readStudentEditValue(
                        "New enrollment date (YYYY-MM-DD): ",
                        input -> parseNotFutureDate(input, "Enrollment date"));
                LocalDate old = enrollment.getEnrollmentDate();
                commitUpdate(enrollment,
                        () -> enrollment.setEnrollmentDate(value),
                        () -> enrollment.setEnrollmentDate(old));
            }
            case 5 -> {
                String value = readStudentEditValue(
                        "Semester (1 SEMESTER_1 / 2 SEMESTER_2 / 3 SEMESTER_3): ",
                        input -> choose(input, "Semester",
                                "SEMESTER_1", "SEMESTER_2", "SEMESTER_3"));
                String old = enrollment.getSemester();
                commitUpdate(enrollment, () -> enrollment.setSemester(value),
                        () -> enrollment.setSemester(old));
            }
            case 6 -> {
                String value = readStudentEditValue(
                        "Status (1 ACTIVE / 2 DROPPED / 3 COMPLETED): ",
                        input -> choose(input, "Enrollment status",
                                "ACTIVE", "DROPPED", "COMPLETED"));
                String old = enrollment.getEnrollmentStatus();
                commitUpdate(enrollment,
                        () -> enrollment.setEnrollmentStatus(value),
                        () -> enrollment.setEnrollmentStatus(old));
            }
            default -> System.out.println("That field is not editable here.");
        }
    }

    private static void editAssessmentField(Assessment assessment, int field)
            throws ValidationException {
        switch (field) {
            case 2 -> {
                Enrollment value = chooseActiveEnrollment();
                if (value != null) {
                    Enrollment old = assessment.getEnrollment();
                    commitUpdate(assessment,
                            () -> assessment.setEnrollment(value),
                            () -> assessment.setEnrollment(old));
                }
            }
            case 3 -> {
                String value = readStudentEditValue(
                        "Type (1 QUIZ / 2 ASSIGNMENT / 3 MIDTERM / 4 FINAL_EXAM): ",
                        input -> choose(input, "Assessment type",
                                "QUIZ", "ASSIGNMENT", "MIDTERM", "FINAL_EXAM"));
                String old = assessment.getAssessmentType();
                commitUpdate(assessment,
                        () -> assessment.setAssessmentType(value),
                        () -> assessment.setAssessmentType(old));
            }
            case 4 -> {
                double value = readStudentEditValue("New maximum score: ",
                        input -> parseScore(input, "Maximum score"));
                double old = assessment.getMaximumScore();
                commitUpdate(assessment,
                        () -> assessment.setMaximumScore(value),
                        () -> assessment.setMaximumScore(old));
            }
            case 5 -> {
                double value = readStudentEditValue("New obtained score: ",
                        input -> parseScore(input, "Obtained score"));
                double old = assessment.getObtainedScore();
                commitUpdate(assessment,
                        () -> assessment.setObtainedScore(value),
                        () -> assessment.setObtainedScore(old));
            }
            case 6 -> {
                LocalDate value = readStudentEditValue(
                        "New assessment date (YYYY-MM-DD): ",
                        input -> parseNotFutureDate(input, "Assessment date"));
                LocalDate old = assessment.getAssessmentDate();
                commitUpdate(assessment,
                        () -> assessment.setAssessmentDate(value),
                        () -> assessment.setAssessmentDate(old));
            }
            default -> System.out.println("That field is not editable here.");
        }
    }

    private static void editFeePaymentField(FeePayment payment, int field)
            throws ValidationException {
        switch (field) {
            case 2 -> {
                Student value = chooseStudent();
                if (value != null) {
                    Student old = payment.getStudent();
                    commitUpdate(payment, () -> payment.setStudent(value),
                            () -> payment.setStudent(old));
                }
            }
            case 3 -> {
                BigDecimal value = readStudentEditValue("New amount: ",
                        input -> parseMoney(input, "Amount"));
                BigDecimal old = payment.getAmount();
                commitUpdate(payment, () -> payment.setAmount(value),
                        () -> payment.setAmount(old));
            }
            case 4 -> {
                LocalDate value = readStudentEditValue(
                        "New payment date (YYYY-MM-DD): ",
                        input -> parseNotFutureDate(input, "Payment date"));
                LocalDate old = payment.getPaymentDate();
                commitUpdate(payment, () -> payment.setPaymentDate(value),
                        () -> payment.setPaymentDate(old));
            }
            case 5 -> {
                String value = readStudentEditValue(
                        "Method (1 CASH / 2 MOBILE_MONEY / "
                                + "3 BANK_TRANSFER / 4 CARD): ",
                        input -> choose(input, "Payment method", "CASH",
                                "MOBILE_MONEY", "BANK_TRANSFER", "CARD"));
                String old = payment.getPaymentMethod();
                commitUpdate(payment, () -> payment.setPaymentMethod(value),
                        () -> payment.setPaymentMethod(old));
            }
            case 6 -> {
                String value = readStudentEditValue("New receipt number: ",
                        input -> requireText(input, "Receipt number", 1, 30));
                String old = payment.getReceiptNumber();
                commitUpdate(payment, () -> payment.setReceiptNumber(value),
                        () -> payment.setReceiptNumber(old));
            }
            default -> System.out.println("That field is not editable here.");
        }
    }

    private static void editLibraryBookField(LibraryBook book, int field)
            throws ValidationException {
        switch (field) {
            case 2 -> {
                String value = readStudentEditValue("New ISBN: ",
                        input -> {
                            String isbn = requireText(input, "ISBN", 10, 17);
                            String normalized = isbn.replace("-", "")
                                    .replace(" ", "");
                            if (!normalized.matches("\\d{9}[\\dX]")
                                    && !normalized.matches("\\d{13}")) {
                                throw new ValidationException(
                                        "ISBN must have 10 or 13 digits.");
                            }
                            return isbn;
                        });
                String old = book.getIsbn();
                commitUpdate(book, () -> book.setIsbn(value),
                        () -> book.setIsbn(old));
            }
            case 3 -> {
                String value = readStudentEditValue("New title: ",
                        input -> requireText(input, "Title", 2, 120));
                String old = book.getTitle();
                commitUpdate(book, () -> book.setTitle(value),
                        () -> book.setTitle(old));
            }
            case 4 -> {
                String value = readStudentEditValue("New author: ",
                        input -> requireText(input, "Author", 2, 120));
                String old = book.getAuthor();
                commitUpdate(book, () -> book.setAuthor(value),
                        () -> book.setAuthor(old));
            }
            case 5 -> {
                int minimumTotal = book.getTotalCopies()
                        - book.getAvailableCopies();
                int value = readStudentEditValue(
                        "New total copies (at least " + minimumTotal + "): ",
                        input -> parseInt(input, "Total copies", minimumTotal,
                                Integer.MAX_VALUE));
                int old = book.getTotalCopies();
                commitUpdate(book, () -> book.setTotalCopies(value),
                        () -> book.setTotalCopies(old));
            }
            default -> System.out.println(
                    "Only ISBN, title, author, and total copies are editable. "
                            + "Available copies are managed by loans.");
        }
    }

    private static void editBookLoanField(BookLoan loan, int field)
            throws ValidationException {
        if (field != 5) {
            System.out.println(
                    "Only the due date is editable; borrower, book, and return "
                            + "status are controlled by loan workflows.");
            return;
        }
        LocalDate value = readStudentEditValue(
                "New due date (YYYY-MM-DD): ",
                input -> {
                    LocalDate dueDate = parseDate(input, "Due date");
                    if (dueDate.isBefore(loan.getBorrowedDate())) {
                        throw new ValidationException(
                                "Due date cannot be before the borrowed date.");
                    }
                    return dueDate;
                });
        LocalDate old = loan.getDueDate();
        commitUpdate(loan, () -> loan.setDueDate(value),
                () -> loan.setDueDate(old));
    }

    private static void commitUpdate(Registrable record, Runnable apply,
                                     Runnable rollback)
            throws ValidationException {
        apply.run();
        try {
            record.validate();
            ensureUniqueUpdatedRecord(record);
            record.printDetails();
            if (confirm("Confirm this update?")) {
                System.out.println("Record updated successfully.");
            } else {
                rollback.run();
                System.out.println("Update canceled.");
            }
        } catch (ValidationException | RuntimeException e) {
            rollback.run();
            throw e;
        }
    }

    private static void ensureUniqueUpdatedRecord(Registrable candidate) {
        Identifiable identifiable = (Identifiable) candidate;
        for (Registrable record : allRecords()) {
            if (record != candidate
                    && ((Identifiable) record).getId()
                            .equalsIgnoreCase(identifiable.getId())) {
                throw new DuplicateRecordException(
                        "Another record already has this ID.");
            }
        }

        if (candidate instanceof Person person) {
            for (Person existing : allPeople()) {
                if (existing != person
                        && existing.getEmail().equalsIgnoreCase(person.getEmail())) {
                    throw new DuplicateRecordException(
                            "Another person already uses this email.");
                }
            }
        }
        if (candidate instanceof AcademicMember member) {
            for (AcademicMember existing : allAcademicMembers()) {
                if (existing != member
                        && (existing.getMemberNumber().equalsIgnoreCase(
                                    member.getMemberNumber())
                            || existing.getInstitutionalEmail().equalsIgnoreCase(
                                    member.getInstitutionalEmail()))) {
                    throw new DuplicateRecordException(
                            "Another academic member already uses this number or email.");
                }
            }
        }
        if (candidate instanceof Student student) {
            for (Student existing : students) {
                if (existing != student
                        && existing.getStudentNumber().equalsIgnoreCase(
                                student.getStudentNumber())) {
                    throw new DuplicateRecordException(
                            "Another student already uses this student number.");
                }
            }
        } else if (candidate instanceof Teacher teacher) {
            for (Teacher existing : teachers) {
                if (existing != teacher
                        && existing.getEmployeeNumber().equalsIgnoreCase(
                                teacher.getEmployeeNumber())) {
                    throw new DuplicateRecordException(
                            "Another teacher already uses this employee number.");
                }
            }
        } else if (candidate instanceof Administrator administrator) {
            for (Administrator existing : administrators) {
                if (existing != administrator
                        && existing.getStaffNumber().equalsIgnoreCase(
                                administrator.getStaffNumber())) {
                    throw new DuplicateRecordException(
                            "Another administrator already uses this staff number.");
                }
            }
        } else if (candidate instanceof Department department) {
            for (Department existing : departments) {
                if (existing != department
                        && existing.getDepartmentCode().equalsIgnoreCase(
                                department.getDepartmentCode())) {
                    throw new DuplicateRecordException(
                            "Another department already uses this code.");
                }
            }
        } else if (candidate instanceof Course course) {
            for (Course existing : courses) {
                if (existing != course
                        && existing.getCourseCode().equalsIgnoreCase(
                                course.getCourseCode())) {
                    throw new DuplicateRecordException(
                            "Another course already uses this code.");
                }
            }
        } else if (candidate instanceof FeePayment payment) {
            ensureUniqueReceiptNumber(payment);
        } else if (candidate instanceof LibraryBook book) {
            String isbn = book.getIsbn().replace("-", "").replace(" ", "");
            for (LibraryBook existing : books) {
                String existingIsbn = existing.getIsbn()
                        .replace("-", "").replace(" ", "");
                if (existing != book && existingIsbn.equalsIgnoreCase(isbn)) {
                    throw new DuplicateRecordException(
                            "Another book already uses this ISBN.");
                }
            }
        } else if (candidate instanceof Enrollment enrollment
                && enrollment.isActive()) {
            for (Enrollment existing : enrollments) {
                if (existing != enrollment && existing.isActive()
                        && existing.getStudent() == enrollment.getStudent()
                        && existing.getCourse() == enrollment.getCourse()
                        && existing.getSemester().equalsIgnoreCase(
                                enrollment.getSemester())) {
                    throw new DuplicateRecordException(
                            "This student already has an active enrollment "
                                    + "in this course and semester.");
                }
            }
        }
    }

    private static List<Person> allPeople() {
        List<Person> people = new ArrayList<>();
        people.addAll(students);
        people.addAll(teachers);
        people.addAll(administrators);
        return people;
    }

    private static List<AcademicMember> allAcademicMembers() {
        List<AcademicMember> members = new ArrayList<>();
        members.addAll(students);
        members.addAll(teachers);
        return members;
    }

    // ===============================================================
    // PIECE 2: safe input readers and field checkers
    // ===============================================================

    // Special values returned by readLine() when the user types a command
    private static final String CMD_BACK = "\u0000BACK";
    private static final String CMD_CANCEL = "\u0000CANCEL";

    // Reads one full line. Typing BACK or CANCEL returns a command value
    // instead of text. If the input ends, the form is cancelled safely.
    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return CMD_CANCEL;
        }
        String line = scanner.nextLine().trim();
        if (line.equalsIgnoreCase("BACK")) {
            return CMD_BACK;
        }
        if (line.equalsIgnoreCase("CANCEL")) {
            return CMD_CANCEL;
        }
        return line;
    }

    // Asks a Y/N question until a clear answer is given
    private static boolean confirm(String question) {
        while (true) {
            System.out.print(question + " (Y/N): ");
            if (!scanner.hasNextLine()) {
                return false;
            }
            String answer = scanner.nextLine().trim();
            if (answer.equalsIgnoreCase("Y") || answer.equalsIgnoreCase("YES")) {
                return true;
            }
            if (answer.equalsIgnoreCase("N") || answer.equalsIgnoreCase("NO")) {
                return false;
            }
            System.out.println("! Please answer Y or N.");
        }
    }

    // Text: required, trimmed, length between min and max
    private static String requireText(String input, String label,
                                      int min, int max) throws ValidationException {
        if (input == null || input.isBlank()) {
            throw new ValidationException(label + " must not be blank.");
        }
        String text = input.trim();
        if (text.length() < min || text.length() > max) {
            throw new ValidationException(
                    label + " must be " + min + "-" + max + " characters.");
        }
        return text;
    }

    // E-mail: required, at most 100 characters, valid syntax
    private static String requireEmail(String input, String label)
            throws ValidationException {
        if (input == null || input.isBlank()) {
            throw new ValidationException(label + " must not be blank.");
        }
        String email = input.trim();
        if (email.length() > 100) {
            throw new ValidationException(
                    label + " must not exceed 100 characters.");
        }
        if (!email.matches(
                "[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}")) {
            throw new ValidationException(
                    label + " must be a valid e-mail (example: name@example.com).");
        }
        return email;
    }

    // Phone: required, optional leading +, then 10-15 digits only
    private static String requirePhone(String input, String label)
            throws ValidationException {
        if (input == null || input.isBlank()) {
            throw new ValidationException(label + " must not be blank.");
        }
        String phone = input.trim();
        String digits = phone.startsWith("+") ? phone.substring(1) : phone;
        if (!digits.matches("\\d+") || digits.length() < 10 || digits.length() > 15) {
            throw new ValidationException(label
                    + " must contain 10-15 digits, with an optional leading +.");
        }
        return phone;
    }

    // Date: parsed with LocalDate; invalid calendar dates are rejected
    private static LocalDate parseDate(String input, String label)
            throws ValidationException {
        if (input == null || input.isBlank()) {
            throw new ValidationException(
                    label + " must not be blank (format YYYY-MM-DD).");
        }
        try {
            return LocalDate.parse(input.trim());
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid date for " + label
                    + ": use YYYY-MM-DD (example: 2004-05-16).");
        }
    }

    private static LocalDate parseLoanDueDate(
            String input, LocalDate borrowedDate) throws ValidationException {
        LocalDate value = parseDate(input, "Due date");
        if (borrowedDate == null || !value.isAfter(borrowedDate)) {
            throw new ValidationException(
                    "Due date must be after the borrowed date.");
        }
        return value;
    }

    private static LocalDate parsePastDate(String input, String label)
            throws ValidationException {
        LocalDate date = parseDate(input, label);
        if (!date.isBefore(LocalDate.now())) {
            throw new ValidationException(label + " must be in the past.");
        }
        return date;
    }

    private static LocalDate parseNotFutureDate(String input, String label)
            throws ValidationException {
        LocalDate date = parseDate(input, label);
        if (date.isAfter(LocalDate.now())) {
            throw new ValidationException(label + " must not be in the future.");
        }
        return date;
    }

    // Whole number inside a range
    private static int parseInt(String input, String label, int min, int max)
            throws ValidationException {
        if (input == null || input.isBlank()) {
            throw new ValidationException(label + " must not be blank.");
        }
        try {
            int value = Integer.parseInt(input.trim());
            if (value < min || value > max) {
                throw new ValidationException(
                        label + " must be between " + min + " and " + max + ".");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new ValidationException(
                    label + " must be a whole number.");
        }
    }

    // Money: BigDecimal, strictly positive, at most 2 decimal places
    private static BigDecimal parseMoney(String input, String label)
            throws ValidationException {
        if (input == null || input.isBlank()) {
            throw new ValidationException(label + " must not be blank.");
        }
        BigDecimal value;
        try {
            value = new BigDecimal(input.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(
                    label + " must be a number (example: 250000.50).");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(label + " must be greater than zero.");
        }
        if (value.stripTrailingZeros().scale() > 2) {
            throw new ValidationException(
                    label + " must have at most 2 decimal places.");
        }
        return value;
    }

    // Score: non-negative number such as 45 or 45.5 (NaN, Infinity,
    // signs and suffixes like 5d are all rejected by the pattern)
    private static double parseScore(String input, String label)
            throws ValidationException {
        if (input == null || !input.trim().matches("\\d+(\\.\\d+)?")) {
            throw new ValidationException(label
                    + " must be a non-negative number (example: 45.5).");
        }
        return Double.parseDouble(input.trim());
    }

    // Fixed choices: the user types the number or the word
    private static String choose(String input, String label, String... options)
            throws ValidationException {
        String text = input == null ? "" : input.trim();
        for (int i = 0; i < options.length; i++) {
            if (text.equals(String.valueOf(i + 1))
                    || text.equalsIgnoreCase(options[i])) {
                return options[i];
            }
        }
        throw new ValidationException(
                label + " must be " + optionsText(options) + ".");
    }

    // Builds text such as "1 ACTIVE / 2 INACTIVE" for prompts and messages
    private static String optionsText(String... options) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < options.length; i++) {
            if (i > 0) {
                text.append(" / ");
            }
            text.append(i + 1).append(" ").append(options[i]);
        }
        return text.toString();
    }
        private static void ensureUniqueReceiptNumber(FeePayment candidate) {
        for (FeePayment existing : payments) {
            if (existing != candidate
                    && existing.getReceiptNumber().trim()
                            .equalsIgnoreCase(candidate.getReceiptNumber().trim())) {
                throw new DuplicateRecordException(
                        "Receipt number " + candidate.getReceiptNumber()
                                + " already exists.");
            }
        }
    }
}