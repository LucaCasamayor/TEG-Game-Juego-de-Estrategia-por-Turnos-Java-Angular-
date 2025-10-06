package ar.edu.utn.frc.tup.piii.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "user_achievements")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAchievementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID userAchievementId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "userId")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "achievement_id")
    private AchievementEntity achievement;

    @Column
    private Date date;
}
