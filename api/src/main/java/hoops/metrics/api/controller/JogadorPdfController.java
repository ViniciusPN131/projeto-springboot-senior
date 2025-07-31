package hoops.metrics.api.controller;

import hoops.metrics.api.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jogadores")
@RequiredArgsConstructor
public class JogadorPdfController {
    private final PdfService pdfService;

    @GetMapping("/{id}/exportar-pdf")
    public ResponseEntity<byte[]> exportarPdf(@PathVariable Long id) {
        return pdfService.exportar(id);
    }
}
