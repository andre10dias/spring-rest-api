package com.github.andre10dias.spring_rest_api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testeslog")
public class TesteLogController {

    private Logger logger = LoggerFactory.getLogger(TesteLogController.class.getName());

    @GetMapping
    public String testeLog() {
        logger.trace("Teste log TRACE");
        logger.debug("Teste log DEBUG");
        logger.info("Teste log INFO");
        logger.warn("Teste log WARNING");
        logger.error("Teste log ERROR");
        return "Logs generated successfully";
    }

}
