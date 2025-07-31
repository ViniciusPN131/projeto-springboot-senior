package hoops.metrics.api.domain;

import hoops.metrics.api.dto.clube.DadosAtualizacaoClube;
import hoops.metrics.api.dto.clube.DadosCadastroClube;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClubeTest {

    @Test
    void deveCriarClubeComDadosValidos() {
        DadosCadastroClube dados = new DadosCadastroClube(
                "Clube Teste", "CT", "São Paulo", "SP", null, null
        );
        Clube clube = new Clube(dados);

        assertEquals("Clube Teste", clube.getNome());
        assertEquals("CT", clube.getSigla());
        assertTrue(clube.getAtivo());
    }

    @Test
    void deveAtualizarInformacoesCorretamente() {
        Clube clube = new Clube(new DadosCadastroClube(
                "Nome Antigo", "NA", "Cidade Antiga", "CA", null, null
        ));

        DadosAtualizacaoClube dadosAtualizacao = new DadosAtualizacaoClube(
                1L, "Nome Novo", "NN", "Cidade Nova", "CN", null
        );
        clube.atualizarInformacoes(dadosAtualizacao);

        assertEquals("Nome Novo", clube.getNome());
        assertEquals("NN", clube.getSigla());
        assertEquals("Cidade Nova", clube.getCidade());
    }

    @Test
    void deveDesativarClubeAoExcluir() {
        Clube clube = new Clube(new DadosCadastroClube(
                "Clube", "C", "Cidade", "E", null, null
        ));
        clube.excluir();
        assertFalse(clube.getAtivo());
    }
}