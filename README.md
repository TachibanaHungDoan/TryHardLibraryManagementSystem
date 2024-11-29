# Application to manage various aspects of a library using Java

## Author
Group 13
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

## Libraries Used
- JavaFX 
- SQLITE-JDBC - For Database Connection
- JBCrypt - Java Strong password hashing

## UML Diagram
![UML](https://drive.google.com/file/d/1tKEMV7dn_u_7TNT0wtqLTYaT1NDEattJ/view?usp=sharing)


## Installation
__Requirements: IDE (Integrated Development Environment), DBMS (Database Management System)__
1. Clone the project from the repository.
2. Open the project in the IDE.
3. Run the project.

## Usage
### For Admin:
1. To login as Admin, enter the username with "Admin" and the password with "0".
2. To play or stop background music, click the BGM button on the right corner of the application.
3. To see all the books in the database, click the Books button (book icon):
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
4. To manage readers in the database, click the Readers button (Man reading book Icon):
    + To search for a reader, type his/her name in the search bar, the reader will be shown in the table.
    + When choosing a reader by clicking in the table, the reader's information will show up in the field on the right.
    + To undo show the reader's information, click the clear Button to remove their information in the field on the 
    right.
    + To delete a reader, choose the reader from the table and click Delete button.
    + To change a reader's information, choose it from the table, modify his/her information and click the Update button.
5. To see all the books that has been borrowed, click the Borrowed Books button (A book with a hand Icon)
6. To return to the login scene, click the Log Out button.
### For Reader:
1. Click the Sign Up button (below text "New to our platform"") to switch to the Register section if you don't have an account:
    + Then fill in the fields of Register section, after that click the Sign Up button to register your account
    + A form of information will appear for you to fill your information (name, gender, phone number, email) to stored 
    in our application database
    + Your password is hashed so that your account will be secured.
2. Click Sign In button (below text "Already have an account") to switch to the Login section after you register. Then
fill the username, password field and click Sign In button to login into our application.
3. On dashboard Scene:
   + To play or stop background music, click the BGM button on the right corner of the application. 
   + To change your password, click the Setting button (Setting Icon) on the right corner of the application.
   + To see the books that display on the two labels, click the button (i Icon).
   + You can see the reading quote changing each 20 seconds.
4. To see all the books available in our application, click the All Books button (book icon):
   +  To search for a book in the table, type its title, author or isbn in the search bar, then the table will display
      the book that you want to search.
   + To see more details of the book that you want from the table, choose it and click the View button.
   + Select a book from the table, click Add to Cart button to store the book you want to borrow.
   + Then when clicking the Aquire button, a window which contains all of the books that u add to Cart appears:
     - Click Cancel button to return.
     - To delete a book from cart, choose it and click Delete from cart button.
     - To borrow books, click Confirm button. The application will check and show notification. The borrowed books's
     return Date is automatically set for the next month. 
5. To see all the books that you have borrowed, click the Books Inventory button:
   + To return a book from the table, click the Return button.
   + To see all the books that you have returned, click the Returned Books Button.
6. To play game, click the Games button:
   + This game is based on HangMan game. This time, you have to guess random author's name letter by letter by clicking
   the alphabet button, if you guess a wrong letter so the cute boi is hanged =))) and you will lose if he die, otherwise
   you win the game by guess all the letter correctly.
   + To restart the game, click the New game button.
   + To show hint of the game, click the Hint button.
7. To return to the login scene, click the Log Out button.

## Demo


## Future improvements
1. Add more complex games.
2. Add a scene that represents all the books that have been returned for admin to manage.
3. Inform reader and print for them a receipt of late fee when they return books overdue.
4. Add more book images for user to see.
5. Improve the graphics and user interface, optimize algorithms for a smooth using application.

## Project status
The project is completed but can be improved by adding much more functions.

## Notes
The application is written for educational purposes.