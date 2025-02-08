package ticket.ticket_consumer.Receiver;


import java.util.concurrent.CountDownLatch;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import ticket.ticket_consumer.TicketService;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;


public class Receiver {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Receiver.class);

    private final JsonParser jsonParser = new JsonParser();

    @Autowired
    TicketService ticketService;

    @KafkaListener(topics = "users")
    public void receive(@Payload String payload, Acknowledgment acknowledgment) {
        try {
            LOGGER.info("received payload='{}'", payload);

            JsonObject jsonObject = jsonParser.parse(payload).getAsJsonObject();
            String userId = jsonObject.get("userId").getAsString();
            String campaignName = jsonObject.get("campaignName").getAsString();
            String area = jsonObject.get("area").getAsString();
            int row = jsonObject.get("row").getAsInt();
            int column = jsonObject.get("column").getAsInt();

            String result = ticketService.addTicket(userId, campaignName, area, row, column);
            LOGGER.info("Processing result: {}", result);
            
            // Acknowledge the message after successful processing
            acknowledgment.acknowledge();

        } catch (Exception e) {
            LOGGER.error("Error processing message: {}", e.getMessage(), e);
            // Don't acknowledge - message will be retried
            throw e;
        }
    }
}
