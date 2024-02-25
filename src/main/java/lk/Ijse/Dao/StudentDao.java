package lk.Ijse.Dao;

import jakarta.transaction.SystemException;
import lk.Ijse.Entity.StudentEntity;

import java.sql.SQLException;
import java.util.List;

public interface StudentDao {
    boolean save(StudentEntity studentEntity);
    boolean delete(Integer id);
    boolean update(StudentEntity studentEntity);
    List<StudentEntity> search(String keyword);

}
