//> using scala "3.3.5"
//> using dep "com.lihaoyi::mainargs:0.7.6"
//> using dep "org.apache.commons:commons-csv:1.14.0"

import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters.*
import org.apache.commons.csv.*
import mainargs._

object LoadGraffitiData:
  @main
  def run(
      @arg(name = "input", short = 'i') input: String,
      @arg(name = "limit", short = 'l') limit: Int = 5
  ): Unit =

    val reader =
      Files.newBufferedReader(Paths.get(input), StandardCharsets.UTF_8)
    val parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)

    val headers = parser.getHeaderNames.asScala
    println(s"Headers: ${headers.mkString(", ")}")

    val iter = parser.iterator().asScala
    println(s"\nFirst $limit rows:")
    iter
      .take(limit)
      .foreach: record =>
        val row = headers.map(h => s"$h=${record.get(h)}").mkString(", ")
        println(row)

  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args)
