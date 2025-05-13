//> using scala "3.3.5"
//> using dep "com.github.haifengl::smile-scala:4.3.0"
//> using dep "com.lihaoyi::mainargs:0.7.6"
//> using dep "org.apache.commons:commons-csv:1.14.0"

import mainargs._
import smile.data._
import smile.io.Read

// Smile CSV is supported by Apache
// Need to dig into the API to do CSV handling
// IMHO, this shows a strength of Scala (leveraging rich Java ecosystem much as Python leverages C and C++

import org.apache.commons.csv.CSVFormat
import scala.jdk.CollectionConverters._

object LoadGraffitiData:
  @main
  def run(
    @arg(name = "input", short = 'i', doc = "Path to the input CSV file")
    input: String
  ): Unit =
    val format = CSVFormat.DEFAULT.withFirstRecordAsHeader()
    val df = Read.csv(java.nio.file.Paths.get(input), format)
    println(s"Loaded dataset with ${df.nrow} rows and ${df.ncol} columns.")
    println("First 5 rows:")
    // println(df.slice(0, 5).toString)
    df.stream().limit(5).forEach(row => println(row))


  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args)

