package cl.novabank.transferencias.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@RestController
@Profile("cloud")
public class ComprobanteController {
    private final S3Client s3;
    private final String bucket;

    public ComprobanteController(S3Client s3, @Value("${RECEIPTS_BUCKET}") String bucket) {
        this.s3 = s3;
        this.bucket = bucket;
    }

    @GetMapping("/api/transferencias/{id}/comprobante")
    public ResponseEntity<byte[]> obtener(@PathVariable long id) {
        try {
            byte[] contenido = s3.getObjectAsBytes(request ->
                    request.bucket(bucket).key("comprobantes/" + id + ".json")).asByteArray();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(contenido);
        } catch (NoSuchKeyException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
