import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class q1 {
    private static final int NUM = 1;
    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.out.println("Please provide a file name as input.");
            System.out.println("NOTE: The file must be present in this same path where you're executing the code.");
            System.out.println("NOTE: Contact 'Shubham Singh' (ssing528@asu.edu) if you're unable to run this program.");
            return;
        }

        for (String arg : args) {
            String idx = arg.split("_")[1].split("\\.")[0];
            List<String> strings = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(arg))) {
                int lineNum = 0;
                String line;
                while ((line = reader.readLine()) != null) {
                    if (lineNum == 0 || lineNum == 2) {
                        lineNum += 1;
                        continue;
                    }
                    lineNum += 1;
                   strings.add(line);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File " + arg + " not found in the current directory.");
                System.out.println("Please ensure that the file name is correct and it is present in current directory.");
                System.out.println("[WARN]: sneaking to next file (if any)");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (strings.isEmpty()) {
                System.out.println("Couldn't read the contents properly. Make sure inputs are correct.");
                System.out.println("Contact 'Shubham Singh' (ssing528@asu.edu) if you're having trouble running the program");
                System.out.println("[WARN]: sneaking to next file (if any)");
            } else {
                GlobalAlignmentAlgorithm globalAlignmentAlgorithm =
                        new GlobalAlignmentAlgorithm(strings.get(0), strings.get(1));
                Data gData = globalAlignmentAlgorithm.findGlobalAlignment();
                String fileName = "sol_q"+NUM+"_t"+idx+".txt";
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                    writer.write(gData.getAlignScore() + "\n");
                    writer.write(gData.getAlignedV()+"\n");
                    writer.write(gData.getAlignedW());
                    if (idx.equals("3"))writer.write("\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }
}
