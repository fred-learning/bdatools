package bda.test.table

/**
 * wreated by Administrator on 2015/5/21.
 */
class web_returns (wr_returned_date_sk:String,wr_returned_time_sk:String,wr_item_sk:String,wr_refunded_customer_sk:String,wr_refunded_cdemo_sk:String,
                   wr_refunded_hdemo_sk:String,wr_refunded_addr_sk:String, wr_returning_customer_sk:String,wr_returning_cdemo_sk:String,
                   wr_returning_hdemo_sk:String, wr_returning_addr_sk:String,wr_web_page_sk:String, wr_reason_sk:String,wr_order_number:String,
                   wr_return_quantity:Int,wr_return_amount:Float,wr_return_tax:Float,wr_return_amt_inc_tax:Float, wr_fee:Float,
                   wr_return_ship_cost:Float,wr_refunded_cash:Float,wr_reversed_charge:Float,wr_account_credit:Float, wr_net_loss:Float)extends Product{

  def canEqual(that: Any) = that.isInstanceOf[web_returns]
  def productArity = 24
  def productElement(n: Int) = n match {
    case 0=>wr_returned_date_sk
    case 1=>wr_returned_time_sk
    case 2=>wr_item_sk
    case 3=>wr_refunded_customer_sk
    case 4=>wr_refunded_cdemo_sk
    case 5=>wr_refunded_hdemo_sk
    case 6=>wr_refunded_addr_sk
    case 7=>wr_returning_customer_sk
    case 8=>wr_returning_cdemo_sk
    case 9=>wr_returning_hdemo_sk
    case 10=>wr_returning_addr_sk
    case 11=>wr_web_page_sk
    case 12=>wr_reason_sk
    case 13=>wr_order_number
    case 14=>wr_return_quantity
    case 15=>wr_return_amount
    case 16=>wr_return_tax
    case 17=>wr_return_amt_inc_tax
    case 18=>wr_fee
    case 19=>wr_return_ship_cost
    case 20=>wr_refunded_cash
    case 21=>wr_reversed_charge
    case 22=>wr_account_credit
    case 23=>wr_net_loss
  }

}
