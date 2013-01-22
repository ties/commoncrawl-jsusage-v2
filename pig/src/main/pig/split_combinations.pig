-- register '../commoncrawl-jsusage-v2/pig/target/${project.build.finalName}';
register '../commoncrawl-jsusage-v2/pig/target/pig-0.8.2-SNAPSHOT.jar';

in = LOAD 'combinations/part-*' USING PigStorage('\t') as (names:chararray, count:long);

DEFINE PowerSet edu.utwente.mbd.udf.PowerSet();
DEFINE SplitCombinations edu.utwente.mbd.udf.SplitCombinations();
DEFINE BagToTuple org.apache.pig.builtin.BagToTuple();

-- generate the combinations from lower-case version of names.
items = FOREACH in GENERATE count, SplitCombinations(LOWER(names)) as names;

limitedItems = FILTER items BY SIZE(names) <= 8; -- 8 scripts yields 2^8=256 items. Reasonable upper limit? 

script_combinations_bag = FOREACH limitedItems {
	sets = PowerSet(names);
	GENERATE count, FLATTEN(sets) as scripts;
}

script_combinations_tuples = FOREACH script_combinations_bag GENERATE BagToTuple(scripts) as scripts, count;

-- grouped op inhoud van tuple denk ik
by_names = GROUP script_combinations_tuples BY scripts;

sum_counts = FOREACH by_names GENERATE group, SUM(script_combinations_tuples.count) as combined;
inc = ORDER sum_counts BY combined;             

rmf tmp;
STORE inc INTO 'tmp';

-- grunt> combinations = FOREACH limitedItems GENERATE count, edu.utwente.mbd.udf.PowerSet(names);
-- 2013-01-19 18:34:29,832 [main] WARN  org.apache.pig.PigServer - Encountered Warning IMPLICIT_CAST_TO_LONG 1 time(s).
-- grunt> describe combinations;
-- 2013-01-19 18:34:32,761 [main] WARN  org.apache.pig.PigServer - Encountered Warning IMPLICIT_CAST_TO_LONG 1 time(s).
-- combinations: {count: long,occurence_tuples: {occurence_tuple: (tuple_of_scripts: (script: chararray))}}