package ticket.ticket_consumer.Receiver;


import java.util.concurrent.CountDownLatch;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;



public class Receiver {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }

    @KafkaListener(topics = "users")
    public void receive(String payload, ConsumerRecord<?,?> cr) {
        try {
            LOGGER.info("received payload='{}'", payload);
            LOGGER.info("from partition={}, offset={}", cr.partition(), cr.offset());
        } catch (Exception e) {
            LOGGER.error("Error processing message", e);
        }
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}
