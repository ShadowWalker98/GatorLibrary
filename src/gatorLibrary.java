import java.io.IOException;

public class gatorLibrary {
    public static void main(String[] args) throws IOException {

        Library library = new Library();
        Library.runOps(library, String.valueOf(args[0]));

    }

}