package Library.Book;

import java.util.Objects;

public class Book {

    Integer bookID;
    String bookName;
    String authorName;
    Boolean availabilityStatus;
    Integer borrowedBy;
    ReservationHeap reservations;

    public Book(Integer bookID, String bookName, String authorName) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.authorName = authorName;
        availabilityStatus = true;
        borrowedBy = null;
        reservations = new ReservationHeap();
    }

    public Book(Integer bookID, String bookName, String authorName, Boolean availabilityStatus) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.authorName = authorName;
        this.availabilityStatus = availabilityStatus;
        borrowedBy = null;
        reservations = new ReservationHeap();
    }

    public void printBook() {
        System.out.println("BookID = " + this.bookID);
        System.out.println("Title = " + "\"" + this.bookName + "\"");
        System.out.println("Author = " + "\"" + this.authorName + "\"");
        System.out.println("Availability Status = \"" + (this.availabilityStatus ? "Yes\"" : "No\""));
        System.out.println("Borrowed by = " + (this.borrowedBy == null ? "None" : borrowedBy));
        System.out.println("Reservations = [" + printReservations() + "]");

    }

    private String printReservations() {
        return this.reservations.printReservations();
    }

    private boolean addReservation(Integer patronID, Integer priority) {
        return this.reservations.addReservation(patronID, priority);
    }

    public String returnBook(Integer patronID) {
        if(!Objects.equals(this.borrowedBy, patronID)) {
            System.out.println("The book is currently borrowed by patronID: " + this.borrowedBy);
            return "";
        }
        // if the book is returned, the book is then given to the next person in the reservation queue
        // if the queue is empty then the borrowedBy field is set to null;
        if(reservations.isEmpty()) {
            this.borrowedBy = null;
            availabilityStatus = true;
            return "Book " + bookID + " Returned by Patron " + patronID;
        } else {
            this.borrowedBy = reservations.returnBook().getPatronID();
            availabilityStatus = false;
            return "Book " + bookID + " Returned by Patron " + patronID + "\n" + "Book " + bookID + " Allotted to Patron " + borrowedBy;

        }

    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Boolean getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(Boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Integer getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(Integer borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public String borrowBook(Integer patronID, Integer priority) {
        if(this.borrowedBy == null && availabilityStatus) {
            this.borrowedBy = patronID;
            return "Book " + bookID + " Borrowed by Patron " + patronID;
        } else if(this.borrowedBy != null && this.borrowedBy.equals(patronID)) {
            return "Book " + bookID + " Already Borrowed by Patron " + patronID;
        } else {
            if(addReservation(patronID, priority)) {
                return "Book " + bookID + " Reserved by Patron " + patronID;
            } else {
                return "Reservations are full, please try again later!";
            }
        }
    }



}
