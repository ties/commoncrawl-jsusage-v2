in = LOAD 'counts/part-*' USING PigStorage as (name:chararray, count:long);

set job.name cdn_analysis;

SPLIT in INTO cdn IF (name MATCHES '^http[s]?://.*'), non_url OTHERWISE;

-- strip 'cn' prefix and protocol
counts = FOREACH cnts GENERATE $1, $3;
-- strip 'cm' prefix
combinations = FOREACH cmbs GENERATE $1, $2;

STORE counts INTO 'counts';
STORE combinations INTO 'combinations';

in = LOAD 'counts_cdn/part-*' USING PigStorage() as (name, count:long)
sorted = ORDER in BY count DESC;
top100 = LIMIT sorted 100;
dump top100;