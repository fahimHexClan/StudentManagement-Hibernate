package lk.Ijse.Dao.Impl;

import lk.Ijse.Dao.StudentDao;
import lk.Ijse.Entity.StudentEntity;
import lk.Ijse.config.FactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    @Override
    public boolean save(StudentEntity studentEntity) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(studentEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Integer id) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();
            StudentEntity studentEntity = session.get(StudentEntity.class, id);
            if (studentEntity != null) {
                session.remove(studentEntity);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(StudentEntity studentEntity) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(studentEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<StudentEntity> search(String keyword) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<StudentEntity> query = session.createQuery("FROM StudentEntity WHERE id = :id OR address LIKE :address OR name LIKE :name");
            query.setParameter("id", Integer.parseInt(keyword));
            query.setParameter("address", "%" + keyword + "%");
            query.setParameter("name", "%" + keyword + "%");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



}
