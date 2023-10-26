import Library.Book.Book;
import Library.Library;

public class Main {
    public static void main(String[] args) {

        Book book = new Book(101, "Eragon", "Paolini");
        book.printBook();
        book.borrowBook(123, 3);
        book.borrowBook(124, 3);
        book.borrowBook(125, 3);
        book.borrowBook(253, 3);
        book.borrowBook(321, 3);
        book.borrowBook(332, 1);
        book.returnBook(123);
        book.returnBook(332);
        book.returnBook(124);

    }
}