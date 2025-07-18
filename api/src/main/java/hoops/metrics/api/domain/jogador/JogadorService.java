package hoops.metrics.api.domain.jogador;

import hoops.metrics.api.domain.clube.ClubeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JogadorService {

    @Autowired
    private ClubeRepository clubeRepository;

    public DadosCadastroJogador validarJogador(@Valid DadosPostJogador dadosPost) {

        return new DadosCadastroJogador(
                dadosPost.nome(),
                dadosPost.data_nascimento(),
                dadosPost.altura(),
                dadosPost.peso(),
                dadosPost.posicao(),
                clubeRepository.findById(dadosPost.clube_id()).get()
        );

    }
}
