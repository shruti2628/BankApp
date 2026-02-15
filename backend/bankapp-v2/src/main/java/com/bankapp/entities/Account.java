package com.bankapp.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "accounts",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "accountNumber"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "panNumber")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private String holderName;

    @Column(nullable = false)
    private Double balance;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 10)
    private String contactNumber;

    @Column(nullable = false, unique = true, length = 10)
    private String panNumber;
}
