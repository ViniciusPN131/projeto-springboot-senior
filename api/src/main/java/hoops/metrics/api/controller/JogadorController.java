package hoops.metrics.api.controller;

import hoops.metrics.api.domain.clube.ClubeRepository;
import hoops.metrics.api.domain.estatisticas.DadosGeraisEstatistica;
import hoops.metrics.api.domain.estatisticas.Estatistica;
import hoops.metrics.api.domain.jogador.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("jogadores")
public class JogadorController {

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private ClubeRepository clubeRepository;

    @Autowired
    private JogadorService jogadorService;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarJogador(@RequestBody @Valid DadosPostJogador dadosPost, UriComponentsBuilder uriBuilder){

        var dados = jogadorService.validarJogador(dadosPost);

        var jogador = new Jogador(dados);

        jogadorRepository.save(jogador);

        var uri = uriBuilder.path("/jogadores/{id}").buildAndExpand(jogador.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoJogador(jogador));

    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemJogador>> listarJogadores(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){

        var page = jogadorRepository.findAll(paginacao).map(DadosListagemJogador::new);

        return  ResponseEntity.ok(page);

    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarJogador(@RequestBody @Valid DadosAtualizacaoJogador dados){

        var jogador = jogadorRepository.getReferenceById(dados.id());
        jogador.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoJogador(jogador));

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarJogador(@PathVariable Long id){

        var jogador = jogadorRepository.getReferenceById(id);
        jogador.excluir();

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/jogador/mvp/{partidaId}")
    public ResponseEntity<List<Estatistica>> buscarMvpDaPartida(@PathVariable Long partidaId) {
        var mvps = jogadorRepository.buscarMvpDaPartida(partidaId);
        return ResponseEntity.ok(mvps);
    }

    @GetMapping("/jogador/geral/{jogadorId}")
    public ResponseEntity<DadosGeraisEstatistica> estatisticasGeraisPorJogador(@PathVariable Long jogadorId) {
        DadosGeraisEstatistica resultado = jogadorRepository.estatisticasGeraisPorJogador(jogadorId);
        var jogador = jogadorRepository.findById(jogadorId).orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        return ResponseEntity.ok(resultado);
    }

}
