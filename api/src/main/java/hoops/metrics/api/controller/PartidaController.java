package hoops.metrics.api.controller;

import hoops.metrics.api.domain.clube.ClubeRepository;
import hoops.metrics.api.domain.partida.*;
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

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarPartida(@RequestBody @Valid DadosCadastroPartida dados, UriComponentsBuilder uriBuilder) {
        var clubeCasa = clubeRepository.getReferenceById(dados.timeCasaId());
        var clubeVisitante = clubeRepository.getReferenceById(dados.timeVisitanteId());

        var partida = new Partida(dados, clubeCasa, clubeVisitante);
        partidaRepository.save(partida);

        var uri = uriBuilder.path("/partidas/{id}").buildAndExpand(partida.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPartida(partida));
    }


    @GetMapping
    public ResponseEntity<Page<DadosListagemPartida>> listarPartidas(@PageableDefault(size = 10, sort = {"data"}) Pageable paginacao) {
        var page = partidaRepository.findAllByAtivoTrue(paginacao).map(DadosListagemPartida::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarPartida(@RequestBody @Valid DadosAtualizacaoPartida dados) {
        var partida = partidaRepository.getReferenceById(dados.id());
        partida.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoPartida(partida));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarPartida(@PathVariable Long id) {
        var partida = partidaRepository.getReferenceById(id);
        partida.excluir();
        return ResponseEntity.noContent().build();
    }
}
