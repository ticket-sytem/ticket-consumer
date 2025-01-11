package ticket.ticket_consumer.repository;

import ticket.ticket_consumer.entity.Campaign;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CampaignRepository extends CrudRepository<Campaign, UUID> {
    Optional<Campaign> findById(UUID id);
    Campaign findByName(String name);
}
