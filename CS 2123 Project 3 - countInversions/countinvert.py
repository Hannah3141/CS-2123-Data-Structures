# Project 3: counting inversions with divide-and-conquer

import random
import time

# input: two sorted arrays L and R
# return: a tuple with two components (T, sc)
# T: the sorted array merging L and R
# sc: the number of split inversions between L and R 
def mergeAndCountSplitInv(L, R):
    T = [] # T is the combined list. Don't change this line
    sc = 0 # sc is the number of split inversions. Don't change this line
    leftPtr = 0
    rightPtr = 0
    while leftPtr < len(L) and rightPtr < len(R): #if there is still sometime in both arrays
        if L[leftPtr] <= R[rightPtr]: #add the smaller value to T
            T.append(L[leftPtr])
            leftPtr += 1
        else:
            T.append(R[rightPtr])
            rightPtr += 1
            sc += len(L) - leftPtr
    if leftPtr < len(L): #add all the remaining values from the array that finishes second
        T.extend(L[leftPtr:])
    if rightPtr < len(R):
        T.extend(R[rightPtr:])
    return T, sc # Don't change this line    

# input: an unordered array A
# return: a tuple with two components (component 1, component 2)
# component 1: the ordered array 
# component 2: the count of inversions in A
# Note: this function will be recursive and will invoke mergeAndCountSplitInv()
def sortAndCountInv(A):
    if len(A) == 0 or len(A) == 1: #there can be no inversions across 0 or 1 items
        return (A, 0)
    mid = int(len(A)/2)
    L, leftInv = sortAndCountInv(A[0:mid]) #split the array into two halves
    R, rightInv = sortAndCountInv(A[mid:])
    T, splitInv = mergeAndCountSplitInv(L, R) #count the inversions between the two halves
    return (T, leftInv + rightInv + splitInv)    

# brute-force inversion counting
# Don't change this function
def bfInv(A):
    k = 0
    for i in range(0,len(A)-1):
        for j in range(i+1, len(A)):
            if A[i]>A[j]: k = k + 1
    return k

# Suppose list X provides a correct ascending order of its elements 
# find the inversion in list Y compared with list X
# Note: this function must invoke your sortAndCountInv() function
def findInv(X, Y):
    dict = {}
    array = []
    for i in range(0, len(X)):
        dict[X[i]] = i #the dictionary stores each value and its index in the correct list
    for j in range(0, len(Y)):
        array.append(dict[Y[j]]) #put the index of X for the corresponding value in Y
    return sortAndCountInv(array)[1] #return the number of inversions counted

if __name__ == "__main__":
    # part1: test the correctness
    Ain1 = [1,5,4,8,10,2,6,9,3,7]
    Aout1, tc1 = sortAndCountInv(Ain1)
    print(Aout1)
    print(tc1)
    
    # part2: compare the performance between divide-and-conquer and brute-force algorithms
    file = open("output.csv", "w")
    file.write("numElements, d&c inversions, runtime, bf inversions, runtime \n")
    inSize = [1000, 2000, 4000, 8000, 16000, 32000]
    for i in range(0, len(inSize)):
        Ain2 = random.sample(range(1000000), inSize[i]) # create a random input list
        t1 = time.time()
        Aout2, tc2 = sortAndCountInv(Ain2) # counting inversions with the divide-and-conquer algorithm
        t2 = time.time()
        bfc2 = bfInv(Ain2) # counting inversions with the brute-force algorithm
        t3 = time.time()
        #print("Number of inversions found by the divide-and-conquer algorithm:", tc2)
        #print("Number of inversions found by the brute-force algorithm:", bfc2)
        #print("Running time of the divide-and-conquer algorithm:", t2-t1, "seconds")
        #print("Running time of the brute-force algorithm:", t3-t2, "seconds")
        file.write(f"{inSize[i]}, {tc2}, {t2-t1}, {bfc2}, {t3-t2} \n")
    
    # part3: solve a practical problem
    # suppose listX provides a correct ascending order of songs, find the number of inversions in listY
    listX = ['F', 'G', 'A', 'C', 'B', 'D', 'E', 'H' ]
    listY = ['A', 'D', 'B', 'C', 'H', 'G', 'E', 'F' ]
    nInv = findInv(listX, listY)
    print("The number of inversions in list Y compared with list X is", nInv)