register '../commoncrawl-jsusage-v2/pig/target/pig-0.8.1.jar';

in = LOAD 'combinations/part-*' USING PigStorage('\t') as (names:chararray, protocol:chararray, count:long);

items = FOREACH in GENERATE edu.utwente.mbd.udf.SplitCombinations(names), protocol, count;