package bda.test.table

/**
 * Created by Administrator on 2015/5/22.
 */
class date_dim(d_date_sk:String,d_date_id:String,d_date:String,d_month_seq:Int,d_week_seq:Int,
               d_quarter_seq:Int,d_year:Int, d_dow:Int,d_moy:Int,d_dom:Int,
               d_qoy:Int,d_fy_year:Int,d_fy_quarter_seq:Int,d_fy_week_seq:Int, d_day_name:String,
               d_quarter_name:String,d_holiday:String,d_weekend:String,d_following_holiday:String,d_first_dom:Int,d_last_dom:Int,
               d_same_day_ly:Int, d_same_day_lq:Int,d_current_day:String,d_current_week:String,d_current_month:String,
               d_current_quarter:String,d_current_year:String)extends Product{

  def canEqual(that: Any) = that.isInstanceOf[date_dim]
  def productArity = 28
  def productElement(n: Int) = n match {
    case 0=>d_date_sk
    case 1=>d_date_id
    case 2=>d_date
    case 3=>d_month_seq
    case 4=>d_week_seq
    case 5=>d_quarter_seq
    case 6=>d_year
    case 7=>d_dow
    case 8=>d_moy
    case 9=>d_dom
    case 10=>d_qoy
    case 11=>d_fy_year
    case 12=>d_fy_quarter_seq
    case 13=>d_fy_week_seq
    case 14=>d_day_name
    case 15=>d_quarter_name
    case 16=>d_holiday
    case 17=>d_weekend
    case 18=>d_following_holiday
    case 19=>d_first_dom
    case 20=>d_last_dom
    case 21=>d_same_day_ly
    case 22=>d_same_day_lq
    case 23=>d_current_day
    case 24=>d_current_week
    case 25=>d_current_month
    case 26=>d_current_quarter
    case 27=>d_current_year
  }

}
