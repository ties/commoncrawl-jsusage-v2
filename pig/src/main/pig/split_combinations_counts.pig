in = LOAD 'jsusage-testset/part-r*' USING PigStorage('\t');

SPLIT in INTO cnts IF ($0 == 'cn'), cmbs IF ($0 == 'co');

-- strip 'cn' prefix
counts = FOREACH cnts GENERATE $1, $2, $3;
-- strip 'cm' prefix
combinations = FOREACH cnts GENERATE $1, $2, $3;

STORE counts INTO 'counts';
STORE combinations INTO 'combinations';