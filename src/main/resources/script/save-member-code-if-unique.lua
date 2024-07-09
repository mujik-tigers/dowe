local uniqueMemberCodeSet = KEYS[1]
local newCode = ARGV[1]

local exists = redis.call('sismember', uniqueMemberCodeSet, newCode)

if exists == 1 then
    return false
else
    redis.call('sadd', uniqueMemberCodeSet, newCode)
    return true
end
