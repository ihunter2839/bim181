import sys

def main():
    fileIn = open(sys.argv[1])
    refGenomeBlocks = fileIn.readline()
    #remove leading and trailing parentheses
    refGenomeBlocks = refGenomeBlocks[1:-2]
    #split synteny block
    refGenomeBlocks = refGenomeBlocks.split(')(')
    #list holding the reference genome
    refGenome = []
    #dict holding positions of end of synteny blocks
    refSynteny = {}
    for z in range(0, len(refGenomeBlocks)):
        refCurBlock = refGenomeBlocks[z]
        temp = refCurBlock.split()
        refGenome += temp
        refSynteny[abs(int(temp[0]))] = True
        refSynteny[abs(int(temp[len(temp)-1]))] = True
    #map absolute value to pos for refGenome
    #use a dictionary 
    refValtoPos = {}
    refValtoPos[0] = -1
    for a in range(0, len(refGenome)):
        refValtoPos[abs(int(refGenome[a]))] = a

    syntenyBlockBeforeSplit = fileIn.readline()
    #remove leading and trailing parentheses
    syntenyBlockBeforeSplit = syntenyBlockBeforeSplit[1:-1]
    #split the synteny blocks 
    syntenyBlockSplit = syntenyBlockBeforeSplit.split(')(')

    numBreaks = 0
    for b in range(0, len(syntenyBlockSplit)):
        curBlockBeforeSplit = syntenyBlockSplit[b]
        curBlock = curBlockBeforeSplit.split()
        for c in range(0, len(curBlock)-1):
            curVal = curBlock[c]
            refPos = refValtoPos[abs(int(curVal))]
            refVal = refGenome[refPos]
            #signs are the same, check forward (right)
            if int(curVal) == int(refVal):
                if int(curVal) in refSynteny:
                    continue
                nextVal = curBlock[c+1]
                if int(nextVal) in refSynteny:
                    continue
                refNextPos = refValtoPos[abs(int(nextVal))]
                refnextVal = refGenome[refNextPos]
                # if nextPos is incorrect or signs mismatch
                if refNextPos != refPos+1 or int(nextVal) != int(refnextVal):
                    numBreaks += 1
            #signs mismatch, check before
            else:
                nextVal = curBlock[c+1]
                refNextPos = refValtoPos[abs(int(nextVal))]
                refnextVal = refGenome[refNextPos]
                if refNextPos != refPos-1 or int(nextVal) == int(refnextVal):
                    numBreaks += 1
    print(numBreaks)

if __name__ == '__main__':
    main()