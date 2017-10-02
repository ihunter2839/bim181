import sys
import math

def main():
    fileIn = open(sys.argv[1])
    refGen = fileIn.readline()
    refGen = refGen[1:-2]
    refGenChroms = refGen.split(')(')
    chromMaps = [None]*len(refGenChroms)
    for a in range(0, len(refGenChroms)):
        curChrom = refGenChroms[a]
        chromMaps[a] = chromToCycle(curChrom)
    for c in chromMaps:
        tmp = ""
        for i in range(1, len(c)):
            tmp += str(c[i]) + " "
        print("(" + tmp[:-1] + ")")

def main2():
    fileIn = open(sys.argv[1])
    #trim leading/trailing parentheses + newline
    cycle = fileIn.readline()[1:-1]
    cycle = cycle.split()
    for z in range(0, len(cycle)):
        cycle[z] = int(cycle[z])
    cycle = [0] + cycle
    print("("+cycleToChrom(cycle)+")")

def main3():
    fileIn = open(sys.argv[1])
    refGen = fileIn.readline()[1:-2]
    refChroms = refGen.split(')(')
    edgeList = [None]
    for r in refChroms:
        curAr = chromToCycle(r)
        #print(str(curAr))
        tmp = coloredEdges(curAr)
        #print(tmp)
        edgeList.append(tmp)
    outString = ""
    for e in edgeList:
        if e == None:
            continue
        for a in range(0, len(e), 2):
            outString += "("+str(e[a])+", "+str(e[a+1])+"), "
    print(outString[:-2])

