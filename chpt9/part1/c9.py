import sys

class edge:

    def __init__(self, value, nextNode):
        self.val = value
        self.next = nextNode

class node:

    def __init__(self, value, index, depth):
        self.val = value
        self.ind = index
        self.dep = depth
        #indicates string of termination
        self.term = False
        #edges maps values to nodes
        self.edges = {}
        self.color = [None]

    def addEdge(self, nextNode):
        self.edges[nextNode.val] = nextNode

class trie:

    def __init__(self, strings):
        self.numNodes = 0
        self.root = node(0, self.numNodes, 0)
        self.root.color = [True]*2
        self.numNodes += 1
        self.nodeList = [self.root]
        #self.buildTrie(strings)

    def buildTrie(self, strings, colPos):
        for s in strings:
            cur = self.root
            for a in s:
                if a not in cur.edges:
                    newNode = node(a, self.numNodes, cur.dep+1)
                    newNode.color = [False]*2
                    cur.addEdge(newNode)
                    self.nodeList.append(newNode.val)
                    self.numNodes += 1
                cur = cur.edges[a]
                cur.color[colPos] = True
            cur.term = True

    def prnAdjList(self, cur):
        if len(cur.edges) == 0:
            return
        for e in cur.edges:
            termNode = cur.edges[e]
            print(str(cur.ind)+"->"+str(termNode.ind)+":"+str(termNode.val))
            self.prnAdjList(termNode)

    def prnNodeVals(self, cur):
        if cur.val == 0:
            print(str(cur.val) + " " + str(cur.color))
        if len(cur.edges) == 0:
            return
        for e in cur.edges:
            termNode = cur.edges[e]
            print(termNode.val + " " + str(termNode.color))
            self.prnNodeVals(termNode)

    def dictNodeVals(self, cur, valDict):
        #update the count of string occurences
        if cur.val not in valDict:
            valDict[cur.val] = 1
        else:
            valDict[cur.val] += 1
        #recursive cases
        if len(cur.edges) > 0:
            for a in cur.edges:
                self.dictNodeVals(cur.edges[a], valDict)
        return valDict


    def trieMatchingGenome(self, text):
        indexes = []
        textLen = len(text)
        for a in range(0, textLen):
            curString = text[a:textLen]
            curNode = self.root
            for c in range(0, len(curString)):
                curChar = curString[c:c+1]
                if curChar in curNode.edges:
                   curNode = curNode.edges[curChar]
                elif curNode.term == True:
                    indexes.append(a) 
                    break
                else:
                    break
        return indexes

    def treeTransform(self, curNode):
        #is leaf node
        if len(curNode.edges) == 0:
            return
        #is non-branching path
        elif len(curNode.edges) == 1 and curNode.val != 0:
            nextNode = list(curNode.edges.values())[0]
            curNode.edges = nextNode.edges
            curNode.val += nextNode.val
            self.treeTransform(curNode)
        else:
            for c in curNode.edges:
                self.treeTransform(curNode.edges[c])
        return

    def setDepth(self, cur, depth):
        cur.dep = depth
        if len(cur.edges) > 0:
            for c in cur.edges:
                nextNode = cur.edges[c]
                self.setDepth(nextNode, depth+1)
        return

    def longestRepeat(self, cur, maxRepeat, pathString):
        #maxRepeat[0] --> maxdepth
        #maxRepeat[1] --> maxVal
        curString = "" 
        if len(cur.edges) > 0:
            #print("cur is: " + str(cur.val))
            #print("path is: " + pathString)
            #print("dep is: " + str(cur.dep))
            if cur.val != 0:
                curString = pathString + cur.val
                #print(curString)
                #print()
                if len(curString) > len(maxRepeat[1]):
                    maxRepeat[0] = cur.dep
                    maxRepeat[1] = curString
        for c in cur.edges:
            maxRepeat = self.longestRepeat(cur.edges[c], maxRepeat, curString)
        return maxRepeat

    def longestSharedSub(self, cur, maxVal, pathString):
        curString = ""
        #print("curVal is: " + str(cur.val))
        #print("curPath is: " + pathString)
        #print("maxVal is: " + maxVal)
        #print("color is: " + str(cur.color))
        if len(cur.edges) > 0 and False not in cur.color:
            #skip the root
            if cur.val != 0:
                curString = pathString + cur.val
                if len(curString) > len(maxVal):
                    maxVal = curString 
            for c in cur.edges:
                nextNode = cur.edges[c]
                maxVal = self.longestSharedSub(nextNode, maxVal, curString)
        return maxVal

    def shortestUniqueSub(self, cur, minVal, pathString):
        curPath = ""
        #print("curVal is: " + str(cur.val))
        #print("curPath is: " + pathString)
        #print("minVal is: " + minVal)
        #print("color is: " + str(cur.color))
        if cur.val != 0:
            curPath = pathString + cur.val
        #blue is text1, pos 0 in color
        #red is text2, pos1 in color
        if cur.color[0] == True and cur.color[1] == False:
            if len(curPath) < len(minVal):
                return curPath
            return minVal
        for c in cur.edges:
            nextNode = cur.edges[c]
            minVal = self.shortestUniqueSub(nextNode, minVal, curPath)
        return minVal


def readInPatterns(fileInput):
    pats = []
    tmp = fileInput.readline()
    while tmp != "":
        if tmp[len(tmp)-1] == '\n':
            pats.append(tmp[:-1] + "$")
        else:
            pats.append(tmp + "$")
        tmp = fileIn.readline()
    return pats

def generateSuffixes(fileInput):
    text = fileInput.readline()
    text = text.split('\n')
    text = text[0]
    suffix = []
    textLen = len(text)
    for a in range(0, textLen):
        suffix.append(text[a:textLen])
    return suffix

def generateSuffixes2(stringInput):
    suffix = []
    for text in stringInput:
        textLen = len(text)
        for a in range(0, textLen):
            print(text[a:textLen])
            suffix.append(text[a:textLen])
    return suffix

def generateSuffixes3(stringIn):
    suffix = []
    textLen = len(stringIn)
    for a in range(0, textLen):
        suffix.append(stringIn[a:textLen])
    return suffix

if __name__ == '__main__':
    sys.setrecursionlimit(2000)
    fileIn = open(sys.argv[1])
    string1 = fileIn.readline()[:-1]
    string2 = fileIn.readline()[:-1]
    pat1 = generateSuffixes3(string1)
    pat2 = generateSuffixes3(string2)
    testTrie = trie(pat1)
    testTrie.buildTrie(pat1, 0)
    testTrie.buildTrie(pat2, 1)
    testTrie.treeTransform(testTrie.root)
    minVal = string1
    minVal = testTrie.shortestUniqueSub(testTrie.root, minVal, "")
    print(minVal)



