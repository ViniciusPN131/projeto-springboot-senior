package hoops.metrics.api.service;

import hoops.metrics.api.domain.Estatistica;
import hoops.metrics.api.dto.estatistica.DadosAtualizacaoEstatistica;
import hoops.metrics.api.dto.estatistica.DadosCadastroEstatistica;
import hoops.metrics.api.dto.estatistica.DadosDetalhamentoEstatistica;
import hoops.metrics.api.dto.estatistica.DadosListagemEstatistica;
import hoops.metrics.api.repository.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstatisticaService {

    JogadorRepository jogadorRepository;
    PartidaRepository partidaRepository;
    EstatisticaRepository estatisticaRepository;

    public EstatisticaService(JogadorRepository jogadorRepository, PartidaRepository partidaRepository, EstatisticaRepository estatisticaRepository) {
        this.jogadorRepository = jogadorRepository;
        this.partidaRepository = partidaRepository;
        this.estatisticaRepository = estatisticaRepository;
    }

    @Transactional
    public DadosDetalhamentoEstatistica cadastrar(@Valid DadosCadastroEstatistica dados) {

        var jogador = jogadorRepository.getReferenceById(dados.jogadorId());
        var partida = partidaRepository.getReferenceById(dados.partidaId());

        var estatisticaCriada = new Estatistica(dados, jogador, partida);
        estatisticaRepository.save(estatisticaCriada);

        return new DadosDetalhamentoEstatistica(estatisticaCriada);

    }

    public Page<DadosListagemEstatistica> listar(Pageable paginacao) {

        return estatisticaRepository.findAll(paginacao).map(DadosListagemEstatistica::new);
    }


    public DadosDetalhamentoEstatistica atualizar(@Valid DadosAtualizacaoEstatistica dados) {

        Estatistica estatistica = estatisticaRepository.getReferenceById(dados.id());
        estatistica.atualizarInformacoes(dados);
        return new DadosDetalhamentoEstatistica(estatistica);

    }

    public void excluir(Long id) {

        estatisticaRepository.deleteById(id);

    }
}
