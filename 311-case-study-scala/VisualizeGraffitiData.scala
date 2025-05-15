//> using scala "3.3.5"
//> using dep "com.lihaoyi::mainargs:0.7.6"
//> using dep "org.apache.commons:commons-csv:1.14.0"
//> using dep "org.knowm.xchart:xchart:3.8.8"

import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.jdk.CollectionConverters.*
import org.apache.commons.csv.*
import org.knowm.xchart.{CategoryChartBuilder, BitmapEncoder}
import org.knowm.xchart.BitmapEncoder.BitmapFormat
import mainargs._

object VisualizeGraffitiData:
  @main
  def run(
    @arg(name = "input", short = 'i') input: String,
    @arg(name = "output", short = 'o') output: String = "graffiti_trend.png"
  ): Unit =
    val reader = Files.newBufferedReader(Paths.get(input), StandardCharsets.UTF_8)
    val parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get().parse(reader)
    val fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    val monthly = scala.collection.mutable.Map.empty[String, Int].withDefaultValue(0)
    parser.iterator().asScala.foreach: record =>
      val date = LocalDate.parse(record.get("Creation Date"), fmt)
      val key = f"${date.getYear}-${date.getMonthValue}%02d"
      monthly(key) += 1

    val (months, counts) = monthly.toSeq.sortBy(_._1).unzip

    val chart = new CategoryChartBuilder()
      .width(800).height(600)
      .title("Graffiti Removal Requests Per Month")
      .xAxisTitle("Month")
      .yAxisTitle("Requests")
      .build()

    chart.addSeries("Requests", months.asJava, counts.asJava.asInstanceOf[java.util.List[Number]])
    BitmapEncoder.saveBitmap(chart, output, BitmapFormat.PNG)

    println(s"Saved chart to $output")

  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args.toIndexedSeq)
