package com.example.mbaningapijpapractice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal amount;

    @ManyToOne
    private Account ownerId;

    private LocalDateTime transactionAt;

    @JoinColumn

    @ManyToOne
    private Account transferReceiverId;

    @ManyToOne
    private Account receiverId;
    @Column(nullable = false, length = 30)
    private String transactionType;

    private String paymentReceiver;

    @Column(columnDefinition="TEXT")
    private String remark;
    private Boolean isDeleted;
}
