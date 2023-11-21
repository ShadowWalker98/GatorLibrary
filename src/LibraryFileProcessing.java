import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibraryFileProcessing {

    // class for handling the IO part of the program
    public LibraryFileProcessing() {}

    public static List<List<String>> parseFile(String fileName) throws IOException {
        // parses the file and sends back a list of commands
        List<List<String>> listOfFunctionCalls = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String line;
        while ((line = reader.readLine()) != null) {
            List<String> functionCall = parseLine(line);
            if (!functionCall.isEmpty()) {
                listOfFunctionCalls.add(functionCall);
            }
        }

        reader.close();
        return listOfFunctionCalls;
    }

    private static List<String> parseLine(String line) {
        List<String> functionCall = new ArrayList<>();

        // Define a regular expression for function calls
        String regex = "(\\w+)\\(([^)]*)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        // Check if the line matches the pattern
        if (matcher.matches()) {
            functionCall.add(matcher.group(1)); // Function Name

            // Split parameters by comma and trim spaces
            String[] parameterArray = matcher.group(2).split("\\s*,\\s*");
            for (String param : parameterArray) {
                functionCall.add(param.replaceAll("\"", ""));
            }
        }

        return functionCall;
    }

    public static String generateOutputFileName(String fileNameWithExtension) {
        // generates the output filename for the input file
        String[] fileArguments = fileNameWithExtension.split("\\.");
        return fileArguments[0] + "_output_file." + fileArguments[1];
    }
}
