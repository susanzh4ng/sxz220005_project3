import java.lang.*;
import java.io.*;
import java.util.Scanner;

public class project3 {
    public int minDegree = 3;
    public int maxKeys = (2*minDegree) - 1;
    public int maxChildren = 2*minDegree;

    public Node() {
        int blockID; //The block id this node is stored in
        int parentBlockID; //The block id this nodes parent is located
        int numKeys; //Number of key/value pairs currently in this node
        int[] keys = new int[maxKeys]; //A sequence of 19 64-bit keys
        int[] values = new int[maxKeys]; //A sequence of 19 64-bit values
        int[] children = new int[maxChildren]; //A sequence of 20 64-bit offsets; these block ids are the child pointers for this node

        Node(int id) {
            this.blockID = id;
        }
    }

	public static void main(String[] args) {

		try {
            Scanner s = new Scanner(System.in);
            System.out.println("-----------------------------------------------------------------"); //print a menu of commands
            System.out.println("                            Menu");
            System.out.println("-----------------------------------------------------------------\n");
            System.out.println("\t create  - create a new index file");
            System.out.println("\t insert  - inserting the key and value into the B-Tree");
            System.out.println("\t search  - search the index for the key");
            System.out.println("\t load    - read the file and insert each pair into the index file");
            System.out.println("\t print   - print every key/value pair in the index to standard output");
            System.out.println("\t extract - save every key/value pair in the index as comma separated pairs to the file\n");
            System.out.println("-----------------------------------------------------------------");
            System.out.print("Enter Command: ");

			String userCommand = args[0];

            if (userCommand.equals("create")) {
                create(args);
            } else if (userCommand.equals("insert")) {
                System.out.print("Simulate insert");
            } else if (userCommand.equals("search")) {
                System.out.print("Simulate search");
            } else if (userCommand.equals("load")) {
                System.out.print("Simulate load");
            } else if (userCommand.equals("print")) {
                System.out.print("Simulate print");
            } else if (userCommand.equals("extract")) {
                System.out.print("Simulate extract");
            } else {
                System.out.println("\nUnknown command. Please try again.");
            }
        } catch(Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            System.exit(1);
        }
    }
    static void create(String[] args) {
        String indexFile = args[1];
    }
}