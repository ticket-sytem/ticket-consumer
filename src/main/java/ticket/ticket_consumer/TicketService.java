package ticket.ticket_consumer;

import ticket.ticket_consumer.entity.Campaign;
import ticket.ticket_consumer.entity.Seat;
import ticket.ticket_consumer.entity.Ticket;
import ticket.ticket_consumer.repository.CampaignRepository;
import ticket.ticket_consumer.repository.SeatRepository;
import ticket.ticket_consumer.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    CampaignRepository campaignRepository;

    private final static Logger log = LoggerFactory.getLogger(TicketService.class);

    public String addTicket(String userId, String campaignName, String area, int row, int column) {
        Seat seat = seatRepository.findByCampaignIdAndAreaAndRowAndColumn(campaignName, area, row, column);
        String seatId = String.valueOf(seat.getId());
        Ticket ticket = new Ticket(UUID.randomUUID(), userId, seatId, false, new Date());
        Ticket savedTicket = ticketRepository.save(ticket);
        //update seat status to occupied
        updateSeatStatus(seatId, "occupied");
        return String.format("save (%s)", savedTicket.getId());
    }

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
            //check paid or not
            List<Ticket> unpaidTickets = ticketRepository.findByPaid(false);
            List<Seat> updateSeat = new ArrayList<>();
            for (Ticket ticket : unpaidTickets) {
                Date date = ticket.getCreationDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MINUTE, 10);
                if (cal.getTime().before(new Date())) {
                    Optional<Seat> seat = seatRepository.findById(UUID.fromString(ticket.getSeatId()));
                    seat.get().setStatus("purchased");
                    updateSeat.add(seat.get());
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



