-- register '../commoncrawl-jsusage-v2/pig/target/${project.build.finalName}';
register '../commoncrawl-jsusage-v2/pig/target/pig-0.8.2-SNAPSHOT.jar';

in = LOAD 'combinations/part-*' USING PigStorage('\t') as (names:chararray, count:long);

-- generate the combinations from lower-case version of names.
items = FOREACH in GENERATE edu.utwente.mbd.udf.SplitCombinations(LOWER(names)) as names, count;

limitedItems = FILTER items BY SIZE(names) <= 8; -- 8 scripts yields 2^8=256 items. Reasonable upper limit? 

combinations = FOREACH limitedItems GENERATE count, edu.utwente.mbd.udf.PowerSets(names);
flat_combinations = FOREACH combinations GENERATE count, FLATTEN(occurence_tuples) as flat_tuples;


-- grouped op inhoud van tuple denk ik
by_names = GROUP occ_count BY names;

sum_counts = FOREACH by_names GENERATE group, SUM(occ_count.count) as combined;
inc = ORDER sum_counts BY combined;             

rmf tmp;
STORE inc INTO 'tmp';
