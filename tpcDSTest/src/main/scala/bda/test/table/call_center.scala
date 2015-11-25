package bda.test.table

/**
 * Created by Administrator on 2015/5/21.
 */
class call_center (cc_call_center_sk:String,cc_call_center_id:String,cc_rec_start_date:String,cc_rec_end_date:String,cc_closed_date_sk:Int,
                   cc_open_date_sk:Int,cc_name:String, cc_class:String,cc_employees:Int,cc_sq_ft:Int,
                   cc_hours:String,cc_manager:String,cc_mkt_id:Int,cc_mkt_class:String, cc_mkt_desc:String,
                   cc_market_manager:String,cc_division:Int,cc_division_name:String,cc_company:Int,cc_company_name:String,cc_street_number:String,
                   cc_street_name:String, cc_street_type:String,cc_suite_number:String,cc_city:String,cc_county:String,
                   cc_state:String,cc_zip:String,cc_country:String, cc_gmt_offset:Float,cc_tax_percentage:Float)extends Product{

  def canEqual(that: Any) = that.isInstanceOf[call_center]
  def productArity = 31
  def productElement(n: Int) = n match {
    case 0=>cc_call_center_sk
    case 1=>cc_call_center_id
    case 2=>cc_rec_start_date
    case 3=>cc_rec_end_date
    case 4=>cc_closed_date_sk
    case 5=>cc_open_date_sk
    case 6=>cc_name
    case 7=>cc_class
    case 8=>cc_employees
    case 9=>cc_sq_ft
    case 10=>cc_hours
    case 11=>cc_manager
    case 12=>cc_mkt_id
    case 13=>cc_mkt_class
    case 14=>cc_mkt_desc
    case 15=>cc_market_manager
    case 16=>cc_division
    case 17=>cc_division_name
    case 18=>cc_company
    case 19=>cc_company_name
    case 20=>cc_street_number
    case 21=>cc_street_name
    case 22=>cc_street_type
    case 23=>cc_suite_number
    case 24=>cc_city
    case 25=>cc_county
    case 26=>cc_state
    case 27=>cc_zip
    case 28=>cc_country
    case 29=>cc_gmt_offset
    case 30=>cc_tax_percentage
  }

}
