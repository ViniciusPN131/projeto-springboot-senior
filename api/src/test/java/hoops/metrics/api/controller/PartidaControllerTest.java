package hoops.metrics.api.controller;

import hoops.metrics.api.domain.clube.Clube;
import hoops.metrics.api.domain.clube.ClubeRepository;
import hoops.metrics.api.domain.partida.*;
import hoops.metrics.api.domain.tecnico.Tecnico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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
    private PartidaRepository partidaRepository;

    @Mock
    private ClubeRepository clubeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarPartidaComSucesso() {
        Clube clubeCasa = new Clube();
        Clube clubeVisitante = new Clube();

        DadosCadastroPartida dados = mock(DadosCadastroPartida.class);
        when(dados.timeCasaId()).thenReturn(1L);
        when(dados.timeVisitanteId()).thenReturn(2L);

        when(clubeRepository.getReferenceById(1L)).thenReturn(clubeCasa);
        when(clubeRepository.getReferenceById(2L)).thenReturn(clubeVisitante);

        Partida partida = new Partida(dados, clubeCasa, clubeVisitante);
        when(partidaRepository.save(any(Partida.class))).thenReturn(partida);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        ResponseEntity<?> response = partidaController.cadastrarPartida(dados, uriBuilder);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void deveListarPartidas() {
        Tecnico tecnico = new Tecnico();
        Clube clubeCasa = new Clube(1L, "Nome", "Sigla", "Cidade", "Estado", true, tecnico);
        Clube clubeVisitante = new Clube(2L, "Nome1", "Sigla1", "Cidade", "Estado", true, tecnico);

        Partida partida = new Partida(
                1L,
                "Ginásio Central",
                LocalDateTime.now(),
                true,
                clubeCasa,
                clubeVisitante
        );

        Page<Partida> page = new PageImpl<>(List.of(partida));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("data"));

        when(partidaRepository.findAllByAtivoTrue(pageable)).thenReturn(page);

        ResponseEntity<Page<DadosListagemPartida>> response = partidaController.listarPartidas(pageable);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }


    @Test
    void deveAtualizarPartida() {
        Long id = 1L;
        DadosAtualizacaoPartida dados = mock(DadosAtualizacaoPartida.class);
        when(dados.id()).thenReturn(id);
        when(dados.local()).thenReturn("Ginásio Central");
        when(dados.dataHora()).thenReturn(LocalDateTime.now());
        when(dados.timeCasaId()).thenReturn(10L);
        when(dados.timeVisitanteId()).thenReturn(20L);

        Partida partida = new Partida(); // ou use um mock se preferir
        when(partidaRepository.getReferenceById(id)).thenReturn(partida);

        ResponseEntity<?> response = partidaController.atualizarPartida(dados);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }


    @Test
    void deveDeletarPartida() {
        Long id = 1L;
        Partida partida = mock(Partida.class);
        when(partidaRepository.getReferenceById(id)).thenReturn(partida);

        ResponseEntity<?> response = partidaController.deletarPartida(id);

        verify(partida).excluir();
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deveBuscarResultadoDaPartida() {
        DadosResultadoPartida resultado = mock(DadosResultadoPartida.class);
        when(partidaRepository.buscarResultadosPartidas(99L)).thenReturn(resultado);

        ResponseEntity<DadosResultadoPartida> response = partidaController.buscarResultadoDaPartida(99L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultado, response.getBody());
    }

}
