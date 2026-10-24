# Project 5: starter code
# Edit distance between gene sequences

#### define the data structures to store the steps to turn the source into the target
#### use these lists in your edit_dist() function, do NOT change them here
# source symbol involved in each step
sourceSymbols = []
# target symbol involved in each step
targetSymbols = []
# four possible actions to take in each step i
# actions[i] = 0: step i is keep
# actions[i] = 1: step i is substitution
# actions[i] = 2: step i is insertion
# actions[i] = 3: step i is deletion
actions = []

# Function to print the results of edit distance algorithm
# Do NOT change this function
# The results must be displayed using this function !!!
def display_results(dist):
    print("The edit distance is ", dist)
    print("The number of steps to turn the source into the target gene:", len(actions))
    print("These steps are: ")
    for a, (sc, tc) in zip(actions, zip(sourceSymbols, targetSymbols)):
        if a==0: #match, keeping operation
            print("Keep "+sc+" unchanged")
        elif a==1: #no match, substitute
            print("Modify "+str(sc)+" to "+tc)
        elif a==2: #insertion
            print("Insert "+tc)
        elif a==3: #deletion
            print("Delete "+sc)
    print("Done")    

# Complete this function
# Output: the edit distance between the src and the tgt
# Please also record your edit steps in the three pre-defined lists: sourceSymbols, targetSymbols and actions
def gene_dist(src, tgt): #func iter_string_compare (source, target) begin
    mem = [[0 for i in range(len(tgt)+1)] for j in range(len(src)+1)] #create a 2D list of tuples to store all of the intermediate edit distances and the directions 
    s = " " + src #add a space before the source and target like we do in the manual algorithm
    t = " " + tgt
    mem[0][0] = 0, 0
    for j in range(1, len(t)): #manually fill in the first row 
        mem[0][j] = j, 2
    for I in range(1, len(s)): #manually fill in the first column 
        mem[I][0] = I, 3
    for i in range(1, len(s)): #iterate through all the remaining cells
        for j in range(1, len(t)): 
            #case 1: check for match at i and j
            if s[i]==t[j]: 
                c_match = mem[i-1][j-1][0]
                c_sub = 2147483647 #set the substitution edit distance to the max integer value if we want our min() to select match instead when the letters match
            else:
                c_sub = mem[i-1][j-1][0] + 1
                c_match = 2147483647 
            #case 2: check for insertion
            c_ins = mem[i][j-1][0] + 1
            #case 3: check for deletion
            c_del = mem[i-1][j][0] + 1

            # fill in the cell with the minimum value of the four cases 
            if c_match <= c_ins and c_match <= c_del and c_match <= c_sub: 
                mem[i][j] = c_match, 0
            elif c_sub <= c_ins and c_sub <= c_match and c_sub <= c_del:
                mem[i][j] = c_sub, 1
            elif c_ins <= c_sub and c_ins <= c_match and c_ins <= c_del:
                mem[i][j] = c_ins, 2
            elif c_del <= c_ins and c_del <= c_match and c_del <= c_sub:
                mem[i][j] = c_del, 3
    while(i != 0 or j != 0): #traceback
        actions.insert(0, mem[i][j][1]) #copy the direction arrow into actions
        sourceSymbols.insert(0, s[i]) #copy the letters into the arrays
        targetSymbols.insert(0, t[j])
        if(mem[i][j][1] == 0 or mem[i][j][1] == 1): #traceback based on the direction of the arrow
            i = i-1
            j = j-1
        elif(mem[i][j][1] == 2):
            j = j-1
        else: 
            i = i-1

    return mem[len(s)-1][len(t)-1][0]

# Follow each part in the main script to test your functions
# Don't change any statements in the main script! 
if __name__ == "__main__":
    seq1 = "AGTC"
    seq2 = "GCCA"
    seq3 = "AGCTATTC"
    seq4 = "GTTCAACG"

    sourceSymbols.clear()
    targetSymbols.clear()
    actions.clear()
    dist12 = gene_dist(seq1, seq2)
    display_results(dist12)

    print()

    sourceSymbols.clear()
    targetSymbols.clear()
    actions.clear()
    dist34 = gene_dist(seq3, seq4)
    display_results(dist34)

    print()

    # Switch the source and target, and observe the resulting distance and edits
    # Notice any similarities/differences compared with the original order? 
    # Explain the observed similarities and differences
    # when you switch the source and target, insert operations become deletes and deletes become inserts. Keeps stay the same, and substitutions have the letters in the opposite order. 
    sourceSymbols.clear()
    targetSymbols.clear()
    actions.clear() 
    dist21= gene_dist(seq2, seq1)
    display_results(dist21)

    print()

    sourceSymbols.clear()
    targetSymbols.clear()
    actions.clear()
    dist43= gene_dist(seq4, seq3)
    display_results(dist43)

