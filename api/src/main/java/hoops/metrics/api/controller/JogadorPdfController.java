package hoops.metrics.api.controller;

import hoops.metrics.api.domain.estatisticas.Estatistica;
import hoops.metrics.api.domain.estatisticas.EstatisticaRepository;
import hoops.metrics.api.domain.jogador.Jogador;
import hoops.metrics.api.domain.jogador.JogadorRepository;
import hoops.metrics.api.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jogadores")
@RequiredArgsConstructor
public class JogadorPdfController {

    private final JogadorRepository jogadorRepository;
    private final EstatisticaRepository estatisticaRepository;
    private final PdfService pdfService;

    @GetMapping("/{id}/exportar-pdf")
    public ResponseEntity<byte[]> exportarPdf(@PathVariable Long id) {
        Jogador jogador = jogadorRepository.findById(id).orElse(null);
        if (jogador == null) {
            return ResponseEntity.notFound().build();
        }

        List<Estatistica> estatisticasMvp = estatisticaRepository.buscarEstatisticasOndeJogadorFoiMvp(id);

        int quantidadeDeVitorias = jogadorRepository.quantidadeDeVitoriasDoJogador(id);

        try {
            byte[] pdf = pdfService.gerarPdfJogador(jogador, estatisticasMvp, quantidadeDeVitorias);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment().filename("jogador_mvp.pdf").build());
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
