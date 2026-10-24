
from heapq import heappush, heappop

bookName = "book1.txt"
with open(bookName) as f:
    D = {} #occurrences counting dictionary
    wcnt = 0 #total characters in the book
    while True:
        line = f.readline() #read in a line (newline char included)
        if len(line)==0: break #end of the book
        else:
            wcnt += len(line)
        for x in line: #extract each char and update counting
            if x in D: D[x]+=1
            else: D[x]=1

V = [] # empty heap to store heap elements
for c in D:
    heappush(V, [c, D[c]])

for i in range(len(V)):
    first = heappop(V)
    second = heappop(V)
    weight = first[1] + second[1]
    heappush(V, [[first, second], weight]) #obv need to add second
