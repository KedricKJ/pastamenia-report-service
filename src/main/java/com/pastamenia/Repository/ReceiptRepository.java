package com.pastamenia.Repository;

import com.pastamenia.entity.Company;
import com.pastamenia.entity.Receipt;
import com.pastamenia.report.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Pasindu Lakmal
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query("select r from Receipt r where r.company=?1 and r.createdAt= (SELECT max(r.createdAt) from Receipt r where r.company=?1)")
    List<Receipt> findReceiptWithMaxCreatedDateAndCompany(Company company);

    @Query("select r from Receipt r where r.company=?1 and r.updatedAt= (SELECT max(r.updatedAt) from Receipt r where r.company=?1)")
    List<Receipt> findReceiptWithMaxUpdatedDateAndCompany(Company company);

    @Query("select r from Receipt r where r.company=?1 and r.createdAt= (SELECT min(r.createdAt) from Receipt r where r.company=?1)")
    List<Receipt> findReceiptWithMinCreatedDateAndCompany(Company company);


    @Query(value = "SELECT rli.item_id as itemId , i.item_name as itemName ,rli.variant_name as " +
      "variantName, c.id as categoryId,c.`name` as categoryName,r.created_at as createdAt , rli" +
      ".gross_total_money as grossTotalMoney,rli.quantity, r.receipt_type as receiptType ,lild" +
      ".name as discountName,lild.money_amount as discountAmount\n" +
      "FROM receipt_line_item rli\n" +
      "LEFT JOIN  item i ON rli.item_id = i.id\n" +
      "LEFT JOIN category c ON i.category_id=c.id\n" +
      "LEFT JOIN receipt r ON rli.receipt_id=r.receipt_no\n" +
      "LEFT JOIN line_itm_line_discount lild ON rli.receipt_line_item_no=lild.receipt_total_discount_no\n" +
      "\tWHERE\n" +
      "\tr.created_at BETWEEN ?1\n" +
      "\tAND ?2 AND r.company_id=?3", nativeQuery = true)
    List<DailySalesMixReport> findDataForDailySaleMixReport(String from, String to, Long companyID);

    @Query(value = "SELECT\n" +
            "\tcount( receipt_no ) AS quantity,\n"+
            "\tdining_option AS category,\n"+
            "\t SUM( total_money ) AS grossSale,\n"+
            "\tSUM( total_discount ) AS totalDiscount,"+
            "\t ROUND(((SUM( total_money ) * 8/108)),2) AS tax,\n"+
            "\t SUM( rtt.money_amount ) AS serviceCharge\n"+
            "\tFROM\n" +
            "\treceipt r\n" +
            "\tleft join receipt_total_tax rtt ON r.receipt_no = rtt.receipt_id\n"+
            "\tWHERE\n" +
            "\tr.receipt_type = 'SALE' AND r.created_at BETWEEN ?1 AND ?2 AND r.company_id=?3\n" +
            "GROUP BY\n" +
            "\tr.dining_option\n" +
            "ORDER BY\n" +
            "CASE\n" +
            "\t\tWHEN dining_option = 'Dine-in' THEN\n" +
            "\t1 \n" +
            "\tWHEN dining_option = 'Takeaway' THEN\n" +
            "\t1 \n" +
            "\tWHEN dining_option = 'Delivery' THEN\n" +
            "\t1 \n" +
            "END ASC", nativeQuery = true)
    List<SaleSummaryReport> findDataForSaleSummaryReportDiningType(String from, String to, Long companyID);


    @Query(value = "SELECT c.`name` AS category,\n" +
            "\tCOUNT( rli.item_id ) AS quantity,\n" +
            "\tROUND( SUM( rli.gross_total_money ), 2 ) AS grossSale,\n" +
            "\tSUM( lild.money_amount ) AS totalDiscount,\n" +
            "\tROUND( ( ( SUM(rli.gross_total_money ) * 8 / 108 ) ), 2 ) AS tax,\n" +
            "\tSUM( rtt.money_amount ) AS surviceCharge" +
            "\tFROM\n" +
            "\treceipt_line_item rli\n" +
            "\tLEFT JOIN item item ON rli.item_id = item.id\n" +
            "\tLEFT JOIN category c ON item.category_id = c.id\n" +
            "\tLEFT JOIN receipt r ON rli.receipt_id = r.receipt_no\n" +
            "\tLEFT JOIN receipt_total_tax rtt ON r.receipt_no = rtt.receipt_id\n" +
            "\tLEFT JOIN line_itm_line_discount lild ON rli.receipt_line_item_no = lild.receipt_line_item_id \n" +
            "WHERE\n" +
            "\tr.receipt_type = 'SALE' AND r.created_at BETWEEN ?1 AND ?2 AND r.company_id=?3\n" +
            "GROUP BY\n" +
            "\tc.`name`", nativeQuery = true)
    List<SaleSummaryReport> findDataForSaleSummaryReportCategory(String from, String to, Long companyID);


    @Query(value = "SELECT\n" +
            "\trp.`name` AS category,\n" +
            "\tCOUNT( rli.item_id ) AS quantity,\n" +
            "\tROUND( SUM( rli.gross_total_money ), 2 ) AS grossSale,\n"+
            "\tSUM( lild.money_amount ) AS totalDiscount,\n" +
            "\tROUND( ( ( SUM(rli.gross_total_money ) * 8 / 108 ) ), 2 ) AS tax,\n" +
            "\tSUM( rtt.money_amount ) AS surviceCharge" +
            "\tFROM\n" +
            "\treceipt_line_item rli\n" +
            "\tLEFT JOIN item item ON rli.item_id = item.id\n" +
            "\tLEFT JOIN category c ON item.category_id = c.id\n" +
            "\tLEFT JOIN receipt r ON rli.receipt_id = r.receipt_no\n" +
            "\tLEFT JOIN receipt_total_tax rtt ON r.receipt_no = rtt.receipt_id\n" +
            "\tLEFT JOIN line_itm_line_discount lild ON rli.receipt_line_item_no = lild.receipt_line_item_id \n" +
            "\tLEFT JOIN receipt_payment rp ON r.receipt_no = rp.receipt_id \n" +
            "WHERE\n" +
            "\tr.receipt_type = 'SALE' AND r.created_at BETWEEN ?1 AND ?2 AND r.company_id=?3\n" +
            "GROUP BY\n" +
            "\trp.`name`\n" +
            "\t", nativeQuery = true)
    List<SaleSummaryReport> findDataForSaleSummaryReportPaymentMode(String from, String to, Long companyID);


    @Query(value = "SELECT count( DISTINCT r.receipt_no) as receiptNo ,r.created_at as createdAt , sum( DISTINCT r.total_money) as totalMoney , count(rli.id) as lineItemId\n" +
            "FROM receipt r\n" +
            "LEFT JOIN receipt_line_item  rli ON r.receipt_no=rli.receipt_id\n" +
            "WHERE r.created_at BETWEEN '2021-12-11T00:03:00.000Z' AND '2021-12-12T00:02:59.000Z'\n" +
            "GROUP BY  r.created_at\n" +
            "ORDER BY r.created_at", nativeQuery = true)
    List<HourlySaleReport> findDataForHourlySaleReport(String from, String to, Long companyID);


    @Query(value = "SELECT\n" +
            "\trp.NAME AS settlement,\n" +
            "\tr.receipt_number AS orderNo,\n" +
            "\tr.dining_option AS orderType,\n" +
            "\tr.created_at AS createdAt,\n" +
            "\te.`name` AS userName,\n" +
            "\tr.total_money AS sale,\n" +
            "\tr._order AS remark\n" +
            "FROM\n" +
            "\treceipt r\n" +
            "\tLEFT JOIN receipt_payment rp ON rp.receipt_id = r.receipt_number\n" +
            "\tLEFT JOIN employee e ON e.id = r.employee_id \n" +
            "WHERE\n" +
            "\tr.created_at BETWEEN ?1 \n" +
            "\tAND ?2 AND r.company_id=?3 \n" +
            "ORDER BY\n" +
            "\trp.`name`", nativeQuery = true)
    List<SettlementModeWiseReport> findDataForSettlementModeViewReport(String from, String to, Long companyID);


    @Query(value = "SELECT\n" +
            "\trp.type AS type,\n" +
            "\tr.receipt_type AS receiptType,\n" +
            "\tr.note AS reason,\n" +
            "\tr._order AS orderNo,\n" +
            "\tr.dining_option AS orderType,\n" +
            "\tr.created_at AS createdAt,\n" +
            "\te.`name` AS authorizedId,\n" +
            "\t(\n" +
            "\tSELECT m.name\n" +
            "\tFROM receipt p\n" +
            "\tLEFT JOIN employee m ON m.id = p.employee_id \n" +
            "\tWHERE p.receipt_number = r.refund_for\n" +
            "\t) as userName,\n" +
            "\tr.total_money AS sale \n" +
            "FROM\n" +
            "\treceipt r\n" +
            "\tLEFT JOIN receipt_payment rp ON rp.receipt_id = r.receipt_number\n" +
            "\tLEFT JOIN employee e ON e.id = r.employee_id \n" +
            "WHERE\n" +
            "\tr.receipt_type = 'REFUND' \n" +
            "\tOR r.receipt_type = 'VOID' \n" +
            "\tAND r.created_at BETWEEN '2021-09-11T00:00:00.000Z' \n" +
            "\tAND '2022-09-12T00:00:00.000Z' \n" +
            "ORDER BY\n" +
            "\trp.`name`", nativeQuery = true)
    List<VoidRefundDetailReport> findDataForVoidRefundDetailForm(String from, String to, Integer companyID);


    @Query(value = "SELECT\n" +
            "\tlilm.NAME AS category,\n" +
            "\tlilm._option AS _option,\n" +
            "\tlilm.price AS price,\n" +
            "\tr.receipt_type AS receiptType,\n" +
            "\tlild.money_amount AS discountAmount \n" +
            "FROM\n" +
            "\tline_item_line_modifier lilm\n" +
            "\tLEFT JOIN receipt_line_item rli ON lilm.receipt_line_item_id = rli.receipt_line_item_no\n" +
            "\tLEFT JOIN receipt r ON rli.receipt_id = r.receipt_no\n" +
            "\tLEFT JOIN line_itm_line_discount lild ON rli.receipt_line_item_no = lild.receipt_total_discount_no\n" +
            "\tWHERE\n" +
            "\tr.created_at BETWEEN ?1\n" +
            "\tAND ?2 AND r.company_id=?3", nativeQuery = true)
    List<DailySaleModifierReport> findDataForDailySaleModifier(String from, String to, Long companyID);


    @Query(value = "SELECT\n" +
            "\tsum( r.total_money ) as refunds ,\n" +
            "\tdining_option AS category\n" +
            "FROM\n" +
            "\treceipt r \n" +
            "WHERE\n" +
            "\tr.receipt_type = 'REFUND' \n" +
            "\tOR r.receipt_type = 'VOID' \n" +
            "\tAND r.created_at BETWEEN ?1 AND ?2 AND r.company_id=?3\n" +
            "GROUP BY\n" +
            "\tr.dining_option", nativeQuery = true)
    List<SaleSummaryReportDineInVoidsAndRefunds> findSaleSummaryReportDineInVoidsAndRefunds(String from, String to, Long companyID);


    @Query(value = "SELECT\n" +
            "\tsum(rli.gross_total_money) AS refunds,\n" +
            "\tc.`name`  AS category \n" +
            "FROM\n" +
            "receipt_line_item rli\n" +
            "\tLEFT JOIN item item ON rli.item_id = item.id\n" +
            "\tLEFT JOIN category c ON item.category_id = c.id\n" +
            "\tLEFT JOIN receipt r ON rli.receipt_id = r.receipt_no\n" +
            "WHERE\n" +
            "\tr.receipt_type = 'REFUND' OR r.receipt_type = 'VOID' AND r.created_at BETWEEN ?1 AND ?2 AND r.company_id=?3\n" +
            "GROUP BY\n" +
            "\t\tc.`name`", nativeQuery = true)
    List<SaleSummaryReportDineInVoidsAndRefunds> findSaleSummaryReportCategoryVoidsAndRefunds(String from, String to, Long companyID);


    @Query(value = "SELECT\n" +
            "\tsum( rli.gross_total_money ) AS refunds,\n" +
            "\trp.`name` AS category \n" +
            "\tFROM\n" +
            "\treceipt_line_item rli\n" +
            "\tLEFT JOIN item item ON rli.item_id = item.id\n" +
            "\tLEFT JOIN category c ON item.category_id = c.id\n" +
            "\tLEFT JOIN receipt r ON rli.receipt_id = r.receipt_no\n" +
            "\tLEFT JOIN receipt_payment rp ON r.receipt_no = rp.receipt_id \n" +
            "WHERE\n" +
            "\tr.receipt_type = 'REFUND' \n" +
            "\tOR r.receipt_type = 'VOID' \n" +
            "\tAND r.created_at BETWEEN ?1 AND ?2 AND r.company_id=?3\n" +
            "GROUP BY\n" +
            "\trp.`name`", nativeQuery = true)
    List<SaleSummaryReportDineInVoidsAndRefunds> findSaleSummaryReportPaymentTypeVoidsAndRefunds(String from, String to, Long companyId);


    @Query(value = "SELECT\n" +
            "\tlild.`name` as category,\n" +
            "\tr.receipt_number as orderNo,\n" +
            "\tr.dining_option as OrderType,\n" +
            "\tr.created_at as orderTime,\n" +
            "\te.name as userId,\n" +
            "\tr.note as reason,\n" +
            "\tlild.percentage as percentage,\n" +
            "\tsum(r.total_money) as sales,\n" +
            "\tr.total_discount as discount,\n" +
            "\tROUND( ( ( SUM(r.total_money ) * 8 / 108 )), 2 ) AS tax,\n" +
            "\tr.surcharge as serviceCharge\n" +
            "FROM\n" +
            "\tline_itm_line_discount lild\n" +
            "\tLEFT JOIN receipt_line_item rli ON  lild.receipt_line_item_id =rli.receipt_line_item_no \n" +
            "\tLEFT JOIN receipt r  ON r.receipt_no = rli.receipt_id\n" +
            "\tLEFT JOIN employee e ON r.employee_id=e.id\n" +
            "GROUP BY r.receipt_number", nativeQuery = true)
    List<DiscountDetailReport> findDiscountDetailReport(String from, String to, Long companyId);


    @Query(value = "select  SUM(r.total_money)  as totalMoney , s.name  as store , count(rp.`name`) as count, rp.`name` as payment\n" +
            "from receipt r\n" +
            "left JOIN store s ON r.store_id=s.id\n" +
            "left JOIN receipt_payment rp ON r.receipt_no=rp.receipt_id\n" +
            "WHERE r.dining_option = 'Delivery' AND rp.name IN('UberEats','PickMe') and r.created_at BETWEEN ?1 AND ?2 AND r.company_id=?3\n" +
            "GROUP BY  rp.`name`", nativeQuery = true)
    List<ConsolidatedDailySalesSummaryReport> findConsolidatedDailySalesSummaryReportDelivery(String from, String to, Long companyId);


    @Query(value = "select s.name as store , r.dining_option as diningOption ,count(r.dining_option) as count, SUM(r.total_money) as totalMoney\n" +
            "from receipt r\n" +
            "left JOIN store s ON r.store_id=s.id\n" +
            "WHERE r.created_at BETWEEN ?1 AND ?2 AND r.company_id=?3  AND r.dining_option IN ('Dine-in','Takeaway')\n" +
            "GROUP BY  r.dining_option", nativeQuery = true)
    List<ConsolidatedDailySalesSummaryReport> findConsolidatedDailySalesSummaryReportDiningOptions(String from, String to, Long companyId);

    @Query(value = " SELECT\n" +
            "\t rli.quantity as quantity,\n" +
            "\t rli.gross_total_money AS amount,\n" +
            "\t  r.note as reason\n" +
            "\t FROM\n" +
            "\t  receipt_line_item rli\n" +
            "\t LEFT JOIN receipt r ON rli.receipt_id = r.receipt_no\n" +
            "\t WHERE\n" +
            "\t r.receipt_type = 'VOID'\n" +
            "\t AND r.created_at BETWEEN ?1 AND ?2 AND r.company_id=?3", nativeQuery = true)
    List<VoidRefundSalesSummaryReport> findVoidsForSaleSummaryReport(String from, String to, Long companyId);



    @Query(value = "SELECT\n" +
            "\t rli.quantity as quantity,\n" +
            "\t rli.gross_total_money AS amount, \n" +
            "\t r.note as reason\n" +
            "FROM\n" +
            "  receipt_line_item rli\n" +
            "\tLEFT JOIN receipt r ON rli.receipt_id = r.receipt_no \n" +
            "WHERE\n" +
            "\tr.receipt_type = 'REFUND' \n" +
            "\tAND r.created_at BETWEEN ?1 AND ?2 AND r.company_id=?3", nativeQuery = true)
    List<VoidRefundSalesSummaryReport> findRefundsForSaleSummaryReport(String from, String to, Long companyId);


    @Query(value = "SELECT\n" +
            "\tSUM(lild.money_amount) AS amount ,\n" +
            "\tlild.name as description,\n" +
            "\tSUM(rli.quantity) as quantity\n" +
            "\tFROM\n" +
            "\treceipt_line_item rli\n" +
            "\tLEFT JOIN receipt r ON rli.receipt_id = r.receipt_no\n" +
            "\tLEFT JOIN line_itm_line_discount lild ON rli.receipt_line_item_no = lild.receipt_line_item_id \n" +
            "\tWHERE\n" +
            "\tlild.money_amount is not null AND r.created_at BETWEEN ?1 AND ?2 AND r.company_id=?3\n" +
            "\tGROUP BY  lild.id", nativeQuery = true)
    List<VoidRefundSalesSummaryReport> findDiscountsForSaleSummaryReport(String from, String to, Long companyId);






}
