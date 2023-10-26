package Library.Book;

import Library.Reservation.Reservation;

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
        System.out.println("Book ID: " + this.bookID);
        System.out.println("Title: " + this.bookName);
        System.out.println("Author: " + this.authorName);
        System.out.println("Availability Status: " + (this.availabilityStatus ? "Yes" : "No"));
        System.out.println("Borrowed by: " + (this.borrowedBy == null ? "None" : borrowedBy));
        if(!reservations.isEmpty()) {
            System.out.println("Reservations: ");
            printReservations();
        } else {
            System.out.println("No reservations for this book.");
        }
    }

    private void printReservations() {
        this.reservations.printReservations();
    }

    private boolean addReservation(Integer patronID, Integer priority) {
        return this.reservations.addReservation(patronID, priority);
    }

    public void returnBook(Integer patronID) {
        if(!Objects.equals(this.borrowedBy, patronID)) {
            System.out.println("The book is currently borrowed by patronID: " + this.borrowedBy);
            return;
        }
        // if the book is returned, the book is then given to the next person in the reservation queue
        // if the queue is empty then the borrowedBy field is set to null;
        if(reservations.isEmpty()) {
            this.borrowedBy = null;
            availabilityStatus = true;
        } else {
            this.borrowedBy = reservations.returnBook().getPatronID();
            System.out.println("Book has been returned by patronID: " + patronID);
            System.out.println("BookID: " + bookID + " is now assigned to patronID: " + this.borrowedBy);
            availabilityStatus = false;
        }

    }

    public void borrowBook(Integer patronID, Integer priority) {
        if(this.borrowedBy == null) {
            System.out.println("Book available, assigning it to Patron: " + patronID);
            this.borrowedBy = patronID;
        } else if(this.borrowedBy.equals(patronID)) {
            System.out.println("Item already borrowed by patron: " + patronID);
        } else {
            System.out.println("Book currently taken by Patron: " + this.borrowedBy + ", adding requester to reservation list.");
            if(addReservation(patronID, priority)) {
                System.out.println("Reservation added successfully for Patron: " + patronID);
            } else {
                System.out.println("Reservations are full, please try again later!");
            }
        }

    }



}
