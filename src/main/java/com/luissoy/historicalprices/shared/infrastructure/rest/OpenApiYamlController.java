package com.luissoy.historicalprices.shared.infrastructure.rest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class OpenApiYamlController {

    private static final String OPENAPI_PATH = "/openapi.yaml";

    @GetMapping(value = "/openapi.yaml", produces = "application/yaml")
    public ResponseEntity<byte[]> openapiYaml() throws IOException {
        Resource resource = new ClassPathResource(OPENAPI_PATH);

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(("{\"error\":\"No static resource openapi.yaml.\"}").getBytes(StandardCharsets.UTF_8));
        }

        byte[] content = resource.getInputStream().readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/yaml"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"openapi.yaml\"");
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}