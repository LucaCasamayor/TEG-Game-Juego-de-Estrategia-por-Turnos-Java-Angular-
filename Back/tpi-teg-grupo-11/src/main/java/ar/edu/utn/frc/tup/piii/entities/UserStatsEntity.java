package ar.edu.utn.frc.tup.piii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "users_stats")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false, name = "user_stats_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false, name = "armies_had")
    private int armiesHad;

    @Column(nullable = false, name = "armies_lost")
    private int armiesLost;

    @Column(nullable = false, name = "armies_defeated")
    private int armiesDefeated;

    @Column(nullable = false, name = "territories_conquered")
    private int territoriesConquered;

    @Column(nullable = false, name = "territories_lost")
    private int territoriesLost;

    @Column(nullable = false, name = "games_played")
    private int gamesPlayed;

    @Column(nullable = false, name = "games_won")
    private int gamesWon;

    @Column(nullable = false, name = "rounds_played")
    private int roundsPlayed;
}