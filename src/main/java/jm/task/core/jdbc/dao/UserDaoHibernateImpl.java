package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT,\n" +
                "name VARCHAR (20) NOT NULL,\n" +
                "lastname VARCHAR (25) NOT NULL,\n" +
                "age SMALLINT ,\n" +
                "primary key (id))";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "TRUNCATE TABLE users";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
        } catch (Exception e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
        }
    }

    @Override
    public void removeUserById(long id) {
        String sql = "DELETE FROM USERS WHERE ID = ?";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(sql).setLong(1, id).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list;
        try (Session session = Util.getSessionFactory().openSession()){
            session.beginTransaction();
            list = session.createQuery("from User").getResultList();
            for (User u: list) {
                System.out.println(u);
            }
            session.getTransaction().commit();
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE USERS";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
        }
    }
}
