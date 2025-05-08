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