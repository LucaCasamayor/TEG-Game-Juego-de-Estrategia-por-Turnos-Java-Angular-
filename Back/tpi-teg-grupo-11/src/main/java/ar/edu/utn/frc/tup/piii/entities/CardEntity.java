package ar.edu.utn.frc.tup.piii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CardId;
    @JoinColumn(name = "territory_id")
    @OneToOne
    private TerritoryEntity territory;
    @ManyToOne
    @JoinColumn(name = "symbol_id")
    private SymbolEntity symbol;
    @OneToMany(mappedBy = "card")
    private List<CardStateEntity> cardStates;
}