---Title: Instant Runoff
---Ballot_Type: Ranked
---Description: each voter ranks the candidates from most preferred to least preferred. Each round the votes are assigned to the candidates based on the number of times they were most preferred, the least most preferred candidate gets elminated and their votes are reassigned to the second most preferred candidate in their respective ballots. this process repeats until a single candidate holds the majority of the votes

local winningCandidate=""
local maxVotes = 0
local threshold = #votes*0.5


local electionTracker ={}

for _,candidate in pairs(candidates) do
	table.insert(electionTracker,{candidate = candidate , numOfVotes = 0})
end

local function addVote(candidate)
	for _,entry in pairs(electionTracker) do
		if entry.candidate == candidate then
			entry.numOfVotes = entry.numOfVotes + 1
		end
	end
end

local function findLeastPreferredCandidate()
	local min = electionTracker[1]
	for _, entry in ipairs(electionTracker) do
		if entry.numOfVotes < min.numOfVotes then
			min = entry
		end
	end
	return min
end

local function keyOf(tbl, value)
    for k, v in pairs(tbl) do
        if v == value then
            return k
        end
    end
    return nil
end


while true
do
	maxVotes = 0
	winningCandidate = ""
	for _, vote in ipairs(votes) do
		addVote(vote[1])
	end

	for _, entry in ipairs(electionTracker) do
		if entry.numOfVotes > maxVotes then
			maxVotes = entry.numOfVotes
			winningCandidate = entry.candidate
		end
	end
	if maxVotes >= threshold then
		return keyOf(candidates, winningCandidate)
	else
		local leastPreferred = findLeastPreferredCandidate()
		for _, vote in ipairs(votes) do
			if vote[1].candidate == leastPreferred.candidate then
				table.remove(vote,1)
			end
		end
		for _, entry in ipairs(electionTracker) do
			entry.numOfVotes = 0
		end
	end
end
