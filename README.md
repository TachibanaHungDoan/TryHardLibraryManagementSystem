# Application to manage various aspects of a library using Java

## Author
Group TryHard
1. Doan Thai Hung - 23021565
2. Nguyen Huu Luu - 23021617
3. Bui Quang Linh - 23021605

## Description
The application is designed to manage various aspects of a library. The application is written in Java and mostly use 
the JavaFX Library. The application is based on the MVC model. The application has two views of Admin-View who takes 
control of managing books, readers that using this application and Reader-View who has ability to borrow, return books 
provided by the application and entertain by playing a Hangman based game. The information of books and readers is 
stored in our application database.
1. The application is designed to manage various aspects of a library.
2. The application is written in Java and mostly use the JavaFX Library.
3. The application is based on the MVC model.
4. The application has two views:
   + Admin-View for admin who takes control of managing books, readers that using this application
   + Reader-View for user(reader) who has ability to borrow, return books provided by the application
   and entertain by playing a Hangman based game.
5. The information of books and readers is stored in our application database.

## UML Diagram

## Installation
__Requirements: IDE (Integrated Development Environment), DBMS (Database Management System)__
1. Clone the project from the repository.
2. Open the project in the IDE.
3. Get the database in src/main/java/resources/libriscopedb. and import it to your DBMS
4. Run the project.

## Usage
### For Admin:
1. To login as Admin, enter the username with "Admin" and the password with "0".
2. To see all the books in the database, click the Books button (book icon):
    + To search for a book in the table, type its title, author or isbn in the search bar, then the table will display 
    the book that you want to search 
    + To see more details of the book that you want from the table, choose it and click the View button.
    + To delete a book from the table, choose it and click the Delete button.
    + To change a book information, choose it from the table and click the Update button, a window will appear for you
    to modify it.
    + To add a book to the table, click the Add button and a window appear for you to:
      - To search for a book, type its title in the search bar, choose from the popup then the ìnformation will 
      immediately appear in the field for you to add.
      - Or you can fill full the field the book information to add.
      - Click Add button to add it.
      - To quickly delete the information from the field, click the clear Button.
      - To import an image, click the import Button.
3. To manage readers in the database, click the Readers button (Man reading book Icon):
    + To search for a reader, type his/her name in the search bar, the reader will be shown in the table.
    + When choosing a reader by clicking in the table, the reader's information will show up in the field on the right.
    + To undo show the reader's information, click the clear Button to remove their information in the field on the 
    right.
    + To delete a reader, choose the reader from the table and click Delete button.
    + To change a reader's information, choose it from the table, modify his/her information and click the Update button.
4. To see all the books that has been borrowed, click the Borrowed Books button (A book with a hand Icon)
5. To return to the login scene, click the Log Out button.
### For Reader:
1. Click the Sign Up button to switch to the Register section if you don't have an account:
    + Yolo
    + n
2. t
3. 
## Future improvements

## Project status
The project is 

## Notes
The application is written for educational purposes.