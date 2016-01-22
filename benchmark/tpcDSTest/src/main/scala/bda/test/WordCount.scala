package bda.test

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._

/**
 * Created by Administrator on 2015/3/6.
 */
object WordCount {
  def main (args: Array[String]): Unit = {
    if (args.length<2){
      System.err.println("Usage: WordCount <inputpath><outflag>")
      System.exit(1)
    }
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val result = sc.textFile(args(0)).flatMap(line=>line.split(" ")).map(word=>(word,1)).reduceByKey(_+_)
    result.saveAsTextFile(args(1)+util.Random.nextDouble())
    System.out.println("finish word count!")
    sc.stop()
  }

}
