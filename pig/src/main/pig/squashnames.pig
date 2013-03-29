register 'pig/target/pig-0.8.2-SNAPSHOT.jar';

in = LOAD '../counts/part-*' USING PigStorage as (name:chararray, count:long);

DEFINE ScriptSrcToTuple edu.utwente.mbd.udf.ScriptSrcToTuple();

squashed = FOREACH in GENERATE FLATTEN(edu.utwente.mbd.udf.ScriptSrcToTuple(name)), count;
top = ORDER squashed by count DESC;
top100 = LIMIT top 100;
dump top100;