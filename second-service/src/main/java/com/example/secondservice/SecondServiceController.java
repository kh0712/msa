package com.example.secondservice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/second-service")
public class SecondServiceController {

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to Second Service";
    }


    @GetMapping("/message")
    public void message(@RequestHeader("second-request") String header){
        log.info(header);
    }

    @GetMapping("/check")
    public String check(){
        log.info("second");
        return "It is second service";
    }
}