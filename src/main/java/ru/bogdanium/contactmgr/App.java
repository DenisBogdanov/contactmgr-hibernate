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

        save(contact);

        // Display a list of contacts
        fetchAllContacts().stream().forEach(System.out::println);
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

    private static void save(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        session.save(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }
}
