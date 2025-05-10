import java.lang.*;
import java.io.*;
import java.util.Scanner;
import java.io.File;
import java.nio.file.Files;
import java.nio.channels.FileChannel;

public class project3 {
    /*public int minDegree = 3;
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
    }*/

	public static void main(String[] args) {

		try {
            Scanner s = new Scanner(System.in);
            if (args.length < 2) {
                System.err.println("!!ERROR: Insufficient number of arguments. Please try again!");
                System.exit(1);
            }
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
                System.out.println("!!ERROR: Unknown command. Please try again.");
            }
        } catch(Exception ex) {
            System.err.println("!!ERROR: " + ex.getMessage());
            System.exit(1);
        }
    }
    static void create(String[] args) {
        if (args.length != 2) {
            System.err.println("!!ERROR: Insufficient number of arguments. Please try again!");
            System.exit(1);
        }
        String path = args[1];
        File indexFile = new File(path);
        if (indexFile.exists() && indexFile.isFile()) {
            System.err.println("!!ERROR: File already exists: "+path);
            System.exit(1);
        }
        try (RandomAccessFile newFile = new RandomAccessFile(path, "rw"); FileChannel channel = newFile.getChannel()) {
            
        } catch (IOException ex) {
            System.err.println("!!ERROR: " + ex.getMessage());
            System.exit(1);
        }
        
    }
}