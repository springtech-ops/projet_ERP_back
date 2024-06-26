package org.springtech.springmarket.query;

public class CustomerQuery {
    public static final String STATS_QUERY = "SELECT " +
            "(SELECT COUNT(*) FROM customer) AS total_customers, " +
            "(SELECT COUNT(*) FROM invoice) AS total_invoices, " +
            "(SELECT SUM(total) FROM invoice) AS total_billed, " +
            "(SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande) AS total_benefit, " +
            "(SELECT COUNT(*) FROM invoice WHERE status = 'unpaid') AS total_unpaid, " +
            "(SELECT SUM(total) FROM invoice WHERE status = 'unpaid') AS total_unpaid_amount;";

    public static final String STATS_QUERY_DATE = "SELECT " +
            "(SELECT COUNT(*) FROM customer WHERE created_at LIKE ?) AS total_customers_date, " +
            "(SELECT COUNT(*) FROM invoice WHERE created_at LIKE ?) AS total_invoices_date, " +
            "(SELECT SUM(total) FROM invoice WHERE created_at LIKE ?) AS total_billed_date, " +
            "(SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande WHERE created_at LIKE ?) AS total_benefit_date, " +
            "(SELECT COUNT(*) FROM invoice WHERE status = 'unpaid' AND created_at LIKE ?) AS total_unpaid_date, " +
            "(SELECT SUM(total) FROM invoice WHERE status = 'unpaid' AND created_at LIKE ?) AS total_unpaid_amount_date;";

    public static final String STATS_QUERY_MONTH_YEAR = "SELECT " +
            "(SELECT COUNT(*) FROM customer WHERE DATE_FORMAT(created_at, '%Y-%m') LIKE ?) AS total_customers_month_year, " +
            "(SELECT COUNT(*) FROM invoice WHERE DATE_FORMAT(created_at, '%Y-%m') LIKE ?) AS total_invoices_month_year, " +
            "(SELECT SUM(total) FROM invoice WHERE DATE_FORMAT(created_at, '%Y-%m') LIKE ?) AS total_billed_month_year, " +
            "(SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande WHERE DATE_FORMAT(created_at, '%Y-%m') = ?) AS total_benefit_month_year, " +
            "(SELECT COUNT(*) FROM invoice WHERE status = 'unpaid' AND DATE_FORMAT(created_at, '%Y-%m') LIKE ?) AS total_unpaid_month_year, " +
            "(SELECT SUM(total) FROM invoice WHERE status = 'unpaid' AND DATE_FORMAT(created_at, '%Y-%m') LIKE ?) AS total_unpaid_amount_month_year;";

    public static final String STATS_QUERY_YEAR = "SELECT " +
            "(SELECT COUNT(*) FROM customer WHERE YEAR(created_at) LIKE ?) AS total_customers_year, " +
            "(SELECT COUNT(*) FROM invoice WHERE YEAR(created_at) LIKE ?) AS total_invoices_year, " +
            "(SELECT SUM(total) FROM invoice WHERE YEAR(created_at) LIKE ?) AS total_billed_year, " +
            "(SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande WHERE YEAR(created_at) LIKE ?) AS total_benefit_year, " +
            "(SELECT COUNT(*) FROM invoice WHERE status = 'unpaid' AND YEAR(created_at) LIKE ?) AS total_unpaid_year, " +
            "(SELECT SUM(total) FROM invoice WHERE status = 'unpaid' AND YEAR(created_at) LIKE ?) AS total_unpaid_amount_year;";

