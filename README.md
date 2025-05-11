# Susan Zhang's Project 3 README
## SXZ220005
## CS 4348.501: Operating Systems

### How to Compile/Run the Program from the Command Line
1. Please change directories into my project folder
````
cd "sxz220005_project"
````
2. Please compile each .java file
````
javac project3.java
````
3. Please run project3.java, which is the entry point to my project, and it takes in various parameters depending on which of the 6 commands you want to perform: create, insert, search, load, print, and export. I have included an input file named "in.csv" for you to use in testing the load command. I have also deleted the index.idx file that I persoanlly used for testing so that you do not run into the "file already exists" error.
4. From there, please type in your test case inputs! Thank you!
````
java project3.java create index.idx
````
````
java project3.java insert index.idx 5 7
````
````
java project3.java search index.idx 5
````
````
java project3.java load index.idx in.csv
````
````
java project3.java print index.idx
````
````
java project3.java extract index.idx out.csv
````
---
### Current Files & Their Roles
#### devlog.md
- My initial thoughts, plans for the upcoming work session, roadblocks, and solutions to aforementioned roadblocks for Project 3

#### in.csv
- The input file, where each line of the file is a comma separated key/value pair.
- User can test the "load" command with this input file

#### project3.java
- An interactive program that creates and manages index files. The index files will each contain a B-tree. The user can create, insert, load-in, search through, print out the contents of, and export such index files.
- **Inputs:** 
    - Each command approves of and takes in unique inputs
        - create: `project3.java create [newIndexFileName]`
        - insert: `project3.java insert [existingIndexFileName] [data key] [data value]`
        - search: `project3.java search [existingIndexFileName] [data key]`
        - load: `project3.java load [existingIndexFileName] [inputFileName]`
        - print: `project3.java print [existingIndexFileName]`
        - extract: `project3.java extract [existingIndexFileName] [newOutputFileName]`
- **Outputs:** 
    - Each command returns a unique output
        - create: creates a new index file with a Header Block; no explicit output
        - insert: inserts the key/value pair in the ordered node; no explicit output
        - search: returns the value associated with the given key
        - load: inserts the key/value pairs from the input file to the index file; no explicit output
        - print: returns the key/value pairs stored in the index file
        - extract: returns an output file that contains each key/value pair of the index file

#### commithistory.txt
- My git commit history
- I am not entirely sure if this should have been included, but I am including it just in case as it is listed in the grading criteria

---
### Other Notes for the TA

- Thank you! Have a great day😃