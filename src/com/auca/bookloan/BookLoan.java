package com.auca.bookloan;

import java.time.LocalDate;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;

public class BookLoan implements Identifiable, Registrable {

    // Six BookLoan attributes
    private final String loanId;
    private final Student student;      // fixed: a loan never changes borrower
    private final LibraryBook book;     // fixed: a loan never changes book
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;     // null until the book is returned

    // Constructor (returnedDate is null when a loan is first created)
    public BookLoan(String loanId, Student student, LibraryBook book,
                    LocalDate borrowedDate, LocalDate dueDate,
                    LocalDate returnedDate) {
        this.loanId = loanId;
        this.student = student;
        this.book = book;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
    }

    // Getters
    public String getLoanId() {
        return loanId;
    }

    public Student getStudent() {
        return student;
    }

    public LibraryBook getBook() {
        return book;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    // Setters (only the dates can change; the setter for returnedDate is
    // how a return is recorded on the existing loan)
    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    // Identifiable interface methods
    @Override
    public String getId() {
        return loanId;
    }

    @Override
    public String getDisplayName() {
        return (student == null ? "No student" : student.getDisplayName())
                + " - " + (book == null ? "No book" : book.getTitle());
    }

    // A loan is active while the book has not been returned
    @Override
    public boolean isActive() {
        return returnedDate == null;
    }

    // Registrable interface methods
    @Override
    public void validate() throws ValidationException {

        // Loan ID: required, 4-30 characters
        if (loanId == null || loanId.isBlank()) {
            throw new ValidationException("Loan ID is required.");
        }
        int idLength = loanId.trim().length();
        if (idLength < MIN_ID_LENGTH || idLength > MAX_ID_LENGTH) {
            throw new ValidationException("Loan ID must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // Student and book: must be real, selected objects
        if (student == null) {
            throw new ValidationException("A student must be selected.");
        }
        if (book == null) {
            throw new ValidationException("A library book must be selected.");
        }

        // Borrowed date: required and not in the future
        if (borrowedDate == null) {
            throw new ValidationException("Borrowed date is required.");
        }
        if (borrowedDate.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Borrowed date must not be in the future.");
        }

        // Due date: required and strictly after the borrowed date
        if (dueDate == null) {
            throw new ValidationException("Due date is required.");
        }
        if (!dueDate.isAfter(borrowedDate)) {
            throw new ValidationException(
                    "Due date must be after the borrowed date.");
        }

        // Returned date: empty until returned; once set it must not be
        // before the borrowed date and not in the future
        if (returnedDate != null) {
            if (returnedDate.isBefore(borrowedDate)) {
                throw new ValidationException(
                        "Returned date must not be before the borrowed date.");
            }
            if (returnedDate.isAfter(LocalDate.now())) {
                throw new ValidationException(
                        "Returned date must not be in the future.");
            }
        }
    }

    @Override
    public void printDetails() {
        System.out.println("BOOK LOAN");
        System.out.println("  01 Loan ID       : " + loanId);
        System.out.println("  02 Student       : " + (student == null
                ? "Not assigned"
                : "[" + student.getStudentNumber() + "] "
                + student.getDisplayName()));
        System.out.println("  03 Book          : " + (book == null
                ? "Not assigned"
                : "[" + book.getBookId() + "] " + book.getTitle()));
        System.out.println("  04 Borrowed date : " + borrowedDate);
        System.out.println("  05 Due date      : " + dueDate);
        System.out.println("  06 Returned date : "
                + (returnedDate == null ? "Not returned yet" : returnedDate));
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + loanId + "] " + getDisplayName() + " (due " + dueDate
                + ") - " + (returnedDate == null ? "ON LOAN" : "RETURNED");
    }

    // Readable text for the object
    @Override
    public String toString() {
        return "BookLoan{loanId='" + loanId + "', student="
                + (student == null ? "none" : student.getDisplayName())
                + ", book=" + (book == null ? "none" : book.getTitle())
                + ", borrowed=" + borrowedDate + ", due=" + dueDate
                + ", returned=" + returnedDate + "}";
    }
}