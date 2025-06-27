# UltiPoll
UltiPoll is an open source highly extendable voting/polling application. The application allows small groups of people to easily run Single-winner and winner-take-all elections
## Usage
### Voting in a poll
~todo

### Setting up a poll
~todo
## Adding Custom Voting Methods
to add in a custom voting method you need to create a lua script which will implement the logic of said method
within the lua script you need to add in 3 header comments , in the following order
~~~lua
---Title: Example
---Ballot_Type: Ranked
---Description: a brief single line description
~~~

the data about the votes will be passed in the `votes` global table , each entry of the votes will be a table corresponding to 1 of 4 possible types of ballots _Ranked_ , _Single_ , _Point_ and _Multiple_

~~~lua
	--Ranked
	votes = {
		 1 = { 1 = "Option A" , 2 = "Option B" , ... },
		 2 = { 1 = "Option C" , 2 = "Option F" , ... },
		 ...
	}
~~~ 
~~~lua
	--Single
	votes = { "Option A" = 10 , "Option B" = 15 ,...} 
~~~ 
~~~lua
	--Point
	votes = {
		"Option A" = { 1 , 5 ,6 , ... },
		"Option B" = { 3 , 1, 4 , ... }, 
		...
	} 
~~~ 
~~~lua
	--Multiple
	votes = { "Option A" = 10 , "Option B" = 15 ,...} 
~~~ 
after computing the winner of the election simply just return the index _(STARTING FROM 1)_ of the winner from the `candidates` global variable
~~~lua
	candidates = { "Option A" , "Option B" , "Option C" , ...}
~~~

### Simple example
first_past_the_post.lua
~~~lua 
---Title: First Past the Post
---Ballot_Type: Single
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

return keyOf(candidiates , maxCandidate)
~~~
