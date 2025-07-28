package hoops.metrics.api.controller;

import hoops.metrics.api.domain.tecnico.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TecnicoControllerTest {

    @InjectMocks
    private TecnicoController tecnicoController;

    @Mock
    private TecnicoRepository tecnicoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarTecnicoComSucesso() {
        DadosCadastroTecnico dados = mock(DadosCadastroTecnico.class);
        Tecnico tecnico = new Tecnico(dados);
        when(tecnicoRepository.save(any(Tecnico.class))).thenReturn(tecnico);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        ResponseEntity<?> response = tecnicoController.cadastrarTecnico(dados, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deveListarTecnicos() {
        Tecnico tecnico = mock(Tecnico.class);
        Page<Tecnico> page = new PageImpl<>(List.of(tecnico));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));

        when(tecnicoRepository.findAllByAtivoTrue(pageable)).thenReturn(page);

        ResponseEntity<Page<DadosListagemTecnico>> response = tecnicoController.listarTecnicoes(pageable);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void deveAtualizarTecnico() {
        Long id = 1L;
        DadosAtualizacaoTecnico dados = mock(DadosAtualizacaoTecnico.class);
        when(dados.id()).thenReturn(id);

        Tecnico tecnico = mock(Tecnico.class);
        when(tecnicoRepository.getReferenceById(id)).thenReturn(tecnico);

        ResponseEntity<?> response = tecnicoController.atualizarTecnico(dados);

        verify(tecnico).atualizarInformacoes(dados);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void deveDeletarTecnico() {
        Long id = 1L;
        Tecnico tecnico = mock(Tecnico.class);
        when(tecnicoRepository.getReferenceById(id)).thenReturn(tecnico);

        ResponseEntity<?> response = tecnicoController.deletarTecnico(id);

        verify(tecnico).excluir();
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deveContarVitoriasPorTecnico() {
        when(tecnicoRepository.contarVitoriasPorTecnico(5L)).thenReturn(7L);

        ResponseEntity<Long> response = tecnicoController.contarVitoriasPorTecnico(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(7L, response.getBody());
    }

}