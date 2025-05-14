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

object SaveFilteredGraffitiData:
  @main
  def run(
    @arg(name = "input", short = 'i') input: String,
    @arg(name = "output", short = 'o') output: String,
    @arg(name = "status", short = 's') status: Option[String] = None,
    @arg(name = "start-date") startDate: Option[String] = None,
    @arg(name = "end-date") endDate: Option[String] = None
  ): Unit =
    val reader = Files.newBufferedReader(Paths.get(input), StandardCharsets.UTF_8)
    val parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)
    val headers = parser.getHeaderNames.asScala
    val fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    val writer = Files.newBufferedWriter(Paths.get(output), StandardCharsets.UTF_8)
    val printer = CSVFormat.DEFAULT
      .withHeader(headers: _*)
      .print(writer)

    val matched = parser.iterator().asScala.filter { record =>
      val rowStatus = record.get("Status")
      val rowDate = LocalDate.parse(record.get("Creation Date"), fmt)
      val statusOK = status.forall(_ == rowStatus)
      val startOK = startDate.forall(sd => !rowDate.isBefore(LocalDate.parse(sd)))
      val endOK = endDate.forall(ed => !rowDate.isAfter(LocalDate.parse(ed)))
      statusOK && startOK && endOK
    }

    matched.foreach { record =>
      val row = headers.map(record.get).asJava
      printer.printRecord(row)
    }

    printer.close()
    println(s"Wrote filtered records to $output")

  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args)
