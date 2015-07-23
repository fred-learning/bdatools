package bda.test

import java.io._

import bda.test.CaseTables._
import bda.test.table._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark._

import scala.io.Source

/**
 * Created by Administrator on 2015/5/20.
 */
object TPCDSCluster {

  def registerTable(sc:SparkContext,sqlc:SQLContext,path:String)={
    import sqlc.implicits._
    //table 1:store_sales
    val rdd_store_sales = sc.textFile(path+"store_sales.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>new store_sales(p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9),p(10).trim.toInt, p(11).trim.toFloat, p(12).trim.toFloat,
          p(13).trim.toFloat, p(14).trim.toFloat, p(15).trim.toFloat, p(16).trim.toFloat, p(17).trim.toFloat, p(18).trim.toFloat,
          p(19).trim.toFloat, p(20).trim.toFloat, p(21).trim.toFloat, p(22).trim.toFloat)).toDF()
    rdd_store_sales.registerTempTable("store_sales")

    //table 2:store_returns
    val rdd_store_returns = sc.textFile(path+"store_returns.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>store_returns(p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9),p(10).trim.toInt, p(11).trim.toFloat, p(12).trim.toFloat,
          p(13).trim.toFloat, p(14).trim.toFloat, p(15).trim.toFloat, p(16).trim.toFloat, p(17).trim.toFloat, p(18).trim.toFloat,
          p(19).trim.toFloat)).toDF()
    rdd_store_returns.registerTempTable("store_returns")

    //table 3:catalog_sales
    val rdd_catalog_sales = sc.textFile(path+"catalog_sales.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>new catalog_sales(p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9),p(10), p(11), p(12), p(13),
          p(14), p(15), p(16), p(17), p(18).trim.toInt, p(19).trim.toFloat,p(20).trim.toFloat, p(21).trim.toFloat,
          p(22).trim.toFloat, p(23).trim.toFloat, p(24).trim.toFloat, p(25).trim.toFloat, p(26).trim.toFloat, p(27).trim.toFloat,
          p(28).trim.toFloat, p(29).trim.toFloat,p(30).trim.toFloat, p(31).trim.toFloat, p(32).trim.toFloat, p(33).trim.toFloat)).toDF()
    rdd_catalog_sales.registerTempTable("catalog_sales")

    //table 4:catalog_returns
    val rdd_catalog_returns = sc.textFile(path+"catalog_returns.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>new catalog_returns(p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9),p(10), p(11), p(12), p(13),
          p(14), p(15), p(16), p(17).trim.toInt, p(18).trim.toFloat, p(19).trim.toFloat,p(20).trim.toFloat, p(21).trim.toFloat,
          p(22).trim.toFloat, p(23).trim.toFloat, p(24).trim.toFloat, p(25).trim.toFloat, p(26).trim.toFloat)).toDF()
    rdd_catalog_returns.registerTempTable("catalog_returns")

    //table 5:web_sales
    val rdd_web_sales = sc.textFile(path+"web_sales.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>new web_sales(p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9),p(10), p(11), p(12), p(13),
          p(14), p(15), p(16), p(17), p(18).trim.toInt, p(19).trim.toFloat,p(20).trim.toFloat, p(21).trim.toFloat,
          p(22).trim.toFloat, p(23).trim.toFloat, p(24).trim.toFloat, p(25).trim.toFloat, p(26).trim.toFloat, p(27).trim.toFloat,
          p(28).trim.toFloat, p(29).trim.toFloat,p(30).trim.toFloat, p(31).trim.toFloat, p(32).trim.toFloat, p(33).trim.toFloat)).toDF()
    rdd_web_sales.registerTempTable("web_sales")

    //table 6:web_returns
    val rdd_web_returns = sc.textFile(path+"web_returns.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>new web_returns(p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9),p(10), p(11), p(12), p(13),
          p(14).trim.toInt, p(15).trim.toFloat, p(16).trim.toFloat, p(17).trim.toFloat, p(18).trim.toFloat, p(19).trim.toFloat,
          p(20).trim.toFloat, p(21).trim.toFloat, p(22).trim.toFloat, p(23).trim.toFloat)).toDF()
    rdd_web_returns.registerTempTable("web_returns")

    //table 7:inventory
    val rdd_inventory = sc.textFile(path+"inventory.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>inventory(p(0), p(1), p(2), p(3).trim.toInt)).toDF()
    rdd_inventory.registerTempTable("inventory")

    //table 8:store
    val rdd_store = sc.textFile(path+"store.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>new store(p(0), p(1), p(2), p(3), p(4), p(5), p(6).trim.toInt, p(7).trim.toInt, p(8), p(9),p(10).trim.toInt, p(11),
          p(12), p(13), p(14).trim.toInt, p(15), p(16).trim.toInt, p(17), p(18), p(19), p(20), p(21), p(22), p(23), p(24), p(25),
          p(26), p(27).trim.toFloat, p(28).trim.toFloat)).toDF()
    rdd_store.registerTempTable("store")

    //table 9:call_center
    val rdd_call_center = sc.textFile(path+"call_center.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>new call_center(p(0), p(1), p(2), p(3), p(4).trim.toInt, p(5).trim.toInt, p(6), p(7), p(8).trim.toInt, p(9).trim.toInt,p(10),
          p(11), p(12).trim.toInt, p(13), p(14), p(15), p(16).trim.toInt, p(17), p(18).trim.toInt, p(19),p(20), p(21), p(22), p(23),
          p(24), p(25), p(26), p(27), p(28), p(29).trim.toFloat,p(30).trim.toFloat)).toDF()
    rdd_call_center.registerTempTable("call_center")

    //table 10:catalog_page
    val rdd_catalog_page = sc.textFile(path+"catalog_page.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>catalog_page(p(0), p(1), p(2).trim.toInt, p(3).trim.toInt, p(4), p(5).trim.toInt, p(6).trim.toInt, p(7), p(8))).toDF()
    rdd_catalog_page.registerTempTable("catalog_page")

    //table 11:web_site
    val rdd_web_site = sc.textFile(path+"web_site.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>new web_site(p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9).trim.toInt,p(10), p(11), p(12),
          p(13).trim.toInt, p(14), p(15), p(16), p(17), p(18),p(19),p(20), p(21), p(22), p(23),
          p(24).trim.toFloat, p(25).trim.toFloat)).toDF()
    rdd_web_site.registerTempTable("web_site")

    //table 12:web_page
    val rdd_web_page = sc.textFile(path+"web_page.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>web_page(p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9),p(10).trim.toInt,
          p(11).trim.toInt, p(12).trim.toInt, p(13).trim.toInt)).toDF()
    rdd_web_page.registerTempTable("web_page")

    //table 13:
    val rdd_warehouse = sc.textFile(path+"warehouse.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>warehouse(p(0), p(1), p(2), p(3).trim.toInt, p(4), p(5), p(6), p(7), p(8), p(9),p(10), p(11), p(12), p(13).trim.toFloat)).toDF()
    rdd_warehouse.registerTempTable("warehouse")

    //table 14:customer
    val rdd_customer = sc.textFile(path+"customer.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>customer(p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9),p(10), p(11).trim.toInt, p(12).trim.toInt, p(13).trim.toInt,
          p(14), p(15), p(16), p(17))).toDF()
    rdd_customer.registerTempTable("customer")

    //table 15:
    val rdd_customer_address = sc.textFile(path+"customer_address.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>customer_address(p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9),p(10), p(11).trim.toFloat, p(12))).toDF()
    rdd_customer_address.registerTempTable("customer_address")

    //table 16:customer_demographics
    val rdd_customer_demographics = sc.textFile(path+"customer_demographics.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>customer_demographics(p(0), p(1), p(2), p(3), p(4).trim.toInt, p(5), p(6).trim.toInt, p(7).trim.toInt, p(8).trim.toInt)).toDF()
    rdd_customer_demographics.registerTempTable("customer_demographics")

    //table 17:date_dim
    val rdd_date_dim = sc.textFile(path+"date_dim.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>new date_dim(p(0), p(1), p(2), p(3).trim.toInt, p(4).trim.toInt, p(5).trim.toInt, p(6).trim.toInt, p(7).trim.toInt, p(8).trim.toInt,
          p(9).trim.toInt,p(10).trim.toInt, p(11).trim.toInt, p(12).trim.toInt, p(13).trim.toInt, p(14), p(15), p(16), p(17), p(18),
          p(19).trim.toInt,p(20).trim.toInt, p(21).trim.toInt, p(22).trim.toInt, p(23), p(24), p(25), p(26), p(27))).toDF()
    rdd_date_dim.registerTempTable("date_dim")

    //table 18:household_demographics
    val rdd_household_demographics = sc.textFile(path+"household_demographics.dat").map(
      _.split("\\|",-1).map(t=>{if(t.equals("")) "0" else t})).map(
        p =>household_demographics(p(0), p(1), p(2), p(3).trim.toInt, p(4).trim.toInt)).toDF()
    rdd_household_demographics.registerTempTable("household_demographics")

    //table 19:item
    val rdd_item = sc.textFile(path+"item.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>item(p(0), p(1), p(2), p(3), p(4), p(5).trim.toFloat, p(6).trim.toFloat, p(7).trim.toInt, p(8), p(9).trim.toInt,p(10),
          p(11).trim.toInt, p(12), p(13).trim.toInt, p(14), p(15), p(16), p(17), p(18), p(19),p(20).trim.toInt, p(21))).toDF()
    rdd_item.registerTempTable("item")

    //table 20:income_band
    val rdd_income_band = sc.textFile(path+"income_band.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>income_band(p(0), p(1).trim.toInt, p(2).trim.toInt)).toDF()
    rdd_income_band.registerTempTable("income_band")

    //table 21:promotion
    val rdd_promotion = sc.textFile(path+"promotion.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>promotion(p(0), p(1), p(2), p(3), p(4), p(5).trim.toFloat, p(6).trim.toInt, p(7), p(8), p(9),p(10), p(11),
          p(12), p(13), p(14), p(15), p(16), p(17), p(18))).toDF()
    rdd_promotion.registerTempTable("promotion")

    //table 22:reason
    val rdd_reason = sc.textFile(path+"reason.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>reason(p(0), p(1), p(2))).toDF()
    rdd_reason.registerTempTable("reason")
    //table 23:ship_mode
    val rdd_ship_mode = sc.textFile(path+"ship_mode.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>ship_mode(p(0), p(1), p(2), p(3), p(4), p(5))).toDF()
    rdd_ship_mode.registerTempTable("ship_mode")

    //table 24:time_dim
    val rdd_time_dim = sc.textFile(path+"time_dim.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>time_dim(p(0), p(1), p(2).trim.toInt, p(3).trim.toInt, p(4).trim.toInt, p(5).trim.toInt, p(6), p(7), p(8), p(9))).toDF()
    rdd_time_dim.registerTempTable("time_dim")

    //table 25:dbgen_version
    val rdd_dbgen_version = sc.textFile(path+"dbgen_version.dat").map(_.split("\\|",-1).map(t=>{
      if(t.equals("")) "0" else t})).map(
        p =>dbgen_version(p(0), p(1), p(2), p(3))).toDF()
    rdd_dbgen_version.registerTempTable("dbgen_version")
  }

  def parseQuery(filepath:String):Array[String]={
    val pattern = "(^select \\* from \\()(.+)(\\) where rownum <= 100$)".r
    var context = "";

    for(line<-Source.fromFile(filepath).getLines){
      if(!line.isEmpty)
        context+=line.trim+" "
    }
    val sqlArr = context.split(";").map(_.trim)
    val filArr = sqlArr.filter(e=>e.startsWith("select"))
    //filArr.foreach(println(_))
    val ret = filArr.map(e=>{
      val ret = pattern.findFirstMatchIn(e)
      ret match {
        case Some(m) =>
          m.group(2)
        case None =>
          e
      }
    })
    //    ret.foreach(println(_))
    ret
  }

  def main(args:Array[String]): Unit = {
    //arg0:data path,hdfs:///yateng/dsdgen_default/
    //arg1:query
    println("the dataset is "+args(0))
    //val queryline = readLine("please input your query:\n")
    val conf = new SparkConf()
    val sc = new SparkContext(conf.setAppName("sql_tpcds_"+args(0)))
    val sqlc = new SQLContext(sc)
    registerTable(sc, sqlc, args(0))

    if (args.length < 2) {
      val query = "select  i_item_id,avg(ss_quantity) agg1,avg(ss_list_price) agg2,avg(ss_coupon_amt) agg3,avg(ss_sales_price) agg4 " +
        "from store_sales, customer_demographics, date_dim, item, promotion where ss_sold_date_sk = d_date_sk and ss_item_sk = i_item_sk " +
        "and ss_cdemo_sk = cd_demo_sk and ss_promo_sk = p_promo_sk and cd_gender = 'M' and cd_marital_status = 'M' and " +
        "cd_education_status = '4 yr Degree' and (p_channel_email = 'N' or p_channel_event = 'N') and d_year = 2001 group by i_item_id " +
        "order by i_item_id"
      println("the query is "+query)
      val ret = sqlc.sql(query)
      println("query over! the count is "+ret.count())
//      ret.foreach(t => println("count:" + t))
    }else{
      val querylist = sc.textFile(args(1)).collect()
      val num = args(2).toInt
      println("the query is "+querylist(num))
      val ret = sqlc.sql(querylist(num))
      println("query over! the count is "+ret.count())
    }
    println("finished!")
    sc.stop()
  }
}
