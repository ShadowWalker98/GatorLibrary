import java.io.IOException;
import java.util.Objects;

public class gatorLibrary {
    public static void main(String[] args) throws IOException {

        // Initialising the library
        Library library = new Library();
        if(Objects.nonNull(args[0])) {
            Library.runOps(library, String.valueOf(args[0]));
        } else {
            System.out.println("Please provide input file like so: java gatorLibrary <inputFile>");
        }
    }

}