package ar.edu.utn.frc.tup.piii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true, name = "img_url")
    private String imgUrl;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, name = "creation_date")
    private Date creationDate;

    @OneToMany
    private List<PlayerEntity> playerProfiles;

    @Column(length = 6)
    private String recovery_code;

    @Column
    private LocalDateTime recovery_code_expiration;

}