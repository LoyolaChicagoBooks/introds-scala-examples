//> using scala "3.3.5"
//> using dep "com.lihaoyi::mainargs:0.7.6"
//> using dep "org.apache.commons:commons-csv:1.14.0"

import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.jdk.CollectionConverters.*
import org.apache.commons.csv.*
import mainargs._

object FilterGraffitiData:
  @main
  def run(
    @arg(name = "input", short = 'i') input: String,
    @arg(name = "status", short = 's') status: String = "Completed",
    @arg(name = "start-date") startDate: String = "2025-01-01",
    @arg(name = "end-date") endDate: String = "2025-01-31",
    @arg(name = "limit", short = 'l') limit: Int = 5,
    @arg(name = "count-only", doc = "If set, only print number of matching rows") countOnly: Boolean = false
  ): Unit =
    val reader = Files.newBufferedReader(Paths.get(input), StandardCharsets.UTF_8)
    val parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)
    val headers = parser.getHeaderNames.asScala
    val fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    val filtered = parser.iterator().asScala.filter { record =>
      val rowStatus = record.get("Status")
      val rowDate = LocalDate.parse(record.get("Creation Date"), fmt)

      val statusOK = rowStatus == status
      val startOK = !rowDate.isBefore(LocalDate.parse(startDate))
      val endOK = !rowDate.isAfter(LocalDate.parse(endDate))

      statusOK && startOK && endOK
    }

    if countOnly then
      val total = filtered.size
      println(s"$total matching rows.")
    else
      val taken = filtered.take(limit).toList
      println(s"Showing ${taken.size} matching rows:")
      taken.foreach { record =>
        val row = headers.map(h => s"$h=${record.get(h)}").mkString(", ")
        println(row)
      }

  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args)

