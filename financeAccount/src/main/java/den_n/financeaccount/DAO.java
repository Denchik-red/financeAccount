package den_n.financeaccount;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;


public class DAO<T> {
    private final SessionFactory sessionFactory;
    private final Class<T> DAOClass;

    public DAO(SessionFactory sessionFactory, Class<T> DAOClass) {
        this.sessionFactory = sessionFactory;
        this.DAOClass = DAOClass;
    }

    public void save(T entity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save entity", e);
        }
    }

    public T findById(int id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            return session.get(DAOClass, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get entity", e);
        }
    }

    public List<T> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("From " + DAOClass.getSimpleName(), DAOClass)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get entities", e);
        }
    }

    public void delete(T entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        if (entity != null) {
            session.delete(entity);
        }
        transaction.commit();
        session.close();
    }

    public void update(T entity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        }  catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update entity", e);
        }
    }
}