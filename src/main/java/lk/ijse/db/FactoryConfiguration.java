package lk.ijse.db;

import lk.ijse.entity.CulinaryProgram;
import lk.ijse.entity.Enrollment;
import lk.ijse.entity.Student;
import lk.ijse.entity.User;
import lk.ijse.entity.Instructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import lk.ijse.entity.Course;

import java.io.FileInputStream;
import java.util.Properties;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;

    private FactoryConfiguration() {
        try {
            Properties properties = new Properties();
            FileInputStream input = new FileInputStream("src/main/resources/hibernate.properties");
            properties.load(input);

            Configuration configuration = new Configuration();
            configuration.setProperties(properties)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(CulinaryProgram.class)
                    .addAnnotatedClass(Enrollment.class)
                    .addAnnotatedClass(Course.class)
                    .addAnnotatedClass(Student.class);



            sessionFactory = configuration.buildSessionFactory();
            System.out.println("Hibernate SessionFactory created successfully, DB should be ready!");
        } catch (Exception e) {
            e.printStackTrace(); // error log print වෙනවා
            throw new RuntimeException("Hibernate configuration failed", e);
        }
    }

    public static FactoryConfiguration getInstance() {
        if (factoryConfiguration == null) {
            factoryConfiguration = new FactoryConfiguration();
        }
        return factoryConfiguration;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
