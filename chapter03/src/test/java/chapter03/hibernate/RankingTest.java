package chapter03.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;

public class RankingTest {
    SessionFactory factory;

    @BeforeClass
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.MYSQL.xml")
                .build();
        factory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    @Test
    public void testSaveRanking() {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Person subject = savePerson(session, "J. C. Smell");
            Person observer = savePerson(session, "Drew Lombardo");
            Skill skill = saveSkill(session, "Java");
            Ranking ranking = new Ranking();
            ranking.setSubject(subject);
            ranking.setObserver(observer);
            ranking.setSkill(skill);
            ranking.setRanking(8);
            session.save(ranking);
            tx.commit();
        }
    }

    @Test
    public void testRankings() {
        populateRankingData();
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Ranking> query = session.createQuery(
                    "from Ranking r where r.subject.name=:name and r.skill.name=:skill",
                    Ranking.class
            );
            query.setParameter("name", "J. C. Smell");
            query.setParameter("skill", "Java");
            IntSummaryStatistics statistics = query.list()
                    .stream()
                    .collect(Collectors.summarizingInt(Ranking::getRanking));
            long count = statistics.getCount();
            int average = (int) statistics.getAverage();
            transaction.commit();
            session.close();
            assertEquals(count, 3);
            assertEquals(average, 7);
        }
    }

    private void populateRankingData() {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            createData(session, "J. C. Smell", "Gene Showrama", "Java", 6);
            createData(session, "J. C. Smell", "Scottball Most", "Java", 7);
            createData(session, "J. C. Smell", "Drew Lombardo", "Java", 8);
            transaction.commit();
        }
    }

    private void createData(Session session, String subjectName, String observerName, String skillName, int rank) {
        Person subject = savePerson(session, subjectName);
        Person observer = savePerson(session, observerName);
        Skill skill = saveSkill(session, skillName);
        Ranking ranking = new Ranking();
        ranking.setSubject(subject);
        ranking.setObserver(observer);
        ranking.setSkill(skill);
        ranking.setRanking(rank);
        session.save(ranking);
    }

    /*Person's methods*/
    private Person findPerson(Session session, String name) {
        Query<Person> query = session.createQuery(
                "from Person p where p.name = :name",
                Person.class
        );
        query.setParameter("name", name);
        Person person = query.uniqueResult();
        return person;
    }

    private Person savePerson(Session session, String name) {
        Person person = findPerson(session, name);
        if (person == null) {
            person = new Person();
            person.setName(name);
            session.save(person);
        }
        return person;
    }

    /*Skill's methods*/
    private Skill findSkill(Session session, String name) {
        Query<Skill> query = session.createQuery(
                "from Skill s where s.name = :name",
                Skill.class
        );
        query.setParameter("name", name);
        Skill Skill = query.uniqueResult();
        return Skill;
    }

    private Skill saveSkill(Session session, String name) {
        Skill Skill = findSkill(session, name);
        if (Skill == null) {
            Skill = new Skill();
            Skill.setName(name);
            session.save(Skill);
        }
        return Skill;
    }

}
