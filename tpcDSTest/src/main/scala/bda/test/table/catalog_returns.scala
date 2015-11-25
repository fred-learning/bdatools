package bda.test.table

/**
 * Created by Administrator on 2015/5/21.
 */
class catalog_returns (cr_returned_date_sk:String,cr_returned_time_sk:String,cr_item_sk:String,cr_refunded_customer_sk:String,cr_refunded_cdemo_sk:String,
                       cr_refunded_hdemo_sk:String,cr_refunded_addr_sk:String, cr_returning_customer_sk:String,cr_returning_cdemo_sk:String,cr_returning_hdemo_sk:String,
                       cr_returning_addr_sk:String,cr_call_center_sk:String,cr_catalog_page_sk:String,cr_ship_mode_sk:String, cr_warehouse_sk:String,
                       cr_reason_sk:String,cr_order_number:String,cr_return_quantity:Int,cr_return_amount:Float,cr_return_tax:Float,cr_return_amt_inc_tax:Float,
                       cr_fee:Float, cr_return_ship_cost:Float,cr_refunded_cash:Float,cr_reversed_charge:Float,cr_store_credit:Float,
                       cr_net_loss:Float)extends Product{

  def canEqual(that: Any) = that.isInstanceOf[catalog_returns]
  def productArity = 27
  def productElement(n: Int) = n match {
    case 0=>cr_returned_date_sk
    case 1=>cr_returned_time_sk
    case 2=>cr_item_sk
    case 3=>cr_refunded_customer_sk
    case 4=>cr_refunded_cdemo_sk
    case 5=>cr_refunded_hdemo_sk
    case 6=>cr_refunded_addr_sk
    case 7=>cr_returning_customer_sk
    case 8=>cr_returning_cdemo_sk
    case 9=>cr_returning_hdemo_sk
    case 10=>cr_returning_addr_sk
    case 11=>cr_call_center_sk
    case 12=>cr_catalog_page_sk
    case 13=>cr_ship_mode_sk
    case 14=>cr_warehouse_sk
    case 15=>cr_reason_sk
    case 16=>cr_order_number
    case 17=>cr_return_quantity
    case 18=>cr_return_amount
    case 19=>cr_return_tax
    case 20=>cr_return_amt_inc_tax
    case 21=>cr_fee
    case 22=>cr_return_ship_cost
    case 23=>cr_refunded_cash
    case 24=>cr_reversed_charge
    case 25=>cr_store_credit
    case 26=>cr_net_loss
  }

}
