//> using scala "3.3.5"
//> using dep "com.lihaoyi::mainargs:0.7.6"
//> using dep "org.apache.commons:commons-csv:1.14.0"

import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters.*
import org.apache.commons.csv.*
import mainargs._

object AggregateGraffitiData:
  @main
  def run(
    @arg(name = "input", short = 'i') input: String,
    @arg(name = "group-by", short = 'g') groupBy: String = "ZIP Code",
    @arg(name = "top", short = 't') top: Int = 10
  ): Unit =
    val reader = Files.newBufferedReader(Paths.get(input), StandardCharsets.UTF_8)
    val parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)

    val counter = scala.collection.mutable.Map.empty[String, Int].withDefaultValue(0)
    val iter = parser.iterator().asScala

    iter.foreach: record =>
      val key = record.get(groupBy)
      counter(key) += 1

    val sorted = counter.toSeq.sortBy(-_._2).take(top)
    println(s"Top $top entries grouped by '$groupBy':")
    sorted.foreach:
      case (k, v) => println(f"$k%-20s → $v%5d")

  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args)
