package bda.test.table

/**
 * Created by Administrator on 2015/5/21.
 */
class web_site(web_site_sk:String,web_site_id:String,web_rec_start_date:String,web_rec_end_date:String,web_name:String,
               web_open_date_sk:String,web_close_date_sk:String, web_class:String,web_manager:String,web_mkt_id:Int,
               web_mkt_class:String,web_mkt_desc:String,web_market_manager:String,web_company_id:Int, web_company_name:String,
               web_street_number:String,web_street_name:String,web_street_type:String,web_suite_number:String,web_city:String,web_county:String,
               web_state:String, web_zip:String,web_country:String,web_gmt_offset:Float,web_tax_percentage:Float)extends Product{

  def canEqual(that: Any) = that.isInstanceOf[web_site]
  def productArity = 26
  def productElement(n: Int) = n match {
    case 0=>web_site_sk
    case 1=>web_site_id
    case 2=>web_rec_start_date
    case 3=>web_rec_end_date
    case 4=>web_name
    case 5=>web_open_date_sk
    case 6=>web_close_date_sk
    case 7=>web_class
    case 8=>web_manager
    case 9=>web_mkt_id
    case 10=>web_mkt_class
    case 11=>web_mkt_desc
    case 12=>web_market_manager
    case 13=>web_company_id
    case 14=>web_company_name
    case 15=>web_street_number
    case 16=>web_street_name
    case 17=>web_street_type
    case 18=>web_suite_number
    case 19=>web_city
    case 20=>web_county
    case 21=>web_state
    case 22=>web_zip
    case 23=>web_country
    case 24=>web_gmt_offset
    case 25=>web_tax_percentage
  }

}
