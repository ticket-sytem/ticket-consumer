package ticket.ticket_consumer.entity;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.UUID;

@PrimaryKeyClass
public class SeatKey implements Serializable {

    @PrimaryKeyColumn(name = "campaign_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String campaignId;

    @PrimaryKeyColumn(name = "area", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String area;

    @PrimaryKeyColumn(name = "row", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private int row;

    @PrimaryKeyColumn(name = "column", ordinal = 3, type = PrimaryKeyType.PARTITIONED)
    private int column;

    public SeatKey(String campaignId, String area, int row, int column) {
        this.campaignId = campaignId;
        this.area = area;
        this.row = row;
        this.column = column;
    }

    // Getters and Setters
    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
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
}

