package ticket.ticket_consumer.Receiver;


import java.util.concurrent.CountDownLatch;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import ticket.ticket_consumer.TicketService;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;


public class Receiver {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private final JsonParser jsonParser = new JsonParser();

    public CountDownLatch getLatch() {
        return latch;
    }

    @Autowired
    TicketService ticketService;

    @KafkaListener(topics = "users")
    public void receive(String payload, ConsumerRecord<?,?> cr) {
        try {
            LOGGER.info("received payload='{}'", payload);
            LOGGER.info("from partition={}, offset={}", cr.partition(), cr.offset());

            // Parse JSON payload using Gson
            JsonObject jsonObject = jsonParser.parse(payload).getAsJsonObject();
            String userId = jsonObject.get("userId").getAsString();
            String campaignName = jsonObject.get("campaignName").getAsString();
            String area = jsonObject.get("area").getAsString();
            int row = jsonObject.get("row").getAsInt();
            int column = jsonObject.get("column").getAsInt();

            ticketService.addTicket(userId, campaignName, area, row, column);

        } catch (Exception e) {
            LOGGER.error("Error processing message", e);
        }
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}
