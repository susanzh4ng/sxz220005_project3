# Susan Zhang's Devlog
## CS 4348.501: Operating Systems


### Thursday, May 8th, 2025
#### 5:13PM
- After reading through the project details and rewatching the Project 3 Overview, I will get started on the project3.java file.
- I think I am still a bit lost and overwhelmed by the sheer size of our index file.
- The structure of this project is similar to Project 1's because the user enters in commands, so I think I will begin with the "user interface" and set up dummy command actions for this session.
    - I can reference my Project 1's "driver.java" code

#### 5:45PM
- I finished with the "user interface" portion of the project.
- It took a while since I kept rereading the commands' functions to fully wrap my head around it
- Next, I think I will try to tackle the B-Tree portion; I will start with creating a Node object
    - From the Project 3 description, it looks like each Node object needs each of the bullet points as attributes

#### 6:15PM
- I think I finished the Node class, as I cannot test it yet

#### 6:35PM
- I decided to take a "brain break" from the B-Tree and start on the user commands, starting with "create"
- I understand that my program will get the filename from String[] args, but I am not sure how to check if that file already exists; let me look into that
- A Google search revealed that I should import some library that can check for me, so I will go ahead and do that soon
- I am pretty tired now, so I think I will take a proper break and end the session

### Saturday, May 10th, 2025
#### 10:29AM
- Since last session, I have completed Homework 5, Question 5, which was coding a B-Tree 
    - I now have a much better understanding of B-Trees and how to implement them
    - I am hoping that I can reuse some of that code in my Project 3
- I am still planning on implementing the "create" index file command for this current session
    - According to Google, in order to check if a file already exists in a folder, I need to import Java's File library and use their built-in functions, so I will do that accordingly
    - It seems easy-enough

#### 10:41AM
- Checking for an existing file under the "create" command works!
- However, I still need to handle error-checking for if the user does not input any commands

#### 10:57AM
- I added checking to make sure the user enters in enough arguments
- However, that conflicted with my menu of commands, since the user would have to enter in 0 arguments to see the menu
- Since the menu of commands is not required/the TA would alreday know the menu of commands, I will just omit it altogether
- Next, I will work on the actual creating a new file!

#### 11:07AM
- The create file works!
- However, I think I did not look ahead far enough, as the new file will also need a Header "node" as the file's first block
    - I am assuming the Header node needs to be appended at file creation
- Instead of creating a Java File, I think it needs to be a Java RandomAccessFile since the user needs to also "search" for key/value pairs and possibly "insert" in the middle of nodes
    - I am assuming a FileWriter also works since, either way, new nodes would be appended to the end of the file, but a RandomAccessFile seems more universal so I will use it just in case

#### 11:24AM
- I am having trouble writing to the actual file

#### 2:07PM
- I took a break, and I am now continuing on the "create" command
- I got the Magic Number to write in the header block, but all other block ID's are showing up as NULL
- I think the issue is when I am writer the content to the index file

#### 3:10PM
- I was able to write the Header block in a new file!
- There were a lot of issues in writing to the file, and I ultimately used .writeLong() function to write the block ID's
- It also took a while to install hexdump on my laptop in order to view my index file
- Currently, my index file matches Professor Salazar's example in class, so I am good to move forward! The "create" command is finished!

#### 3:24PM
- Next, I think I will start implementing the Node, which I can mainly take from my Homework 5, Question 5

#### 3:39PM
- Next, I will begin on the "insert" command

#### 3:55PM
- I think there is a chance that I can alter my Homework 5, Question 5 code for the "insert" command as well

#### 5:13PM
- I am getting a lot of errors, mainly for the disconnet between Project 3's requirements - especially writing to the index file - and my Homework 5, Question 5 code
- There is a pretty good chance that I will have to restart completely since there have been so many bugs

#### 5:52PM
- My code is starting to get pretty convoluted, as a "simple" insert also requires inserting keys, checking to see if the key has already been inserted, checking to see if the node it is being inserted into is the root, if the root is node, if the node is full, how to split the node, etc.

#### 8:20PM
- That took *quite* a while but my "insert" command works!
    - Using hexdump, I see that it matches Professor Salazar's example!
- Because I wanted to keep it consistent when reading from the index file and writing to the index file, I made all keys and values be of data type long
- Reading the Node from the index file and writing/updating the Node to the index file definitely tricky, especially since I needed it to perform hexdump and see if my index file was updating correctly
    - I was able to access the location of the Node by multiply the block ID and 512 (the block size)
- I also had to completely restructure my Node class and move some functions over to the project 3 program for better access
    - I also made all attributes and functions from Node public because they are used and updated quite often

#### 8:44PM
- This goes out of order, but next up, I think I will try to implement the "print" command
    - It will be a lot more helpful than relying on hexdump to check my B-Tree
- From the Project 3 Overview, it seems like you go through the array of the index files and print it out, block-by-block

#### 9:05M
- In order to create an array of key/value pairs, I will maintain an ArrayList of them, adding a pair for each block in the index file
- I think I will create a separate object class for this, as that seems easier to implement than a 2-D Array

#### 10:27PM
- I think I got "print" to work!
- For the most part, it was pretty straight-forward, just tedious since I had to maintain an Array List of key/value pairs
- I was also able to reuse some code and the logic from my Homework 5 code!

### Sunday, May 11th, 2025
#### 2:45AM
- To be blatantly honest, since last check-in, I have been working on the "load" command
- I ran into so many bugs and error and have tried so many debugging techniques and tested out my code so many times but it always went completely over my head and it made my code so much more convoluted
- Ultimately, I will still attempt the "load" command and show my thought process, but the real issue lies in my "insert" command
    -"insert" works until the root node has to split
    - I honestly just have to move on

- Having to do `git reset --hard` also overrode my devlog entries, but the summary for my "load" command is:
    - my program line-by-line read in the .csv file
    - split each line at the comma, designating the first integer as the key and the second integer as the value of the key/value pair
    - finally, each pair uses the "insert" command to insert it into the B-tree

#### 3:09AM
- Next, I will try and implement the "search" command

#### 3:19AM
- It seems like I can follow a similar structure to what I did for the "print" command, as they both expect you to perform depth-first search down the tree to find the key
    - Once again, I can use a helper function and implement recursion

#### 3:51AM
- I was able to reference my Homework 5, Question 5 code in my "search" command!
- As a summary, I read the node from the index file based on it's block ID, then I found the nearest key to the given key, and returned the value at the given key

#### 10:52AM
- Next, I will work on the "extract" function
- I am thinking of using a BufferedWriter because I do not know how else to write to a new file within a program
- I think I can use something similar to the "print" command since both needs to go through the index file and collect key/value pairs
    - I can use an ArrayList of Pair objects again

#### 11:26AM
- Finished with the "extract" command
- I still have to create my README.md and compress my files into a .zip file, but I think this is my final devlog.md entry for Project 1!
- Thank you so much for reading😀