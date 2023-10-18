package com.ticketpurchaseapp.purchase.util;

import java.util.ArrayList;
import java.util.List;

import com.ticketpurchaseapp.purchase.dto.Seat;

public class SeatAlgorithm {

    // assume that seatList is sorted by row_id, then seat_id
    public List<Seat> getSeatAllocation(List<Seat> seatList, int groupSize) {
        List<Seat> result = new ArrayList<>();
        List<Seat> row = new ArrayList<>();
        int currRow = seatList.get(0).getRowId();
        for (Seat seat : seatList) {
            if  (seat.getRowId() == currRow) {
                row.add(seat);
                if (row.size() >= groupSize) {
                    for (int i = 0; i < groupSize; i++) {
                        result.add(row.get(i));
                    }
                    return result;
                }
            } else {
                row = new ArrayList<>();
                row.add(seat);
                currRow = seat.getRowId();
            }
        }
        return null;
    }
}
