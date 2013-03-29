register 'pig/target/pig-0.9-SNAPSHOT.jar';

in = LOAD 'counts/part-*' USING PigStorage as (name:chararray, count:long);
--- negative lookahead (?!_not_match_this_part)
--- BY ... NOT MATCHES is not supported.
non_url = FILTER in BY name MATCHES '^(?!(http[s]?://)).*';

DEFINE ScriptSrcToTuple edu.utwente.mbd.udf.ScriptSrcToTuple();

normalized = FOREACH non_url {
	parts = edu.utwente.mbd.udf.ScriptSrcToTuple(name);
	GENERATE parts.name, count;
}

each_name = GROUP normalized BY name;

sum_per_name = FOREACH each_name GENERATE group as name, SUM(normalized.count) as total;
ordered = ORDER sum_per_name BY total DESC;

top = LIMIT ordered 100;
dump top;