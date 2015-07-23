package bda.test

import org.apache.spark._

/**
 * Created by Administrator on 2015/5/17.
 */
object DownLoadBBBM {
  def main(args:Array[String]) ={
    val conf = new SparkConf()
    val sc = new SparkContext(conf.setAppName("download bdbm"))
    //sc.hadoopConfiguration.set("fs.s3n.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem")
    sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId","AKIAJOGRMOHNCC6TWJZA")
    sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey","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")

    //val dataset = sc.textFile("s3n://big-data-benchmark/pavlo/text/tiny/crawl")
    val dataset = sc.textFile("s3n://testbyfred/test/1")
    print(dataset.count())
    dataset.foreach(println(_))
    dataset.saveAsTextFile("hdfs://192.168.3.57:8020/yateng/test2")
    println("finished!")
  }
}
