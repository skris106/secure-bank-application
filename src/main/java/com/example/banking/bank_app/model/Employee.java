package com.example.banking.bank_app.model;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="employee")

public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employee_id;

    @Column(name="employee_name")
    @SafeHtml
    private String employee_name;

    @Column(name="gender")
    @SafeHtml
    private String gender;

    @Column(name="age")
    private Integer age;

    @Column(name="tier_level")
    private Integer tier_level;

    @Column(name="designation_id")
    private Integer designation_id;

    @Column(name="contact_no")
    @SafeHtml
    private String contact_no;

    @Column(name="email_id")
    @SafeHtml
    private String email_id;

    @Column(name="address")
    @SafeHtml
    private String address;

    @Column(name="created")
    private Timestamp created;

    @Column(name="updated")
    private Timestamp updated;

    @Transient
    private String password;

    public Long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getTier_level() {
        return tier_level;
    }

    public void setTier_level(Integer tier_level) {
        this.tier_level = tier_level;
    }

    public Integer getDesignation_id() {
        return designation_id;
    }

    public void setDesignation_id(Integer designation_id) {
        this.designation_id = designation_id;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
