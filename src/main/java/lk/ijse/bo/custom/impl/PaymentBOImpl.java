package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dao.custom.CourseDAO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.dto.courseDTO;
import lk.ijse.entity.Payment;
import lk.ijse.entity.Student;
import lk.ijse.entity.course;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentBOImpl implements PaymentBO {

    private final PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDAO(DAOFactory.DAOType.PAYMENT);
    private final StudentDAO studentDAO = (StudentDAO) DAOFactory.getDAO(DAOFactory.DAOType.STUDENT);
    private final CourseDAO programDAO = (CourseDAO) DAOFactory.getDAO(DAOFactory.DAOType.PROGRAM);

    @Override
    public boolean savePayment(PaymentDTO dto) {
        return paymentDAO.save(toEntity(dto));
    }

    @Override
    public boolean updatePayment(PaymentDTO dto) {
        return paymentDAO.update(toEntity(dto));
    }

    @Override
    public boolean deletePayment(String id) {
        return paymentDAO.delete(id);
    }

    @Override
    public PaymentDTO getPayment(String id) {
        Payment payment = paymentDAO.findById(id);
        return payment != null ? toDTO(payment) : null;
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentDAO.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public String generatePaymentId() {
        return paymentDAO.generatePaymentId();
    }

    // Load all students
    @Override
    public List<StudentDTO> getAllStudents() {
        return studentDAO.findAll().stream()
                .map(s -> new StudentDTO(
                        s.getStudentId(),
                        s.getName(),
                        s.getAddress(),
                        s.getTel(),
                        s.getRegistrationDate(),
                        s.getEmail(),
                        null,
                        s.getAmount()
                ))
                .collect(Collectors.toList());
    }

    // Load all programs
    @Override
    public List<courseDTO> getAllPrograms() {
        return programDAO.findAll().stream()
                .map(p -> new courseDTO(p.getProgramId(), p.getProgramName(), p.getDuration(), p.getFee()))
                .collect(Collectors.toList());
    }

    private Payment toEntity(PaymentDTO dto) {
        Student student = new Student(dto.getStudentId());
        course program = new course(dto.getProgramId());

        java.sql.Date paymentDate = java.sql.Date.valueOf(dto.getPaymentDate());

        return new Payment(
                dto.getPaymentId(),
                student,
                dto.getAmount(),
                paymentDate,
                dto.getStatus(),
                program
        );
    }

    private PaymentDTO toDTO(Payment entity) {
        return new PaymentDTO(
                entity.getPaymentId(),
                entity.getStudent().getStudentId(),
                entity.getProgram().getProgramId(),
                entity.getAmount(),
                entity.getPaymentDate().toString(),
                entity.getStatus()
        );
    }

    @Override
    public boolean updateStatus(String paymentId, String newStatus) throws Exception {
        Payment payment = paymentDAO.findById(paymentId);
        if (payment != null) {
            payment.setStatus(newStatus);
            return paymentDAO.update(payment);
        }
        return false;
    }

    // 🔹 New methods for controller calculations
    @Override
    public StudentDTO findStudentById(String studentId) {
        Student s = studentDAO.findById(studentId);
        if (s == null) return null;
        return new StudentDTO(
                s.getStudentId(),
                s.getName(),
                s.getAddress(),
                s.getTel(),
                s.getRegistrationDate(),
                s.getEmail(),
                null,
                s.getAmount()
        );
    }

    @Override
    public courseDTO findProgramById(String programId) {
        course c = programDAO.findById(programId);
        if (c == null) return null;
        return new courseDTO(
                c.getProgramId(),
                c.getProgramName(),
                c.getDuration(),
                c.getFee()
        );
    }
}
