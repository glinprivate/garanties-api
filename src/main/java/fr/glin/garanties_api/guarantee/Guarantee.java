package fr.glin.garanties_api.guarantee;

import java.math.BigDecimal;

public record Guarantee(String ref, String type, BigDecimal amount, String currency) {
}
