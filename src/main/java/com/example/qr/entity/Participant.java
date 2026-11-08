package com.example.qr.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "participant")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QrCode> qrCodes = new ArrayList<>();

    public Participant() {
    }

    public Participant(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<QrCode> getQrCodes() {
        return qrCodes;
    }

    public void setQrCodes(List<QrCode> qrCodes) {
        this.qrCodes = qrCodes;
    }
}