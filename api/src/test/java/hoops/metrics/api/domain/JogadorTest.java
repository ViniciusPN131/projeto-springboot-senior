package hoops.metrics.api.domain;

import hoops.metrics.api.dto.jogador.DadosAtualizacaoJogador;
import hoops.metrics.api.dto.jogador.DadosCadastroJogador;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class JogadorTest {

    @Test
    void deveCriarJogadorComDadosValidos() {
        DadosCadastroJogador dados = new DadosCadastroJogador(
                "Jogador Teste", "12345678900", LocalDate.of(2000, 1, 1), 180, 75.0f, Posicao.ALA, 1L, null
        );
        Jogador jogador = new Jogador(dados);

        assertEquals("Jogador Teste", jogador.getNome());
        assertEquals(180, jogador.getAltura());
        assertTrue(jogador.getAtivo()); // Verifica se está ativo por padrão
    }

    @Test
    void deveAtualizarInformacoesCorretamente() {
        Jogador jogador = new Jogador(new DadosCadastroJogador(
                "Nome Antigo", "11111111111", LocalDate.now(), 170, 70.0f, Posicao.ARMADOR, 1L, null
        ));

        DadosAtualizacaoJogador dadosAtualizacao = new DadosAtualizacaoJogador(
                1L, "Nome Novo", 80.0f, 175, null, Posicao.PIVO
        );
        jogador.atualizarInformacoes(dadosAtualizacao);

        assertEquals("Nome Novo", jogador.getNome());
        assertEquals(80.0f, jogador.getPeso());
        assertEquals(Posicao.PIVO, jogador.getPosicao());
    }

    @Test
    void deveDesativarJogadorAoExcluir() {
        Jogador jogador = new Jogador(new DadosCadastroJogador(
                "Jogador", "22222222222", LocalDate.now(), 185, 85.0f, Posicao.ALA, 1L, null
        ));
        jogador.excluir();
        assertFalse(jogador.getAtivo());
    }
}