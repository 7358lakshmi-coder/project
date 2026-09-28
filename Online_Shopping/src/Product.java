public class Product {

    int productId;
    String name;
    String category;
    double price;
    int stock;

    public Product(int productId, String name, String category,
                   double price, int stock) {

        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public void display() {

        System.out.println(
            productId + " | " +
            name + " | " +
            category + " | " +
            price + " | " +
            stock
        );
    }
}