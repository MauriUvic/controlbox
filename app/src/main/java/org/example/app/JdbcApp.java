package org.example.app;

public class JdbcApp {
    public static void main(String[] args) {
        testPersonRepository();
    }

    private static void testPersonRepository() {
        PersonRepository repository = new PersonRepository();

        // Create and save new persons
        Person person1 = new Person();
        person1.setId(1);
        person1.setFirstName("David");
        person1.setLastName("Gilmour");

        Person person2 = new Person();
        person2.setId(2);
        person2.setFirstName("Jimi");
        person2.setLastName("Hendrix");

        try {
            // Save persons
            repository.save(person1);
            repository.save(person2);

            // Print all persons

            for (Person p : repository.getAll()) {
                System.out.println( p.getId());
                System.out.println( p.getFirstName());
                System.out.println( p.getLastName());
                System.out.println("-------------------");
            }

            //delte person
          //repository.delete(person1);
          //repository.delete(person2);


        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}