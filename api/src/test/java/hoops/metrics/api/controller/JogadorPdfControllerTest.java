package hoops.metrics.api.controller;

import hoops.metrics.api.domain.*;
import hoops.metrics.api.dto.estatistica.DadosCadastroEstatistica;
import hoops.metrics.api.dto.jogador.DadosCadastroJogador;
import hoops.metrics.api.service.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JogadorPdfControllerTest {

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private JogadorPdfController jogadorPdfController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void exportarPdf_deveRetornarNotFoundQuandoJogadorNaoExiste() {
        when(pdfService.exportar(1L)).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<byte[]> response = jogadorPdfController.exportarPdf(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(pdfService).exportar(1L);
    }

    @Test
    void exportarPdf_deveRetornarPdfComSucesso() {
        byte[] pdfMock = new byte[10];
        when(pdfService.exportar(1L)).thenReturn(ResponseEntity.ok(pdfMock));

        ResponseEntity<byte[]> response = jogadorPdfController.exportarPdf(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(pdfMock, response.getBody());
        verify(pdfService).exportar(1L);
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
                        1
                ),
                jogador,
                partida
        );
    }
}