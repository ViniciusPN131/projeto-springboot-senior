package hoops.metrics.api.controller;

import hoops.metrics.api.repository.EstatisticaRepository;
import hoops.metrics.api.repository.JogadorRepository;
import hoops.metrics.api.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jogadores")
@RequiredArgsConstructor
public class JogadorPdfController {
    private final JogadorRepository jogadorRepository;
    private final EstatisticaRepository estatisticaRepository;
    private final PdfService pdfService;  // Usar a instância injetada

    @GetMapping("/{id}/exportar-pdf")
    public ResponseEntity<byte[]> exportarPdf(@PathVariable Long id) {
        return pdfService.exportar(id);
    }
}
