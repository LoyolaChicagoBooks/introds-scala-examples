//> using scala "3.3.5"
//> using dep "com.lihaoyi::mainargs:0.7.6"

import java.io.{BufferedInputStream, FileOutputStream}
import java.net.URL
import mainargs._

object FetchGraffitiData:
  val datasetURL = "https://data.cityofchicago.org/api/views/hec5-y4x5/rows.csv?accessType=DOWNLOAD"

  @main
  def run(
    @arg(name = "output", short = 'o', doc = "Path to save the downloaded CSV file")
    output: String = "311_graffiti.csv"
  ): Unit =
    val urlStream = new BufferedInputStream(new URL(datasetURL).openStream())
    val fileOut = new FileOutputStream(output)

    urlStream.transferTo(fileOut)

    urlStream.close()
    fileOut.close()

    println(s"Downloaded dataset to $output")

  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args)

