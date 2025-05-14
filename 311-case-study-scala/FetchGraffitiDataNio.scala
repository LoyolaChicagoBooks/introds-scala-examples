//> using scala "3.3.5"
//> using dep "com.lihaoyi::mainargs:0.7.6"

import java.net.URI
import java.nio.channels.Channels
import java.io.FileOutputStream
import java.nio.file.Paths
import mainargs._

object FetchGraffitiData:
  val datasetURL = "https://data.cityofchicago.org/api/views/hec5-y4x5/rows.csv?accessType=DOWNLOAD"

  @main
  def run(
    @arg(name = "output", short = 'o', doc = "Path to save the downloaded CSV file")
    output: String = "311_graffiti.csv"
  ): Unit =
    val readableByteChannel = Channels.newChannel(URI.create(datasetURL).toURL.openStream())
    val fileOutputStream = new FileOutputStream(Paths.get(output).toFile)

    fileOutputStream.getChannel.transferFrom(readableByteChannel, 0, Long.MaxValue)

    readableByteChannel.close()
    fileOutputStream.close()

    println(s"Downloaded dataset to $output")

  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args)

