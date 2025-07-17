---Title: Iterated Plurality Runoff
---Ballot_Type: Ranked
---Description: each voter ranks the candidates from most preferred to least preferred. in the first round each candidate gets the number of votes according to how many times they were ranked most prefered. if the winning candidate doesn't achieve a majority vicotry, all the other most preferred candidates are removed from their ballots and the votes go to the second most preferred candidate. this process repeats until a majority winner is found


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
		for _, vote in ipairs(votes) do
			if vote[1] ~= winningCandidate then
				table.remove(vote,1)
			end
		end
		for _, entry in ipairs(electionTracker) do
			entry.numOfVotes = 0
		end
	end
end
