// ...existing imports...
import cat.uvic.teknos.dam.controlbox.jdbc.JdbcProductSupplierRepository;
import cat.uvic.teknos.dam.controlbox.ProductSupplierMenu;
// ...existing code...

public class App {
    public static void main(String[] args) {
        // ...existing code...
        Connection connection = // ...get your JDBC connection here...
        JdbcProductSupplierRepository productSupplierRepository = new JdbcProductSupplierRepository(connection);
        ProductSupplierMenu productSupplierMenu = new ProductSupplierMenu(productSupplierRepository);

        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            System.out.println("\n--- Main Menu ---");
            // ...other menu options...
            System.out.println("X. ProductSupplier Menu");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                // ...other cases...
                case X: // Replace X with the menu number you want for ProductSupplier
                    productSupplierMenu.show();
                    break;
                case 0:
                    System.out.println("Exiting application.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (option != 0);
        // ...existing code...
    }
    // ...existing code...
}
