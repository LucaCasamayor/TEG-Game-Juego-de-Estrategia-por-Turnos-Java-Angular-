package ar.edu.utn.frc.tup.piii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "players_games_stats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerGameStatsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerGameStatsId;
    @OneToOne
    @JoinColumn
    private PlayerEntity player;
    @Column
    private int armiesHad;
    @Column
    private int armiesLost;
    @Column
    private int armiesDefeated;
    @Column
    private int territoriesConquered;
    @Column
    private int territoriesLost;
    @Column
    private int cardsTraded;
}