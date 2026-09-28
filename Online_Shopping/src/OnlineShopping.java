import java.sql.*;
import java.util.Scanner;

public class OnlineShopping {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n===== ONLINE SHOPPING SYSTEM =====");

            System.out.println("1. Register Customer");
            System.out.println("2. Add Product");
            System.out.println("3. View Products");
            System.out.println("4. Search Product");
            System.out.println("5. Add To Cart");
            System.out.println("6. Place Order");
            System.out.println("7. View Orders");
            System.out.println("8. Cancel Order");
            System.out.println("9. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    registerCustomer();
                    break;

                case 2:
                    addProduct();
                    break;

                case 3:
                    viewProducts();
                    break;

                case 4:
                    searchProduct();
                    break;

                case 5:
                    addToCart();
                    break;

                case 6:
                    placeOrder();
                    break;

                case 7:
                    viewOrders();
                    break;

                case 8:
                    cancelOrder();
                    break;

                case 9:
                    System.out.println("Thank you!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice");
            }
        }
    }


    // 1. REGISTER CUSTOMER

    static void registerCustomer() {

        try {

            Connection con =
                dbConnection.getConnection();

            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Phone: ");
            String phone = sc.nextLine();

            String sql =
                "INSERT INTO customer(name,email,phone) " +
                "VALUES(?,?,?)";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            ps.executeUpdate();

            System.out.println(
                "Customer registered successfully"
            );

            con.close();

        } catch (Exception e) {

            System.out.println(e);
        }
    }


    // 2. ADD PRODUCT

    static void addProduct() {

        try {

            Connection con =
                dbConnection.getConnection();

            sc.nextLine();

            System.out.print("Enter Product Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Category: ");
            String category = sc.nextLine();

            System.out.print("Enter Price: ");
            double price = sc.nextDouble();

            System.out.print("Enter Stock: ");
            int stock = sc.nextInt();

            String sql =
                "INSERT INTO product(name,category,price,stock) " +
                "VALUES(?,?,?,?)";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);

            ps.executeUpdate();

            System.out.println(
                "Product added successfully"
            );

            con.close();

        } catch (Exception e) {

            System.out.println(e);
        }
    }


    // 3. VIEW PRODUCTS

    static void viewProducts() {

        try {

            Connection con =
                dbConnection.getConnection();

            Statement st =
                con.createStatement();

            ResultSet rs =
                st.executeQuery("SELECT * FROM product");

            System.out.println("\n----- PRODUCTS -----");

            while (rs.next()) {

                Product p = new Product(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                );

                p.display();
            }

            con.close();

        } catch (Exception e) {

            System.out.println(e);
        }
    }


    // 4. SEARCH PRODUCT

    static void searchProduct() {

        try {

            Connection con =
                dbConnection.getConnection();

            sc.nextLine();

            System.out.print("Enter Product Name: ");
            String name = sc.nextLine();

            String sql =
                "SELECT * FROM product WHERE name LIKE ?";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setString(1, "%" + name + "%");

            ResultSet rs =
                ps.executeQuery();

            System.out.println("\n----- SEARCH RESULT -----");

            while (rs.next()) {

                Product p = new Product(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                );

                p.display();
            }

            con.close();

        } catch (Exception e) {

            System.out.println(e);
        }
    }


    // 5. ADD TO CART

    static void addToCart() {

        try {

            Connection con =
                dbConnection.getConnection();

            System.out.print("Enter Customer ID: ");
            int customerId = sc.nextInt();

            System.out.print("Enter Product ID: ");
            int productId = sc.nextInt();

            System.out.print("Enter Quantity: ");
            int quantity = sc.nextInt();

            String sql =
                "INSERT INTO cart(customer_id,product_id,quantity) " +
                "VALUES(?,?,?)";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, customerId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);

            ps.executeUpdate();

            System.out.println(
                "Product added to cart"
            );

            con.close();

        } catch (Exception e) {

            System.out.println(e);
        }
    }


    // 6. PLACE ORDER

    static void placeOrder() {

        try {

            Connection con =
                dbConnection.getConnection();

            System.out.print("Enter Customer ID: ");
            int customerId = sc.nextInt();

            String sql =
                "SELECT p.price,c.quantity " +
                "FROM cart c " +
                "JOIN product p " +
                "ON c.product_id=p.product_id " +
                "WHERE c.customer_id=?";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, customerId);

            ResultSet rs =
                ps.executeQuery();

            double total = 0;

            while (rs.next()) {

                double price =
                    rs.getDouble("price");

                int quantity =
                    rs.getInt("quantity");

                total = total + (price * quantity);
            }

            if (total == 0) {

                System.out.println("Cart is empty");

                con.close();

                return;
            }

            String orderSql =
                "INSERT INTO orders" +
                "(customer_id,total_amount,status) " +
                "VALUES(?,?,?)";

            PreparedStatement orderPs =
                con.prepareStatement(orderSql);

            orderPs.setInt(1, customerId);
            orderPs.setDouble(2, total);
            orderPs.setString(3, "PLACED");

            orderPs.executeUpdate();

            String deleteSql =
                "DELETE FROM cart WHERE customer_id=?";

            PreparedStatement deletePs =
                con.prepareStatement(deleteSql);

            deletePs.setInt(1, customerId);

            deletePs.executeUpdate();

            System.out.println(
                "Order placed successfully"
            );

            System.out.println(
                "Total Amount: " + total
            );

            con.close();

        } catch (Exception e) {

            System.out.println(e);
        }
    }


    // 7. VIEW ORDERS

    static void viewOrders() {

        try {

            Connection con =
                dbConnection.getConnection();

            System.out.print("Enter Customer ID: ");
            int customerId = sc.nextInt();

            String sql =
                "SELECT * FROM orders " +
                "WHERE customer_id=?";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, customerId);

            ResultSet rs =
                ps.executeQuery();

            System.out.println("\n----- ORDERS -----");

            while (rs.next()) {

                System.out.println(
                    "Order ID: " +
                    rs.getInt("order_id") +

                    " | Amount: " +
                    rs.getDouble("total_amount") +

                    " | Status: " +
                    rs.getString("status")
                );
            }

            con.close();

        } catch (Exception e) {

            System.out.println(e);
        }
    }


    // 8. CANCEL ORDER

    static void cancelOrder() {

        try {

            Connection con =
                dbConnection.getConnection();

            System.out.print("Enter Order ID: ");
            int orderId = sc.nextInt();

            String sql =
                "UPDATE orders " +
                "SET status='CANCELLED' " +
                "WHERE order_id=?";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, orderId);

            int result =
                ps.executeUpdate();

            if (result > 0) {

                System.out.println(
                    "Order cancelled successfully"
                );

            } else {

                System.out.println(
                    "Order not found"
                );
            }

            con.close();

        } catch (Exception e) {

            System.out.println(e);
        }
    }
}