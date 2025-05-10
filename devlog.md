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
- I will actually first test it with the minimal degree being 3

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