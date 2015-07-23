package bda.test

import org.apache.spark._
import org.apache.spark.sql.SQLContext
/**
 * Created by Administrator on 2015/5/20.
 */
object sqltest {
  case class Person(name:String,age:Int)
  def main(args:Array[String]): Unit ={

    val conf = new SparkConf()
    val sc = new SparkContext(conf.setAppName("sql"))
    val sqlc = new SQLContext(sc)

    val df = sqlc.jsonFile("D:/people.json")
    df.show()
    df.printSchema()
    df.select("name").show()
    df.select(df("name"),df("age")+1).show()
    df.filter(df("age")>21).show()
    df.groupBy("age").count().show()

    //val df2 = sqlc.load("D:/people.json", "json")
    //df2.select("name", "age").save("hdfs://192.168.3.57:8020/yateng/namesAndAges.parquet", "parquet")


    import sqlc.implicits._
    val people = sc.textFile("D:/people.txt").map(_.split(",")).map(p => Person(p(0), p(1).trim.toInt)).toDF()
    people.registerTempTable("people")
    val teenagers = sqlc.sql("SELECT name FROM people WHERE age>=13 AND age<=19")
    teenagers.map(t=>"Name:"+t(0)).collect().foreach(println)



    //people.saveAsParquetFile("hdfs://192.168.3.57:8020/yateng/people.parquet")
    val parquetFile = sqlc.parquetFile("hdfs://192.168.3.57:8020/yateng/people.parquet")
    parquetFile.registerTempTable("parquetFile")
    val teenagers2 = sqlc.sql("SELECT name FROM parquetFile WHERE age >= 13 AND age <= 19")
    teenagers2.map(t => "Name: " + t(0)).collect().foreach(println)
    println("finished!")
  }
}
