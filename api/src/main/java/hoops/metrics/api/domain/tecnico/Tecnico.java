package hoops.metrics.api.domain.tecnico;

import hoops.metrics.api.domain.clube.Clube;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "tecnicos")
@Entity(name = "Tecnico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Tecnico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @ManyToOne
    @JoinColumn(name = "clube_id")
    private Clube clube;

}
