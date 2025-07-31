package hoops.metrics.api.domain;

import hoops.metrics.api.dto.partida.DadosAtualizacaoPartida;
import hoops.metrics.api.dto.partida.DadosCadastroPartida;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

    @Test
    void deveCriarPartidaComDadosValidos() {
        Clube mandante = new Clube();
        Clube visitante = new Clube();
        LocalDateTime dataHora = LocalDateTime.now();
        DadosCadastroPartida dados = new DadosCadastroPartida(
                1L,2L, LocalDateTime.now(), "Estádio Teste"
        );
        Partida partida = new Partida(dados, mandante, visitante);

        assertEquals("Estádio Teste", partida.getLocal());
        assertEquals(dataHora, partida.getDataHora());
    }

    @Test
    void deveAtualizarPartidaCorretamente() {
        Partida partida = new Partida(
                new DadosCadastroPartida(1L,2L, LocalDateTime.now(), "Estádio Teste"),
                new Clube(), new Clube()
        );

        LocalDateTime novaData = LocalDateTime.now().plusDays(1);
        DadosAtualizacaoPartida dadosAtualizacao = new DadosAtualizacaoPartida(
                1L, 1L,2L, novaData, "Local Novo"
        );
        partida.atualizarInformacoes(dadosAtualizacao);

        assertEquals("Local Novo", partida.getLocal());
        assertEquals(novaData, partida.getDataHora());
    }

    @Test
    void deveDesativarPartidaAoExcluir() {
        Partida partida = new Partida(
                new DadosCadastroPartida(1L,2L, LocalDateTime.now(), "Estádio Teste"),
                new Clube(), new Clube()
        );
        partida.excluir();
        assertFalse(partida.isAtivo());
    }
}