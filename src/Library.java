import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Library {

    // Base class for the Gator Library
    RedBlackTree library;

    BufferedWriter bw;


    public Library() {
        library = new RedBlackTree();
        bw = null;
    }

    // writes out book info
    private void printBook(Integer bookID) {
        Optional<Book> optionalBook = findBook(bookID);
        if(optionalBook.isPresent()) {
            try {
                bw.write(optionalBook.get().writeBookOutput() + "\n");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                bw.write("Book " + bookID + " not found in the library" + "\n\n");

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    // prints book info within a range
    private void printBooks(Integer bookID1, Integer bookID2) {
        try {
            List<RedBlackNode> listOfNodes = library.inorderTraversal();
            for (RedBlackNode node : listOfNodes) {
                if (node.getBookId() >= bookID1 && node.getBookId() <= bookID2) {
                    bw.write(node.getBookData().writeBookOutput() + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // inserts a book into the library
    private void insertBook(Integer bookID, String bookName, String authorName, String availabilityStatus) {
        Boolean avStatus = availabilityStatus.toLowerCase().contains("yes");
        Book newBook = new Book(bookID, bookName, authorName, avStatus);
        this.library.insert(newBook);
    }

    // allows a patron to borrow a book with a priority patronPriority
    private void borrowBook(Integer patronID, Integer bookID, Integer patronPriority) {
        Optional<Book> optionalBook = findBook(bookID);
        if(optionalBook.isPresent()) {
            Book book = optionalBook.get();
            String borrowBook = book.borrowBook(patronID, patronPriority);
            try {
                bw.write(borrowBook + "\n");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // allows patron to return the book
    private void returnBook(Integer patronID, Integer bookID) {
        Optional<Book> optionalBook = findBook(bookID);
        // if there is a book to be returned then writing out the output to the file
        if(optionalBook.isPresent()) {
            Book book = optionalBook.get();
            String res = book.returnBook(patronID);
            try {
                bw.write(res + "\n");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // removing a book from the library
    private void deleteBook(Integer bookID) {
        String deletionString = this.library.deleteBook(bookID);
        try {
            bw.write(deletionString + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // finding the closest book within a targetId
    private void findClosestBook(Integer targetID) {
        List<RedBlackNode> closest = new LinkedList<>();
        int currClosest = Integer.MAX_VALUE;
        List<RedBlackNode> list = library.inorderTraversal();
        // we do inorder traversal and find the closest one
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

    public int colorFlipCount() {
        int flips = library.getMetricCounter().getColorFlipCount();
        try {
            bw.write("Colour Flip Count: " + flips + "\n\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return flips;
    }

    // Responsible for writing out program terminated to the file
    private void programTermination() {
        try {
            bw.write("Program Terminated!!");
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    private Optional<Book> findBook(Integer bookID) {
        return Optional.ofNullable(library.findBook(bookID));
    }

    public void levelOrderTraversal() {
        library.levelOrderTraversal();
    }


    public static void runOps(Library library, String fileName) {
        // initialises the buffered writer for writing to the file
        try {
            library.bw = new BufferedWriter(new FileWriter(LibraryFileProcessing.generateOutputFileName(fileName)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // processes the commands given in the input file
        processCommands(library, processFile(fileName));
    }
    private static List<List<String>> processFile(String fileName) {
        try {
            return LibraryFileProcessing.parseFile(fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void processCommands(Library library, List<List<String>> commands) {
        // calls the commands one after another as given in the input file
        if (commands == null) {
            return;
        }
        for(List<String> command : commands) {
            // first argument is the function name
            // the following are the arguments to the function
            String functionCall = command.get(0);
            if(functionCall.equalsIgnoreCase("PrintBook")) {
                int bookId = Integer.parseInt(command.get(1));
                library.printBook(bookId);
            } else if(functionCall.equalsIgnoreCase("PrintBooks")) {
                int bookId1 = Integer.parseInt(command.get(1));
                int bookId2 = Integer.parseInt(command.get(2));
                library.printBooks(bookId1, bookId2);
            } else if(functionCall.equalsIgnoreCase("InsertBook")) {
                int bookId = Integer.parseInt(command.get(1));
                String title = command.get(2);
                String author = command.get(3);
                String availabilty = command.get(4);
                library.insertBook(bookId, title, author, availabilty);
            } else if(functionCall.equalsIgnoreCase("FindClosestBook")) {
                int bookId = Integer.parseInt(command.get(1));
                library.findClosestBook(bookId);
            } else if(functionCall.equalsIgnoreCase("BorrowBook")) {
                int patronId = Integer.parseInt(command.get(1));
                int bookId = Integer.parseInt(command.get(2));
                int patronPriority = Integer.parseInt(command.get(3));
                library.borrowBook(patronId, bookId, patronPriority);
            } else if(functionCall.equalsIgnoreCase("ReturnBook")) {
                int patronId = Integer.parseInt(command.get(1));
                int bookId = Integer.parseInt(command.get(2));
                library.returnBook(patronId, bookId);
            } else if(functionCall.equalsIgnoreCase("DeleteBook")) {
                int bookId = Integer.parseInt(command.get(1));
                library.deleteBook(bookId);
            } else if(functionCall.equalsIgnoreCase("Quit")) {
                library.programTermination();
                System.exit(0);
            } else if(functionCall.equalsIgnoreCase("ColorFlipCount")) {
                library.colorFlipCount();
            }

        }
        try {
            library.bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
