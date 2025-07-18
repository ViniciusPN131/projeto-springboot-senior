package hoops.metrics.api.domain.jogador;

import hoops.metrics.api.domain.clube.Clube;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DadosPostJogador(

        @NotBlank
        String nome,

        @NotNull
        LocalDate data_nascimento,

        @NotNull
        int altura,

        @NotNull
        float peso,

        @NotNull
        Posicao posicao,

        @NotNull
        Long clube_id
) {}
