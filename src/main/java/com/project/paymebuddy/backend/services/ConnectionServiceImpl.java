package com.project.paymebuddy.backend.services;

import com.project.paymebuddy.backend.entities.Connection;
import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import com.project.paymebuddy.backend.exceptions.DomainException;
import com.project.paymebuddy.backend.repositories.ConnectionRepository;
import com.project.paymebuddy.backend.repositories.PayMyBuddyUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public record ConnectionServiceImpl(
        ConnectionRepository connectionRepository, PayMyBuddyUserRepository userRepository) implements ConnectionService {

    @Override
    public Connection addConnection(PayMyBuddyUser sender, PayMyBuddyUser target) {

        if(!isUserAlreadyConnected(getConnections(sender), target)) {
            Connection connection = new Connection();
            connection.setConnectionDate(LocalDateTime.now());
            connection.setTarget(target);
            connection.setSender(sender);

            return connectionRepository.save(connection);
        } else throw new DomainException(DomainException.Severity.LOGIC, DomainException.Code.BAD_REQUEST, "Target already connected");
    }

    @Override
    public Set<Connection> getConnections(PayMyBuddyUser user) {
        Set<Connection> connections = connectionRepository.findBySender(user);
        Set<Connection> receivedConnections = connectionRepository.findByTarget(user);
        connections.addAll(receivedConnections);

        return connections;
    }

    static boolean isUserAlreadyConnected(Set<Connection> connections, PayMyBuddyUser user) {
        List<String> usernames =  connections.stream().map(connection -> connection.getSender().getUsername()).toList();
        List<String> targetUsernames = connections.stream().map(connection -> connection.getTarget().getUsername()).toList();
        return usernames.contains(user.getUsername()) || targetUsernames.contains(user.getUsername());
    }
}
