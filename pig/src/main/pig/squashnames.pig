register 'pig/target/pig-0.9-SNAPSHOT.jar';

set job.name squashing_names;

in = LOAD 'counts/part-*' USING PigStorage as (name:chararray, count:long);
--- negative lookahead (?!_not_match_this_part)
--- BY ... NOT MATCHES is not supported.
SPLIT in INTO non_url IF (name MATCHES '^(?!(http[s]?://)).*'), url OTHERWISE;

DEFINE ScriptSrcToTuple edu.utwente.mbd.udf.ScriptSrcToTuple();

normalized = FOREACH non_url {
	parts = ScriptSrcToTuple(name);
	GENERATE parts.name, count;
}

set job.name grouping_names;

each_name = GROUP normalized BY name;

sum_per_name = FOREACH each_name GENERATE group as name, SUM(normalized.count) as total;

set job.name sort_store_groups;
-- We want one result file for ease of processing
-- drops total efficiency.
ordered = ORDER sum_per_name BY total DESC;
top_1m = LIMIT ordered 1000000;

STORE top_1m INTO 'scriptname_counts';