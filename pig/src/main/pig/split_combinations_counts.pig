SET default_parallel 8
in = LOAD 'jsusage-testset/part-r*' USING PigStorage('\t');

SPLIT in INTO cnts IF ($0 == 'cn'), cmbs IF ($0 == 'co');

-- strip 'cn' prefix and protocol
counts = FOREACH cnts GENERATE $1, $3;
-- strip 'cm' prefix
combinations = FOREACH cmbs GENERATE $1, $2;

STORE counts INTO 'counts';
STORE combinations INTO 'combinations';