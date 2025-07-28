package hoops.metrics.api.controller;

import hoops.metrics.api.domain.estatisticas.*;
import hoops.metrics.api.domain.jogador.Jogador;
import hoops.metrics.api.domain.jogador.JogadorRepository;
import hoops.metrics.api.domain.partida.DadosResultadoPartida;
import hoops.metrics.api.domain.partida.Partida;
import hoops.metrics.api.domain.partida.PartidaRepository;
import jakarta.servlet.http.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class EstatisticaControllerTest {

    @Mock
    private EstatisticaRepository estatisticaRepository;

    @Mock
    private JogadorRepository jogadorRepository;

    @Mock
    private PartidaRepository partidaRepository;

    @InjectMocks
    private EstatisticaController estatisticaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarEstatisticaComSucesso() {
        DadosCadastroEstatistica dados = new DadosCadastroEstatistica(1L, 2L, 20, 5, 7, 2, 1, 2);
        Jogador jogador = mock(Jogador.class);
        Partida partida = mock(Partida.class);
        Estatistica estatistica = new Estatistica(dados, jogador, partida);

        when(jogadorRepository.getReferenceById(1L)).thenReturn(jogador);
        when(partidaRepository.getReferenceById(2L)).thenReturn(partida);
        when(estatisticaRepository.save(any())).thenReturn(estatistica);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        ResponseEntity<DadosDetalhamentoEstatistica> response = estatisticaController.cadastrarEstatistica(dados, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        URI location = response.getHeaders().getLocation();
        assertNotNull(location);
        assertNotNull(response.getBody());
    }

    @Test
    void deveListarEstatisticasPaginadas() {
        Estatistica estatistica = mock(Estatistica.class);
        Page<Estatistica> page = new PageImpl<>(List.of(estatistica));
        Pageable pageable = PageRequest.of(0, 10);

        Jogador jogador = mock(Jogador.class);
        Partida partida = mock(Partida.class);

        when(estatisticaRepository.findAll(pageable)).thenReturn(page);

        when(estatistica.getJogador()).thenReturn(jogador);
        when(estatistica.getPartida()).thenReturn(partida);

        ResponseEntity<Page<DadosListagemEstatistica>> response = estatisticaController.listar(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void deveAtualizarEstatistica() {
        DadosAtualizacaoEstatistica dados = new DadosAtualizacaoEstatistica(1L, 25, 6, 8, 3, 2, 1);
        Estatistica estatistica = mock(Estatistica.class);

        Jogador jogador = mock(Jogador.class);
        Partida partida = mock(Partida.class);

        when(estatisticaRepository.getReferenceById(1L)).thenReturn(estatistica);

        when(estatistica.getJogador()).thenReturn(jogador);
        when(estatistica.getPartida()).thenReturn(partida);

        ResponseEntity<DadosDetalhamentoEstatistica> response = estatisticaController.atualizar(dados);

        verify(estatistica).atualizarInformacoes(dados);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deveDeletarEstatistica() {
        doNothing().when(estatisticaRepository).deleteById(1L);

        ResponseEntity<Void> response = estatisticaController.deletar(1L);

        verify(estatisticaRepository).deleteById(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deveBuscarMvpDaPartida() {
        Estatistica estatistica = mock(Estatistica.class);
        when(estatisticaRepository.buscarMvpDaPartida(10L)).thenReturn(List.of(estatistica));

        ResponseEntity<List<Estatistica>> response = estatisticaController.buscarMvpDaPartida(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void deveContarVitoriasPorTecnico() {
        when(estatisticaRepository.contarVitoriasPorTecnico(5L)).thenReturn(7L);

        ResponseEntity<Long> response = estatisticaController.contarVitoriasPorTecnico(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(7L, response.getBody());
    }

    @Test
    void deveContarVitoriasPorClube() {
        when(estatisticaRepository.contarVitoriasPorClube(3L)).thenReturn(12L);

        ResponseEntity<Long> response = estatisticaController.contarVitoriasPorClube(3L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(12L, response.getBody());
    }

    @Test
    void deveBuscarResultadoDaPartida() {
        DadosResultadoPartida resultado = mock(DadosResultadoPartida.class);
        when(estatisticaRepository.buscarResultadosPartidas(99L)).thenReturn(resultado);

        ResponseEntity<DadosResultadoPartida> response = estatisticaController.buscarResultadoDaPartida(99L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultado, response.getBody());
    }

    @Test
    void deveBuscarEstatisticasGeraisPorJogador() {
        DadosGeraisEstatistica dados = mock(DadosGeraisEstatistica.class);
        Jogador jogador = mock(Jogador.class);

        when(estatisticaRepository.estatisticasGeraisPorJogador(1L)).thenReturn(dados);
        when(jogadorRepository.findById(1L)).thenReturn(Optional.of(jogador));

        ResponseEntity<DadosGeraisEstatistica> response = estatisticaController.estatisticasGeraisPorJogador(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dados, response.getBody());
    }
}
