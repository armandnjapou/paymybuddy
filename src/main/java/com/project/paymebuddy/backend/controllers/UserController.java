package com.project.paymebuddy.backend.controllers;

import com.project.paymebuddy.backend.entities.Connection;
import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import com.project.paymebuddy.backend.exceptions.DomainException;
import com.project.paymebuddy.backend.repositories.ConnectionRepository;
import com.project.paymebuddy.backend.repositories.PayMyBuddyUserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

import static com.project.paymebuddy.backend.utils.Constants.API_V1_PATH;

@RestController
@RequestMapping(API_V1_PATH + "/users")
@SecurityRequirement(name = "backendapi")
public record UserController(PayMyBuddyUserRepository userRepository, ConnectionRepository connectionRepository) {

    @GetMapping("/")
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PayMyBuddyUser> getOne(@PathVariable Long userId) {
        return new ResponseEntity<>(userRepository.findById(userId).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "User not found...")), HttpStatus.OK);
    }

    @GetMapping("/{userId}/addConnection")
    public ResponseEntity<Object> addConnection(@PathVariable Long userId, @RequestParam String targetEmail) {

        PayMyBuddyUser sender = userRepository.findById(userId).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Sender not found!"));
        PayMyBuddyUser target = userRepository.findByUsername(targetEmail).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Target not found!"));

        if(!sender.getConnections().contains(target)) {
            Connection connection = new Connection();
            connection.setConnectionDate(LocalDateTime.now());
            connection.setSender(sender);
            connection.setTarget(target);
            connectionRepository.save(connection);
            sender.addConnection(target);

            PayMyBuddyUser updated = userRepository.save(sender);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else throw new DomainException(DomainException.Severity.LOGIC, DomainException.Code.BAD_REQUEST, "Target already connected");
    }

    @GetMapping("/{userId}/connections")
    public ResponseEntity<Object> getAllConnections(@PathVariable Long userId) {
        PayMyBuddyUser pmb = userRepository.findById(userId).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "User not found with id: " + userId));
        Set<PayMyBuddyUser> connections = pmb.getConnections();
        return ResponseEntity.ok(connections);
    }

}
