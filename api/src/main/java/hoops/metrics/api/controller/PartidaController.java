package hoops.metrics.api.controller;

import hoops.metrics.api.dto.partida.*;
import hoops.metrics.api.repository.ClubeRepository;
import hoops.metrics.api.repository.PartidaRepository;
import hoops.metrics.api.service.PartidaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("partidas")
public class PartidaController {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private ClubeRepository clubeRepository;

    private final PartidaService partidaService;

    @Autowired
    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarPartida(@RequestBody @Valid DadosCadastroPartida dados, UriComponentsBuilder uriBuilder) {
        DadosDetalhamentoPartida partidaCriada = partidaService.cadastrar(dados);

        var uri = uriBuilder.path("/partidas/{id}").buildAndExpand(partidaCriada.id()).toUri();
        return ResponseEntity.created(uri).body(partidaCriada);
    }


    @GetMapping
    public ResponseEntity<Page<DadosListagemPartida>> listarPartidas(@PageableDefault(size = 10, sort = {"dataHora"}) Pageable paginacao) {
        Page<DadosListagemPartida> page = partidaService.listar(paginacao);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarPartida(@RequestBody @Valid DadosAtualizacaoPartida dados) {
        DadosDetalhamentoPartida partidaAtualizada = partidaService.atualizar(dados);
        return ResponseEntity.ok(partidaAtualizada);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarPartida(@PathVariable Long id) {
        partidaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resultado/{partidaId}")
    public ResponseEntity<DadosResultadoPartida> buscarResultadoDaPartida(@PathVariable Long partidaId) {
        DadosResultadoPartida resultado = partidaService.buscarResultado(partidaId);
        return ResponseEntity.ok(resultado);
    }

}
