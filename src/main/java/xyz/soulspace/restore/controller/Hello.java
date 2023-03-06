package xyz.soulspace.restore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin
@Controller
@Slf4j
@Tag(name = "Hello控制器(CinemaController)")
public class Hello {
    @Operation(summary = "Hello")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("hello");
    }
}