def main4():
    fileIn = open(sys.argv[1])
    graph = fileIn.readline()
    graph = graph[1:-1].split("), (")
    graphStr = ""
    for g in graph:
        #trim parentheses
        tmp = g.split(", ")
        for h in tmp:
            graphStr += h + " "
    graph = graphStr.split()
    for g in range(0, len(graph)):
        graph[g] = int(graph[g]) 
    genOut = ""
    a = 0
    while a < len(graph):
        genOut += "("
        start = int((graph[a]/2))+int((graph[a]%2))
        #positive orientation
        if graph[a] % 2 == 0:
            genOut += "+"+str(int(graph[a]/2))
        else:
            genOut += "-"+str((int(graph[a])//2) + 1)
        a += 1
        #while the second element isnt the last
        while (int(graph[a]//2)+int(graph[a]%2)) != start and a < len(graph)-1:
            if graph[a] % 2 == 0:
                genOut += " -"+str(int(graph[a]/2))
            else:
                genOut += " +"+str(int(graph[a]/2)+1)
            a += 2
        genOut += ")"
        a += 1
    print(genOut)

def main5():
    fileIn = open(sys.argv[1])
    #remove first and last parentheses and newline
    colEdgeString = fileIn.readline()[1:-2]
    #edges to be swapped in order i, i', j, j'
    swapEdges = fileIn.readline()
    swapEdges = swapEdges.split(", ")
    #remove remaining parentheses
    colEdges = colEdgeString.split("), (")
    edgeAr = [None]
    edgeDict = {}
    for a in range(1, len(colEdges)+1):
        curEdge = colEdges[a-1].split(", ")
        c1 = int(curEdge[0])
        c2 = int(curEdge[1])
        edgeAr.append(c1)
        edgeAr.append(c2)
        edgeDict[c1] = (2*a)-1
        edgeDict[c2] = 2*a
    indI = edgeDict[int(swapEdges[0])]
    indI2 = edgeDict[int(swapEdges[1])]
    indJ = edgeDict[int(swapEdges[2])]
    indJ2 = edgeDict[int(swapEdges[3])]
    #make (i,j) @ indI and indI2
    i2 = edgeAr[indI2]
    edgeAr[indI2] = edgeAr[indJ]
    edgeAr[indJ] = i2
    edgeOut = ""
    for b in range(1, len(edgeAr)-1, 2):
        edgeOut += "("+str(edgeAr[b])+", "+str(edgeAr[b+1])+"), "
    print(edgeOut[:-2])

def main6():
    #read in genome, turn into colored edges
    fileIn = open(sys.argv[1])
    refGen = fileIn.readline()[1:-2]
    #split into list of 4 integers
    breakOp = fileIn.readline().split(", ")
    refChroms = refGen.split(')(')
    edgeList = [None]
    for r in refChroms:
        curAr = chromToCycle(r)
        #print(str(curAr))
        tmp = coloredEdges(curAr)
        edgeList.append(tmp)
    edgeListComb =[]
    for a in range(1, len(edgeList)):
        edgeListComb += edgeList[a]
    #(i, i') --> (i,j) need pos of i', j
    i = int(breakOp[0])
    indI = edgeListComb.index(i) 
    i2 = int(breakOp[1])
    indI2 = edgeListComb.index(i2)
    j = int(breakOp[2])
    indJ = edgeListComb.index(j)
    j2 = int(breakOp[3])
    indJ2 = edgeListComb.index(j2)

    first = min(indI, indI2, indJ, indJ2)
    second = min(max(indI, indI2), max(indJ, indJ2))
    third = max(min(indI, indI2), min(indJ, indJ2))
    fourth = max((max(indI, indI2), max(indJ, indJ2)))
    if first == indI:
        if third == indJ:
            p1 = edgeListComb[:first+1]
            p2 = edgeListComb[third:second-1:-1]
            p3 = edgeListComb[fourth:]
            edgeListComb = p1 + p2 + p3
        else:
            p1 = edgeListComb[:first+1]
            p2 = edgeListComb[fourth:]
            p3 = edgeListComb[third:third+1] + edgeListComb[second:third]
            edgeListComb = p1 + p2 + p3
    elif first == indI2:
        if third == indJ2:
            p1 = edgeListComb[:first+1]
            p2 = edgeListComb[third:second-1:-1]
            p3 = edgeListComb[fourth:]
            edgeListComb = p1 + p2 + p3
        else:
            p1 = edgeListComb[:first+1]
            p2 = edgeListComb[fourth:]
            p3 = edgeListComb[third:third+1] + edgeListComb[second:third]
            edgeListComb = p1 + p2 + p3
    elif first == indJ:
        if third == indI:
            p1 = edgeListComb[:first+1]
            p2 = edgeListComb[third:second-1:-1]            
            p3 = edgeListComb[fourth:]
            edgeListComb = p1 + p2 + p3
        else:
            p1 = edgeListComb[:first+1]
            p2 = edgeListComb[fourth:]
            p3 = edgeListComb[third:third+1] + edgeListComb[second:third]
            edgeListComb = p1 + p2 + p3 
    else:
        if third == indI2:
            p1 = edgeListComb[:first+1]
            p2 = edgeListComb[third:second-1:-1]
            p3 = edgeListComb[fourth:]
            edgeListComb = p1 + p2 + p3
        else:
            p1 = edgeListComb[:first+1]
            p2 = edgeListComb[fourth:]
            p3 = edgeListComb[third:third+1] + edgeListComb[second:third]
            edgeListComb = p1 + p2 + p3  
    genOut = ""
    a = 0
    while a < len(edgeListComb):
        genOut += "("
        start = int((edgeListComb[a]/2))+int((edgeListComb[a]%2))
        #positive orientation
        if edgeListComb[a] % 2 == 0:
            genOut += "+"+str(int(edgeListComb[a]/2))
        else:
            genOut += "-"+str((int(edgeListComb[a])//2) + 1)
        a += 1
        #while the second element isnt the last
        while (int(edgeListComb[a]//2)+int(edgeListComb[a]%2)) != start and a < len(edgeListComb)-1:
            if edgeListComb[a] % 2 == 0:
                genOut += " -"+str(int(edgeListComb[a]/2))
            else:
                genOut += " +"+str(int(edgeListComb[a]/2)+1)
            a += 2
        genOut += ")"
        a += 1
    print(genOut)

def main7():
    fileIn = open(sys.argv[1])
    refGen = fileIn.readline()[1:-2]
    refChroms = refGen.split(')(')
    mutGen = fileIn.readline()[1:-1]
    mutChroms = mutGen.split(')(')

    #make reference array
    edgeRT = [None]
    for r in refChroms:
        curAr = chromToCycle(r)
        tmp = coloredEdges(curAr)
        edgeRT.append(tmp)
    edgeR = []
    for a in range(1, len(edgeRT)):
        edgeR += edgeRT[a]
    #make mutant array
    edgeMT = []
    for m in mutChroms:
        curAr = chromToCycle(m)
        tmp = coloredEdges(curAr)
        edgeMT.append(tmp)
    edgeM = []
    for b in range(0, len(edgeMT)):
        edgeM += edgeMT[b]
    #mark which edges have been visited in ref edges
    visited = [False]*len(edgeR)
    refD = {}
    mutD = {}
    for c in range(0, len(edgeR)):
        refD[edgeR[c]] = c
        mutD[edgeM[c]] = c
    cycles = 0
    while True:
        flag = 1
        try:
            startInd = visited.index(False)
            visited[startInd] = True
            start = edgeR[startInd]
            if startInd % 2 == 0:
                endInd = startInd + 1
            else:
                endInd = startInd - 1
            end = edgeR[endInd]
        except ValueError:
            break
        cur = start

        while cur != start or flag == 1:
            flag = 0
            #move from ref to mut
            curPosMut = mutD[cur]
            #cur is first element in edge
            if curPosMut % 2 == 0: 
                cur = edgeM[curPosMut+1]
            else:
                cur = edgeM[curPosMut-1]
            #move back to ref
            curPosRef = refD[cur]
            visited[curPosRef] = True
            if curPosRef % 2 == 0:
                cur = edgeR[curPosRef+1]
                visited[curPosRef+1] = True
            else:
                cur = edgeR[curPosRef-1]
                visited[curPosRef-1] = True
        cycles += 1
    print((len(edgeR)//2) - cycles)

def main8():
    fileIn = open(sys.argv[1])
    mutGen = fileIn.readline()[1:-2]
    mutChroms = mutGen.split(")(")
    refGen = fileIn.readline()[1:-1]
    refChroms = refGen.split(")(")

    edgeRT = [None]
    for r in refChroms:
        curAr = chromToCycle(r)
        tmp = coloredEdges(curAr)
        edgeRT.append(tmp)
    edgeR = []
    for a in range(1, len(edgeRT)):
        edgeR += edgeRT[a]
    #make mutant array
    edgeMT = []
    for m in mutChroms:
        curAr = chromToCycle(m)
        tmp = coloredEdges(curAr)
        edgeMT.append(tmp)
    edgeM = []
    for b in range(0, len(edgeMT)):
        edgeM += edgeMT[b]
    #mark which edges have been visited in ref edges
    visited = [False]*len(edgeR)
    refD = {}
    mutD = {}
    for c in range(0, len(edgeR)):
        refD[edgeR[c]] = c
        mutD[edgeM[c]] = c
    print(mutD)
    while str(edgeM) != str(edgeR):
        a = 0
        while edgeM[a] == edgeR[a]:
            a+=1
        # j is first mismatch
        print(a)
        indJM = mutD[edgeR[a]]
        print(edgeR[a])
        print(indJM)

        if a % 2 == 1:
            indIM = mutD[edgeR[a-1]]
        else:
            indIM = mutD[edgeR[a+1]]
        if indIM % 2 == 0:
            indI2M = indIM + 1
        else:
            indI2M = indIM - 1
        if indJM % 2 == 0:
            indJ2M = indJM + 1
        else:
            indJ2M = indJM - 1
        print(str(indIM) + " " + str(indI2M) + " " + str(indJM) + " " + str(indJ2M))
        print(edgeM)
        print(len(edgeM))
        print(edgeM[20])
        first = min(indIM, indI2M, indJM, indJ2M)
        second = min(max(indIM, indI2M), max(indJM, indJ2M))
        third = max(min(indIM, indI2M), min(indJM, indJ2M))
        fourth = max((max(indIM, indI2M), max(indJM, indJ2M)))
        #print(str(first) + str(second) + str(third) + str(fourth))
        if first == indIM:
            if third == indJM:
                p1 = edgeM[:first+1]
                p2 = edgeM[third:second-1:-1]
                p3 = edgeM[fourth:]
                edgeM = p1 + p2 + p3
            else:
                p1 = edgeM[:first+1]
                p2 = edgeM[fourth:]
                p3 = edgeM[third:third+1] + edgeM[second:third]
                edgeM = p1 + p2 + p3
        elif first == indI2M:
            if third == indJ2M:
                p1 = edgeM[:first+1]
                p2 = edgeM[third:second-1:-1]
                p3 = edgeM[fourth:]
                edgeM = p1 + p2 + p3
            else:
                p1 = edgeM[:first+1]
                p2 = edgeM[fourth:]
                p3 = edgeM[third:third+1] + edgeM[second:third]
                edgeM = p1 + p2 + p3
        elif first == indJM:
            if third == indIM:
                p1 = edgeM[:first+1]
                p2 = edgeM[third:second-1:-1]            
                p3 = edgeM[fourth:]
                edgeM = p1 + p2 + p3
            else:
                p1 = edgeM[:first+1]
                p2 = edgeM[fourth:]
                p3 = edgeM[third:third+1] + edgeM[second:third]
                edgeM = p1 + p2 + p3 
        else:
            if third == indI2M:
                p1 = edgeM[:first+1]
                p2 = edgeM[third:second-1:-1]
                p3 = edgeM[fourth:]
                edgeM = p1 + p2 + p3
            else:
                p1 = edgeM[:first+1]
                p2 = edgeM[fourth:]
                p3 = edgeM[third:third+1] + edgeM[second:third]
                edgeM = p1 + p2 + p3  
        for z in range(0, len(edgeM)):
            mutD[edgeM[z]] = z

def printGraph(edgeM):
    genOut = ""
    y = 0
    while y < len(edgeM):
        genOut += "("
        start = int((edgeM[y]/2))+int((edgeM[y]%2))
        #positive orientation
        if edgeM[y] % 2 == 0:
            genOut += "+"+str(int(edgeM[y]/2))
        else:
            genOut += "-"+str((int(edgeM[y])//2) + 1)
        y += 1
        #while the second element isnt the last
        while ( (y < (len(edgeM)-1)) and (int(edgeM[y]//2)+int(edgeM[y]%2)) != start):
            if edgeM[y] % 2 == 0:
                genOut += " -"+str(int(edgeM[y]/2))
            else:
                genOut += " +"+str(int(edgeM[y]/2)+1)
            y += 2
        genOut += ")"
        y += 1
    print(genOut)

def graphToGen(graph):
    genOut = ""
    a = 0
    while a < len(graph):
        genOut += "("
        start = int((graph[a]/2))+int((graph[a]%2))
        #positive orientation
        if graph[a] % 2 == 0:
            genOut += "+"+str(int(graph[a]/2))
        else:
            genOut += "-"+str((int(graph[a])//2) + 1)
        a += 1
        #while the second element isnt the last
        while (int(graph[a]//2)+int(graph[a]%2)) != start and a < len(graph)-1:
            if graph[a] % 2 == 0:
                genOut += " -"+str(int(graph[a]/2))
            else:
                genOut += " +"+str(int(graph[a]/2)+1)
            a += 2
        genOut += ")"
        a += 1
    return genOut

def coloredEdges(genAr):
    edges = [None]*(len(genAr)-1)
    for i in range(1, len(genAr)-1):
        edges[i-1] = genAr[i+1]
    edges[len(edges)-1] = genAr[1]
    return edges


def chromToCycle(refChrom):
    #head = 2x
    #tail = 2x-1
    #refChrom is an unsplit string containg syn vals
    blocks = refChrom.split()
    chromMap = [0]*(len(blocks)*2 + 1)
    #iterate over all blocks in chrom
    pos = 1
    for b in range(0, len(blocks)):
        #turn string to int form
        synVal = int(blocks[b])
        #block in positive orientation
        #tail before head
        if synVal > 0:
            chromMap[pos] = (2*synVal)-1
            chromMap[pos+1] = 2*synVal
        #block in negative orientation
        #head before tail
        else:
            chromMap[pos] = 2*abs(synVal)
            chromMap[pos+1] = (2*abs(synVal))-1
        pos += 2
    return chromMap

def cycleToChrom(refCycle):
    tmp = ""
    for p in range(1, len(refCycle), 2):
        v1 = refCycle[p]
        v2 = refCycle[p+1]
        #head is greater than tail
        #check whether v1 or v2 is head
        if v1 > v2:
            tmp += "-" + str(v1//2) + " "
        else:
            tmp += "+" + str(v2//2) + " "
    return tmp[:-1]


if __name__ == '__main__':
    #main method for chromToCycle
    #main()
    #main method for cycleToChrom
    #main2()
    #main method for coloredEdges
    #main3()
    #main method for graphToGenome
    #main4()
    #main method for twoBreak
    #main5()
    #why am I writing python code like this?
    #main method for twoBreak on genome
    #main6()
    #main method for twoBreakDist
    #main7()
    #main method for twoBreakSort
    main8()
