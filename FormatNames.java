import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 
 * CSCU9T4 Java strings and files exercise.
 *
 */
public class FormatNames {

    public static void main(String[] args) {
        // Replace this with statements to set the file name (input) and file name (output).
        // Initially it will be easier to hardcode suitable file names.
        // Finally, add code to read the filenames as arguments from the command line.
        File input = new File("input.txt");
        File output = new File("output.txt");

        // try with file resources
        try (Scanner in = new Scanner(input); PrintWriter out = new PrintWriter(output)) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                Scanner lineScanner = new Scanner(line);
                ArrayList<String> tokens = new ArrayList<String>();     // list of all "words" in the line
                StringBuilder name = new StringBuilder();               // this will hold the formatted name

                // scan line to separate tokens
                while (lineScanner.hasNext()) {
                    tokens.add(lineScanner.next());
                }
                lineScanner.close();

                // format parts of the name and add it to the StringBuilder
                for (int i = 0; i < tokens.size() - 1; i++) {
                    name.append(tokens.get(i).substring(0, 1).toUpperCase()).append(tokens.get(i).substring(1)).append(" ");
                }

                // convert the final String to a date class
                String dateString = tokens.get(tokens.size()-1);
                LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("ddMMuuuu"));

                // write formatted output to the file
                out.printf("%1$-20s\t%2$td/%2$tm/%2$tY\n", name, date);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }

    } // main

} // FilesInOut
