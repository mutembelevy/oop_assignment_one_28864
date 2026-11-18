
package com.auca.feepayment;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.exceptions.ValidationException;
import com.auca.student.Student;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FeePayment implements Identifiable, Registrable {

    // Six FeePayment attributes
    private final String paymentId;
    private Student student;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String receiptNumber;

    // Constructor
    public FeePayment(
            String paymentId,
            Student student,
            BigDecimal amount,
            LocalDate paymentDate,
            String paymentMethod,
            String receiptNumber) {

        this.paymentId = paymentId;
        this.student = student;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.receiptNumber = receiptNumber;
    }

    // Getters
    public String getPaymentId() {
        return paymentId;
    }

    public Student getStudent() {
        return student;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    // Setters
    public void setStudent(Student student) {
        this.student = student;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    // Identifiable interface methods
    @Override
    public String getId() {
        return paymentId;
    }

    @Override
    public String getDisplayName() {
        return "Payment " + receiptNumber;
    }

    @Override
    public boolean isActive() {
        return paymentId != null && !paymentId.isBlank()
                && amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    // Registrable interface methods
    @Override
    public void validate() throws ValidationException {

        // Payment ID: required, 4-30 characters
        if (paymentId == null || paymentId.isBlank()) {
            throw new ValidationException("Payment ID is required.");
        }
        int idLength = paymentId.trim().length();
        if (idLength < MIN_ID_LENGTH || idLength > MAX_ID_LENGTH) {
            throw new ValidationException("Payment ID must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // Student: must be a real, selected Student object
        if (student == null) {
            throw new ValidationException("A student must be selected.");
        }

        // Amount: strictly positive, at most 2 decimal places
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Payment amount must be greater than zero.");
        }
        if (amount.stripTrailingZeros().scale() > 2) {
            throw new ValidationException(
                    "Payment amount must have at most 2 decimal places.");
        }

        // Payment date: required and not in the future
        if (paymentDate == null) {
            throw new ValidationException("Payment date is required.");
        }
        if (paymentDate.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Payment date must not be in the future.");
        }

        // Payment method: only CASH, MOBILE_MONEY, BANK_TRANSFER or CARD
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new ValidationException("Payment method is required.");
        }
        if (!paymentMethod.equals("CASH")
                && !paymentMethod.equals("MOBILE_MONEY")
                && !paymentMethod.equals("BANK_TRANSFER")
                && !paymentMethod.equals("CARD")) {
            throw new ValidationException("Payment method must be CASH, "
                    + "MOBILE_MONEY, BANK_TRANSFER or CARD.");
        }

        // Receipt number: required, at most 30 characters
        if (receiptNumber == null || receiptNumber.isBlank()) {
            throw new ValidationException("Receipt number is required.");
        }
        if (receiptNumber.trim().length() > 30) {
            throw new ValidationException(
                    "Receipt number must not exceed 30 characters.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("FEE PAYMENT");
        System.out.println("  01 Payment ID     : " + paymentId);
        System.out.println("  02 Student        : " + (student == null
                ? "Not assigned"
                : "[" + student.getStudentNumber() + "] "
                + student.getDisplayName()));
        System.out.println("  03 Amount         : "
                + (amount == null ? "null" : amount.toPlainString()));
        System.out.println("  04 Payment date   : " + paymentDate);
        System.out.println("  05 Payment method : " + paymentMethod);
        System.out.println("  06 Receipt number : " + receiptNumber);
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + paymentId + "] Receipt " + receiptNumber + " - "
                + (amount == null ? "null" : amount.toPlainString())
                + (student == null ? "" : " - " + student.getDisplayName());
    }

    // Readable text for the object
    @Override
    public String toString() {
        return "FeePayment{paymentId='" + paymentId + "', student="
                + (student == null ? "none" : student.getDisplayName())
                + ", amount=" + amount + ", date=" + paymentDate
                + ", method='" + paymentMethod + "', receipt='"
                + receiptNumber + "'}";
    }
}