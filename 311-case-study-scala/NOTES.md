# Motivation

A George example to inspire interest in the APIs.
These are working notes and not yet polished.

## **Analyzing 311 Service Requests in Chicago**

This dataset is available from the [Chicago Data Portal](https://data.cityofchicago.org/Service-Requests/311-Service-Requests-Graffiti-Removal/hec5-y4x5) and includes:

* Timestamps (date reported, completed)
* Location (latitude, longitude, street address, zip code)
* Service type and status
* Department assigned

### Why this dataset works well to introduce data science principles

**✅ Covers most Smile features:**

* Readable as CSV (✅)
* Columns are strongly typed: dates, strings, numerics, booleans (✅)
* Grouping by service type, zip code, or completion status (✅)
* Aggregation: count, average response time (✅)
* Filtering by status or timeframe (✅)
* Sorting by response time or volume (✅)
* Joins possible if you pull in zip code boundaries or neighborhoods (✅)
* No need for pivoting, plotting, or complex reshaping
* Bonus: Use Scala/Java for time parsing or grouping by day/month (e.g., via `java.time.LocalDate`)

### Example questions to explore

* How many requests per zip code or community area?
* What is the average completion time per service type?
* Which zip codes have the slowest response?
* What’s the weekly/monthly trend in number of complaints?
* Which types of service are most common?

