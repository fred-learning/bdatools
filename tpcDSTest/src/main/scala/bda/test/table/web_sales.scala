package bda.test.table

/**
 * Created by Administrator on 2015/5/21.
 */
class web_sales (ws_sold_date_sk:String,ws_sold_time_sk:String,ws_ship_date_sk:String,ws_item_sk:String,ws_bill_customer_sk:String,
                 ws_bill_cdemo_sk:String,ws_bill_hdemo_sk:String, ws_bill_addr_sk:String,ws_ship_customer_sk:String,ws_ship_cdemo_sk:String,
                 ws_ship_hdemo_sk:String,ws_ship_addr_sk:String,ws_web_page_sk:String,ws_web_site_sk:String, ws_ship_mode_sk:String,
                 ws_warehouse_sk:String,ws_promo_sk:String,ws_order_number:String,ws_quantity:Int,ws_wholesale_cost:Float,ws_list_price:Float,
                 ws_sales_price:Float, ws_ext_discount_amt:Float,ws_ext_sales_price:Float,ws_ext_wholesale_cost:Float,ws_ext_list_price:Float,
                 ws_ext_tax:Float,ws_coupon_amt:Float,ws_ext_ship_cost:Float, ws_net_paid:Float,ws_net_paid_inc_tax:Float,
                 ws_net_paid_inc_ship:Float,ws_net_paid_inc_ship_tax:Float,ws_net_profit:Float)extends Product{

  def canEqual(that: Any) = that.isInstanceOf[web_sales]
  def productArity = 34
  def productElement(n: Int) = n match {
    case 0=>ws_sold_date_sk
    case 1=>ws_sold_time_sk
    case 2=>ws_ship_date_sk
    case 3=>ws_item_sk
    case 4=>ws_bill_customer_sk
    case 5=>ws_bill_cdemo_sk
    case 6=>ws_bill_hdemo_sk
    case 7=>ws_bill_addr_sk
    case 8=>ws_ship_customer_sk
    case 9=>ws_ship_cdemo_sk
    case 10=>ws_ship_hdemo_sk
    case 11=>ws_ship_addr_sk
    case 12=>ws_web_page_sk
    case 13=>ws_web_site_sk
    case 14=>ws_ship_mode_sk
    case 15=>ws_warehouse_sk
    case 16=>ws_promo_sk
    case 17=>ws_order_number
    case 18=>ws_quantity
    case 19=>ws_wholesale_cost
    case 20=>ws_list_price
    case 21=>ws_sales_price
    case 22=>ws_ext_discount_amt
    case 23=>ws_ext_sales_price
    case 24=>ws_ext_wholesale_cost
    case 25=>ws_ext_list_price
    case 26=>ws_ext_tax
    case 27=>ws_coupon_amt
    case 28=>ws_ext_ship_cost
    case 29=>ws_net_paid
    case 30=>ws_net_paid_inc_tax
    case 31=>ws_net_paid_inc_ship
    case 32=>ws_net_paid_inc_ship_tax
    case 33=>ws_net_profit
  }

}
