package ar.edu.utn.frc.tup.piii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "symbols")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SymbolEntity {
    @Id
    @Column(nullable = false,unique = true, name ="symbol_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long symbolId;

    @Column(nullable = false,name="symbol")
    private String symbol;

    @Column(name="image" )
    private String img;

    @OneToMany(mappedBy = "symbol", cascade = CascadeType.ALL)
    private List<CardEntity> cards;
}