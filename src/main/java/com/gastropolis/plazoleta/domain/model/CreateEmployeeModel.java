package com.gastropolis.plazoleta.domain.model;

import java.time.LocalDate;

public class CreateEmployeeModel {

    private String name;
    private String lastName;
    private String dni;
    private String phone;
    private LocalDate birthDate;
    private String email;
    private String password;

    public CreateEmployeeModel() {
    }

    public CreateEmployeeModel(String name, String lastName, String dni, String phone,
                               LocalDate birthDate, String email, String password) {
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
        this.phone = phone;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
