package bda.test.table

/**
 * Created by Administrator on 2015/5/20.
 */
//table 8:store
class store(s_store_sk:String,s_store_id:String,s_rec_start_date:String,s_rec_end_date:String,s_closed_date_sk:String,s_store_name:String,
                  s_number_employees:Int,s_floor_space:Int,s_hours:String,s_manager:String,s_market_id:Int,s_geography_class:String,
                  s_market_desc:String,s_market_manager:String,s_division_id:Int,s_division_name:String,s_company_id:Int,s_company_name:String,
                  s_street_number:String,s_street_name:String,s_street_type:String,s_suite_number:String,s_city:String,s_county:String,
                  s_state:String,s_zip:String,s_country:String,s_gmt_offset:Float,s_tax_percentage:Float) extends Product{

  def canEqual(that: Any) = that.isInstanceOf[store]
  def productArity = 29
  def productElement(n: Int) = n match {
    case 0=>s_store_sk
    case 1=>s_store_id
    case 2=>s_rec_start_date
    case 3=>s_rec_end_date
    case 4=>s_closed_date_sk
    case 5=>s_store_name
    case 6=>s_number_employees
    case 7=>s_floor_space
    case 8=>s_hours
    case 9=>s_manager
    case 10=>s_market_id
    case 11=>s_geography_class
    case 12=>s_market_desc
    case 13=>s_market_manager
    case 14=>s_division_id
    case 15=>s_division_name
    case 16=>s_company_id
    case 17=>s_company_name
    case 18=>s_street_number
    case 19=>s_street_name
    case 20=>s_street_type
    case 21=>s_suite_number
    case 22=>s_city
    case 23=>s_county
    case 24=>s_state
    case 25=>s_zip
    case 26=>s_country
    case 27=>s_gmt_offset
    case 28=>s_tax_percentage
  }
}
