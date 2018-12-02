package com.genesys.gir.qa.testcopystream.Controllers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.genesys.gir.qa.testcopystream.Models.ResponseContent;
import com.genesys.gir.qa.testcopystream.Models.WebDavConfigComponent;

@RestController
@Configuration
@EnableConfigurationProperties(WebDavConfigComponent.class)
public class ApiController {
    @Autowired
    WebDavConfigComponent webDavConfigComponent;

    Logger logger = LogManager.getLogger(ApiController.class);

    @GetMapping("/greeting")
    @ResponseBody
    ResponseContent greeting(@RequestParam(name = "message", required = false, defaultValue = "Hello") String message) {
        logger.info("webdav host:" + webDavConfigComponent.getHost());
        logger.info("webdav port:" + webDavConfigComponent.getPort());
        return new ResponseContent(200, message);
    }

    @RequestMapping(value = "/*")
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseContent others(){
        return new ResponseContent(404,"Not Found");
    }
}