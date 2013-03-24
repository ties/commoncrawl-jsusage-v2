in = LOAD 'counts_cdn/part-*' USING PigStorage() as (name, count:long)
sorted = ORDER in BY count DESC;
top100 = LIMIT sorted 100;
dump top100;