package kigali.clinic.rw.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kigali.clinic.rw.service.DashboardService;

@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping(value = "/summary")
    public ResponseEntity<Map<String, Long>> getSummary() {
        return new ResponseEntity<>(dashboardService.getSummary(), HttpStatus.OK);
    }
}
