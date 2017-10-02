import sys
import math

class synBlock:
    val = None
    #1 for positive, -1 for negative
    ori = None

    def __init__(self, value, orientation):
        self.val = value
        self.ori = orientation

    def getVal(self):
        return self.val

    def getOri(self):
        return self.ori

    def prnSyn(self):
        print(str(self))
        print(self.val)
        print(self.ori)

class colorEdge:
    h = None
    t = None

    def __init__(self, head, tail):
        self.h = head
        self.t = tail

    def getHead(self):
        return self.h

    def getTail(self):
        return self.t

    def str(self):
        return "(" + str(self.h) + ", " + str(self.t) + ")"


class genome:

    def genFromEdges(self):
        return 0


    def createEdges(self):
        #iterate through synteny blocks, create "colored edges"
        #an edge from * --> tail is in (+) orientation
        #an edge from * --> head is in (-) orientation
        blockLen = len(self.blocks)
        sb = self.blocks
        ce = self.colEdges
        for a in range(0, blockLen):
            cur = sb[a]
            if a != blockLen -1:
                nex = sb[a+1]
            else:
                nex = sb[0]

            if cur > 0 and nex > 0:
                ce.append(colorEdge(abs(cur*2), abs(nex*2)-1))
            elif cur > 0 and nex < 0:
                ce.append(colorEdge(abs(cur*2), abs(nex*2)))
            elif cur < 0 and nex > 0:
                ce.append(colorEdge(abs(cur*2)-1, abs(nex*2)-1))
            else:
                ce.append(colorEdge(abs(cur*2)-1, abs(nex*2)))

    def prnColEdges(self):
        outStr = ""
        for a in self.colEdges:
            outStr += a.str() + ","
        print(outStr[:-1])

    def prnGen(self):
        strOut = "("
        blockLen = len(self.blocks)
        for b in range(0, blockLen-1):
            val = self.blocks[b]
            if val > 0:
                strOut += "+" + str(val) + " "
            else:
               strOut += str(val) + " " 
        val = self.blocks[blockLen-1]
        if val > 0:
            strOut += "+" + str(val) + ")"
        else:
            strOut += str(val) + ")"
        print(strOut)

    def __init__(self, blocksIn):
        self.blocks = blocksIn
        self.colEdges = []
        self.createEdges()


class overlapGraph():
    #red == reference colored edges
    red = None
    #blue == mutant colored edges
    blue = None

    def __init__(self, ref, mut):
        red = ref
        blue = mut

    def twoBreak(i, i2, j, j2):
        return 0

def makeGen(genString):
    genString = genString.split('\n')
    tmp = ""
    for g0 in genString:
        tmp += g0
    genString = tmp
    genString = genString.split("(")
    tmp = ""
    for g1 in genString:
        tmp += g1
    genString = tmp
    genString = genString.split(")")
    tmp = ""
    for g2 in genString:
        tmp += g2
    genString = tmp
    genString = genString.split()
    blockList = []
    for g3 in genString:
        blockList.append(int(g3))
    return genome(blockList)


if __name__ == '__main__':
    fileIn = open(sys.argv[1])
    mutGen = fileIn.readline()
    refGen = fileIn.readline()
    synTest = synBlock(1, 1)
    testGen = makeGen(refGen)
    testGen.prnGen()
    testGen.prnColEdges()
    testGen2 = makeGen(mutGen)
    testGen2.prnGen()
    testGen2.prnColEdges()

