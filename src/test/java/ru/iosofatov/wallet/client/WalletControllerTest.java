package ru.iosofatov.wallet.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.iosofatov.wallet.client.api.controllers.WalletController;
import ru.iosofatov.wallet.client.api.dto.WalletDto;
import ru.iosofatov.wallet.client.api.service.WalletService;
import java.math.BigDecimal;
import java.util.UUID;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    void getBalance_shouldReturnBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(1500);

        when(walletService.getBalance(walletId)).thenReturn(balance);

        mockMvc.perform(get("/api/v1/wallets/{WALLET_UUID}", walletId))
                .andExpect(status().isOk())
                .andExpect(content().string(balance.toString()));
    }

    @Test
    void processTransaction_shouldReturnSuccessMessage() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletDto dto = new WalletDto(walletId, WalletDto.OperationType.DEPOSIT, BigDecimal.valueOf(1000));

        doNothing().when(walletService).processOperation(dto);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction Success!"));
    }
}

