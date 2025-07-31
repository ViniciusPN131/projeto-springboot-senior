package hoops.metrics.api.service;

import hoops.metrics.api.domain.*;
import hoops.metrics.api.dto.estatistica.DadosCadastroEstatistica;
import hoops.metrics.api.dto.jogador.DadosCadastroJogador;
import hoops.metrics.api.repository.EstatisticaRepository;
import hoops.metrics.api.repository.JogadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PdfServiceTest {

    @Mock
    private JogadorRepository jogadorRepository;

    @Mock
    private EstatisticaRepository estatisticaRepository;

    @InjectMocks
    private PdfService pdfService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void gerarPdfJogador_deveGerarPdfComSucesso() throws Exception {
        Jogador jogador = criarJogadorValido();
        List<Estatistica> estatisticas = List.of(criarEstatisticaValida(jogador));

        byte[] pdf = pdfService.gerarPdfJogador(jogador, estatisticas, 5);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    private Jogador criarJogadorValido() {
        Clube clube = new Clube();
        return new Jogador(new DadosCadastroJogador(
                "Jogador Teste",
                "12345678900",
                LocalDate.of(1990, 1, 1),
                190,
                85.5f,
                Posicao.ALA,
                clube.getId(),
                clube
        ));
    }

    private Estatistica criarEstatisticaValida(Jogador jogador) {
        Partida partida = new Partida();
        return new Estatistica(
                new DadosCadastroEstatistica(
                        jogador.getId(),
                        partida.getId(),
                        25,
                        3,
                        5,
                        7,
                        2,
                        1,
                        1
                ),
                jogador,
                partida
        );
    }
}