---Title: Highest Average
---Ballot_Type: Point
---Description: picks the candidate with the highest average score 

local candidateRatings = {}

for _, candidate in ipairs(candidates) do
	table.insert(candidateRatings,{candidate = candidate , ratings = {}})
end

local function addRating(candidate, rating)
	for _, entry in ipairs(candidateRatings) do
		if entry.candidate == candidate then
			table.insert(entry.ratings, rating)
		end
	end
end

for _, vote in ipairs(votes)do
	for candidate, rating in pairs(vote) do
		addRating(candidate,rating)
	end
end

local function getAverage(candidateRating)
	local sum =0
	for _, rating in ipairs(candidateRating.rating) do
		if rating ~= -1 then
			sum = sum + rating
		end
	end
	return sum/#candidateRating.rating
end

local winningCandidate =""
local highestAverage = 0

for _, candidate in ipairs(candidateRatings) do
	local avg = getAverage(candidate)
	if avg > highestAverage then
		highestAverage = avg
		winningCandidate = candidate.candidate
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

return keyOf(winningCandidate,candidates)
