package org.springtech.springmarket.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springtech.springmarket.domain.Stats;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsRowMapper implements RowMapper<Stats> {

    @Override
    public Stats mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Stats.builder()
                .totalCustomers(resultSet.getInt("total_customers"))
                .totalInvoices(resultSet.getInt("total_invoices"))
                .totalBilled(resultSet.getDouble("total_billed"))
                .totalBenefit(resultSet.getDouble("total_benefit"))
                .totalUnpaid(resultSet.getInt("total_unpaid"))
                .totalUnpaidAmount(resultSet.getDouble("total_unpaid_amount"))
                .build();
    }

}
