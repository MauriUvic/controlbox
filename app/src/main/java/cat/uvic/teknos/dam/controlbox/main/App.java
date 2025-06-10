package cat.uvic.teknos.dam.controlbox.main;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcMovementRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.JdbcOrderRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.JdbcProductRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcMovement;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcOrder;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcProduct;
import cat.uvic.teknos.dam.controlbox.jdbc.JdbcProductSupplierRepository;
import cat.uvic.teknos.dam.controlbox.model.Movement;
import cat.uvic.teknos.dam.controlbox.model.Order;
import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.model.ProductSupplier;

import java.util.Scanner;

public class App {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        var dataSource = new SingleConnectionDataSource();
        var movementRepository = new JdbcMovementRepository(dataSource);
        var orderRepository = new JdbcOrderRepository(dataSource);
        var productRepository = new JdbcProductRepository(dataSource);
        var requestRepository = new cat.uvic.teknos.dam.controlbox.jdbc.JdbcRequestRepository(dataSource);
        var supplierRepository = new cat.uvic.teknos.dam.controlbox.jdbc.JdbcSupplierRepository(dataSource);
        var productSupplierRepository = new JdbcProductSupplierRepository(dataSource.getConnection());

        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Manage Movements");
            System.out.println("2. Manage Orders");
            System.out.println("3. Manage Products");
            System.out.println("4. Manage Requests");
            System.out.println("5. Manage Suppliers");
            System.out.println("6. Manage ProductSuppliers");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String mainOption = scanner.nextLine();

