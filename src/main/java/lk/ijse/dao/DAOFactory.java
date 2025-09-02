package lk.ijse.dao;

import lk.ijse.dao.custom.impl.*;

public class DAOFactory {

    public enum DAOType{
        PROGRAM,STUDENT,QUERY,ENROLLMENT,USER,COURSE
    }

    public static SuperDAO getDAO(DAOType daoType){
        return switch (daoType) {
            case PROGRAM -> new CourseDAOImpl();
            case STUDENT -> new StudentDAOImpl();
            case QUERY -> new QueryDAOImpl();

            case USER -> new UserDAOImpl();
            case COURSE -> new CourseDAOImpl();
            default -> null;
        };
    }
}
