package ticket.ticket_consumer.entity;


import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("seat")
public class Seat {

    @PrimaryKey
    private SeatKey key;
    @Column(value = "price")
    private int price;
    @Column(value = "status")
    private String status;
    @Column(value = "id")
    private UUID id;

    public Seat(SeatKey key, int price, String status, UUID id) {
        this.key = key;
        this.price = price;
        this.status = status;
        this.id = id;
    }

    // Getters and Setters
    public SeatKey getKey() {
        return key;
    }

    public void setKey(SeatKey key) {
        this.key = key;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
