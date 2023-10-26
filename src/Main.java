import Library.Book.Book;

public class Main {
    public static void main(String[] args) {
        BookInitialisation();


    }

    public static void BookInitialisation() {
        Book book = new Book(101, "Eragon", "Paolini");
        book.printBook();
        book.borrowBook(123, 3);
        book.borrowBook(124, 3);
        book.borrowBook(125, 3);
        book.borrowBook(253, 3);
        book.borrowBook(321, 3);
        book.borrowBook(332, 1);
        book.returnBook(123);
        book.printBook();
        book.returnBook(332);
        book.printBook();
        book.returnBook(124);
        book.returnBook(125);
        book.printBook();
    }
}