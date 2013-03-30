register 'pig/target/pig-0.9-SNAPSHOT-jar-with-dependencies.jar';

in = LOAD 'counts/part-*' USING PigStorage as (name:chararray, count:long);

-- classcast exceptions with schematuples!
set pig.schematuple false;

set job.name cdn_analysis;
rm summed_by_host;

DEFINE SplitHostPath edu.utwente.mbd.udf.SplitHostPath();

SPLIT in INTO url IF (name MATCHES '^http[s]?://.*'), non_url OTHERWISE;

raw_host_path = FOREACH url {
	host_file =  SplitHostPath(name);
	-- substring of max length 42 ,to combine tracking urls
	GENERATE host_file.host, host_file.filename as filename, count;
}

by_host_filename = GROUP raw_host_path BY (host, filename);
-- we convert from path -> filename and then group
summed_filename_by_host = FOREACH by_host_filename GENERATE FLATTEN(group) as (host, filename), SUM(raw_host_path.count) as total;

by_host = GROUP summed_filename_by_host BY host;

top_by_host = FOREACH by_host {
	most = ORDER summed_filename_by_host BY total DESC;
	top_100 = LIMIT most 100;
	GENERATE group, SUM(summed_filename_by_host.total) as total, top_100;
}

sorted_hosts = ORDER top_by_host BY total DESC;
top1000 = LIMIT sorted_hosts 1000;

STORE top1000 INTO 'top_1000_hosts';