package ar.edu.utn.frc.tup.piii.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalPactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long globalPactId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "playerId")
    private PlayerEntity player1;

    @ManyToOne
    @JoinColumn(referencedColumnName = "playerId")
    private PlayerEntity player2;

    @Column
    private boolean isActive;

}
