package com.ticketpurchaseapp.purchase.controller;

import com.ticketpurchaseapp.purchase.controller.PurchaseController;
import com.ticketpurchaseapp.purchase.dto.SeatCategoryInfo;
import com.ticketpurchaseapp.purchase.dto.SeatCategorySelection;
import com.ticketpurchaseapp.purchase.service.SeatService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import com.ticketpurchaseapp.purchase.common.exception.EventRegisterException;
import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.common.exception.PurchaseException;
import com.ticketpurchaseapp.purchase.common.exception.UserException;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private PurchaseController purchaseController;

    @Mock
    private SeatService seatService;

    @Test
    public void getSeatCategoryInfosForSpecificShow_EventNotFound() throws Exception {
        String eventId = "event1";
        String showId = "show1";
    
        List<SeatCategoryInfo> seatCategoryInfos = new ArrayList<>();
        seatCategoryInfos.add(new SeatCategoryInfo());
    
        Mockito.when(seatService.getSeatCategoryInfos(eventId, showId)).thenReturn(seatCategoryInfos);
    
        mockMvc.perform(MockMvcRequestBuilders.get("/purchase/{eventId}/{showId}/categories", eventId, showId))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
    

    @Test
    public void getSeatCategoryInfosForSpecificShow_EventRegisterException() throws Exception {
        String eventId = "event1";
        String showId = "show1";

        Mockito.when(seatService.getSeatCategoryInfos(eventId, showId))
                .thenThrow(new EventRegisterException("Invalid Event ID / Show ID"));

        mockMvc.perform(MockMvcRequestBuilders.get("/purchase/{eventId}/{showId}/categories", eventId, showId))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void saveSeatCategorySelection_InvalidArgsException() throws Exception {
        SeatCategorySelection form = new SeatCategorySelection();

        Mockito.when(seatService.saveSeatCategorySelectionForGroup(form)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/purchase/seat-category-selection")
                .contentType("application/json")
                .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    public void saveSeatCategorySelection_PurchaseException() throws Exception {
        SeatCategorySelection form = new SeatCategorySelection();

        Mockito.when(seatService.saveSeatCategorySelectionForGroup(form))
                .thenThrow(new PurchaseException("Purchase error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/purchase/seat-category-selection")
                .contentType("application/json")
                .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }
}
