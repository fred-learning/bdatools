package bda.test

import org.apache.spark._

import scala.io.Source

/**
 * Created by Administrator on 2015/5/18.
 */
object Test {
  def main(args:Array[String]): Unit ={

    val pattern = "(^select \\* from \\()(.+)(\\) where rownum <= 100$)".r
    var context = "";
    for(line<-Source.fromFile("D:/query_0.sql").getLines){
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
    ret.foreach(println(_))
  }
}
