import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 
 * CSCU9T4 Java strings and files exercise.
 *
 */
public class FilesInOut {

    public static void main(String[] args) {
        boolean upperCase = false;
        File input = null;
        File output = null;

        if (args.length == 0) {
            // MANUAL INPUT, if no command line arguments were given

            Scanner in = new Scanner(System.in);

            // get input file
            do {
                System.out.println("Supply file path for input:");
                input = new File(in.nextLine());
                if (!input.canRead())
                    System.out.println("Error: can not read from file.");
            } while (!input.canRead());

            // get output file
            do {
                try {
                    System.out.println("Supply file path for output:");
                    output = new File(in.nextLine());
                    output.createNewFile();
                    if (!output.canWrite())
                        System.out.println("Error: can not write to file.");
                } catch (Exception e) {
                    System.out.println("Error: file can not be created");
                }
            } while (!output.canWrite());

            // get -u flag
            boolean readFlag = false;
            String flagInput;
            System.out.println("Convert to all UPPERCASE? Y/N");
            do {
                flagInput = in.nextLine();
                if (flagInput.length() == 1) {
                    switch (flagInput.toUpperCase()) {
                        case "Y":
                            upperCase = true;
                            readFlag = true;
                            break;
                        case "N":
                            readFlag = true;
                            break;
                        default:
                            break;
                    }
                }
            } while(!readFlag);

            in.close();
        }
        else if (args.length < 2) {
            System.out.println("Error: too few arguments. Both an input file and an output file must be specified.");
            return;
        }
        else if (args.length > 3) {
            System.out.println("Error: too many arguments.");
        }
        else {
            // 2 or 3 arguments means command line input
            for (String arg : args) {
                if (arg.equals("-u")) {
                    upperCase = true;
                } else if (input == null) {
                    input = new File(arg);
                } else if (output == null) {
                    output = new File(arg);
                }
            }
        }
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
                    if (upperCase) {
                        name.append(tokens.get(i).toUpperCase());
                    }
                    else {
                        name.append(tokens.get(i).substring(0, 1).toUpperCase()).append(tokens.get(i).substring(1));
                    }

                    // if the current token is only a single character, treat it as a middle name
                    if (tokens.get(i).length() == 1) {
                        name.append(".");
                    }

                    // space between names
                    name.append(" ");
                }

                // convert the final String to a date class
                String dateString = tokens.get(tokens.size()-1);
                LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("ddMMuuuu"));

                // write formatted output to the file
                out.printf("%1$-20s\t%2$td/%2$tm/%2$tY\n", name, date);
            }
        } catch (FileNotFoundException | NullPointerException e) {
            System.out.println("Error: Input file not found or output file not accessible.");
        }
    } // main

} // FilesInOut
