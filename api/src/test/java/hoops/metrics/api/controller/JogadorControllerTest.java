
package hoops.metrics.api.controller;

import hoops.metrics.api.domain.clube.Clube;
import hoops.metrics.api.domain.clube.ClubeRepository;
import hoops.metrics.api.domain.jogador.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JogadorControllerTest {

    @InjectMocks
    private JogadorController jogadorController;

    @Mock
    private JogadorRepository jogadorRepository;

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private JogadorService jogadorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarJogadorComSucesso() {
        Clube clube = new Clube();
        DadosPostJogador dadosPost = mock(DadosPostJogador.class);
        DadosCadastroJogador dadosCadastro = new DadosCadastroJogador(
                "João Silva",
                LocalDate.of(2000, 1, 1),
                190,
                85.5f,
                Posicao.ARMADOR,
                clube
        );
        Jogador jogador = new Jogador(dadosCadastro);

        when(jogadorService.validarJogador(dadosPost)).thenReturn(dadosCadastro);
        when(jogadorRepository.save(any(Jogador.class))).thenReturn(jogador);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        ResponseEntity<?> response = jogadorController.cadastrarJogador(dadosPost, uriBuilder);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getHeaders().getLocation().toString().contains("/jogadores/"));
        assertNotNull(response.getBody());
    }

    @Test
    void deveListarJogadores() {
        Clube clube = new Clube();
        Jogador jogador = new Jogador(new DadosCadastroJogador(
                "Pedro Lima",
                LocalDate.of(1998, 3, 15),
                185,
                80.0f,
                Posicao.ALA_PIVO,
                clube
        ));
        Page<Jogador> page = new PageImpl<>(List.of(jogador));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));

        when(jogadorRepository.findAll(pageable)).thenReturn(page);

        ResponseEntity<Page<DadosListagemJogador>> response = jogadorController.listarJogadores(pageable);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void deveAtualizarJogador() {
        Long id = 1L;
        Clube clube = new Clube();
        Jogador jogadorExistente = new Jogador(new DadosCadastroJogador(
                "Lucas",
                LocalDate.of(1990, 6, 10),
                195,
                88.0f,
                Posicao.ALA_PIVO,
                clube
        ));

        DadosAtualizacaoJogador dadosAtualizacao = new DadosAtualizacaoJogador(
                id,
                "Lucas Atualizado",
                80,
                190,
                clube,
                Posicao.ALA_PIVO
        );

        when(jogadorRepository.getReferenceById(id)).thenReturn(jogadorExistente);

        ResponseEntity<?> response = jogadorController.atualizarJogador(dadosAtualizacao);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void deveDeletarJogador() {
        Long id = 1L;
        Clube clube = new Clube();
        Jogador jogador = new Jogador(new DadosCadastroJogador(
                "Marcelo",
                LocalDate.of(1992, 7, 22),
                192,
                86.0f,
                Posicao.ALA_PIVO,
                clube
        ));

        when(jogadorRepository.getReferenceById(id)).thenReturn(jogador);

        ResponseEntity<?> response = jogadorController.deletarJogador(id);

        assertEquals(204, response.getStatusCodeValue());
    }
}
