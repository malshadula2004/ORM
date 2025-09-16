package lk.ijse.dto;

import java.sql.Date;

public class StudentDTO {
    private String studentId;
    private String name;
    private String address;
    private Long tel;
    private Date registrationDate;
    private String email;
    private String course;
    private Double amount;

    public StudentDTO() {}

    public StudentDTO(String studentId, String name, String address, Long tel, Date registrationDate,
                      String email, String course, Double amount) {
        this.studentId = studentId;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.registrationDate = registrationDate;
        this.email = email;
        this.course = null;
        this.amount = amount;
    }

    // Getters & Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Long getTel() { return tel; }
    public void setTel(Long tel) { this.tel = tel; }

    public Date getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
