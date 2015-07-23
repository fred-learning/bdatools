package bda.test.table

/**
 * Created by Administrator on 2015/5/21.
 */
class catalog_sales(cs_sold_date_sk:String,cs_sold_time_sk:String,cs_ship_date_sk:String,cs_bill_customer_sk:String,cs_bill_cdemo_sk:String,
                    cs_bill_hdemo_sk:String,cs_bill_addr_sk:String, cs_ship_customer_sk:String,cs_ship_cdemo_sk:String,cs_ship_hdemo_sk:String,
                    cs_ship_addr_sk:String,cs_call_center_sk:String,cs_catalog_page_sk:String,cs_ship_mode_sk:String, cs_warehouse_sk:String,
                    cs_item_sk:String,cs_promo_sk:String,cs_order_number:String,cs_quantity:Int,cs_wholesale_cost:Float,cs_list_price:Float,
                    cs_sales_price:Float, cs_ext_discount_amt:Float,cs_ext_sales_price:Float,cs_ext_wholesale_cost:Float,cs_ext_list_price:Float,
                    cs_ext_tax:Float,cs_coupon_amt:Float,cs_ext_ship_cost:Float, cs_net_paid:Float,cs_net_paid_inc_tax:Float,
                    cs_net_paid_inc_ship:Float,cs_net_paid_inc_ship_tax:Float,cs_net_profit:Float)extends Product{

  def canEqual(that: Any) = that.isInstanceOf[store_sales]
  def productArity = 34
  def productElement(n: Int) = n match {
    case 0=>cs_sold_date_sk
    case 1=>cs_sold_time_sk
    case 2=>cs_ship_date_sk
    case 3=>cs_bill_customer_sk
    case 4=>cs_bill_cdemo_sk
    case 5=>cs_bill_hdemo_sk
    case 6=>cs_bill_addr_sk
    case 7=>cs_ship_customer_sk
    case 8=>cs_ship_cdemo_sk
    case 9=>cs_ship_hdemo_sk
    case 10=>cs_ship_addr_sk
    case 11=>cs_call_center_sk
    case 12=>cs_catalog_page_sk
    case 13=>cs_ship_mode_sk
    case 14=>cs_warehouse_sk
    case 15=>cs_item_sk
    case 16=>cs_promo_sk
    case 17=>cs_order_number
    case 18=>cs_quantity
    case 19=>cs_wholesale_cost
    case 20=>cs_list_price
    case 21=>cs_sales_price
    case 22=>cs_ext_discount_amt
    case 23=>cs_ext_sales_price
    case 24=>cs_ext_wholesale_cost
    case 25=>cs_ext_list_price
    case 26=>cs_ext_tax
    case 27=>cs_coupon_amt
    case 28=>cs_ext_ship_cost
    case 29=>cs_net_paid
    case 30=>cs_net_paid_inc_tax
    case 31=>cs_net_paid_inc_ship
    case 32=>cs_net_paid_inc_ship_tax
    case 33=>cs_net_profit
  }

}
