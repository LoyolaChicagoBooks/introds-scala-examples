# Monte Carlo Simulation in Scala Using the SMILE Library

[SMILE](https://haifengl.github.io/), the Statistical Machine Intelligence and Learning Engine, includes a general-purpose math API we will explore here.

This example also illustrates how we can run Scala code as scripts.

## Data Generation

The first stage of the example performs the Monte Carlo simulation.

```scala
❯ scala-cli GenerateDarts.scala -- --help
run
Generate random darts for Monte Carlo simulation
  -n <int>           Number of darts to generate
  -o --output <str>  Output CSV file
```

For example:

```scala
❯ scala-cli GenerateDarts.scala
Compiling project (Scala 3.3.7, JVM (25))
Compiled project (Scala 3.3.7, JVM (25))
....................................................................................................
Generated 1000000 darts and saved to darts.csv
```

# Area Estimation

The second stage of the example estimates the area based on the provided input data.

```scala
❯ scala-cli EstimateArea.scala -- --help
run
Monte Carlo area estimator
  -i --input <str>   Input CSV file of darts (default: darts.csv)
  -o --output <str>  Optional output file to write the estimated area
```

For example:

```scala
❯ scala-cli EstimateArea.scala
Estimated area of unit circle: 3.14318 using 785795/1000000 darts
```