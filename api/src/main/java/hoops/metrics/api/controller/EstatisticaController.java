package hoops.metrics.api.controller;

import hoops.metrics.api.domain.clube.DadosCadastroClube;
import hoops.metrics.api.domain.estatisticas.*;
import hoops.metrics.api.domain.jogador.JogadorRepository;
import hoops.metrics.api.domain.partida.PartidaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticaController {

    @Autowired
    private EstatisticaRepository estatisticaRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private PartidaRepository partidaRepository;

    @PostMapping
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity cadastrarEstatistica(@RequestBody @Valid DadosCadastroEstatistica dados, UriComponentsBuilder uriBuilder){

        var jogador = jogadorRepository.getReferenceById(dados.jogadorId());
        var partida = partidaRepository.getReferenceById(dados.partidaId());

        var estatistica = new Estatistica(dados, jogador, partida);
        estatisticaRepository.save(estatistica);

        var uri = uriBuilder.path("/estatisticas/{id}").buildAndExpand(estatistica.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoEstatistica(estatistica));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemEstatistica>> listar(
            @PageableDefault(size = 10, sort = {"id"}) Pageable paginacao) {

        var page = estatisticaRepository.findAll(paginacao).map(DadosListagemEstatistica::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoEstatistica> atualizar(
            @RequestBody @Valid DadosAtualizacaoEstatistica dados) {

        var estatistica = estatisticaRepository.getReferenceById(dados.id());
        estatistica.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoEstatistica(estatistica));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        estatisticaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
