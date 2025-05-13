//> using scala "3.3.5"
//> using dep "com.github.haifengl::smile-scala:4.3.0"
//> using dep "com.lihaoyi::mainargs:0.7.6"
//> using dep "org.apache.commons:commons-csv:1.14.0"

import java.nio.file.Paths
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.jdk.CollectionConverters.*

import smile.data._
import smile.io.Read

import mainargs._
import org.apache.commons.csv.CSVFormat

object FilterGraffitiData:

  @main
  def run(
    @arg(name = "input", short = 'i', doc = "Path to the input CSV file")
    input: String,
    @arg(name = "status", short = 's', doc = "Filter by request status (e.g., Completed)")
    status: Option[String] = None,
    @arg(name = "start-date", doc = "Filter requests created on or after this date (YYYY-MM-DD)")
    startDate: Option[String] = None,
    @arg(name = "end-date", doc = "Filter requests created on or before this date (YYYY-MM-DD)")
    endDate: Option[String] = None,
    @arg(name = "limit", short = 'l', doc = "Maximum number of rows to display")
    limit: Int = 5
  ): Unit =
    val format = CSVFormat.DEFAULT.withFirstRecordAsHeader()
    val df = Read.csv(Paths.get(input), format)

    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    val filtered = df.stream().filter { row =>
      val rowStatus = row.get("Status")
      val rowDate = LocalDate.parse(row.get("Creation Date").toString, formatter)
      //val rowDate = LocalDate.parse(row.get("Creation Date"), formatter)

      val statusOK = status.forall(_ == rowStatus)
      val startOK = startDate.forall(date => !rowDate.isBefore(LocalDate.parse(date)))
      val endOK = endDate.forall(date => !rowDate.isAfter(LocalDate.parse(date)))

      statusOK && startOK && endOK
    }.toList.asScala

    println(s"Filtered dataset has ${filtered.size} rows.")
    println(s"First ${Math.min(limit, filtered.size)} matching rows:")
    filtered.take(limit).foreach(println)

  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args)
