package ar.edu.utn.frc.tup.piii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "dices")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diceId;
    @Column
    private int attackerDice;
    @Column
    private int defenderDice;

}
