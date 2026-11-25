package fr.glin.garanties_api.guarantee;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GuaranteeController.class)
class GuaranteeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsThreeGuarantees() throws Exception {
        mockMvc.perform(get("/api/guarantees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].ref").value("GAR-2026-001"))
                .andExpect(jsonPath("$[0].amount").value(150000.00))
                .andExpect(jsonPath("$[0].currency").value("EUR"));
    }
}
