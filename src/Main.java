import Library.Book.Book;
import Library.Library;
import RedBlackTreeImpl.RedBlackTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Library library = new Library();
        Library.runOps(library, String.valueOf(args[0]));

    }

}