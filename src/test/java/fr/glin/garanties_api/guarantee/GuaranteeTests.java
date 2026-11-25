package fr.glin.garanties_api.guarantee;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class GuaranteeTests {

    private static final Guarantee BID_BOND =
            new Guarantee("GAR-2026-001", "BID_BOND", new BigDecimal("150000.00"), "EUR");

    @Test
    void exposesComponents() {
        assertThat(BID_BOND.ref()).isEqualTo("GAR-2026-001");
        assertThat(BID_BOND.type()).isEqualTo("BID_BOND");
        assertThat(BID_BOND.amount()).isEqualByComparingTo("150000");
        assertThat(BID_BOND.currency()).isEqualTo("EUR");
    }

    @Test
    void equalWhenAllComponentsMatch() {
        Guarantee same = new Guarantee("GAR-2026-001", "BID_BOND", new BigDecimal("150000.00"), "EUR");
        assertThat(BID_BOND).isEqualTo(same).hasSameHashCodeAs(same);
    }

    @Test
    void notEqualWhenAnyComponentDiffers() {
        assertThat(BID_BOND)
                .isNotEqualTo(new Guarantee("GAR-2026-002", "BID_BOND", new BigDecimal("150000.00"), "EUR"))
                .isNotEqualTo(new Guarantee("GAR-2026-001", "PERFORMANCE_BOND", new BigDecimal("150000.00"), "EUR"))
                .isNotEqualTo(new Guarantee("GAR-2026-001", "BID_BOND", new BigDecimal("1.00"), "EUR"))
                .isNotEqualTo(new Guarantee("GAR-2026-001", "BID_BOND", new BigDecimal("150000.00"), "USD"));
    }

    @Test
    void amountScaleMattersForEquality() {
        // BigDecimal.equals compares scale: 150000.00 and 150000 are different records.
        assertThat(BID_BOND).isNotEqualTo(new Guarantee("GAR-2026-001", "BID_BOND", new BigDecimal("150000"), "EUR"));
    }

    @Test
    void toStringContainsAllComponents() {
        assertThat(BID_BOND.toString()).contains("GAR-2026-001", "BID_BOND", "150000.00", "EUR");
    }
}
