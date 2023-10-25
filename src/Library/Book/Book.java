package Library.Book;

public class Book {

    Integer bookID;
    String bookName;
    String authorName;
    Boolean availabilityStatus;
    Integer borrowedBy;
    MinHeap reservationHeap;

    public Book(Integer bookID, String bookName, String authorName) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.authorName = authorName;
        availabilityStatus = true;
        borrowedBy = null;
        reservationHeap = new MinHeap();
    }

    public Book(Integer bookID, String bookName, String authorName, Boolean availabilityStatus) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.authorName = authorName;
        this.availabilityStatus = availabilityStatus;
        borrowedBy = null;
        reservationHeap = new MinHeap();
    }

    public void printBook() {
        System.out.println("Library.Library.Book.Book ID: " + this.bookID);
        System.out.println("Title: " + this.bookName);
        System.out.println("Author: " + this.authorName);
        System.out.println("Availability Status: " + (this.availabilityStatus ? "Yes" : "No"));
        System.out.println("Borrowed by: " + this.borrowedBy);
        System.out.println("Reservations: " + this.reservationHeap.toString());
    }




}
