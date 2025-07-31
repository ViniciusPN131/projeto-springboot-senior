package hoops.metrics.api.domain;

import hoops.metrics.api.dto.estatistica.DadosAtualizacaoEstatistica;
import hoops.metrics.api.dto.estatistica.DadosCadastroEstatistica;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstatisticaTest {

    @Test
    void deveCriarEstatisticaComDadosValidos() {
        Jogador jogador = new Jogador();
        Partida partida = new Partida();
        DadosCadastroEstatistica dados = new DadosCadastroEstatistica(
                20L, 3L, 20, 7, 5, 4, 1, 1, 1
        );
        Estatistica estatistica = new Estatistica(dados, jogador, partida);

        assertEquals(20, estatistica.getTotalPontos());
        assertEquals(5, estatistica.getRebotesOfensivos());
        assertEquals(jogador, estatistica.getJogador());
    }

    @Test
    void deveAtualizarEstatisticasCorretamente() {
        Estatistica estatistica = new Estatistica(
                new DadosCadastroEstatistica(
                        20L, 3L, 30, 2, 10, 4, 5, 1, 1
                ),
                new Jogador(), new Partida()
        );

        DadosAtualizacaoEstatistica dadosAtualizacao = new DadosAtualizacaoEstatistica(
                30L, null, 3, null, 5, null, 1, 1
        );
        estatistica.atualizarInformacoes(dadosAtualizacao);

        assertEquals(30, estatistica.getTotalPontos());
        assertEquals(10, estatistica.getRebotesOfensivos());
        assertEquals(5, estatistica.getRoubosDeBola());
        // Campos não atualizados devem permanecer os mesmos
        assertEquals(3, estatistica.getTotalFaltas());
    }
}