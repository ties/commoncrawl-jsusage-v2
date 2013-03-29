register 'pig/target/pig-0.9-SNAPSHOT.jar';

in = LOAD 'counts/part-*' USING PigStorage as (name:chararray, count:long);

set job.name cdn_analysis;

DEFINE SplitHostPath edu.utwente.mbd.udf.SplitHostPath();

SPLIT in INTO url IF (name MATCHES '^http[s]?://.*'), non_url OTHERWISE;

host_path = FOREACH url {
	GENERATE FLATTEN(SplitHostPath(name)) as (host, path), count;
}

by_host = GROUP host_path BY host;

host_stats = FOREACH by_host {
	inner_counts = FOREACH host_path GENERATE path, count;
	sorted_inner = ORDER inner_counts BY count DESC;
	GENERATE group as host, SUM(host_path.count) as total, sorted_inner;
}

top_descending = ORDER host_stats BY total;
top1000 = LIMIT top_descending 1000;

STORE top1000 INTO 'top_1000_hosts';