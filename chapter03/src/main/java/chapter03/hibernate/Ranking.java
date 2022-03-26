package chapter03.hibernate;

import javax.persistence.*;

@Entity
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Person subject;
    @ManyToOne
    private Person observer;
    @ManyToOne
    private Skill skill;
    @Column
    private Integer ranking;

    public Ranking() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getSubject() {
        return subject;
    }

    public void setSubject(Person subject) {
        this.subject = subject;
    }

    public Person getObserver() {
        return observer;
    }

    public void setObserver(Person observer) {
        this.observer = observer;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "id=" + id +
                ", subject=" + subject +
                ", observer=" + observer +
                ", skill=" + skill +
                ", ranking=" + ranking +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ranking ranking1 = (Ranking) o;

        if (id != null ? !id.equals(ranking1.id) : ranking1.id != null) return false;
        if (subject != null ? !subject.equals(ranking1.subject) : ranking1.subject != null) return false;
        if (observer != null ? !observer.equals(ranking1.observer) : ranking1.observer != null) return false;
        if (skill != null ? !skill.equals(ranking1.skill) : ranking1.skill != null) return false;
        return ranking != null ? ranking.equals(ranking1.ranking) : ranking1.ranking == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (observer != null ? observer.hashCode() : 0);
        result = 31 * result + (skill != null ? skill.hashCode() : 0);
        result = 31 * result + (ranking != null ? ranking.hashCode() : 0);
        return result;
    }
}
