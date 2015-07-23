package bda.test

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2015/3/8.
 */
object Kmeans {
  def main(args: Array[String]) {
    //0:filename
    //1:numclusters
    //2:numiterations

    val sparkConf = new SparkConf()
    val sc = new SparkContext(sparkConf)
    val data = sc.textFile(args(0))
    val parsedData = data.map(s=>Vectors.dense(s.split(' ').map(_.toDouble))).cache()

    val numClusters = args(1).toInt
    val numIterations = args(2).toInt
    val clusters = KMeans.train(parsedData,numClusters,numIterations)

    val WSSE = clusters.computeCost(parsedData)
    println("Within Set Sum of Squared Errors = " + WSSE)
    sc.stop()
  }
}
