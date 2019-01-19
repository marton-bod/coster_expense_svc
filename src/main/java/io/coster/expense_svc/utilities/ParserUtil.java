package io.coster.expense_svc.utilities;

import lombok.Builder;
import lombok.Getter;

public class ParserUtil {

    public static YearMonthParseResult parseYearAndMonth(String param) {
        try {
            String[] yearAndMonth = param.split("-");
            int y = Integer.parseInt(yearAndMonth[0]);
            int m = Integer.parseInt(yearAndMonth[1]);
            if (2019 <= y && y <= 2025 && 1 <= m && m <= 12) {
                return YearMonthParseResult.builder()
                        .isValid(true)
                        .year(y)
                        .month(m)
                        .build();
            }
        } catch (Exception e) {
        }
        return YearMonthParseResult.builder().isValid(false).build();
    }

    @Getter
    @Builder
    public static class YearMonthParseResult {
        boolean isValid;
        int year;
        int month;
    }
}
