package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentBOImpl implements StudentBO {

    StudentDAO studentDAO = (StudentDAO) DAOFactory.getDAO(DAOFactory.DAOType.STUDENT);

    @Override
    public void saveStudent(StudentDTO dto) {
        studentDAO.saveStudent(toEntity(dto));
    }

    @Override
    public void updateStudent(StudentDTO dto) {
        studentDAO.updateStudent(toEntity(dto));
    }

    @Override
    public void deleteStudent(StudentDTO dto) {
        studentDAO.deleteStudent(toEntity(dto));
    }

    @Override
    public StudentDTO getStudent(String id) {
        Student s = studentDAO.getStudent(id);
        return s != null ? toDTO(s) : null;
    }

    @Override
    public List<StudentDTO> getAllStudent() {
        List<Student> list = studentDAO.getAllStudent();
        List<StudentDTO> dtoList = new ArrayList<>();
        for (Student s : list) dtoList.add(toDTO(s));
        return dtoList;
    }

    @Override
    public String generateNewId() {
        return studentDAO.generateNewId();
    }

    private Student toEntity(StudentDTO dto) {
        return new Student(
                dto.getStudentId(),
                dto.getName(),
                dto.getAddress(),
                dto.getTel(),
                dto.getRegistrationDate(),
                dto.getEmail(),
                dto.getCourse(),
                dto.getAmount()
        );
    }

    private StudentDTO toDTO(Student entity) {
        return new StudentDTO(
                entity.getStudentId(),
                entity.getName(),
                entity.getAddress(),
                entity.getTel(),
                entity.getRegistrationDate(),
                entity.getEmail(),
                entity.getCourse(),
                entity.getAmount()
        );
    }
}
