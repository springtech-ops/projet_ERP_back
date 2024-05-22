package org.springtech.springmarket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
    // Default statistics
    private int totalCustomers;
    private int totalInvoices;
    private double totalBilled;
    private double totalBenefit;
    private int totalUnpaid;
    private double totalUnpaidAmount;

    // Date specific
    private int totalCustomersDate;
    private int totalInvoicesDate;
    private double totalBilledDate;
    private double totalBenefitDate;
    private int totalUnpaidDate;
    private double totalUnpaidAmountDate;

    // Month Year specific
    private int totalCustomersMonthYear;
    private int totalInvoicesMonthYear;
    private double totalBilledMonthYear;
    private double totalBenefitMonthYear;
    private int totalUnpaidMonthYear;
    private double totalUnpaidAmountMonthYear;

    // Year specific
    private int totalCustomersYear;
    private int totalInvoicesYear;
    private double totalBilledYear;
    private double totalBenefitYear;
    private int totalUnpaidYear;
    private double totalUnpaidAmountYear;

    // Last 7 Days specific
    private int totalCustomersLastDays;
    private int totalInvoicesLastDays;
    private double totalBilledLastDays;
    private double totalBenefitLastDays;
    private int totalUnpaidLastDays;
    private double totalUnpaidAmountLastDays;

    // Constructeurs pour chaque groupe de données
    public Stats(double totalUnpaidAmount, int totalCustomers, int totalInvoices, double totalBilled, double totalBenefit, int totalUnpaid) {
        this.totalCustomers = totalCustomers;
        this.totalInvoices = totalInvoices;
        this.totalBilled = totalBilled;
        this.totalBenefit = totalBenefit;
        this.totalUnpaid = totalUnpaid;
        this.totalUnpaidAmount = totalUnpaidAmount;
    }

    public Stats(int totalCustomersDate, double totalUnpaidAmountDate, int totalInvoicesDate, double totalBilledDate, double totalBenefitDate, int totalUnpaidDate) {
        this.totalCustomersDate = totalCustomersDate;
        this.totalInvoicesDate = totalInvoicesDate;
        this.totalBilledDate = totalBilledDate;
        this.totalBenefitDate = totalBenefitDate;
        this.totalUnpaidDate = totalUnpaidDate;
        this.totalUnpaidAmountDate = totalUnpaidAmountDate;
    }

    public Stats(int totalCustomersMonthYear, int totalInvoicesMonthYear, double totalUnpaidAmountMonthYear, double totalBilledMonthYear, double totalBenefitMonthYear, int totalUnpaidMonthYear) {
        this.totalCustomersMonthYear = totalCustomersMonthYear;
        this.totalInvoicesMonthYear = totalInvoicesMonthYear;
        this.totalBilledMonthYear = totalBilledMonthYear;
        this.totalBenefitMonthYear = totalBenefitMonthYear;
        this.totalUnpaidMonthYear = totalUnpaidMonthYear;
        this.totalUnpaidAmountMonthYear = totalUnpaidAmountMonthYear;
    }

    public Stats(int totalCustomersYear, int totalInvoicesYear, int totalUnpaidYear, double totalBilledYear, double totalBenefitYear, double totalUnpaidAmountYear) {
        this.totalCustomersYear = totalCustomersYear;
        this.totalInvoicesYear = totalInvoicesYear;
        this.totalBilledYear = totalBilledYear;
        this.totalBenefitYear = totalBenefitYear;
        this.totalUnpaidYear = totalUnpaidYear;
        this.totalUnpaidAmountYear = totalUnpaidAmountYear;
    }

    public Stats(int totalCustomersLastDays, double totalBilledLastDays, int totalInvoicesLastDays, double totalBenefitLastDays, int totalUnpaidLastDays, double totalUnpaidAmountLastDays) {
        this.totalCustomersLastDays = totalCustomersLastDays;
        this.totalInvoicesLastDays = totalInvoicesLastDays;
        this.totalBilledLastDays = totalBilledLastDays;
        this.totalBenefitLastDays = totalBenefitLastDays;
        this.totalUnpaidLastDays = totalUnpaidLastDays;
        this.totalUnpaidAmountLastDays = totalUnpaidAmountLastDays;
    }
}



