-- register '../commoncrawl-jsusage-v2/pig/target/${project.build.finalName}';
register '../commoncrawl-jsusage-v2/pig/target/pig-0.8.2-SNAPSHOT.jar';

SET DEFAULT_PARALLEL 8;

in = LOAD 'combinations/part-*' USING PigStorage('\t') as (names:chararray, count:long);

-- generate the combinations from lower-case version of names.
items = FOREACH in GENERATE edu.utwente.mbd.udf.SplitCombinations(LOWER(names)) as names, count;

limitedItems = FILTER items BY SIZE(names) <= 12; -- 12 scripts yields 2^12=4096 items. Reasonable upper limit? 

perms = FOREACH limitedItems GENERATE count, FLATTEN(edu.utwente.mbd.udf.PowerSets(names)) as names;

by_names = GROUP perms BY names;

sum_counts = FOREACH by_names GENERATE group, SUM(perms.count) as combined;
inc = ORDER sum_counts BY combined;             

rmf tmp;
STORE inc INTO 'tmp';
