import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Driver {

    // Remove cases if you dont want to evaluate the code on them
    static final String[] cases = new String[] { "small", "large", "dfs" };
    static final long SEED = 12199; // Seed value for random number generator
    static final String IN_DIR = "inputs/"; // Inputs directory
    static final String OUT_DIR = "outputs/"; // Outputs directory
    static final String CORR_DIR = "model_outputs/"; // Model Outputs directory

    static String nodesFile = "GeneratedNodes.csv";
    static String egdesFile = "GeneratedEdges.csv";
    static String out = "out.txt";

    static String name;

    public static void main(String[] args) throws FileNotFoundException {
        System.out.print("Enter your name: ");
        Scanner sc = new Scanner(System.in);
        GraphGenerator gen = new GraphGenerator();
        name = sc.nextLine();
        System.out.print("\n");

        for (String s : cases) {
            gen.buildGraph(s);
        }
        for (String s : cases) {
            runCode(s);
            boolean c = compareOutputs(OUT_DIR + name + "_" + s + "_" + out, CORR_DIR + "model" + "_" + s + "_" + out);
            System.out.println("Output for \"" + s + "\" " + (c ? "has matched \n" : "does not match \n"));
        }
    }

    static void runCode(String type) throws FileNotFoundException {
        long avgTime;
        long rankTime;
        long dfsTime;

        File file = new File(OUT_DIR + name + "_" + type + "_" + out);
        file.getParentFile().mkdirs();
        PrintStream console = System.out;
        System.out.println("Running testcases \"" + type + "\"...");
        PrintStream ps = new PrintStream(file);
        System.setOut(ps);
        String nodes = IN_DIR + type + nodesFile;
        String edges = IN_DIR + type + egdesFile;

        avgTime = runFunction("average", nodes, edges);
        rankTime = runFunction("rank", nodes, edges);
        dfsTime = runFunction("independent_storylines_dfs", nodes, edges);

        System.setOut(console);
        System.out.println("Time taken to run average is: " + avgTime / 1000000 + "ms");
        System.out.println("Time taken to run rank is: " + rankTime / 1000000 + "ms");
        System.out.println("Time taken to run independant_storylines_dfs is: " + dfsTime / 1000000 + "ms");
        System.out.println("Output stored in " + name + "_" + type + "_" + out);
    }

    static long runFunction(String func, String nodes, String edges) {
        long start;
        long end;
        System.out.println(func);
        start = System.nanoTime();
        try {
            assignment4.main(new String[] { nodes, edges, func });
        } catch (Exception e) {
            System.out.println("Error occured");
        }
        end = System.nanoTime() - start;
        return end;
    }

    static boolean compareOutputs(String file1, String file2) {
        try {
            File f1 = new File(file1);
            File f2 = new File(file2);
            Scanner myReader = new Scanner(f1);
            Scanner corrReader = new Scanner(f2);
            int i = 0;
            while (myReader.hasNextLine()) {
                i++;
                String s1 = myReader.nextLine();
                String s2 = corrReader.nextLine();
                if (!s1.equals(s2)) {
                    return false;
                }
            }
            if (corrReader.hasNextLine()) {
                return false;
            }
            myReader.close();
            corrReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

class GraphGenerator {
    Random rand;
    int numV;
    int numE;
    int maxWeight = 500;

    GraphGenerator() {
        rand = new Random();
        rand.setSeed(Driver.SEED);
    }

    void buildGraph(String type) {
        if (type.compareTo("dfs") == 0) {
            generateDfs();
        } else {
            switch (type) {
                case "small":
                    numV = 1000;
                    numE = (numV * (numV - 1) / 4) + rand.nextInt(numV * (numV - 1) / 4);
                    break;
                case "large":
                    numV = 100000;
                    numE = (int) (numV * 1.5) + rand.nextInt(numV / 2);
                    break;
                default:
                    break;
            }
            generate(type);
        }
    }

    void generateDfs() {
        int numComponents = 20 + rand.nextInt(10);
        int sum = 0;
        System.out.println("Generating \"dfs\" testcase...");
        for (int i = 0; i < numComponents; i++) {
            numV = 300 + rand.nextInt(200);
            numE = (numV * (numV - 1) / 4) + rand.nextInt(numV * (numV - 1) / 4);
            makeNodesCSV("dfs", i == 0, sum);
            makeEdgesCSV("dfs", i == 0, sum);
            sum += numV;
        }
        System.out.println("Nodes file created");
        System.out.println("Edges file created\n");
    }

    void generate(String type) {
        System.out.println("Generating \"" + type + "\" testcase...");
        makeNodesCSV(type, true, 0);
        System.out.println("Nodes file created");
        makeEdgesCSV(type, true, 0);
        System.out.println("Edges file created\n");
    }

    private void makeEdgesCSV(String type, boolean newFile, int base) {
        File file = new File(Driver.IN_DIR + type + Driver.egdesFile);
        file.getParentFile().mkdirs();
        try (BufferedWriter out = new BufferedWriter(
                new FileWriter(Driver.IN_DIR + type + Driver.egdesFile, !newFile))) {
            if (newFile)
                out.write("Source,Target,Weight\n");
            HashSet<String> set = new HashSet<>();
            for (int i = 0; i < numE; i++) {
                int a = base + rand.nextInt(numV);
                int b = base + rand.nextInt(numV);
                while (a == b) {
                    b = base + rand.nextInt(numV);
                }
                while (set.contains(a + "," + b) || set.contains(b + "," + a)) {
                    a = base + rand.nextInt(numV);
                    b = base + rand.nextInt(numV);
                    while (a == b) {
                        b = base + rand.nextInt(numV);
                    }
                }
                set.add(a + "," + b);
                int wt = 1 + rand.nextInt(maxWeight);
                out.write(a + "," + b + "," + wt + "\n");
            }
        } catch (IOException e) {
            System.out.println("Exception Occurred" + e);
        }
    }

    private void makeNodesCSV(String type, boolean newFile, int base) {
        File file = new File(Driver.IN_DIR + type + Driver.nodesFile);
        file.getParentFile().mkdirs();
        try (BufferedWriter out = new BufferedWriter(
                new FileWriter(Driver.IN_DIR + type + Driver.nodesFile, !newFile))) {
            if (newFile)
                out.write("Id,Label\n");
            for (int i = 0; i < numV; i++) {
                int node = base + i;
                out.write(node + "," + node + "\n");
            }
        } catch (IOException e) {
            System.out.println("Exception Occurred" + e);
        }
    }
}
