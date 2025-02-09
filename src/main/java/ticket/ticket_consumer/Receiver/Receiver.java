package ticket.ticket_consumer.Receiver;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import ticket.ticket_consumer.TicketService;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

public class Receiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
    private final JsonParser jsonParser = new JsonParser();

    @Autowired
    TicketService ticketService;

    @KafkaListener(
        topics = "users",
        containerFactory = "kafkaListenerContainerFactory",
        topicPartitions = {
            @TopicPartition(
                topic = "users",
                partitions = {"0", "1", "2"}  // Explicitly listen to all partitions
            )
        }
    )
    public void receive(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try {
            LOGGER.info("received payload='{}' from partition={}", record.value(), record.partition());
            
            JsonObject jsonObject = jsonParser.parse(record.value()).getAsJsonObject();
            String area = jsonObject.get("area").getAsString();
            
            // Log which partition handles which area
            LOGGER.info("Processing area {} on partition {}", area, record.partition());
            
            String userId = jsonObject.get("userId").getAsString();
            String campaignName = jsonObject.get("campaignName").getAsString();
            int row = jsonObject.get("row").getAsInt();
            int column = jsonObject.get("column").getAsInt();

            String result = ticketService.addTicket(userId, campaignName, area, row, column);
            LOGGER.info("Processing result: {} for partition {}", result, record.partition());
            
            acknowledgment.acknowledge();
        } catch (Exception e) {
            LOGGER.error("Error processing message: {}", e.getMessage(), e);
            throw e;
        }
    }
}
