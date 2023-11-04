package com.ticketpurchaseapp.purchase.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.ticketpurchaseapp.purchase.dto.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatAlgorithmTest {

    @Test
    public void testGetSeatAllocation() {
        // Create a list of Seat objects sorted by row_id and seat_id
        List<Seat> seatList = new ArrayList<>();
        Seat seat1 = new Seat();
        seat1.setRowId(1);
        seat1.setSeatId(1);

        Seat seat2 = new Seat();
        seat2.setRowId(1);
        seat2.setSeatId(2);

        Seat seat3 = new Seat();
        seat3.setRowId(1);
        seat3.setSeatId(3);

        Seat seat4 = new Seat();
        seat4.setRowId(2);
        seat4.setSeatId(1);

        Seat seat5 = new Seat();
        seat5.setRowId(2);
        seat5.setSeatId(2);

        Seat seat6 = new Seat();
        seat6.setRowId(3);
        seat6.setSeatId(1);

        Seat seat7 = new Seat();
        seat7.setRowId(3);
        seat7.setSeatId(2);

        Seat seat8 = new Seat();
        seat8.setRowId(4);
        seat8.setSeatId(1);

        Seat seat9 = new Seat();
        seat9.setRowId(4);
        seat9.setSeatId(2);

        seatList.add(seat1);
        seatList.add(seat2);
        seatList.add(seat3);
        seatList.add(seat4);
        seatList.add(seat5);
        seatList.add(seat6);
        seatList.add(seat7);
        seatList.add(seat8);
        seatList.add(seat9);

        // Create an instance of SeatAlgorithm
        SeatAlgorithm seatAlgorithm = new SeatAlgorithm();

        // Test with a group size of 3
        List<Seat> result = seatAlgorithm.getSeatAllocation(seatList, 3);

        // Verify that the result is not null
        assertNotNull(result);

        // Verify that the result contains the expected seats (in this case, the first 3 seats in row 1)
        assertEquals(3, result.size());
        assertEquals(seat1, result.get(0));
        assertEquals(seat2, result.get(1));
        assertEquals(seat3, result.get(2));

        // Test with a group size larger than available seats in a row
        result = seatAlgorithm.getSeatAllocation(seatList, 5);

        // Verify that the result is null because there are not enough seats in a row
        assertNull(result);

        // Test with an empty seatList
        result = seatAlgorithm.getSeatAllocation(new ArrayList<>(), 2);

        // Verify that the result is null because the seatList is empty
        assertNull(result);
    }
}
