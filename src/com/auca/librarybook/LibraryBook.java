package com.auca.librarybook;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

public class LibraryBook implements Identifiable, Registrable {

    // Six LibraryBook attributes
    private final String bookId;
    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

    // Constructor
    public LibraryBook(String bookId, String isbn, String title,
                       String author, int totalCopies, int availableCopies) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    // Getters
    public String getBookId() {
        return bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    // Setters (bookId has none: an ID must never change)
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    // Identifiable interface methods
    @Override
    public String getId() {
        return bookId;
    }

    @Override
    public String getDisplayName() {
        return title;
    }

    @Override
    public boolean isActive() {
        return availableCopies > 0;
    }

    // Registrable interface methods
    @Override
    public void validate() throws ValidationException {

        // Book ID: required, 4-30 characters
        if (bookId == null || bookId.isBlank()) {
            throw new ValidationException("Book ID is required.");
        }
        int idLength = bookId.trim().length();
        if (idLength < MIN_ID_LENGTH || idLength > MAX_ID_LENGTH) {
            throw new ValidationException("Book ID must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // ISBN: required, 10 or 13 digits (hyphens and spaces are ignored;
        // an ISBN-10 may end with X)
        if (isbn == null || isbn.isBlank()) {
            throw new ValidationException("ISBN is required.");
        }
        String cleanIsbn = isbn.trim().replace("-", "").replace(" ", "");
        if (!cleanIsbn.matches("\\d{9}[\\dX]") && !cleanIsbn.matches("\\d{13}")) {
            throw new ValidationException(
                    "ISBN must have 10 or 13 digits (example: 978-0132350884).");
        }

        // Title: required, 2-120 characters
        if (title == null || title.isBlank()) {
            throw new ValidationException("Book title is required.");
        }
        int titleLength = title.trim().length();
        if (titleLength < 2 || titleLength > 120) {
            throw new ValidationException("Book title must be 2-120 characters.");
        }

        // Author: required, 2-120 characters
        if (author == null || author.isBlank()) {
            throw new ValidationException("Book author is required.");
        }
        int authorLength = author.trim().length();
        if (authorLength < 2 || authorLength > 120) {
            throw new ValidationException("Book author must be 2-120 characters.");
        }

        // Copy counts: not negative, and available never exceeds total
        if (totalCopies < 0) {
            throw new ValidationException("Total copies cannot be negative.");
        }
        if (availableCopies < 0) {
            throw new ValidationException("Available copies cannot be negative.");
        }
        if (availableCopies > totalCopies) {
            throw new ValidationException(
                    "Available copies cannot exceed total copies.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("LIBRARY BOOK");
        System.out.println("  01 Book ID          : " + bookId);
        System.out.println("  02 ISBN             : " + isbn);
        System.out.println("  03 Title            : " + title);
        System.out.println("  04 Author           : " + author);
        System.out.println("  05 Total copies     : " + totalCopies);
        System.out.println("  06 Available copies : " + availableCopies);
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + bookId + "] " + title + " by " + author
                + " (" + availableCopies + "/" + totalCopies + " available)";
    }

    // Readable text for the object
    @Override
    public String toString() {
        return "LibraryBook{bookId='" + bookId + "', isbn='" + isbn
                + "', title='" + title + "', author='" + author
                + "', totalCopies=" + totalCopies
                + ", availableCopies=" + availableCopies + "}";
    }
}