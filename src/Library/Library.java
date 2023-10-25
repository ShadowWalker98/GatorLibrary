package Library;

import Library.Book.ReservationHeap;

public class Library {

    public void printBook(Integer bookID) {}

    public void printBooks(Integer bookID1, Integer bookID2) {}

    public void insertBook(Integer bookID, String bookName, String authorName, Boolean availabilityStatus, Integer borrowedBy, ReservationHeap reservationHeap) {}

    public void borrowBook(Integer patronID, Integer bookID, Integer patronPriority) {}

    public void returnBook(Integer patronID, Integer bookID) {}

    public void deleteBook(Integer bookID) {}

    public void findClosestBook(Integer targetID) {}

    public void colorFlipCount() {}

}
