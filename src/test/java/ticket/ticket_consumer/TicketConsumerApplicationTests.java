package ticket.ticket_consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ticket.ticket_consumer.config.TestKafkaConfig;

@SpringBootTest
@Import(TestKafkaConfig.class)
class TicketConsumerApplicationTests {

//	@Test
//	void contextLoads() {
//	}

}
