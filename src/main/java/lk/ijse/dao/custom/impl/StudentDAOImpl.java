package lk.ijse.dao.custom.impl;

import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.db.FactoryConfiguration;
import lk.ijse.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void saveStudent(Student student) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.save(student);
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteStudent(Student student) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            String studentId = student.getStudentId();

            session.createQuery("DELETE FROM Lesson l WHERE l.student.studentId = :id")
                    .setParameter("id", studentId)
                    .executeUpdate();

            session.createQuery("DELETE FROM Payment p WHERE p.student.studentId = :id")
                    .setParameter("id", studentId)
                    .executeUpdate();

            session.delete(student);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void updateStudent(Student student) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.merge(student);
        tx.commit();
        session.close();
    }

    @Override
    public List<Student> getAllStudent() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Student> students = session.createQuery("from Student", Student.class).list();
        session.close();
        return students;
    }

    @Override
    public Student getStudent(String studentId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Student student = session.get(Student.class, studentId);
        session.close();
        return student;
    }

    @Override
    public Long getStudentCount() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Query<Long> query = session.createQuery("SELECT COUNT(s) FROM Student s", Long.class);
        Long count = query.uniqueResult();
        session.close();
        return count;
    }

    @Override
    public Student findById(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Student student = session.get(Student.class, id);
        session.close();
        return student;
    }

    @Override
    public List<Student> findAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Student> students = session.createQuery("FROM Student", Student.class).list();
        session.close();
        return students;
    }

    @Override
    public String generateNewId() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Query<String> query = session.createQuery("SELECT s.studentId FROM Student s ORDER BY s.studentId DESC", String.class);
        query.setMaxResults(1);
        String lastId = query.uniqueResult();
        session.close();
        if (lastId != null) {
            int newId = Integer.parseInt(lastId.replace("S", "")) + 1;
            return String.format("S%03d", newId);
        } else {
            return "S001";
        }
    }
}
