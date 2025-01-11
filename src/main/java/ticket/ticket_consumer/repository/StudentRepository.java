package ticket.ticket_consumer.repository;

import java.util.Optional;
import java.util.UUID;

import ticket.ticket_consumer.entity.TestStudent;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<TestStudent, UUID> {
    Optional<TestStudent> findByName(String username);
}
