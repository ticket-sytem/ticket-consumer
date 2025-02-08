package ticket.ticket_consumer;

import ticket.ticket_consumer.entity.Campaign;
import ticket.ticket_consumer.entity.Seat;
import ticket.ticket_consumer.entity.SeatKey;
import ticket.ticket_consumer.entity.Ticket;
import ticket.ticket_consumer.repository.CampaignRepository;
import ticket.ticket_consumer.repository.SeatRepository;
import ticket.ticket_consumer.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import com.datastax.driver.core.LocalDate;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    CampaignRepository campaignRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final static Logger log = LoggerFactory.getLogger(TicketService.class);

    public String addTicket(String userId, String campaignName, String area, int row, int column) {
        try {
            Campaign campaign = campaignRepository.findByName(campaignName);
            if (campaign == null) return "error: no campaign";

            log.info("get campaign: " + campaignName);

            String campaignId = String.valueOf(campaign.getId());
            Seat seat = seatRepository.findByKeyCampaignIdAndKeyAreaAndKeyRowAndKeyColumn(campaignId, area, row, column);
            if (seat == null) return "error: no seat";

            // Check if seat is already taken
            if (!"absent".equals(seat.getStatus())) {
                return "Seat is already taken";
            }

            log.info("get seat: " + seat.getKey());
            String seatId = String.valueOf(seat.getId());
            Ticket ticket = new Ticket(UUID.randomUUID(), userId, seatId, false, Instant.now());
            Ticket savedTicket = ticketRepository.save(ticket);

            log.info("saved ticket: " + savedTicket);
            //update seat status to occupied
            updateSeatStatus(seatId, "occupied");
            log.info("update seat: " + seatId);
            return String.format("save (%s)", savedTicket.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            return "error";
        }
    }
//    public String addTicket(String userId, String campaignName, String area, int row, int column) {
//        String lockKey = String.format("lock:seat:%s:%s:%d:%d", campaignName, area, row, column);
//        boolean locked = false;
//        try {
//            // Try to acquire distributed lock
//            locked = redisTemplate.opsForValue()
//                .setIfAbsent(lockKey, "locked", Duration.ofSeconds(10));
//            
//            if (!locked) {
//                return "Seat is being processed by another request";
//            }
//            
//            // Your existing ticket creation logic
//            Seat seat = seatRepository.findByCampaignIdAndAreaAndRowAndColumn(
//                campaignName, area, row, column);
//            
//            // Check if seat is already taken
//            if (!"absent".equals(seat.getStatus())) {
//                return "Seat is already taken";
//            }
//            
//            // Continue with ticket creation
//            String seatId = String.valueOf(seat.getId());
//            Ticket ticket = new Ticket(UUID.randomUUID(), userId, seatId, false, new Date());
//            Ticket savedTicket = ticketRepository.save(ticket);
//            updateSeatStatus(seatId, "occupied");
//            
//            return String.format("save (%s)", savedTicket.getId());
//            
//        } finally {
//            if (locked) {
//                redisTemplate.delete(lockKey);
//            }
//        }
//    }

    public String getTicket(UUID id) {
        StringBuilder result = new StringBuilder();
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            result.append(ticket.get().getId() + " " + ticket.get().isPaid());
            log.info("Ticket: {}", ticket.get().getId() + " " + ticket.get().isPaid());
        }
        return result.toString();
    }

    public String payTicket(String id) {
        try {
            StringBuilder result = new StringBuilder();
            Optional<Ticket> ticket = ticketRepository.findById(UUID.fromString(id));
            if (ticket.isPresent()) {
                ticket.get().setPaid(true);
                ticketRepository.save(ticket.get());
                updateSeatStatus(ticket.get().getSeatId(), "purchased");
            } else {
                throw new Exception("Error. Ticket#" + id + " is not found.");
            }
            return result.toString();
        } catch (Exception e) {
            log.error("payTicket", e.getMessage());
            return e.getMessage();
        }
    }

    public String releaseTicket(String id) {
        try {
            List<Ticket> unpaidTickets = ticketRepository.findByPaid(false);
            List<Seat> updateSeat = new ArrayList<>();
            LocalDateTime cutoffTime = LocalDateTime.now().minus(Duration.ofMinutes(10));
            
            for (Ticket ticket : unpaidTickets) {
                LocalDateTime creationTime = ticket.getCreationDate();
                // Check if ticket is older than 10 minutes
                if (creationTime.isBefore(cutoffTime)) {
                    Optional<Seat> seat = seatRepository.findById(UUID.fromString(ticket.getSeatId()));
                    seat.ifPresent(s -> {
                        s.setStatus("purchased");
                        updateSeat.add(s);
                    });
                }
            }
            seatRepository.saveAll(updateSeat);
            return "Release " + updateSeat.size() + " tickets.";
        } catch (Exception e) {
            log.error("releaseTicket", e.getMessage());
            return e.getMessage();
        }
    }

    private void updateSeatStatus(String seatId, String status) {
        Optional<Seat> seat = seatRepository.findById(UUID.fromString(seatId));
        if (seat.isPresent()) {
            seat.get().setStatus(status);
            seatRepository.save(seat.get());
        }
    }
}



