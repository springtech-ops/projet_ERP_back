package org.springtech.springmarket.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springtech.springmarket.domain.Stats;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.springtech.springmarket.query.CustomerQuery.*;

public class StatsRowMapper implements RowMapper<Stats> {
    private String query;

    // Constructeur prenant la valeur de la requête
    public StatsRowMapper(String query) {
        this.query = query;
    }

    @Override
    public Stats mapRow(ResultSet rs, int rowNum) throws SQLException {
        switch (query) {
            case STATS_QUERY_DATE:
                return new Stats(
                        rs.getInt("total_customers_date"),
                        rs.getDouble("total_unpaid_amount_date"),
                        rs.getInt("total_invoices_date"),
                        rs.getDouble("total_billed_date"),
                        rs.getDouble("total_benefit_date"),
                        rs.getInt("total_unpaid_date")
                );
            case STATS_QUERY_MONTH_YEAR:
                return new Stats(
                        rs.getInt("total_customers_month_year"),
                        rs.getInt("total_invoices_month_year"),
                        rs.getDouble("total_unpaid_amount_month_year"),
                        rs.getDouble("total_billed_month_year"),
                        rs.getDouble("total_benefit_month_year"),
                        rs.getInt("total_unpaid_month_year")
                );
            case STATS_QUERY_YEAR:
                return new Stats(
                        rs.getInt("total_customers_year"),
                        rs.getInt("total_invoices_year"),
                        rs.getInt("total_unpaid_year"),
                        rs.getDouble("total_billed_year"),
                        rs.getDouble("total_benefit_year"),
                        rs.getDouble("total_unpaid_amount_year")
                );
            case STATS_QUERY_LAST_DAYS:
                return new Stats(
                        rs.getInt("total_customers_last_days"),
                        rs.getDouble("total_billed_last_days"),
                        rs.getInt("total_invoices_last_days"),
                        rs.getDouble("total_benefit_last_days"),
                        rs.getInt("total_unpaid_last_days"),
                        rs.getDouble("total_unpaid_amount_last_days")
                );
            // Ajoutez des cas pour les autres requêtes si nécessaire
            default:
                // Utilisez la requête par défaut si aucune requête spécifique n'est sélectionnée
                return new Stats(
                        rs.getDouble("total_unpaid_amount"),
                        rs.getInt("total_customers"),
                        rs.getInt("total_invoices"),
                        rs.getDouble("total_billed"),
                        rs.getDouble("total_benefit"),
                        rs.getInt("total_unpaid")
                );
        }
    }

//    @Override
//    public Stats mapRow(ResultSet resultSet, int rowNum) throws SQLException {
//        return Stats.builder()
//                // Default statistics
//                .totalCustomers(resultSet.getInt("total_customers"))
//                .totalInvoices(resultSet.getInt("total_invoices"))
//                .totalBilled(resultSet.getDouble("total_billed"))
//                .totalBenefit(resultSet.getDouble("total_benefit"))
//                .totalUnpaid(resultSet.getInt("total_unpaid"))
//                .totalUnpaidAmount(resultSet.getDouble("total_unpaid_amount"))
//
//                // Date specific statistics
//                .totalCustomersDate(resultSet.getInt("total_customers_date"))
//                .totalInvoicesDate(resultSet.getInt("total_invoices_date"))
//                .totalBilledDate(resultSet.getDouble("total_billed_date"))
//                .totalBenefitDate(resultSet.getDouble("total_benefit_date"))
//                .totalUnpaidDate(resultSet.getInt("total_unpaid_date"))
//                .totalUnpaidAmountDate(resultSet.getDouble("total_unpaid_amount_date"))
//
//                // Month Year specific statistics
//                .totalCustomersMonthYear(resultSet.getInt("total_customers_month_year"))
//                .totalInvoicesMonthYear(resultSet.getInt("total_invoices_month_year"))
//                .totalBilledMonthYear(resultSet.getDouble("total_billed_month_year"))
//                .totalBenefitMonthYear(resultSet.getDouble("total_benefit_month_year"))
//                .totalUnpaidMonthYear(resultSet.getInt("total_unpaid_month_year"))
//                .totalUnpaidAmountMonthYear(resultSet.getDouble("total_unpaid_amount_month_year"))
//
//                // Year specific statistics
//                .totalCustomersYear(resultSet.getInt("total_customers_year"))
//                .totalInvoicesYear(resultSet.getInt("total_invoices_year"))
//                .totalBilledYear(resultSet.getDouble("total_billed_year"))
//                .totalBenefitYear(resultSet.getDouble("total_benefit_year"))
//                .totalUnpaidYear(resultSet.getInt("total_unpaid_year"))
//                .totalUnpaidAmountYear(resultSet.getDouble("total_unpaid_amount_year"))
//
//                // Last 7 Days specific statistics
//                .totalCustomersLastDays(resultSet.getInt("total_customers_last_days"))
//                .totalInvoicesLastDays(resultSet.getInt("total_invoices_last_days"))
//                .totalBilledLastDays(resultSet.getDouble("total_billed_last_days"))
//                .totalBenefitLastDays(resultSet.getDouble("total_benefit_last_days"))
//                .totalUnpaidLastDays(resultSet.getInt("total_unpaid_last_days"))
//                .totalUnpaidAmountLastDays(resultSet.getDouble("total_unpaid_amount_last_days"))
//                .build();
//    }


//    public Stats mapRow(ResultSet rs, int rowNum, String query) throws SQLException {
//
//    }


}
