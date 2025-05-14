//> using scala "3.3.5"
//> using dep "com.lihaoyi::mainargs:0.7.6"
//> using dep "me.tongfei:progressbar:0.10.1"

import java.io.{FileOutputStream, InputStream}
import java.net.URI
import java.nio.file.Paths
import java.nio.channels.Channels
import me.tongfei.progressbar.ProgressBar
import mainargs._

object FetchGraffitiData:
  val datasetURL = "https://data.cityofchicago.org/api/views/hec5-y4x5/rows.csv?accessType=DOWNLOAD"

  @main
  def run(
    @arg(name = "output", short = 'o', doc = "Path to save the downloaded CSV file")
    output: String = "311_graffiti.csv"
  ): Unit =
    val url = URI.create(datasetURL).toURL
    val conn = url.openConnection()
    val totalSize = conn.getContentLengthLong
    val inputStream: InputStream = conn.getInputStream

    val rbc = Channels.newChannel(inputStream)
    val out = new FileOutputStream(Paths.get(output).toFile)
    val outChannel = out.getChannel

    val buffer = java.nio.ByteBuffer.allocate(8192)
    var downloaded: Long = 0

    val progressBar = new ProgressBar("Downloading", totalSize)

    while (rbc.read(buffer) != -1) {
      buffer.flip()
      val bytes = outChannel.write(buffer)
      downloaded += bytes
      progressBar.stepBy(bytes)
      buffer.clear()
    }

    progressBar.close()
    rbc.close()
    out.close()

    println(s"Downloaded $downloaded bytes to $output")

  def main(args: Array[String]): Unit =
    ParserForMethods(this).runOrExit(args)

