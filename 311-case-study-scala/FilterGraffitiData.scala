//> using scala "3.3.5"
//> using dep "com.lihaoyi::mainargs:0.7.6"
//> using dep "org.apache.commons:commons-csv:1.10.0"

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
    @arg(name = "status", short = 's') status: Option[String] = None,
    @arg(name = "start-date") startDate: Option[String] = None,
    @arg(name = "end-date") endDate: Option[String] = None,
    @arg(name = "limit", short = 'l') limit: Int = 5
  ): Unit =
    val reader = Files.newBufferedReader(Paths.get(input), StandardCharsets.UTF_8)
    val parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)
    val headers = parser.getHeaderNames.asScala
    val fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    val filtered = parser.iterator().asScala.filter { record =>
      val statusOK = status.forall(s => record.get("Status") == s)
      val createdAt = LocalDate.parse(record.get("Creation Date"), fmt)
      val startOK = startDate.forall(sd => !createdAt.isBefore(LocalDate.parse(sd)))
      val endOK = endDate.forall(ed => !createdAt.isAfter(LocalDate.parse(ed)))
      statusOK && startOK && endOK
    }

    val taken = filtered.take(limit).toList
    println(s"Showing ${taken.size} matching rows:")
    taken.foreach { record =>
      val row = headers.map(h => s"$h=${record.get(h)}").mkString(", ")
      println(row)
    }

  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args)
