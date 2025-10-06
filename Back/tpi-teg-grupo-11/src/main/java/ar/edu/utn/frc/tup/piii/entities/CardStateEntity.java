package ar.edu.utn.frc.tup.piii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_states")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardStateId;
    @ManyToOne
    @JoinColumn
    private CardEntity card;
    @ManyToOne
    @JoinColumn
    private GameEntity game;
    @ManyToOne
    @JoinColumn
    private PlayerEntity player;
    @Column
    private boolean wasUsed;

}