    public static final String STATS_QUERY_LAST_DAYS = "SELECT " +
            "(SELECT COUNT(*) FROM customer WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)) AS total_customers_last_days, " +
            "(SELECT COUNT(*) FROM invoice WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)) AS total_invoices_last_days, " +
            "(SELECT SUM(total) FROM invoice WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)) AS total_billed_last_days, " +
            "(SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)) AS total_benefit_last_days, " +
            "(SELECT COUNT(*) FROM invoice WHERE status = 'unpaid' AND created_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)) AS total_unpaid_last_days, " +
            "(SELECT SUM(total) FROM invoice WHERE status = 'unpaid' AND created_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)) AS total_unpaid_amount_last_days;";


//    public static final String STATS_QUERY = "SELECT (SELECT COUNT(*) FROM customer) AS total_customers, (SELECT COUNT(*) FROM invoice) AS total_invoices, (SELECT SUM(total) FROM invoice) AS total_billed, (SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande) AS total_benefit, (SELECT COUNT(*) FROM invoice WHERE status = 'unpaid') AS total_unpaid, (SELECT SUM(total) FROM invoice WHERE status = 'unpaid') AS total_unpaid_amount;";

//    public static final String STATS_QUERY = "SELECT " +
//            "(SELECT COUNT(*) FROM customer) AS total_customers, " +
//            "(SELECT COUNT(*) FROM invoice) AS total_invoices, " +
//            "(SELECT SUM(total) FROM invoice) AS total_billed, " +
//            "(SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande) AS total_benefit, " +
//            "(SELECT COUNT(*) FROM invoice WHERE status = 'unpaid') AS total_unpaid, " +
//            "(SELECT SUM(total) FROM invoice WHERE status = 'unpaid') AS total_unpaid_amount, " +
//            "(SELECT COUNT(*) FROM customer WHERE DATE(created_at) = CURRENT_DATE()) AS new_customers_today, " +
//            "(SELECT COUNT(*) FROM invoice WHERE DATE(create_at) = CURRENT_DATE()) AS new_invoices_today, " +
//            "(SELECT SUM(total) FROM invoice WHERE DATE(create_at) = CURRENT_DATE()) AS total_billed_today, " +
//            "(SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande WHERE DATE(created_at) = CURRENT_DATE()) AS total_benefit_today, " +
//            "(SELECT COUNT(*) FROM invoice WHERE status = 'unpaid' AND DATE(create_at) = CURRENT_DATE()) AS total_unpaid_today, " +
//            "(SELECT SUM(total) FROM invoice WHERE status = 'unpaid' AND DATE(create_at) = CURRENT_DATE()) AS total_unpaid_amount_today, " +
//            "(SELECT COUNT(*) FROM customer WHERE WEEK(created_at) = WEEK(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS new_customers_this_week, " +
//            "(SELECT COUNT(*) FROM invoice WHERE WEEK(create_at) = WEEK(CURRENT_DATE()) AND YEAR(create_at) = YEAR(CURRENT_DATE())) AS new_invoices_this_week, " +
//            "(SELECT SUM(total) FROM invoice WHERE WEEK(create_at) = WEEK(CURRENT_DATE()) AND YEAR(create_at) = YEAR(CURRENT_DATE())) AS total_billed_this_week, " +
//            "(SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande WHERE WEEK(created_at) = WEEK(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_benefit_this_week, " +
//            "(SELECT COUNT(*) FROM invoice WHERE status = 'unpaid' AND WEEK(create_at) = WEEK(CURRENT_DATE()) AND YEAR(create_at) = YEAR(CURRENT_DATE())) AS total_unpaid_this_week, " +
//            "(SELECT SUM(total) FROM invoice WHERE status = 'unpaid' AND WEEK(create_at) = WEEK(CURRENT_DATE()) AND YEAR(create_at) = YEAR(CURRENT_DATE())) AS total_unpaid_amount_this_week, " +
//            "(SELECT COUNT(*) FROM customer WHERE MONTH(created_at) = MONTH(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS new_customers_this_month, " +
//            "(SELECT COUNT(*) FROM invoice WHERE MONTH(create_at) = MONTH(CURRENT_DATE()) AND YEAR(create_at) = YEAR(CURRENT_DATE())) AS new_invoices_this_month, " +
//            "(SELECT SUM(total) FROM invoice WHERE MONTH(create_at) = MONTH(CURRENT_DATE()) AND YEAR(create_at) = YEAR(CURRENT_DATE())) AS total_billed_this_month, " +
//            "(SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande WHERE MONTH(created_at) = MONTH(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_benefit_this_month, " +
//            "(SELECT COUNT(*) FROM invoice WHERE status = 'unpaid' AND MONTH(create_at) = MONTH(CURRENT_DATE()) AND YEAR(create_at) = YEAR(CURRENT_DATE())) AS total_unpaid_this_month, " +
//            "(SELECT SUM(total) FROM invoice WHERE status = 'unpaid' AND MONTH(create_at) = MONTH(CURRENT_DATE()) AND YEAR(create_at) = YEAR(CURRENT_DATE())) AS total_unpaid_amount_this_month, " +
//            "(SELECT COUNT(*) FROM customer WHERE YEAR(created_at) = YEAR(CURRENT_DATE())) AS new_customers_this_year, " +
//            "(SELECT COUNT(*) FROM invoice WHERE YEAR(create_at) = YEAR(CURRENT_DATE())) AS new_invoices_this_year, " +
//            "(SELECT SUM(total) FROM invoice WHERE YEAR(create_at) = YEAR(CURRENT_DATE())) AS total_billed_this_year, " +
//            "(SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande WHERE YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_benefit_this_year, " +
//            "(SELECT COUNT(*) FROM invoice WHERE status = 'unpaid' AND YEAR(create_at) = YEAR(CURRENT_DATE())) AS total_unpaid_this_year, " +
//            "(SELECT SUM(total) FROM invoice WHERE status = 'unpaid' AND YEAR(create_at) = YEAR(CURRENT_DATE())) AS total_unpaid_amount_this_year";





//Ma requete SQL brute
//    SELECT
//            (SELECT COUNT(*) FROM customer) AS total_customers,
//    (SELECT COUNT(*) FROM invoice) AS total_invoices,
//    (SELECT SUM(total) FROM invoice) AS total_billed,
//    (SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande) AS total_benefit,
//    (SELECT COUNT(*) FROM invoice WHERE status = 'unpaid') AS total_unpaid,
//    (SELECT SUM(total) FROM invoice WHERE status = 'unpaid') AS total_unpaid_amount,
//    -- Agrégations par jour
//            (SELECT COUNT(*) FROM customer WHERE DATE(created_at) = CURRENT_DATE()) AS new_customers_today,
//    (SELECT COUNT(*) FROM invoice WHERE DATE(created_at) = CURRENT_DATE()) AS new_invoices_today,
//    (SELECT SUM(total) FROM invoice WHERE DATE(created_at) = CURRENT_DATE()) AS total_billed_today,
//    (SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande WHERE DATE(created_at) = CURRENT_DATE()) AS total_benefit_today,
//    (SELECT COUNT(*) FROM invoice WHERE status = 'unpaid' AND DATE(created_at) = CURRENT_DATE()) AS total_unpaid_today,
//    (SELECT SUM(total) FROM invoice WHERE status = 'unpaid' AND DATE(created_at) = CURRENT_DATE()) AS total_unpaid_amount_today,
//    -- Agrégations par semaine
//            (SELECT COUNT(*) FROM customer WHERE WEEK(created_at) = WEEK(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS new_customers_this_week,
//    (SELECT COUNT(*) FROM invoice WHERE WEEK(created_at) = WEEK(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS new_invoices_this_week,
//    (SELECT SUM(total) FROM invoice WHERE WEEK(created_at) = WEEK(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_billed_this_week,
//    (SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande WHERE WEEK(created_at) = WEEK(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_benefit_this_week,
//    (SELECT COUNT(*) FROM invoice WHERE status = 'unpaid' AND WEEK(created_at) = WEEK(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_unpaid_this_week,
//    (SELECT SUM(total) FROM invoice WHERE status = 'unpaid' AND WEEK(created_at) = WEEK(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_unpaid_amount_this_week,
//    -- Agrégations par mois
//            (SELECT COUNT(*) FROM customer WHERE MONTH(created_at) = MONTH(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS new_customers_this_month,
//    (SELECT COUNT(*) FROM invoice WHERE MONTH(created_at) = MONTH(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS new_invoices_this_month,
//    (SELECT SUM(total) FROM invoice WHERE MONTH(created_at) = MONTH(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_billed_this_month,
//    (SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande WHERE MONTH(created_at) = MONTH(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_benefit_this_month,
//    (SELECT COUNT(*) FROM invoice WHERE status = 'unpaid' AND MONTH(created_at) = MONTH(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_unpaid_this_month,
//    (SELECT SUM(total) FROM invoice WHERE status = 'unpaid' AND MONTH(created_at) = MONTH(CURRENT_DATE()) AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_unpaid_amount_this_month,
//    -- Agrégations par année
//            (SELECT COUNT(*) FROM customer WHERE YEAR(created_at) = YEAR(CURRENT_DATE())) AS new_customers_this_year,
//    (SELECT COUNT(*) FROM invoice WHERE YEAR(created_at) = YEAR(CURRENT_DATE())) AS new_invoices_this_year,
//    (SELECT SUM(total) FROM invoice WHERE YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_billed_this_year,
//    (SELECT SUM(prix_vente_total - prix_achat_total) FROM ligne_commande WHERE YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_benefit_this_year,
//    (SELECT COUNT(*) FROM invoice WHERE status = 'unpaid' AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_unpaid_this_year,
//    (SELECT SUM(total) FROM invoice WHERE status = 'unpaid' AND YEAR(created_at) = YEAR(CURRENT_DATE())) AS total_unpaid_amount_this_year;



}
