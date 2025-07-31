package hoops.metrics.api.domain;

import hoops.metrics.api.dto.tecnico.DadosAtualizacaoTecnico;
import hoops.metrics.api.dto.tecnico.DadosCadastroTecnico;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TecnicoTest {

    @Test
    void deveCriarTecnicoComDadosValidos() {
        DadosCadastroTecnico dados = new DadosCadastroTecnico(
                "Técnico Teste", "CREF123"
        );
        Tecnico tecnico = new Tecnico(dados);

        assertEquals("Técnico Teste", tecnico.getNome());
        assertEquals("CREF123", tecnico.getCref());
        assertTrue(tecnico.getAtivo());
    }

    @Test
    void deveLancarExcecaoComDadosNulos() {
        assertThrows(IllegalArgumentException.class, () -> new Tecnico(null));
    }

    @Test
    void deveAtualizarTecnicoCorretamente() {
        Tecnico tecnico = new Tecnico(new DadosCadastroTecnico(
                "Nome Antigo", "CREF000"
        ));

        DadosAtualizacaoTecnico dadosAtualizacao = new DadosAtualizacaoTecnico(
                1L, "Nome Novo", "CREF999"
        );
        tecnico.atualizarInformacoes(dadosAtualizacao);

        assertEquals("Nome Novo", tecnico.getNome());
        assertEquals("CREF999", tecnico.getCref());
    }

    @Test
    void deveDesativarTecnicoAoExcluir() {
        Tecnico tecnico = new Tecnico(new DadosCadastroTecnico(
                "Técnico", "CREF123"
        ));
        tecnico.excluir();
        assertFalse(tecnico.getAtivo());
    }
}