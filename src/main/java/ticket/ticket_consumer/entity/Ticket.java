package ticket.ticket_consumer.entity;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("ticket")
public class Ticket {
    @PrimaryKey
    private UUID id;

    @Column("userid")
    private String userId;

    @Column("seatid")
    private String seatId;

    @Column("paid")
    private boolean paid;

    @Column("creationdate")
    private LocalDateTime creationDate;

    public Ticket(UUID id, String userId, String seatId, boolean paid, Instant creationTime) {
        this.id = id;
        this.userId = userId;
        this.seatId = seatId;
        this.paid = paid;
        this.creationDate = LocalDateTime.ofInstant(creationTime, java.time.ZoneOffset.UTC);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
