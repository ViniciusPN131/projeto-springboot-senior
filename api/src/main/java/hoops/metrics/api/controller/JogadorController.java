package hoops.metrics.api.controller;

import hoops.metrics.api.dto.jogador.*;
import hoops.metrics.api.dto.partida.DadosMvpPartida;
import hoops.metrics.api.repository.ClubeRepository;
import hoops.metrics.api.repository.JogadorRepository;
import hoops.metrics.api.service.JogadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequestMapping("jogadores")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class JogadorController {

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private ClubeRepository clubeRepository;

    @Autowired
    private JogadorService jogadorService;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarJogador(@RequestBody @Valid DadosCadastroJogador dados, UriComponentsBuilder uriBuilder){

        DadosDetalhamentoJogador jogadorCriado = jogadorService.cadastrar(dados);

        if (jogadorCriado==null) return ResponseEntity.badRequest().build();

        var uri = uriBuilder.path("/jogadores/{id}").buildAndExpand(jogadorCriado.id()).toUri();
        return ResponseEntity.created(uri).body(jogadorCriado);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemJogador>> listarJogadores(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){

        Page page = jogadorService.listarAtivos(paginacao);

        return  ResponseEntity.ok(page);

    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarJogador(@RequestBody @Valid DadosAtualizacaoJogador dados){

        DadosDetalhamentoJogador clubeAtualizado = jogadorService.atualizar(dados);
        return ResponseEntity.ok(clubeAtualizado);

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarJogador(@PathVariable Long id){

        if (jogadorService.excluir(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/jogador/mvp/{partidaId}")
    public ResponseEntity<List<DadosMvpPartida>> buscarMvpDaPartida(@PathVariable Long partidaId) {
        List<DadosMvpPartida> mvps = jogadorRepository.buscarMvpDaPartida(partidaId);
        return ResponseEntity.ok(mvps);
    }

    @GetMapping("/jogador/geral/{jogadorId}")
    public ResponseEntity<DadosGeraisJogador> estatisticasGeraisPorJogador(@PathVariable Long jogadorId) {
        if(!jogadorRepository.existsById(jogadorId)) throw new RuntimeException("Jogador não encontrado");
        DadosGeraisJogador resultado = jogadorRepository.estatisticasGeraisPorJogador(jogadorId);
        if (resultado==null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(resultado);
    }

}
