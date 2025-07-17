---Title: Schulze Method
---Ballot_Type: Ranked
---Description: picks the candidate which would win in a head to head race against the other candidates most amount of times

local schulzeMatrix={}
for _, candidate in ipairs(candidates) do
	local candidateMatrix = {}
	for _, innerCandidate in ipairs(candidates) do
		table.insert(candidateMatrix, {candidate = innerCandidate , score = 0})
	end
	table.insert(schulzeMatrix, {candidate = candidate , scores = candidateMatrix})
end

local function indexOf(tbl, val)
    for k, v in pairs(tbl) do
        if v == val then
            return k
        end
    end
    return nil
end

local function beats(candidateA, candidateB)
	local winsAgainst = 0
	for _, vote in ipairs(votes) do
		if indexOf(vote,candidateA) < indexOf(vote,candidateB) then
			winsAgainst = winsAgainst + 1
		end
	end
	return winsAgainst
end

for i, candidate in ipairs(schulzeMatrix) do
	for j, innerCandidate in ipairs(candidate) do
		schulzeMatrix[i][j].score = beats(candidate.candidate,innerCandidate.candidate)
	end
end



-- first take the path along the conections between each node from A to B
-- in our path find the minimum connection
-- go one node before that connection 
-- and take the second largest connection 
-- from there keep following the strongest path until B
-- check our current Stregth against the inital path , if our current Streght is greater than the initial path repeat otherwise we have found the strongest path
local function strongestChain(candidateA , candidateB)
	if candidateA == nil then
		return -math.huge
	end
	local function getConnectionStrengths(candidateA)
		local res = {}
		for _, node in ipairs() do
			if node.candidate == candidateA then
				for _, connection in ipairs(node.scores) do
					table.insert(res,connection)
				end
			end
		end
		table.sort(res, function (a,b)
			return a.score > b.score
		end)
		return res
	end
	local function dumbMaxPath(candidateA , candidateB)
		local path = {}
		local function checkIfEdgeInPath(edge)
			for _, e in ipairs(path) do
				if e.a == edge.a and e.b == edge.b and e.weight == edge.weight then
					return true
				end
				return false
			end
		end

		local function checkIfNodeInPath(node)
			for _, e in ipairs(path) do
				if node == e.a or node == e.b
					return true
				end
				return false
			end
		end
		local currNode = candidateA
		while not checkIfNodeInPath(candidateB)
		do
			local connections =  getConnectionStrengths(currNode)

			local counter = 1
			local nextConnection = connections[counter]
			local edge = {a = currNode , b = nextConnection.candidate , weight = nextConnection.score}
			while checkIfEdgeInPath(edge)
			do
				counter = counter + 1
				nextConnection = connections[counter]
				edge = {a = currNode , b = nextConnection.candidate , weight = nextConnection.score}
			end
			table.insert(path , edge)
			currNode = edge.b
		end
		return path
	end

	local function weakestLink(path)
		local weakest = path[1]
		local index = 1
		for _, link in ipairs(path) do
			if link.weight < weakest.weight then
				weakest = link
			end
			index = index + 1
		end
		return index , weakest.weight
	end

	local weakest = -math.huge
	local oldweakest = weakest
	local currNode = candidateA
	repeat 
		local path = dumbMaxPath(currNode, candidateB)
		oldweakest = weakest
		index, weakest = weakestLink(path)
		if index == 1 then 
			return weakest
		end
		currNode = path[index-1].a
	until weakest > oldweakest
	return oldweakest
end

local winningCandidate = candidates[1] 
local winingChain = -math.huge 
for _, candidate in ipairs(candidates) do
	local chainStrength = strongestChain(winningCandidate,candidate) 
	if chainStrength > winingChain then
		winingChain = chainStrength
		winningCandidate = candidate
	end
end

return indexOf(candidates, winningCandidate)
