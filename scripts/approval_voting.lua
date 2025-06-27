---Title: Approval Voting
---Ballot_Type: Multiple
---Description: selects the candidate with the most votes
local maxCandidate = ""
local maxVotes = 0
for candidate , votes in pairs(votes) do
	if votes > maxVotes then
		maxVotes = votes
		maxCandidate = candidate
	end
end

--search for maxCandidate index in candidates
function keyOf(tbl, value)
    for k, v in pairs(tbl) do
        if v == value then
            return k
        end
    end
    return nil
end

return keyOf(candidates , maxCandidate)