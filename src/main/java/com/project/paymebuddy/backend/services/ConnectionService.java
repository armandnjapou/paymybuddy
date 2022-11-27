package com.project.paymebuddy.backend.services;

import com.project.paymebuddy.backend.entities.Connection;
import com.project.paymebuddy.backend.entities.PayMyBuddyUser;

import java.util.Set;
public interface ConnectionService {

    Connection addConnection(PayMyBuddyUser sender, PayMyBuddyUser target);

    Set<Connection> getConnections(PayMyBuddyUser user);
}
