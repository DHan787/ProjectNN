package edu.neu.projnn.projectnn.Controller;

import edu.neu.projnn.projectnn.Service.ConcurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessController {

    @Autowired
    private ConcurrencyService concurrencyService;

    @GetMapping("/request-access")
    public ResponseEntity<String> requestAccess(@RequestParam String sessionId) {
        boolean accessGranted = concurrencyService.requestAccess(sessionId);
        if (accessGranted) {
            return ResponseEntity.ok("Access granted");
        } else {
            return ResponseEntity.ok("Please wait, you are in the queue");
        }
    }

    @PostMapping("/release-access")
    public void releaseAccess() {
        concurrencyService.releaseAccess();
    }

    @GetMapping("/queue-size")
    public ResponseEntity<Integer> getQueueSize() {
        return ResponseEntity.ok(concurrencyService.getQueueSize());
    }
}
