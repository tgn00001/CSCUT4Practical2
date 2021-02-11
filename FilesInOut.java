import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * CSCU9T4 Java strings and files exercise.
 *
 */
public class FilesInOut {
    public static void main(String[] args) {
        boolean upperCase = false;
        boolean html = false;
        File input = null;
        File output = null;

        // used for html title generation
        String title = "";
        Pattern p = Pattern.compile("\\..*");

        if (args.length == 0) {
            // MANUAL INPUT, if no command line arguments were given

            Scanner in = new Scanner(System.in);

            // get input file
            do {
                System.out.println("Supply file path for input:");
                input = new File(in.nextLine());
                if (!input.exists() || !input.canRead())
                    System.out.println("Error: can not read from file.");
            } while (!input.exists() || !input.canRead());

            // get output file
            do {
                try {
                    System.out.println("Supply file path for output:");
                    output = new File(in.nextLine());
                    output.createNewFile();
                    if (!input.exists() || !output.canWrite())
                        System.out.println("Error: can not write to file.");
                } catch (Exception e) {
                    System.out.println("Error: file can not be created");
                }
            } while (!input.exists() || !output.canWrite());

            upperCase = readFlag("Convert to all UPPERCASE? Y/N", in);    // get -u flag
            html = readFlag("Output as HTML? Y/N", in);                   //get -h flag

            in.close();
        }
        else if (args.length < 2) {
            System.out.println("Error: too few arguments. Both an input file and an output file must be specified.");
            return;
        }
        else if (args.length > 4) {
            System.out.println("Error: too many arguments.");
        }
        else {
            // 2 to 4 arguments means command line input
            for (String arg : args) {
                if (arg.equals("-u")) {
                    upperCase = true;
                } else if (arg.equals("-h")) {
                    html = true;
                } else if (input == null) {
                    input = new File(arg);
                } else if (output == null) {
                    output = new File(arg);
                }
            }
        }

        title = output.getName();
        Matcher m = p.matcher(title);
        title = m.replaceAll("");

        // try with file resources
        try (Scanner in = new Scanner(input); PrintWriter out = new PrintWriter(output)) {
            if (html) {
                out.printf("<html>\n\t<head>\n\t\t<title>" + title + "</title>\n\t</head>\n\n\t<body>\n");
            }

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
                if (html)
                    out.printf("\t\t<p>");

                out.printf("%1$-20s\t%2$td/%2$tm/%2$tY", name, date);

                if (html)
                    out.printf("</p>");
                out.printf("\n");
            }
            if (html) {
                out.printf("\t</body>\n</html>");
            }
        } catch (FileNotFoundException | NullPointerException e) {
            System.out.println("Error: Input file not found or output file not accessible.");
        }
    } // main

    /**
     * Reads a flag usually given at the command line using System.in
     * @param prompt the prompt to give the user
     * @return true for -[flag], false for no flag
     */
    public static boolean readFlag(String prompt, Scanner in) {
        boolean readFlag = false;
        boolean flag = false;
        String flagInput;

        // until flag is read
        do {
            // try to get an input that is one letter, and is y or n
            try {
                System.out.println(prompt);
                flagInput = in.nextLine();
                if (flagInput.length() == 1) {
                    switch (flagInput.toUpperCase()) {
                        case "Y":
                            flag = true;
                            readFlag = true;
                            break;
                        case "N":
                            readFlag = true;
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error, please try to input again.");
            }
        } while(!readFlag);

        return flag;
    }// readFlag
} // FilesInOut
