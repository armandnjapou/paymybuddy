package com.project.paymebuddy.backend.controllers;

import com.project.paymebuddy.backend.entities.Connection;
import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import com.project.paymebuddy.backend.exceptions.DomainException;
import com.project.paymebuddy.backend.repositories.ConnectionRepository;
import com.project.paymebuddy.backend.repositories.PayMyBuddyUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.project.paymebuddy.backend.utils.Constants.API_V1_PATH;

@RestController
@RequestMapping(API_V1_PATH + "/users")
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
    public ResponseEntity<Object> addConnection(@PathVariable Long userId, @RequestParam Long targetId) {
        Optional<PayMyBuddyUser> optionalSender = userRepository.findById(userId);
        if (optionalSender.isPresent()) {
            Optional<PayMyBuddyUser> optionalTarget = userRepository.findById(targetId);
            if (optionalTarget.isPresent()) {
                Connection connection = new Connection();
                connection.setConnectionDate(LocalDateTime.now());
                connection.setSender(optionalSender.get());
                connection.setTarget(optionalTarget.get());
                connectionRepository.save(connection);

                optionalSender.get().addConnection(optionalTarget.get());
                PayMyBuddyUser updated = userRepository.save(optionalSender.get());
                return new ResponseEntity<>(updated, HttpStatus.OK);
            } else
                throw new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Target not found!");
        } else
            throw new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Sender not found!");
    }
}
