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
