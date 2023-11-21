import Library.Book.Book;
import Library.Library;
import RedBlackTreeImpl.RedBlackTree;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        library.insertBook(1, "Book1", "Author1", "Yes");
        library.insertBook(8, "Book8", "Author8", "Yes");
        library.insertBook(3, "Book3", "Author3", "Yes");
        library.insertBook(5, "Book4", "Author4", "Yes");
        library.insertBook(9, "Book4", "Author4", "Yes");
        library.insertBook(7, "Book4", "Author4", "Yes");
        library.insertBook(6, "Book4", "Author4", "Yes");
        library.insertBook(4, "Book4", "Author4", "Yes");
        library.insertBook(2, "Book4", "Author4", "Yes");
        library.insertBook(10, "Book4", "Author4", "Yes");
        library.insertBook(11, "Book4", "Author4", "Yes");
        library.insertBook(12, "Book4", "Author4", "Yes");
        library.deleteBook(7); // Lr(0)
        library.deleteBook(9);
        library.deleteBook(8);
        library.deleteBook(10);
        library.deleteBook(12);
        library.deleteBook(11);
//        library.deleteBook(4);
        library.levelOrderTraversal();
//        library.borrowBook(101, 1, 1);
//        library.borrowBook(102, 1, 2);
//        library.borrowBook(103, 1, 2);
//        library.borrowBook(104, 1, 2);
//        library.borrowBook(105, 1, 2);
//        library.returnBook(101, 1);
//        library.deleteBook(8);
//        library.levelOrderTraversal();

//        library.printBook(1);
//        library.findClosestBook(4);
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