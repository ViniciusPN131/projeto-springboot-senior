package hoops.metrics.api.controller;

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

@RestController
@RequestMapping("jogadores")
public class JogadorController {

    @Autowired
    private JogadorRepository jogadorRepository;
    private JogadorRepository clubeRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarJogador(@RequestBody @Valid DadosCadastroJogador dados, UriComponentsBuilder uriBuilder){
        var jogador = new Jogador(dados);
        jogadorRepository.save(jogador);

        var uri = uriBuilder.path("/jogadores/{id}").buildAndExpand(jogador.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoJogador(jogador));

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

    @GetMapping
    public ResponseEntity<Page<DadosListagemJogador>> listarJogadores(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){

        var page = jogadorRepository.findAll(paginacao).map(DadosListagemJogador::new);

        return  ResponseEntity.ok(page);

    }

}
