package hoops.metrics.api.controller;

import hoops.metrics.api.domain.clube.ClubeRepository;
import hoops.metrics.api.domain.tecnico.*;
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
@RequestMapping("tecnicos")
public class TecnicoController {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarTecnico(@RequestBody @Valid DadosCadastroTecnico dados, UriComponentsBuilder uriBuilder){
        var tecnico = new Tecnico(dados);
        tecnicoRepository.save(tecnico);

        var uri = uriBuilder.path("/tecnicoes/{id}").buildAndExpand(tecnico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoTecnico(tecnico));

    }

    @GetMapping("/{id}/vitorias")
    public ResponseEntity<Long> buscarVitoriasDoTecnico(@PathVariable Long id) {
        Long vitorias = tecnicoRepository.vitoriasDoTecnico(id);
        return ResponseEntity.ok(vitorias);
    }


    @GetMapping
    public ResponseEntity<Page<DadosListagemTecnico>> listarTecnicoes(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){

        var page = tecnicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemTecnico::new);

        return  ResponseEntity.ok(page);

    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarTecnico(@RequestBody @Valid DadosAtualizacaoTecnico dados){

        var tecnico = tecnicoRepository.getReferenceById(dados.id());
        tecnico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoTecnico(tecnico));

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarTecnico(@PathVariable Long id){

        var tecnico = tecnicoRepository.getReferenceById(id);
        tecnico.excluir();

        return ResponseEntity.noContent().build();

    }
    
}
