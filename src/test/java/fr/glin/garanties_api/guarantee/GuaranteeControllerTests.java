package fr.glin.garanties_api.guarantee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GuaranteeController.class)
class GuaranteeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsThreeGuarantees() throws Exception {
        mockMvc.perform(get("/api/guarantees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].ref").value("GAR-2026-001"))
                .andExpect(jsonPath("$[0].amount").value(150000.00))
                .andExpect(jsonPath("$[0].currency").value("EUR"));
    }

    @ParameterizedTest
    @CsvSource({
            "0, GAR-2026-001,  BID_BOND,         150000.00, EUR",
            "1, GAR-2026-002,  PERFORMANCE_BOND, 500000.00, USD",
            "2, GAR-2026-0044, ADVANCE_PAYMENT,  75000.00,  GBP"
    })
    void returnsEveryGuaranteeWithAllFields(int index, String ref, String type, double amount, String currency)
            throws Exception {
        String item = "$[" + index + "]";
        mockMvc.perform(get("/api/guarantees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(item + ".ref").value(ref))
                .andExpect(jsonPath(item + ".type").value(type))
                .andExpect(jsonPath(item + ".amount").value(amount))
                .andExpect(jsonPath(item + ".currency").value(currency));
    }

    @Test
    void exposesOnlyTheRecordFields() throws Exception {
        mockMvc.perform(get("/api/guarantees"))
                .andExpect(jsonPath("$[0].length()").value(4));
    }

    @Test
    void respondsWithJson() throws Exception {
        mockMvc.perform(get("/api/guarantees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void refsAreUnique() {
        List<Guarantee> guarantees = new GuaranteeController().findAll();
        assertThat(guarantees).extracting(Guarantee::ref).doesNotHaveDuplicates();
    }

    @Test
    void returnedListIsImmutable() {
        List<Guarantee> guarantees = new GuaranteeController().findAll();
        assertThat(guarantees).isUnmodifiable();
    }

    @Test
    void rejectsPost() throws Exception {
        mockMvc.perform(post("/api/guarantees").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void rejectsDelete() throws Exception {
        mockMvc.perform(delete("/api/guarantees"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void unknownPathIsNotFound() throws Exception {
        mockMvc.perform(get("/api/guarantees/GAR-2026-001"))
                .andExpect(status().isNotFound());
    }
}
