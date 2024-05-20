package org.springtech.springmarket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Stats {

    private int totalCustomers;
    private int totalInvoices;
    private double totalBilled;  // Sales
    private double totalBenefit; // Earnings
    private int totalUnpaid;  // Total Bill Unpaid
    private double totalUnpaidAmount; // Total Sales Unpaid

}

