package com.project.paymebuddy.backend.controllers;

import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import com.project.paymebuddy.backend.exceptions.DomainException;
import com.project.paymebuddy.backend.repositories.PayMyBuddyUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.project.paymebuddy.backend.utils.Constants.API_V1_PATH;

@RestController
@RequestMapping(API_V1_PATH + "/users")
public record UserController(PayMyBuddyUserRepository userRepository) {

    @GetMapping("/")
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PayMyBuddyUser> getOne(@PathVariable Long userId) {
        return new ResponseEntity<>(userRepository.findById(userId).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "User not found...")), HttpStatus.OK);
    }

    @GetMapping("/{userId}/addConnection")
    public ResponseEntity<Object> addConnection(@RequestParam Long userId, @RequestParam Long targetId) {
        /*Optional<PayMyBuddyUser> sender = userRepository.findById(userId);
        if (sender.isPresent()) {
            Optional<PayMyBuddyUser> target = userRepository.findById(targetId);
            if (target.isPresent()) {
                ConnectionKey con = new ConnectionKey();
                con.setConnectionDate(LocalDateTime.now());
                //con.setUser(sender.get());
                con.setTargetId(target.get().getId());
                ConnectionKey updated = connectionRepository.save(con);

                return new ResponseEntity<>(updated, HttpStatus.OK);
            } else
                throw new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Target not found!");
        } else
            throw new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Sender not found!");*/
        return null;
    }
}
