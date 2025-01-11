package ticket.ticket_consumer.entity;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
public class Seat {
    @PrimaryKey
    private UUID id;
    private String area;
    private int row;
    private int column;
    private int price;
    private String status; //absent, occupied, purchased
    private String campaignId;

    public Seat(UUID id, String area, int row, int column, int price, String status, String campaignId) {
        this.id = id;
        this.area = area;
        this.row = row;
        this.column = column;
        this.price = price;
        this.status = status;
        this.campaignId = campaignId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
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

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }
}
