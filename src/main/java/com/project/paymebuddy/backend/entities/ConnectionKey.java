package com.project.paymebuddy.backend.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class ConnectionKey implements Serializable {

    @Column(name = "sender_id")
    Long senderId;

    @Column(name = "target_id")
    Long targetId;
}
