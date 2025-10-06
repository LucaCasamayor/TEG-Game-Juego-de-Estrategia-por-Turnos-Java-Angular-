package ar.edu.utn.frc.tup.piii.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Symbol {
        private Long symbolId;
        private String Symbol;
        private String img;
}
