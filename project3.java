import java.lang.*;
import java.io.*;
import java.util.Scanner;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;

public class project3 {
    static public String magic = "4348PRJ3"; 
    static public int blockSize = 512;
    static public int minDegree = 10;
    static public int maxKeys = (2*minDegree) - 1;
    static public int maxChildren = 2*minDegree;
    
    public static class Pair {
        public long key;
        public long value;

        public Pair(long key, long value) {
            this.key = key;
            this.value = value;
        }
    }

    public static class Node {
        public int currNumKeys;
        public long[] keys;
        public long[] values;
        public long[] children;
        public long blockID;
        public long parentID;
        
        public Node(long blockID, long parentID) {
            this.blockID = blockID;
            this.parentID = parentID;
            this.keys = new long[maxKeys];
            this.values = new long[maxKeys];
            this.children = new long[maxChildren];
            this.currNumKeys = 0;
        }

        public boolean isLeaf() {
            for (int i=0; i<maxChildren; i++) {
                if (children[i] != 0) {
                    return false;
                }
            }
            return true;
        }
        
        public void insertKey(long key, long value){
            int i = currNumKeys-1;
            while (i>= 0 && key<keys[i]){ //Move all larger keys/values ahead ...
                keys[i+1] = keys[i];
                values[i+1] = values[i];
                i--;
            }
            keys[i+1] = key; //... and insert key/value at ordered position
            currNumKeys++;
            values[i+1] = value;
        }
    }

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
                insert(args);
            } else if (userCommand.equals("search")) {
                search(args);
            } else if (userCommand.equals("load")) {
                load(args);
            } else if (userCommand.equals("print")) {
                printOut(args);
            } else if (userCommand.equals("extract")) {
                extract(args);
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
        if (indexFile.exists() && indexFile.isFile()) { //If that file already exists, fail with an error message
            System.err.println("!!ERROR: File already exists: "+path);
            System.exit(1);
        }
        try (RandomAccessFile newFile = new RandomAccessFile(path, "rw")) { //Create a new index file
            byte[] magicNum = magic.getBytes(StandardCharsets.US_ASCII); //the magic number as a sequence of ASCII values
            newFile.write(magicNum); //first 8 bytes of the Header field
            newFile.writeLong(0L); //second 8 bytes; the ID of the block containing the root node
            newFile.writeLong(1L); //third 8 bytes; the ID of the next block to be added to the file
            for (int i=24; i<blockSize; i++) {
                newFile.writeByte(0); //the remaining bytes are unused
            }
        } catch (IOException ex) {
            System.err.println("!!ERROR: " + ex.getMessage());
            System.exit(1);
        }
    }

    static void insert (String[] args) throws IOException {
         if (args.length != 4) {
            System.err.println("!!ERROR: Insufficient number of arguments. Please try again!");
            System.exit(1);
        }
        String path = args[1];
        long key = Long.parseLong(args[2]);
        long value = Long.parseLong(args[3]);

        try (RandomAccessFile indexFile = new RandomAccessFile(path, "rw")) { //Create instance of an index file
            indexFile.seek(8); //first 8 bytes of the Header field
            long rootID = indexFile.readLong(); //the ID of the block containing the root node
            long nextBlockID = indexFile.readLong(); //the ID of the next block to be added to the file

            if (rootID == 0){ //if tree is empty ...
                Node newRoot = new Node(1, 0); //... then create new root ...
                newRoot.insertKey(key, value);
                writeNode(indexFile, newRoot);

                indexFile.seek(8); //... and update the file with the new root
                indexFile.writeLong(1L);
                indexFile.writeLong(2L);
            } else { //if tree is not empty...
                Node ogRoot = readNode(indexFile, rootID);
                if (ogRoot.currNumKeys == maxKeys){ //if root is full...
                    Node newRoot = new Node(nextBlockID, 0); //... then create new root
                    ogRoot.parentID = nextBlockID;
                    writeNode(indexFile, ogRoot);

                    newRoot.children[0] = rootID; //...with root being new root's first child
                    splitChild(indexFile, newRoot, 0, ogRoot); //split og root

                    indexFile.seek(8); //... and update the file with the root
                    indexFile.writeLong(nextBlockID);
                    indexFile.writeLong(nextBlockID+1);

                    insertNode(indexFile, newRoot, key, value);
                } else { //if root is not full...
                    insertNode(indexFile, ogRoot, key, value); //then enter key into the root node
                }
            }
        } catch (NumberFormatException ex) {
            System.err.println("!!ERROR: " + ex.getMessage());
            System.exit(1);
        }

        
    }
    static void insertNode (RandomAccessFile file, Node node, long key, long value) {
        int i = node.currNumKeys - 1;
        if (node.isLeaf()) {
            while (i>= 0 && key<node.keys[i]){ //Move all larger keys/values ahead ...
                node.keys[i+1] = node.keys[i];
                node.values[i+1] = node.values[i];
                i--;
            }
            if (i>=0 && key==node.keys[i]) {//if key already exists ...
                node.values[i] = value; //... then update key/value pair
            } else {
                node.keys[i+1] = key; //... else, insert key/value at ordered position
                node.currNumKeys++;
                node.values[i+1] = value;
            }
            writeNode(file, node);
        } else {
            while (i>=0 && key<node.keys[i]){ //find child node to insert key
                i--;
            }
            i++;

            if (i>=0 && i<=node.currNumKeys && key==node.keys[i-1]) {//if key already exists ...
                node.values[i-1] = value; //... then update key/value pair
                writeNode(file, node);
                return;
            }
        Node childNode = readNode(file, node.children[i]);
            if (childNode.currNumKeys == maxKeys) {
                splitChild(file, node, i, childNode);
            }
            if (key > node.keys[i]) {
                i++;
                childNode = readNode(file, node.children[i]);
            } else if (key == node.keys[i]) {
                node.values[i] = value;
                writeNode(file, node);
                return;
            }
        insertNode(file, childNode, key, value); //recursively insert in subtree
        }
    }
    static void splitChild(RandomAccessFile file, Node parent, int i, Node og){
        try {
            file.seek(16);
            long nextBlockID = file.readLong();
            
            Node newNode = new Node(nextBlockID, parent.blockID);
            file.seek(16);
            file.writeLong(nextBlockID+1);

            newNode.currNumKeys = minDegree-1; //create a new node with (minDegree-1) keys
            for (int j=0; j<minDegree-1; j++){
                newNode.keys[j] = og.keys[j+minDegree]; //new node's keys are the latter half of the original node
                newNode.values[j] = og.values[j+minDegree];
                og.keys[j+minDegree] = 0;
                og.values[j+minDegree] = 0;
            }
            if(!og.isLeaf()){
                for(int j=0; j<minDegree; j++){
                    newNode.children[j] = og.children[j+minDegree]; //og node's children are the transferred to the new node
                    og.children[j+minDegree] = 0;

                    if (newNode.children[j] != 0) {
                        Node childNode = readNode(file, newNode.children[j]);
                        childNode.parentID = newNode.blockID;
                        writeNode(file, childNode);
                    }
                }
            }
            
            //newNode.currNumKeys = minDegree-1;
            og.currNumKeys = minDegree-1;
            
            for (int j=parent.currNumKeys; j>i; j--) {
                parent.children[j+1] = parent.children[j];
            }
            parent.children[i+1] = newNode.blockID;

            for (int j=(parent.currNumKeys)-1; j>=i; j--) {
                parent.keys[j+1] = parent.keys[j];
                parent.values[j+1] = parent.values[j];
            }
            parent.keys[i] = og.keys[minDegree - 1];
            parent.values[i] = og.values[minDegree - 1];
            og.keys[minDegree - 1] = 0;
            og.values[minDegree - 1] = 0;
            parent.currNumKeys++;

            writeNode(file, og);
            writeNode(file, newNode);
            writeNode(file, parent);
        } catch(Exception ex) {
            System.err.println("!!ERROR: " + ex.getMessage());
            System.exit(1);
        }

    }

    static void writeNode (RandomAccessFile file, Node node) {
        try {
        file.seek(node.blockID * blockSize);
        file.writeLong(node.blockID);
        file.writeLong(node.parentID);
        file.writeLong(node.currNumKeys);
        for (int i=0; i<maxKeys; i++) {
            file.writeLong(node.keys[i]);
        }
        for (int i=0; i<maxKeys; i++) {
            file.writeLong(node.values[i]);
        }
        for (int i=0; i<maxChildren; i++) {
            file.writeLong(node.children[i]);
        }
        } catch(Exception ex) {
            System.err.println("!!ERROR: " + ex.getMessage());
            System.exit(1);
        }
    }
    static Node readNode (RandomAccessFile file, long ID) {
        try {
            file.seek(ID * blockSize);

            long bID = file.readLong();
            long pID = file.readLong();
            int cnumKeys = (int) file.readLong();
            Node node = new Node(bID, pID);
            node.currNumKeys = cnumKeys;

            for (int i=0; i<maxKeys; i++) {
                node.keys[i] = file.readLong();
            }
            for (int i=0; i<maxKeys; i++) {
                node.values[i] = file.readLong();
            }
            for (int i=0; i<maxChildren; i++) {
                node.children[i] = file.readLong();
            }
            return node;
        } catch(Exception ex) {
            System.err.println("!!ERROR: " + ex.getMessage());
            System.exit(1);
            return null;
        }
    }
    
    static void printOut (String[] args) {
        if (args.length != 2) {
            System.err.println("!!ERROR: Insufficient number of arguments. Please try again!");
            System.exit(1);
        }
        String path = args[1];
        try (RandomAccessFile indexFile = new RandomAccessFile(path, "r")) {
            indexFile.seek(8); //first 8 bytes of the Header field
            long rootID = indexFile.readLong(); //the ID of the block containing the root node
        
            if (rootID == 0) {
                System.out.println();
                return;
            }

            List<Pair> pairs = new ArrayList<>();
            insertPairs(indexFile, rootID, pairs);

            for (Pair p : pairs) {
                System.out.println("Key = "+p.key+", Value = "+p.value);
            }
        } catch (IOException ex) {
            System.err.println("!!ERROR: " + ex.getMessage());
            System.exit(1);
        }
    }
    static void insertPairs (RandomAccessFile file, long ID, List<Pair> pairs) {
        try {
            Node node = readNode(file, ID);
            int i;
            for (i=0; i<node.currNumKeys; i++) {
                if (!node.isLeaf()){
                    insertPairs(file, node.children[i], pairs); //traverse the tree, depth-first order...
                }
                if ((node.keys[i] != 0) || (node.values[i] != 0)) {
                    pairs.add(new Pair(node.keys[i], node.values[i]));
                }
            }
            if ((!node.isLeaf()) && (i<maxChildren) && (node.children[i] != 0)){
                insertPairs(file, node.children[i], pairs); //traverse the tree, depth-first order...
            }
        } catch (Exception ex) {
            System.err.println("!!ERROR: " + ex.getMessage());
            System.exit(1);
        }
    }

    static void load(String[] args) {
        if (args.length != 3) {
            System.err.println("!!ERROR: Insufficient number of arguments. Please try again!");
            System.exit(1);
        }
        
        String indexPath = args[1];
        String csvPath = args[2];        
        try {
            List<String> lines = Files.readAllLines(Paths.get(csvPath));
            for (int j = 0; j < lines.size(); j++) {
                String line = lines.get(j).trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    try {
                        long key = Long.parseLong(parts[0].trim());
                        long value = Long.parseLong(parts[1].trim());
                        
                        String[] insertArgs = {"insert", indexPath, String.valueOf(key), String.valueOf(value)};
                        insert(insertArgs);
                    } catch (NumberFormatException e) {
                        System.err.println("!!ERROR: Invalid number format in CSV on line " + (j + 1));
                    } catch (Exception e) {
                        System.err.println("!!ERROR: Failed to insert key/value pair on line " + (j + 1));
                    }
                } else {
                    System.err.println("!!ERROR: Invalid CSV format on line " + (j + 1));
                }
            } 
        } catch (IOException ex) {
            System.err.println("!!ERROR: Failed to read CSV file: " + ex.getMessage());
            System.exit(1);
        }
    }

    
    static void search (String[] args) {
        if (args.length != 3) {
            System.err.println("!!ERROR: Insufficient number of arguments. Please try again!");
            System.exit(1);
        }
        
        String indexPath = args[1];
        long key = Long.parseLong(args[2]); 
        try (RandomAccessFile indexFile = new RandomAccessFile(indexPath, "r")) {
            indexFile.seek(8); //first 8 bytes of the Header field
            long rootID = indexFile.readLong(); //the ID of the block containing the root node
        
            if (rootID == 0) {
                System.out.println("Empty tree; key is not found!");
                return;
            }

            Long value = searchNode(indexFile, rootID, key);
            if (value != null) {
                System.out.println("Value '"+value+"' is at key '"+key+"'");
            } else {
                System.out.println("Key is not found!");
            }
        } catch (IOException ex) {
            System.err.println("!!ERROR: " + ex.getMessage());
            System.exit(1);
        }
    }
    static Long searchNode (RandomAccessFile file, long ID, long key) {
        try {
            Node node = readNode(file, ID);
            if (node == null) {  // Check if readNode returned null
                System.err.println("!!ERROR: Failed to read node with ID: " + ID);
                return null;
            }

            int i = 0;
            while (i<node.currNumKeys && key>node.keys[i]) {
                i++;
            }
            if (i<node.currNumKeys && node.keys[i] == key){ //if key matches, return Node
                return node.values[i];
            }
            if((!node.isLeaf()) && (i<node.children.length) && (node.children[i] != 0)){ //if closest node is a leaf, then key is not found
                return searchNode(file, node.children[i], key); //recursively search in subtree
            }
            return null;
        } catch (Exception ex) {
            System.err.println("!!!ERROR: " + ex.getMessage());
            return null;
        }
    }
    static void extract (String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("!!ERROR: Insufficient number of arguments. Please try again!");
            System.exit(1);
        }

        String path = args[1];
        File indexP = new File(path);
        if (!indexP.exists() || !indexP.isFile()) { //If that file already exists, fail with an error message
            System.err.println("!!ERROR: Invalid index file: "+path);
            System.exit(1);
        }
        try (RandomAccessFile indexFile = new RandomAccessFile(path, "r")) {


            String csvPath = args[2];
            File csv = new File(csvPath);
            if (csv.exists() && csv.isFile()) { //If that file already exists, fail with an error message
                System.err.println("!!ERROR: File already exists: "+csvPath);
                System.exit(1);
            }

            try (RandomAccessFile file = new RandomAccessFile(path, "r")) { //Create a new .csv file
                file.seek(8);
                long rootID = file.readLong();
                List<Pair> pairs = new ArrayList<>();
                if (rootID != 0) {
                    insertPairs(file, rootID, pairs);
                }

                try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvPath))) {
                    for (Pair p : pairs) {
                        writer.write(p.key+","+p.value);
                        writer.newLine();
                    }
                }
            }
        }
    }
}