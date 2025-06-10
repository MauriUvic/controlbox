package cat.uvic.teknos.dam.controlbox.main;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcMovementRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcMovement;
import cat.uvic.teknos.dam.controlbox.model.Movement;

import java.util.*;

public class App {
    public static void main(String[] args) {
        var dataSource = new SingleConnectionDataSource();
        var movementRepository = new JdbcMovementRepository(dataSource);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Movement Menu ---");
            System.out.println("1. List all movements");
            System.out.println("2. Add new movement");
            System.out.println("3. Get movement by ID");
            System.out.println("4. Delete movement by ID");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String option =  "2";
            if (scanner.hasNextLine()) {
                option = scanner.nextLine();
            }
            option = scanner.nextLine();


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
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
