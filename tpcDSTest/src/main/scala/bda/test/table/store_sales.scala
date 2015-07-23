package bda.test.table

/**
 * Created by Administrator on 2015/5/20.
 */
//table 1:store_sales
class store_sales(ss_sold_date_sk:String,ss_sold_time_sk:String,ss_item_sk:String,ss_customer_sk:String, ss_cdemo_sk:String,
                  ss_hdemo_sk:String, ss_addr_sk:String,ss_store_sk:String,ss_promo_sk:String, ss_ticket_number:String,
                  ss_quantity:Int,ss_wholesale_cost:Float, ss_list_price:Float, ss_sales_price:Float,ss_ext_discount_amt:Float,
                  ss_ext_sales_price:Float,ss_ext_wholesale_cost:Float, ss_ext_list_price:Float,ss_ext_tax:Float,
                  ss_coupon_amt:Float,ss_net_paid:Float,ss_net_paid_inc_tax:Float, ss_net_profit:Float)extends Product{

  def canEqual(that: Any) = that.isInstanceOf[store_sales]
  def productArity = 23
  def productElement(n: Int) = n match {
    case 0=>ss_sold_date_sk
    case 1=>ss_sold_time_sk
    case 2=>ss_item_sk
    case 3=>ss_customer_sk
    case 4=>ss_cdemo_sk
    case 5=>ss_hdemo_sk
    case 6=>ss_addr_sk
    case 7=>ss_store_sk
    case 8=>ss_promo_sk
    case 9=>ss_ticket_number
    case 10=>ss_quantity
    case 11=>ss_wholesale_cost
    case 12=>ss_list_price
    case 13=>ss_sales_price
    case 14=>ss_ext_discount_amt
    case 15=>ss_ext_sales_price
    case 16=>ss_ext_wholesale_cost
    case 17=>ss_ext_list_price
    case 18=>ss_ext_tax
    case 19=>ss_coupon_amt
    case 20=>ss_net_paid
    case 21=>ss_net_paid_inc_tax
    case 22=>ss_net_profit
  }

}
