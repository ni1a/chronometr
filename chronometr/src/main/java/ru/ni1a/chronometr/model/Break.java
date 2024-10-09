package ru.ni1a.chronometr.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Break {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBreak;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false) // Связь с работником, его ID
    private Employee employee;

    @Column(name = "Start break", scale = 2, nullable = false)
    private LocalDateTime startBreak;

    @Column(name = "End Break", scale = 2, nullable = false)
    private LocalDateTime endBreak;

    public Long getIdBreak() {
        return idBreak;
    }

    public void setIdBreak(Long idBreak) {
        this.idBreak = idBreak;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDateTime getStartBreak() {
        return startBreak;
    }

    public void setStartBreak(LocalDateTime startBreak) {
        this.startBreak = startBreak;
    }

    public LocalDateTime getEndBreak() {
        return endBreak;
    }

    public void setEndBreak(LocalDateTime endBreak) {
        this.endBreak = endBreak;
    }
}
