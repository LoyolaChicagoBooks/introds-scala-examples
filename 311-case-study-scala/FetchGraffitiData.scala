//> using scala "3.3.5"
//> using dep "com.lihaoyi::mainargs:0.7.6"

import java.net.URI
import java.nio.file.{Files, Paths}
import java.nio.channels.Channels
import mainargs._

object FetchGraffitiData:
  val datasetURL = "https://data.cityofchicago.org/api/views/hec5-y4x5/rows.csv?accessType=DOWNLOAD"

  @main
  def run(
    @arg(name = "output", short = 'o') output: String = "311_graffiti.csv"
  ): Unit =
    val readableByteChannel = Channels.newChannel(URI.create(datasetURL).toURL.openStream())
    val fileOutputStream = Files.newOutputStream(Paths.get(output))
    fileOutputStream.getChannel.transferFrom(readableByteChannel, 0, Long.MaxValue)
    readableByteChannel.close()
    fileOutputStream.close()
    println(s"Downloaded dataset to $output")

  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args)
