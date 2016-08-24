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

## Creating Edge and Node Files

### Ratings by 16 year olds

```
select concat("user_",user_id) as source, concat("movie_",movie_id) as target, "rated" as type, rating.id, rating as label, rating/5 as weight into outfile 'ratings.16.csv'  FIELDS terminated by ',' OPTIONALLY enclosed by '"' LINES terminated by '\n' from rating INNER JOIN user on rating.user_id = user.id WHERE user.age = 16;
```

Users that are 16:
```
select concat("user_",id) as id, age, gender, job into outfile 'users.16.csv'  FIELDS terminated by ',' OPTIONALLY enclosed by '"' LINES terminated by '\n' from user WHERE user.age = 16;
```

Find the movies that they rated:
```
select concat("movie_",id) as id, title as label, IF(action, 'true', 'false'),  IF( adventure, 'true', 'false'),  IF( animation, 'true', 'false'),  IF( children , 'true', 'false'),  IF( comedy , 'true', 'false'),  IF( crime , 'true', 'false'),  IF( documentary , 'true', 'false'),  IF( drama , 'true', 'false'),  IF( fantasy , 'true', 'false'),  IF( filmnoir , 'true', 'false'),  IF( horror , 'true', 'false'),  IF( musical , 'true', 'false'),  IF( mystery , 'true', 'false'),  IF( romance , 'true', 'false'),  IF( scifi , 'true', 'false'),  IF( theater , 'true', 'false'), IF( thriller , 'true', 'false'), IF( unknown , 'true', 'false'), IF( war, 'true', 'false'), IF( western, 'true', 'false') into outfile 'movies.16.csv'  FIELDS terminated by ',' OPTIONALLY enclosed by '"' LINES terminated by '\n' from movie WHERE id in (select movie_id from rating INNER JOIN user on rating.user_id = user.id WHERE user.age = 16);
```

### Top 30 movies for users over the age of 40

```
SELECT * FROM (SELECT concat("movie_",movie.id) as id, title as label,
       avg(rating) AS avg_rating,
       count(rating) AS count_rating,
                      CASE
   WHEN animation = 1 THEN '3'
   WHEN documentary = 1 THEN '2'
              WHEN action = 1
                   OR children =1
                   OR comedy =1 THEN '1'

              ELSE '0'
          END AS actionchildrencomedy ,
          CASE
              WHEN action = 1 THEN '1'
              WHEN action = 0 THEN '0'
              ELSE 'NN'
          END AS action ,
          CASE
              WHEN adventure = 1 THEN '1'
              WHEN adventure = 0 THEN '0'
              ELSE 'NN'
          END AS adventure ,
          CASE
              WHEN animation = 1 THEN '1'
              WHEN animation = 0 THEN '0'
              ELSE 'NN'
          END AS animation ,
          CASE
              WHEN children = 1 THEN '1'
              WHEN children = 0 THEN '0'
              ELSE 'NN'
          END AS children ,
          CASE
              WHEN comedy = 1 THEN '1'
              WHEN comedy = 0 THEN '0'
              ELSE 'NN'
          END AS comedy ,
          CASE
              WHEN crime = 1 THEN '1'
              WHEN crime = 0 THEN '0'
              ELSE 'NN'
          END AS crime ,
          CASE
              WHEN documentary = 1 THEN '1'
              WHEN documentary = 0 THEN '0'
              ELSE 'NN'
          END AS documentary ,
          CASE
              WHEN drama = 1 THEN '1'
              WHEN drama = 0 THEN '0'
              ELSE 'NN'
          END AS drama ,
          CASE
              WHEN fantasy = 1 THEN '1'
              WHEN fantasy = 0 THEN '0'
              ELSE 'NN'
          END AS fantasy ,
          CASE
              WHEN filmnoir = 1 THEN '1'
              WHEN filmnoir = 0 THEN '0'
              ELSE 'NN'
          END AS filmnoir ,
          CASE
              WHEN horror = 1 THEN '1'
              WHEN horror = 0 THEN '0'
              ELSE 'NN'
          END AS horror ,
          CASE
              WHEN musical = 1 THEN '1'
              WHEN musical = 0 THEN '0'
              ELSE 'NN'
          END AS musical ,
          CASE
              WHEN mystery = 1 THEN '1'
              WHEN mystery = 0 THEN '0'
              ELSE 'NN'
          END AS mystery ,
          CASE
              WHEN romance = 1 THEN '1'
              WHEN romance = 0 THEN '0'
              ELSE 'NN'
          END AS romance ,
          CASE
              WHEN scifi = 1 THEN '1'
              WHEN scifi = 0 THEN '0'
              ELSE 'NN'
          END AS scifi ,
          CASE
              WHEN thriller = 1 THEN '1'
              WHEN thriller = 0 THEN '0'
              ELSE 'NN'
          END AS thriller ,
          CASE
              WHEN UNKNOWN = 1 THEN '1'
              WHEN UNKNOWN = 0 THEN '0'
              ELSE 'NN'
          END AS UNKNOWN ,
          CASE
              WHEN war = 1 THEN '1'
              WHEN war = 0 THEN '0'
              ELSE 'NN'
          END AS war ,
          CASE
              WHEN western = 1 THEN '1'
              WHEN western = 0 THEN '0'
              ELSE 'NN'
          END AS western
FROM rating
INNER JOIN movie ON rating.movie_id = movie.id
INNER JOIN user ON rating.user_id = user.id
               WHERE age>40
GROUP BY movie.id
ORDER BY avg_rating DESC) r
WHERE count_rating > 30 limit 30
```

```
SELECT concat("user_",user_id) as source, concat("movie_",movie_id) as target, rating as label, rating/5 as weight, rating.id as id
FROM rating
INNER JOIN
  (SELECT id
   FROM user
   WHERE user.age > 40) AS u2 ON rating.user_id = u2.id
INNER JOIN
  (SELECT id
   FROM
     (SELECT movie.id,
             title AS label,
             avg(rating) AS avg_rating,
             count(rating) AS count_rating
      FROM rating
      INNER JOIN movie ON rating.movie_id = movie.id
      INNER JOIN user AS u1 ON rating.user_id = u1.id
      WHERE age>40
      GROUP BY movie.id
      ORDER BY avg_rating DESC) r
   WHERE count_rating > 30 LIMIT 30) AS m2 ON rating.movie_id = m2.id limit 500
```

```
SELECT concat("user_",user_id) as id, age, gender, job
FROM rating
INNER JOIN
  (SELECT *
   FROM user
   WHERE user.age > 40) AS u2 ON rating.user_id = u2.id
INNER JOIN
  (SELECT id
   FROM
     (SELECT movie.id,
             title AS label,
             avg(rating) AS avg_rating,
             count(rating) AS count_rating
      FROM rating
      INNER JOIN movie ON rating.movie_id = movie.id
      INNER JOIN user AS u1 ON rating.user_id = u1.id
      WHERE age>40
      GROUP BY movie.id
      ORDER BY avg_rating DESC) r
   WHERE count_rating > 30 LIMIT 30) AS m2 ON rating.movie_id = m2.id GROUP BY user_id limit 500
 ```

## License

[MIT](/LICENSE)

F. Maxwell Harper and Joseph A. Konstan. 2015. The MovieLens Datasets: History and Context. ACM Transactions on Interactive Intelligent Systems (TiiS) 5, 4, Article 19 (December 2015), 19 pages. DOI=http://dx.doi.org/10.1145/2827872
