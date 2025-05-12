//> using scala 3.3.6
//> using dep "com.lihaoyi::mainargs:0.7.6"

import mainargs.{main, arg, ParserForMethods}
import java.nio.file.{Path, Files, StandardOpenOption}
import java.nio.charset.StandardCharsets

object GenerateDarts:

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args.toIndexedSeq)

  @main(doc = "Generate random darts for Monte Carlo simulation")
  def run(
    @arg(short = 'n', doc = "Number of darts to generate") n: Int = 1000000,
    @arg(short = 'o', doc = "Output CSV file") output: String = "darts.csv"
  ): Unit =
    
    val random = scala.util.Random

    Files.writeString(Path.of(output), s"x,y,inside\n", StandardCharsets.UTF_8)

    val sb = new StringBuilder
    (1 to n).foreach: i =>
      val x = random.nextDouble() * 2 - 1
      val y = random.nextDouble() * 2 - 1
      val inside = x * x + y * y <= 1
      sb.append(s"$x,$y,$inside\n")

    Files.writeString(Path.of(output), sb, StandardCharsets.UTF_8, StandardOpenOption.APPEND)

    println(s"Generated $n darts and saved to $output")
