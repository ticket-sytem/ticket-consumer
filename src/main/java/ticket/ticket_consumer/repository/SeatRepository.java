package ticket.ticket_consumer.repository;

import ticket.ticket_consumer.entity.Seat;
import ticket.ticket_consumer.entity.SeatKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository extends CrudRepository<Seat, SeatKey> {
    Optional<Seat> findById(UUID id);
    //    Optional<Seat> findById(UUID id);
//    Seat findByCampaignIdAndAreaAndRowAndColumn(String campaignId, String area, int row, int column);
    Seat findByKey(SeatKey key);
    Seat findByKeyCampaignIdAndKeyAreaAndKeyRowAndKeyColumn(String campaignId, String area, int row, int column);
}
