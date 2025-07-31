package hoops.metrics.api.dto.jogador;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.domain.Posicao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DadosCadastroJogador(

        @NotBlank
        String nome,

        @NotBlank
        @Size(min = 11, max = 11)
        String cpf,

        @NotNull
        @Past
        LocalDate data_nascimento,

        @NotNull
        int altura,

        @NotNull
        float peso,

        @NotNull
        Posicao posicao,

        @NotNull
        Long clube_id,

        Clube clube
) {}
