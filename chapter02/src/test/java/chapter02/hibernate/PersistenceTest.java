package chapter02.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class PersistenceTest {
    private SessionFactory factory = null;

    @BeforeClass
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public Message saveMessage(String text) {
        Message message = new Message(text);
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(message);
            transaction.commit();
        }
        return message;
    }

    @Test
    public void readMessage() {
        String textForSaveIntoDB = "Hello, my friends!";
        Message savedMessage = saveMessage(textForSaveIntoDB);
        List<Message> list;
        try (Session session = factory.openSession()) {
            list = session.createQuery("from chapter02.hibernate.Message", Message.class).list();
        }
        assertEquals(list.size(), 1);
        for (Message message : list) {
            System.out.println(message);
        }
        assertEquals(list.get(0), savedMessage);
    }
}