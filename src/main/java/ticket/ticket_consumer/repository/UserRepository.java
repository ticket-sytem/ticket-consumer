package ticket.ticket_consumer.repository;

import ticket.ticket_consumer.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findById(UUID id);
    Optional<User> findByName(String name);
}
