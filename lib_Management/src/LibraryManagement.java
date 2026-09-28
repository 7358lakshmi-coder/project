
import java.sql.*;
import java.util.Scanner;

public class LibraryManagement {

    static Scanner sc = new Scanner(System.in);

    // 1. ADD BOOK
    public static void addBook() {

        try {
            Connection con = dbConnection.getConnection();

            System.out.print("Enter Book ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Book Title: ");
            String title = sc.nextLine();

            System.out.print("Enter Author: ");
            String author = sc.nextLine();

            System.out.print("Enter Quantity: ");
            int quantity = sc.nextInt();

            String sql = "INSERT INTO book VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.setInt(4, quantity);

            ps.executeUpdate();

            System.out.println("Book Added Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2. VIEW BOOKS
    public static void viewBooks() {

        try {
            Connection con = dbConnection.getConnection();

            String sql = "SELECT * FROM book";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\nBook ID\tTitle\tAuthor\tQuantity");

            while (rs.next()) {

                System.out.println(
                    rs.getInt("book_id") + "\t" +
                    rs.getString("title") + "\t" +
                    rs.getString("author") + "\t" +
                    rs.getInt("quantity")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. SEARCH BOOK
    public static void searchBook() {

        try {
            Connection con = dbConnection.getConnection();

            System.out.print("Enter Book Title: ");
            String title = sc.nextLine();

            String sql = "SELECT * FROM book WHERE title LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + title + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println("Book ID: " + rs.getInt("book_id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Author: " + rs.getString("author"));
                System.out.println("Quantity: " + rs.getInt("quantity"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4. REGISTER STUDENT
    public static void registerStudent() {

        try {
            Connection con = dbConnection.getConnection();

            System.out.print("Enter Student ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Student Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Department: ");
            String department = sc.nextLine();

            String sql = "INSERT INTO student VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, department);

            ps.executeUpdate();

            System.out.println("Student Registered Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 5. BORROW BOOK
    public static void borrowBook() {

        try {
            Connection con = dbConnection.getConnection();

            System.out.print("Enter Book ID: ");
            int bookId = sc.nextInt();

            System.out.print("Enter Student ID: ");
            int studentId = sc.nextInt();

            String check = "SELECT quantity FROM book WHERE book_id = ?";

            PreparedStatement ps1 = con.prepareStatement(check);
            ps1.setInt(1, bookId);

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {

                int quantity = rs.getInt("quantity");

                if (quantity > 0) {

                    String sql =
                        "INSERT INTO borrow(book_id, student_id, borrow_date) " +
                        "VALUES (?, ?, CURDATE())";

                    PreparedStatement ps2 = con.prepareStatement(sql);

                    ps2.setInt(1, bookId);
                    ps2.setInt(2, studentId);

                    ps2.executeUpdate();

                    String update =
                        "UPDATE book SET quantity = quantity - 1 WHERE book_id = ?";

                    PreparedStatement ps3 = con.prepareStatement(update);

                    ps3.setInt(1, bookId);
                    ps3.executeUpdate();

                    System.out.println("Book Borrowed Successfully!");

                } else {
                    System.out.println("Book is not available!");
                }

            } else {
                System.out.println("Book not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 6. RETURN BOOK
    public static void returnBook() {

        try {
            Connection con = dbConnection.getConnection();

            System.out.print("Enter Book ID: ");
            int bookId = sc.nextInt();

            System.out.print("Enter Student ID: ");
            int studentId = sc.nextInt();

            String sql =
                "UPDATE borrow SET return_date = CURDATE() " +
                "WHERE book_id = ? AND student_id = ? " +
                "AND return_date IS NULL";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, bookId);
            ps.setInt(2, studentId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                String update =
                    "UPDATE book SET quantity = quantity + 1 WHERE book_id = ?";

                PreparedStatement ps2 = con.prepareStatement(update);

                ps2.setInt(1, bookId);
                ps2.executeUpdate();

                System.out.println("Book Returned Successfully!");

            } else {
                System.out.println("Borrow record not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 7. VIEW BORROWED BOOKS
    public static void viewBorrowedBooks() {

        try {
            Connection con = dbConnection.getConnection();

            String sql =
                "SELECT b.title, s.student_name, " +
                "br.borrow_date, br.return_date " +
                "FROM borrow br " +
                "JOIN book b ON br.book_id = b.book_id " +
                "JOIN student s ON br.student_id = s.student_id";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\nTitle\tStudent\tBorrow Date\tReturn Date");

            while (rs.next()) {

                System.out.println(
                    rs.getString("title") + "\t" +
                    rs.getString("student_name") + "\t" +
                    rs.getDate("borrow_date") + "\t" +
                    rs.getDate("return_date")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {

        while (true) {

            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Search Book");
            System.out.println("4. Register Student");
            System.out.println("5. Borrow Book");
            System.out.println("6. Return Book");
            System.out.println("7. View Borrowed Books");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addBook();
                    break;

                case 2:
                    viewBooks();
                    break;

                case 3:
                    searchBook();
                    break;

                case 4:
                    registerStudent();
                    break;

                case 5:
                    borrowBook();
                    break;

                case 6:
                    returnBook();
                    break;

                case 7:
                    viewBorrowedBooks();
                    break;

                case 8:
                    System.out.println("Thank You!");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}
