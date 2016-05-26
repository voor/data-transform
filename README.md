# Data Transform

Just a simple utility class for converting [Movie Lens](https://movielens.org/) data into a single, non-normalized CSV for import into other systems.

## Building

```
./mvn clean verify
```

## Running

```
java -jar target/data-transform-*.jar all
```

The resulting file should be at `target/movie-data.csv`

## License

[MIT](/LICENSE)

F. Maxwell Harper and Joseph A. Konstan. 2015. The MovieLens Datasets: History and Context. ACM Transactions on Interactive Intelligent Systems (TiiS) 5, 4, Article 19 (December 2015), 19 pages. DOI=http://dx.doi.org/10.1145/2827872