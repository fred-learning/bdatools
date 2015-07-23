package bda.test

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._

/**
 * Created by Administrator on 2015/4/16.
 */
object TextSearch {
  def main (args: Array[String]): Unit = {
    if (args.length<1){
      System.err.println("Usage: search <inputpath>")
      System.exit(1)
    }
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val file = sc.textFile(args(0))
    val errors = file.filter(line => line.contains("0"))
    // Count all the errors
    errors.count()
    // Count errors mentioning MySQL
    errors.filter(line => line.contains("1")).count()
    // Fetch the MySQL errors as an array of strings
    errors.filter(line => line.contains("1")).collect()
    System.out.println("finish search!")
    sc.stop()
  }
}
