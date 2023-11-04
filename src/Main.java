import Library.Book.Book;
import RedBlackTreeImpl.RedBlackTree;

public class Main {
    public static void main(String[] args) {
        rlCase();
    }

    public static void rlCase() {
        RedBlackTree redBlackTree = new RedBlackTree();
        redBlackTree.inorderTraversal();
        redBlackTree.insert(27);
        redBlackTree.insert(34);
        redBlackTree.insert(30);
//        redBlackTree.insert(30);
        redBlackTree.inorderTraversal();
    }

    public static void lrCase() {
        RedBlackTree redBlackTree = new RedBlackTree();
        redBlackTree.inorderTraversal();
        redBlackTree.insert(27);
        redBlackTree.insert(15);
        redBlackTree.insert(20);
        redBlackTree.insert(30);
        redBlackTree.inorderTraversal();
    }

    public static void llCase() {
        RedBlackTree redBlackTree = new RedBlackTree();
        redBlackTree.inorderTraversal();
        redBlackTree.insert(52);
        redBlackTree.insert(4);
        redBlackTree.insert(1);
        redBlackTree.insert(97);
        redBlackTree.inorderTraversal();
    }

    public static void rrCase() {
        RedBlackTree redBlackTree = new RedBlackTree();
        redBlackTree.inorderTraversal();
        redBlackTree.insert(3);
        redBlackTree.insert(7);
        redBlackTree.insert(20);
        redBlackTree.insert(45);
        redBlackTree.inorderTraversal();
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