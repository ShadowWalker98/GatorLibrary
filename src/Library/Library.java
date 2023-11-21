package Library;

import Library.Book.Book;
import Library.Metrics.MetricCounter;
import RedBlackTreeImpl.RedBlackNode;
import RedBlackTreeImpl.RedBlackTree;

import java.util.*;

public class Library {
    // TODO: Modify printing implementation to write to file instead
    RedBlackTree library;
    MetricCounter metricCounter;

    public Library() {
        library = new RedBlackTree();
        metricCounter = new MetricCounter();

    }

    public void printBook(Integer bookID) {
        Optional<Book> optionalBook = findBook(bookID);
        if(optionalBook.isPresent()) {
            optionalBook.get().printBook();
        } else {
            System.out.println("Book " + bookID + " not found in the Library");
        }
    }

    public void printBooks(Integer bookID1, Integer bookID2) {
        List<RedBlackNode> listOfNodes = library.inorderTraversal();
        for (RedBlackNode node : listOfNodes) {
            if (node.getBookId() <= bookID2 && node.getBookId() >= bookID1) {
                node.getBookData().printBook();
            }
        }
    }

    public void insertBook(Integer bookID, String bookName, String authorName, String availabilityStatus) {
        Boolean avStatus = availabilityStatus.toLowerCase().contains("yes");
        Book newBook = new Book(bookID, bookName, authorName, avStatus);
        this.library.insert(newBook);
    }

    public void borrowBook(Integer patronID, Integer bookID, Integer patronPriority) {
        Optional<Book> optionalBook = findBook(bookID);
        if(optionalBook.isPresent()) {
            Book book = optionalBook.get();
            String borrowBook = book.borrowBook(patronID, patronPriority);
            System.out.println(borrowBook);
            // TODO: Write out the borrowBook string to the file, for now I'm printing it out
        }
    }

    public void returnBook(Integer patronID, Integer bookID) {
        Optional<Book> optionalBook = findBook(bookID);
        if(optionalBook.isPresent()) {
            Book book = optionalBook.get();
            String res = book.returnBook(patronID);
            System.out.println(res);
            // TODO: Write out the returnBook string to the file, for now I'm printing it out
        }
    }

    public void deleteBook(Integer bookID) {}

    public void findClosestBook(Integer targetID) {
        List<RedBlackNode> closest = new LinkedList<>();
        int currClosest = Integer.MAX_VALUE;
        List<RedBlackNode> list = library.inorderTraversal();

        for (RedBlackNode node : list) {
            if(Math.abs(node.getBookData().getBookID() - targetID) < currClosest) {
                closest.clear();
                closest.add(node);
                currClosest = Math.abs(node.getBookData().getBookID() - targetID);
            } else if(Objects.equals(node.getBookData().getBookID() - targetID, currClosest)) {
                closest.add(node);
            }
        }
        for (RedBlackNode node : closest) {
            printBook(node.getBookId());
        }
    }

    public void colorFlipCount() {}

    private Optional<Book> findBook(Integer bookID) {
        return Optional.ofNullable(library.findBook(bookID));
    }

}
