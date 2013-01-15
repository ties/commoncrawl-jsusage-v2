register 'lib/*.jar'; 
register 'dist/lib/commoncrawl-examples-1.0.1-HM.jar';

in = LOAD 'testset/*.arc.gz' USING org.commoncrawl.pig.ArcLoader() as (date, length, type, statuscode, ipaddress, url, html);
protocols = foreach in generate REGEX_EXTRACT(url, '(http|https)://.*', 1) as proto;

by_proto = group protocols by proto;

num_by_proto = foreach by_proto generate group, COUNT(protocols);

dump num_by_proto;
