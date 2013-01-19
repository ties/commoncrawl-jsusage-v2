-- register '../../../target/${project.build.finalName}.jar';
register '../../../target/pig-0.8.2-SNAPSHOT.jar';

in = LOAD 'testset/*.arc.gz' USING org.commoncrawl.pig.ArcLoader() as (date, length, type, statuscode, ipaddress, url, html);
extensions = foreach in generate REGEX_EXTRACT(url, '.*\\.(.*)$', 1) as ext;

by_ext = group extensions by ext;

num_by_ext = foreach by_ext generate group, COUNT(extensions) as num;
filt_num_by_ext = filter num_by_ext by num > 5;

ordered = order filt_num_by_ext by num;

dump ordered;
