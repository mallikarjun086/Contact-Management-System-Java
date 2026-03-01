import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

// ================= CONTACT MODEL =================
class Contact implements Serializable, Comparable<Contact> {
    String name;
    String phone;
    String email;

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                " | Phone: " + phone +
                " | Email: " + email;
    }

    // Sorting by name
    @Override
    public int compareTo(Contact other) {
        return this.name.compareToIgnoreCase(other.name);
    }
}

// ================= MAIN SYSTEM =================
public class ContactManager {

    private static final String FILE_NAME = "contacts.dat";
    private static List<Contact> contacts = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    // Validation patterns
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    public static void main(String[] args) {

        loadContacts();

        int choice;
        do {
            menu();
            choice = getInt();

            switch (choice) {
                case 1 -> addContact();
                case 2 -> viewContacts();
                case 3 -> searchContact();
                case 4 -> editContact();
                case 5 -> deleteContact();
                case 6 -> saveContacts();
                case 0 -> {
                    saveContacts();
                    System.out.println("👋 Exiting... Data Saved.");
                }
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 0);
    }

    // ================= MENU =================
    private static void menu() {
        System.out.println("\n====== Contact Management System ======");
        System.out.println("1. Add Contact");
        System.out.println("2. View Contacts");
        System.out.println("3. Search Contact");
        System.out.println("4. Edit Contact");
        System.out.println("5. Delete Contact");
        System.out.println("6. Save Contacts");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
    }

    // ================= ADD =================
    private static void addContact() {

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        String phone;
        while (true) {
            System.out.print("Enter Phone (10 digits): ");
            phone = scanner.nextLine();
            if (PHONE_PATTERN.matcher(phone).matches()) break;
            System.out.println("❌ Invalid phone number.");
        }

        String email;
        while (true) {
            System.out.print("Enter Email: ");
            email = scanner.nextLine();
            if (EMAIL_PATTERN.matcher(email).matches()) break;
            System.out.println("❌ Invalid email format.");
        }

        contacts.add(new Contact(name, phone, email));
        Collections.sort(contacts);

        System.out.println("✅ Contact added successfully.");
    }

    // ================= VIEW =================
    private static void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts available.");
            return;
        }

        System.out.println("\n---- Contact List ----");
        int i = 1;
        for (Contact c : contacts) {
            System.out.println(i++ + ". " + c);
        }
    }

    // ================= SEARCH =================
    private static void searchContact() {

        System.out.print("Enter name to search: ");
        String keyword = scanner.nextLine().toLowerCase();

        boolean found = false;

        for (Contact c : contacts) {
            if (c.name.toLowerCase().contains(keyword)) {
                System.out.println(c);
                found = true;
            }
        }

        if (!found)
            System.out.println("No matching contact found.");
    }

    // ================= EDIT =================
    private static void editContact() {

        viewContacts();
        if (contacts.isEmpty()) return;

        System.out.print("Enter contact number to edit: ");
        int index = getInt() - 1;

        if (index < 0 || index >= contacts.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Contact c = contacts.get(index);

        System.out.print("New Name (" + c.name + "): ");
        c.name = scanner.nextLine();

        System.out.print("New Phone (" + c.phone + "): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty() && PHONE_PATTERN.matcher(phone).matches())
            c.phone = phone;

        System.out.print("New Email (" + c.email + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty() && EMAIL_PATTERN.matcher(email).matches())
            c.email = email;

        Collections.sort(contacts);
        System.out.println("✅ Contact updated.");
    }

    // ================= DELETE =================
    private static void deleteContact() {

        viewContacts();
        if (contacts.isEmpty()) return;

        System.out.print("Enter contact number to delete: ");
        int index = getInt() - 1;

        if (index < 0 || index >= contacts.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        contacts.remove(index);
        System.out.println("🗑 Contact deleted.");
    }

    // ================= SAVE =================
    private static void saveContacts() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(contacts);
            System.out.println("💾 Contacts saved.");

        } catch (IOException e) {
            System.out.println("Error saving contacts.");
        }
    }

    // ================= LOAD =================
    private static void loadContacts() {

        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            contacts = (ArrayList<Contact>) in.readObject();

        } catch (Exception e) {
            System.out.println("Could not load saved contacts.");
        }
    }

    // ================= SAFE INPUT =================
    private static int getInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter valid number: ");
            scanner.next();
        }
        int num = scanner.nextInt();
        scanner.nextLine();
        return num;
    }
}