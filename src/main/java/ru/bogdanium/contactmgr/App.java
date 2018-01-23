package ru.bogdanium.contactmgr;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import ru.bogdanium.contactmgr.model.Contact;

import java.util.List;


public class App {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry =
                new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new Contact.ContactBuilder("Denis", "Bogdanov")
                .withEmail("denis@bogdanium.ru")
                .withPhone(12345678L)
                .build();

        int id = save(contact);

        // Display a list of contacts before the update
        System.out.printf("\n\nBefore update\n\n");
        fetchAllContacts().stream().forEach(System.out::println);

        Contact c = findContactById(id);

        c.setFirstName("Petya");

        System.out.printf("\n\nUpdating...\n\n");
        update(c);
        System.out.printf("\n\nUpdate complete!\n\n");

        System.out.printf("\n\nAfter update\n\n");
        fetchAllContacts().stream().forEach(System.out::println);
    }

    private static Contact findContactById(int id) {
        Session session = sessionFactory.openSession();

        Contact contact = session.get(Contact.class, id);

        session.close();

        return contact;
    }

    private static void update(Contact contact) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.update(contact);

        session.getTransaction().commit();

        session.close();
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {
        // Open a session
        Session session = sessionFactory.openSession();

        // Create Criteria
        Criteria criteria = session.createCriteria(Contact.class);

        // Get a list of Contact objects
        List<Contact> contacts = criteria.list();

        // Close the session
        session.close();

        return contacts;
    }

    private static int save(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        int id = (int) session.save(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();

        return id;
    }
}
