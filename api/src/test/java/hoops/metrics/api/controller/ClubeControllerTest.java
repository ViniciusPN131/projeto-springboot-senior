package hoops.metrics.api.controller;

import hoops.metrics.api.domain.clube.*;
import hoops.metrics.api.domain.tecnico.Tecnico;
import hoops.metrics.api.domain.tecnico.TecnicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClubeControllerTest {

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private TecnicoRepository tecnicoRepository;

    @InjectMocks
    private ClubeController clubeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarClubeComSucesso() {
        DadosCadastroClube dados = new DadosCadastroClube("Clube A", "CLUBE", "Cidade", "Estado", 1L, null);
        Tecnico tecnico = mock(Tecnico.class);
        Clube clube = new Clube(dados);
        clube.setTecnico(tecnico);

        when(tecnicoRepository.findById(1L)).thenReturn(Optional.of(tecnico));
        when(clubeRepository.save(any())).thenReturn(clube);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        ResponseEntity response = clubeController.cadastrarClube(dados, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        URI location = response.getHeaders().getLocation();
        assertNotNull(location);
        assertNotNull(response.getBody());
    }

    @Test
    void deveListarClubesAtivos() {
        Clube clube = mock(Clube.class);
        Page<Clube> page = new PageImpl<>(List.of(clube));
        Pageable pageable = PageRequest.of(0, 10);

        Tecnico tecnico = mock(Tecnico.class);

        when(clubeRepository.findAllByAtivoTrue(pageable)).thenReturn(page);

        when(clube.getTecnico()).thenReturn(tecnico);

        ResponseEntity<Page<DadosListagemClube>> response = clubeController.listarClubes(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void deveAtualizarClube() {
        DadosAtualizacaoClube dados = new DadosAtualizacaoClube(1L, "Clube", "Sigla", "Cidade", "Estado", mock(Tecnico.class));
        Clube clube = mock(Clube.class);

        when(clubeRepository.getReferenceById(1L)).thenReturn(clube);

        ResponseEntity response = clubeController.atualizarClube(dados);

        verify(clube).atualizarInformacoes(dados);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deveDeletarClube() {
        Clube clube = mock(Clube.class);

        when(clubeRepository.getReferenceById(1L)).thenReturn(clube);

        ResponseEntity response = clubeController.deletarClube(1L);

        verify(clube).excluir();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
