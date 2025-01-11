package ticket.ticket_consumer.repository;

import ticket.ticket_consumer.entity.Seat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository extends CrudRepository<Seat, UUID> {
    Optional<Seat> findById(UUID id);
    List<Seat> findByCampaignId(String campaignId);
    Seat findByCampaignIdAndAreaAndRowAndColumn(String campaignId, String area, int row, int column);
}
