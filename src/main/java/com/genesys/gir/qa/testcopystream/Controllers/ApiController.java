package com.genesys.gir.qa.testcopystream.Controllers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.genesys.gir.qa.testcopystream.Models.ResponseContent;

@RestController
@RequestMapping(value = "/greeting")
public class ApiController {

    private Logger logger = LogManager.getLogger(ApiController.class);

    @GetMapping("/me")
    ResponseContent greeting(@RequestParam(name = "message", required = false, defaultValue = "Hello") String message) {
        return new ResponseContent(200, message);
    }

    @RequestMapping(value = "/*")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseContent others(){
        return new ResponseContent(404,"Not Found");
    }
}