            switch (mainOption) {
                case "1":
                    manageMovements(movementRepository);
                    break;
                case "2":
                    manageOrders(orderRepository);
                    break;
                case "3":
                    manageProducts(productRepository);
                    break;
                case "4":
                    manageRequests(requestRepository);
                    break;
                case "5":
                    manageSuppliers(supplierRepository);
                    break;
                case "6":
                    manageProductSuppliers(productSupplierRepository, productRepository, supplierRepository);
                    break;
                case "0":
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void manageMovements(JdbcMovementRepository movementRepository) {
        while (true) {
            System.out.println("\n--- Movement Menu ---");
            System.out.println("1. List all movements");
            System.out.println("2. Add new movement");
            System.out.println("3. Get movement by ID");
            System.out.println("4. Delete movement by ID");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Movements:");
                    for (Movement m : movementRepository.getAll()) {
                        System.out.println("ID: " + m.getId() + ", Type: " + m.getType() + ", Quantity: " + m.getQuantity() + ", Date: " + m.getDate() + ", Reference: " + m.getReference());
                    }
                    break;
                case "2":
                    JdbcMovement newMovement = new JdbcMovement();
                    System.out.print("Type (IN/OUT): ");
                    newMovement.setType(scanner.nextLine());
                    System.out.print("Quantity: ");
                    newMovement.setQuantity(Integer.parseInt(scanner.nextLine()));
                    System.out.print("Date (YYYY-MM-DDTHH:MM:SS): ");
                    newMovement.setDate(scanner.nextLine());
                    System.out.print("Reference: ");
                    newMovement.setReference(scanner.nextLine());
                    movementRepository.save(newMovement);
                    System.out.println("Movement saved.");
                    break;
                case "3":
                    System.out.print("Enter movement ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    Movement found = movementRepository.getMovementById(id);
                    if (found != null) {
                        System.out.println("ID: " + found.getId() + ", Type: " + found.getType() + ", Quantity: " + found.getQuantity() + ", Date: " + found.getDate() + ", Reference: " + found.getReference());
                    } else {
                        System.out.println("Movement not found.");
                    }
                    break;
                case "4":
                    System.out.print("Enter movement ID to delete: ");
                    int delId = Integer.parseInt(scanner.nextLine());
                    movementRepository.delete(delId);
                    System.out.println("Movement deleted (if existed).");
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void manageOrders(JdbcOrderRepository orderRepository) {
        while (true) {
            System.out.println("\n--- Order Menu ---");
            System.out.println("1. List all orders");
            System.out.println("2. Add new order");
            System.out.println("3. Get order by ID");
            System.out.println("4. Delete order by ID");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Orders:");
                    for (Order o : orderRepository.getAll()) {
                        System.out.println("ID: " + o.getId() + ", Date: " + o.getDate() + ", Total: " + o.getTotalAmount() + ", Status: " + o.getStatus() + ", Delivery: " + o.getDeliveryDate());
                    }
                    break;
                case "2":
                    JdbcOrder newOrder = new JdbcOrder();
                    System.out.print("Date (YYYY-MM-DD): ");
                    newOrder.setDate(scanner.nextLine());
                    System.out.print("Total Amount: ");
                    newOrder.setTotalAmount(Double.parseDouble(scanner.nextLine()));
                    System.out.print("Status: ");
                    newOrder.setStatus(scanner.nextLine());
                    System.out.print("Delivery Date (YYYY-MM-DD): ");
                    newOrder.setDeliveryDate(scanner.nextLine());
                    orderRepository.save(newOrder);
                    System.out.println("Order saved.");
                    break;
                case "3":
                    System.out.print("Enter order ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    Order found = orderRepository.getOrderById(id);
                    if (found != null) {
                        System.out.println("ID: " + found.getId() + ", Date: " + found.getDate() + ", Total: " + found.getTotalAmount() + ", Status: " + found.getStatus() + ", Delivery: " + found.getDeliveryDate());
                    } else {
                        System.out.println("Order not found.");
                    }
                    break;
                case "4":
                    System.out.print("Enter order ID to delete: ");
                    int delId = Integer.parseInt(scanner.nextLine());
                    orderRepository.delete(delId);
                    System.out.println("Order deleted (if existed).");
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void manageProducts(JdbcProductRepository productRepository) {
        while (true) {
            System.out.println("\n--- Product Menu ---");
            System.out.println("1. List all products");
            System.out.println("2. Add new product");
            System.out.println("3. Get product by ID");
            System.out.println("4. Delete product by ID");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Products:");
                    for (Product p : productRepository.getAll()) {
                        System.out.println("ID: " + p.getId() + ", Name: " + p.getName() + ", Description: " + p.getDescription() + ", Price: " + p.getUnitPrice() + ", Stock: " + p.getStock());
                    }
                    break;
                case "2":
                    JdbcProduct newProduct = new JdbcProduct();
                    System.out.print("Name: ");
                    newProduct.setName(scanner.nextLine());
                    System.out.print("Description: ");
                    newProduct.setDescription(scanner.nextLine());
                    System.out.print("Price: ");
                    newProduct.setUnitPrice(Double.parseDouble(scanner.nextLine()));
                    System.out.print("Stock: ");
                    newProduct.setStock(Double.parseDouble(scanner.nextLine()));
                    productRepository.save(newProduct);
                    System.out.println("Product saved.");
                    break;
                case "3":
                    System.out.print("Enter product ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    Product found = productRepository.get(id);
                    if (found != null) {
                        System.out.println("ID: " + found.getId() + ", Name: " + found.getName() + ", Description: " + found.getDescription() + ", Price: " + found.getUnitPrice() + ", Stock: " + found.getStock());
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;
                case "4":
                    System.out.print("Enter product ID to delete: ");
                    int delId = Integer.parseInt(scanner.nextLine());
                    productRepository.delete(delId);
                    System.out.println("Product deleted (if existed).");
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void manageRequests(cat.uvic.teknos.dam.controlbox.jdbc.JdbcRequestRepository requestRepository) {
        while (true) {
            System.out.println("\n--- Request Menu ---");
            System.out.println("1. List all requests");
            System.out.println("2. Add new request");
            System.out.println("3. Get request by ID");
            System.out.println("4. Delete request by ID");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Requests:");
                    for (var r : requestRepository.getAll()) {
                        System.out.println("ID: " + r.getId() + ", Product: " + r.getProduct() + ", Quantity: " + r.getQuantity() + ", Date: " + r.getDate() + ", Status: " + r.getStatus() + ", Requester: " + r.getRequester());
                    }
                    break;
                case "2":
                    var newRequest = new cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcRequest();
                    System.out.print("Product ID: ");
                    newRequest.setProduct(Long.parseLong(scanner.nextLine()));
                    System.out.print("Quantity: ");
                    newRequest.setQuantity(Integer.parseInt(scanner.nextLine()));
                    System.out.print("Date (YYYY-MM-DD): ");
                    newRequest.setDate(scanner.nextLine());
                    System.out.print("Status: ");
                    newRequest.setStatus(scanner.nextLine());
                    System.out.print("Requester: ");
                    newRequest.setRequester(scanner.nextLine());
                    requestRepository.save(newRequest);
                    System.out.println("Request saved.");
                    break;
                case "3":
                    System.out.print("Enter request ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    var found = requestRepository.getRequestById(id);
                    if (found != null) {
                        System.out.println("ID: " + found.getId() + ", Product: " + found.getProduct() + ", Quantity: " + found.getQuantity() + ", Date: " + found.getDate() + ", Status: " + found.getStatus() + ", Requester: " + found.getRequester());
                    } else {
                        System.out.println("Request not found.");
                    }
                    break;
                case "4":
                    System.out.print("Enter request ID to delete: ");
                    int delId = Integer.parseInt(scanner.nextLine());
                    requestRepository.delete(delId);
                    System.out.println("Request deleted (if existed).");
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void manageSuppliers(cat.uvic.teknos.dam.controlbox.jdbc.JdbcSupplierRepository supplierRepository) {
        while (true) {
            System.out.println("\n--- Supplier Menu ---");
            System.out.println("1. List all suppliers");
            System.out.println("2. Add new supplier");
            System.out.println("3. Get supplier by ID");
            System.out.println("4. Delete supplier by ID");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Suppliers:");
                    for (var s : supplierRepository.getAll()) {
                        System.out.println("ID: " + s.getId() + ", Name: " + s.getName() + ", Contact: " + s.getContactName() + ", Email: " + s.getEmail() + ", Phone: " + s.getPhone() + ", Address: " + s.getAddress());
                    }
                    break;
                case "2":
                    var newSupplier = new cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcSupplier();
                    System.out.print("Name: ");
                    newSupplier.setName(scanner.nextLine());
                    System.out.print("Contact Name: ");
                    newSupplier.setContactName(scanner.nextLine());
                    System.out.print("Email: ");
                    newSupplier.setEmail(scanner.nextLine());
                    System.out.print("Phone: ");
                    newSupplier.setPhone(scanner.nextLine());
                    System.out.print("Address: ");
                    newSupplier.setAddress(scanner.nextLine());
                    supplierRepository.save(newSupplier);
                    System.out.println("Supplier saved.");
                    break;
                case "3":
                    System.out.print("Enter supplier ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    var found = supplierRepository.get(id);
                    if (found != null) {
                        System.out.println("ID: " + found.getId() + ", Name: " + found.getName() + ", Contact: " + found.getContactName() + ", Email: " + found.getEmail() + ", Phone: " + found.getPhone() + ", Address: " + found.getAddress());
                    } else {
                        System.out.println("Supplier not found.");
                    }
                    break;
                case "4":
                    System.out.print("Enter supplier ID to delete: ");
                    int delId = Integer.parseInt(scanner.nextLine());
                    supplierRepository.delete(delId);
                    System.out.println("Supplier deleted (if existed).");
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void manageProductSuppliers(
            JdbcProductSupplierRepository productSupplierRepository,
            JdbcProductRepository productRepository,
            cat.uvic.teknos.dam.controlbox.jdbc.JdbcSupplierRepository supplierRepository
    ) {
        while (true) {
            System.out.println("\n--- ProductSupplier Menu ---");
            System.out.println("1. List all ProductSuppliers");
            System.out.println("2. Add new ProductSupplier");
            System.out.println("3. Modify ProductSupplier");
            System.out.println("4. Delete ProductSupplier");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("ProductSuppliers:");
                    for (ProductSupplier ps : productSupplierRepository.getAll()) {
                        System.out.println("ID: " + ps.getId() +
                                ", Product ID: " + (ps.getProduct() != null ? ps.getProduct().getId() : "null") +
                                ", Supplier ID: " + (ps.getSupplier() != null ? ps.getSupplier().getId() : "null"));
                    }
                    break;
                case "2":
                    // Add new ProductSupplier
                    System.out.print("Enter Product ID: ");
                    Long productId;
                    try {
                        productId = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid Product ID.");
                        break;
                    }
                    var product = productRepository.get(productId.intValue());
                    if (product == null) {
                        System.out.println("Product not found.");
                        break;
                    }
                    System.out.print("Enter Supplier ID: ");
                    Long supplierId;
                    try {
                        supplierId = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid Supplier ID.");
                        break;
                    }
                    var supplier = supplierRepository.get(supplierId.intValue());
                    if (supplier == null) {
                        System.out.println("Supplier not found.");
                        break;
                    }
                    // Use the concrete implementation
                    ProductSupplier newProductSupplier = new cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcProductSupplier();
                    newProductSupplier.setProduct(product);
                    newProductSupplier.setSupplier(supplier);
                    productSupplierRepository.save(newProductSupplier);
                    System.out.println("ProductSupplier added.");
                    break;
                case "3":
                    System.out.print("Enter ProductSupplier ID to modify: ");
                    Long psId;
                    try {
                        psId = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                    ProductSupplier ps = productSupplierRepository.get(psId.intValue());
                    if (ps == null) {
                        System.out.println("ProductSupplier not found.");
                        break;
                    }
                    System.out.print("New Product ID: ");
                    Long newProductId;
                    try {
                        newProductId = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid Product ID.");
                        break;
                    }
                    var newProduct = productRepository.get(newProductId.intValue());
                    if (newProduct == null) {
                        System.out.println("Product not found.");
                        break;
                    }
                    System.out.print("New Supplier ID: ");
                    Long newSupplierId;
                    try {
                        newSupplierId = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid Supplier ID.");
                        break;
                    }
                    var newSupplier = supplierRepository.get(newSupplierId.intValue());
                    if (newSupplier == null) {
                        System.out.println("Supplier not found.");
                        break;
                    }
                    ps.setProduct(newProduct);
                    ps.setSupplier(newSupplier);
                    productSupplierRepository.save(ps);
                    System.out.println("ProductSupplier modified.");
                    break;
                case "4":
                    System.out.print("Enter ProductSupplier ID to delete: ");
                    Long delPsId;
                    try {
                        delPsId = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                    ProductSupplier psToDelete = productSupplierRepository.get(delPsId.intValue());
                    if (psToDelete == null) {
                        System.out.println("ProductSupplier not found.");
                        break;
                    }
                    productSupplierRepository.delete(psToDelete);
                    System.out.println("ProductSupplier deleted.");
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
