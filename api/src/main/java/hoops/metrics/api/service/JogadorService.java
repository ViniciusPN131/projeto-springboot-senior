package hoops.metrics.api.service;

import hoops.metrics.api.domain.Jogador;
import hoops.metrics.api.dto.clube.DadosDetalhamentoClube;
import hoops.metrics.api.dto.jogador.*;
import hoops.metrics.api.repository.ClubeRepository;
import hoops.metrics.api.repository.JogadorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JogadorService {

    @Autowired
    private ClubeRepository clubeRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Transactional
    public Jogador cadastrar(@Valid DadosPostJogador dadosPost) {

        var dados = validarJogador(dadosPost);

        if (dados != null) {

            var jogador = new Jogador(dados);

            jogadorRepository.save(jogador);

            return jogador;
        }

        return null;

    }

    public Page listar(Pageable paginacao) {

        return jogadorRepository.findAll(paginacao).map(DadosListagemJogador::new);

    }

    @Transactional
    public DadosDetalhamentoJogador atualizar(@Valid DadosAtualizacaoJogador dados) {

        var jogador = jogadorRepository.getReferenceById(dados.id());
        jogador.atualizarInformacoes(dados);
        return new DadosDetalhamentoJogador(jogador);

    }


    public DadosCadastroJogador validarJogador(@Valid DadosPostJogador dadosPost) {
        if (jogadorRepository.existsByCpf(dadosPost.cpf())) {
            return new DadosCadastroJogador(
                    dadosPost.nome(),
                    dadosPost.cpf(),
                    dadosPost.data_nascimento(),
                    dadosPost.altura(),
                    dadosPost.peso(),
                    dadosPost.posicao(),
                    clubeRepository.findById(dadosPost.clube_id()).get()
            );
        }
        return null;

    }

    public boolean excluir(Long id) {

        if (jogadorRepository.existsById(id)) {

            var jogador = jogadorRepository.getReferenceById(id);

            jogador.excluir();
            return true;
        }

        return false;

    }
}
