package hoops.metrics.api.service;


import hoops.metrics.api.domain.Tecnico;
import hoops.metrics.api.dto.tecnico.DadosAtualizacaoTecnico;
import hoops.metrics.api.dto.tecnico.DadosCadastroTecnico;
import hoops.metrics.api.dto.tecnico.DadosDetalhamentoTecnico;
import hoops.metrics.api.dto.tecnico.DadosListagemTecnico;
import hoops.metrics.api.repository.TecnicoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TecnicoService {

    private TecnicoRepository tecnicoRepository;

    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    @Transactional
    public DadosDetalhamentoTecnico cadastrar(DadosCadastroTecnico dados) {
        if (tecnicoRepository.existsByCref(dados.cref())) {
            throw new IllegalArgumentException("CREF já cadastrado!");
        }

        Tecnico tecnico = new Tecnico(dados);
        tecnicoRepository.save(tecnico);
        return new DadosDetalhamentoTecnico(tecnico);
    }

    public Page<DadosListagemTecnico> listarAtivos(Pageable paginacao) {

        return tecnicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemTecnico::new);

    }

    @Transactional
    public DadosDetalhamentoTecnico atualizar(@Valid DadosAtualizacaoTecnico dados) {

        var tecnico = tecnicoRepository.getReferenceById(dados.id());
        tecnico.atualizarInformacoes(dados);
        return new DadosDetalhamentoTecnico(tecnico);

    }

    @Transactional
    public boolean excluir(Long id) {
        if(tecnicoRepository.existsById(id)) {
            var tecnico = tecnicoRepository.getReferenceById(id);
            tecnico.excluir();
            return true;
        }
        return false;
    }


    public int contarVitorias(Long tecnicoId) {

        return tecnicoRepository.contarVitorias(tecnicoId);

    }
}
