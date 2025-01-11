package ticket.ticket_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import ticket.ticket_consumer.Receiver.Receiver;

@SpringBootApplication
public class TicketConsumerApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(TicketConsumerApplication.class, args);
//	}

	private static final Logger log = LoggerFactory.getLogger(TicketConsumerApplication.class);

	@Autowired
	private Receiver receiver;

	public static void main(String[] args) {
		SpringApplication.run(TicketConsumerApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return args -> {
			// Keep the application running and continuously receiving messages
			while(true) {
				try {
					log.info("Waiting for receiver...");
					// Wait for messages indefinitely
					receiver.getLatch().await();
					// Reset the latch for the next message
					receiver.resetLatch();
				} catch (InterruptedException e) {
					log.error("Interrupted while waiting for messages", e);
					Thread.currentThread().interrupt();
					break;
				}
			}
		};
	}
}


