package fr.glin.garanties_api.guarantee;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guarantees")
public class GuaranteeController {

    private static final List<Guarantee> GUARANTEES = List.of(
            new Guarantee("GAR-2026-001", "BID_BOND", new BigDecimal("150000.00"), "EUR"),
            new Guarantee("GAR-2026-002", "PERFORMANCE_BOND", new BigDecimal("500000.00"), "USD"),
            new Guarantee("GAR-2026-0044", "ADVANCE_PAYMENT", new BigDecimal("75000.00"), "GBP"));

    @GetMapping
    public List<Guarantee> findAll() {
        return GUARANTEES;
    }
}
