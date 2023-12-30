package kpi.ye.currencyexchange.exchangerate;

import lombok.Data;

import java.time.LocalDate;

@Data
class TimePeriod {
    private LocalDate oneWeek;
    private LocalDate twoWeeks;
    private LocalDate oneMonth;
    private LocalDate twoMonths;
    private LocalDate sixMonths;
    private LocalDate oneYear;
    private LocalDate twoYears;
    private LocalDate fiveYears;
    private LocalDate tenYears;

    TimePeriod() {
        LocalDate now = LocalDate.now();
        this.oneWeek = now.minusWeeks(1);
        this.twoWeeks = now.minusWeeks(2);
        this.oneMonth = now.minusMonths(1);
        this.twoMonths = now.minusMonths(2);
        this.sixMonths = now.minusMonths(6);
        this.oneYear = now.minusYears(1);
        this.twoYears = now.minusYears(2);
        this.fiveYears = now.minusYears(5);
        this.tenYears = now.minusYears(10);
    }
}
