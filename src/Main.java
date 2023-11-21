import Library.Book.Book;
import Library.Library;
import RedBlackTreeImpl.RedBlackTree;

public class Main {
    public static void main(String[] args) {
//        redBlackTreeTest();
//        BookInitialisation();
        Library library = new Library();
        library.insertBook(1, "Book1", "Author1", "Yes");
        library.insertBook(8, "Book8", "Author8", "Yes");
        library.insertBook(3, "Book3", "Author3", "Yes");
        library.insertBook(4, "Book4", "Author4", "Yes");
        library.borrowBook(101, 1, 1);
        library.borrowBook(102, 1, 2);
        library.borrowBook(103, 1, 2);
        library.borrowBook(104, 1, 2);
        library.borrowBook(105, 1, 2);
        library.returnBook(101, 1);
        library.printBook(1);


    }

    public static void redBlackTreeTest() {
        RedBlackTree rb = new RedBlackTree();
        rb.insert(new Book(2, "saatvik", "saatvik", true));
        rb.insert(new Book(8, "saatvik", "saatvik", true));
        rb.insert(new Book(9, "saatvik", "saatvik", true));
        rb.insert(new Book(7, "saatvik", "saatvik", true));
        rb.insert(new Book(6, "saatvik", "saatvik", true));
        rb.insert(new Book(5, "saatvik", "saatvik", true));
        rb.insert(new Book(96, "saatvik", "saatvik", true));
        rb.levelOrderTraversal();
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