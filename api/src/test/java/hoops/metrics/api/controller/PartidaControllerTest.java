package hoops.metrics.api.controller;

import hoops.metrics.api.domain.Partida;
import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.dto.partida.*;
import hoops.metrics.api.service.PartidaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartidaControllerTest {

    @InjectMocks
    private PartidaController partidaController;

    @Mock
    private PartidaService partidaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarPartidaComSucesso() {
        DadosCadastroPartida dados = mock(DadosCadastroPartida.class);
        Partida partida = mock(Partida.class);
        Clube clubeCasa = mock(Clube.class);
        Clube clubeVisitante = mock(Clube.class);

        when(clubeCasa.getNome()).thenReturn("Clube A");
        when(clubeVisitante.getNome()).thenReturn("Clube B");
        when(partida.getClubeDaCasa()).thenReturn(clubeCasa);
        when(partida.getClubeVisitante()).thenReturn(clubeVisitante);

        DadosDetalhamentoPartida detalhamento = new DadosDetalhamentoPartida(partida);
        when(partidaService.cadastrar(dados)).thenReturn(detalhamento);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");
        ResponseEntity<?> response = partidaController.cadastrarPartida(dados, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals(detalhamento, response.getBody());
    }

    @Test
    void deveListarPartidas() {
        Partida partida = mock(Partida.class);
        Clube clubeCasa = mock(Clube.class);
        Clube clubeVisitante = mock(Clube.class);

        when(clubeCasa.getNome()).thenReturn("Clube X");
        when(clubeVisitante.getNome()).thenReturn("Clube Y");
        when(partida.getClubeDaCasa()).thenReturn(clubeCasa);
        when(partida.getClubeVisitante()).thenReturn(clubeVisitante);

        DadosListagemPartida dados = new DadosListagemPartida(partida);
        Page<DadosListagemPartida> page = new PageImpl<>(List.of(dados));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dataHora"));

        when(partidaService.listar(pageable)).thenReturn(page);

        ResponseEntity<Page<DadosListagemPartida>> response = partidaController.listarPartidas(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(dados, response.getBody().getContent().get(0));
    }

    @Test
    void deveAtualizarPartida() {
        DadosAtualizacaoPartida dados = mock(DadosAtualizacaoPartida.class);
        Partida partida = mock(Partida.class);
        Clube clubeCasa = mock(Clube.class);
        Clube clubeVisitante = mock(Clube.class);

        when(clubeCasa.getNome()).thenReturn("Clube 1");
        when(clubeVisitante.getNome()).thenReturn("Clube 2");
        when(partida.getClubeDaCasa()).thenReturn(clubeCasa);
        when(partida.getClubeVisitante()).thenReturn(clubeVisitante);

        DadosDetalhamentoPartida detalhamento = new DadosDetalhamentoPartida(partida);
        when(partidaService.atualizar(dados)).thenReturn(detalhamento);

        ResponseEntity<?> response = partidaController.atualizarPartida(dados);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(detalhamento, response.getBody());
    }

    @Test
    void deveDeletarPartida() {
        Long id = 1L;
        doNothing().when(partidaService).excluir(id);

        ResponseEntity<?> response = partidaController.deletarPartida(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(partidaService).excluir(id);
    }

    @Test
    void deveBuscarResultadoDaPartida() {
        DadosResultadoPartida resultado = mock(DadosResultadoPartida.class);
        when(partidaService.buscarResultado(99L)).thenReturn(resultado);

        ResponseEntity<DadosResultadoPartida> response = partidaController.buscarResultadoDaPartida(99L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultado, response.getBody());
    }
}