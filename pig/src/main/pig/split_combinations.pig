-- register '../commoncrawl-jsusage-v2/pig/target/${project.build.finalName}';
register '../commoncrawl-jsusage-v2/pig/target/pig-0.8.2-SNAPSHOT.jar';

in = LOAD 'combinations/part-*' USING PigStorage('\t') as (names:chararray, protocol:chararray, count:long);

items = FOREACH in GENERATE edu.utwente.mbd.udf.SplitCombinations(names) as names, protocol, count;

limitedItems = FILTER items BY SIZE(names) <= 12; -- 12 scripts yields 2^12=4096 items. Reasonable upper limit? 

perms = FOREACH limitedItems GENERATE count, FLATTEN(edu.utwente.mbd.udf.PowerSets(names)) as names;

dump perms;
