import sys

def generateSuffix(string):
    suffix = []
    sL = len(string)
    for a in range(0,sL):
        suffix.append(string[a:sL])
    return suffix

def readPatterns(file):
    pat = []
    tmp = file.readline()
    while tmp != "":
        pat.append(tmp[:-1])
        tmp = file.readline()
    return pat

if __name__ == "__main__":
    fileIn = open(sys.argv[1])
    string1 = fileIn.readline()[:-1]
    sufAr = generateSuffix(string1)
    pat = readPatterns(fileIn)
    sufAr = sufAr + pat
    sufAr.sort()

    totLen = len(string1)
    sufArPos = []
    for s in sufAr:
        pos = totLen - len(s)
        sufArPos.append(pos)

    patPos = []
    for p in pat:
        patPos.append(sufAr.index(p))

    out = []
    #print(str(sufAr))
    #print(str(sufArPos))
    #print(str(patPos))
    #print(str(pat))
    for p in range(0, len(pat)):
        #length of current pattern
        curPat = pat[p]
        #print(curPat)
        curPatLen = len(curPat)
        #position of pattern in suffix array
        curPatPos = patPos[p]
        count = 1
        start = sufArPos[curPatPos + count]
        end = start + curPatLen
        while end <= totLen and string1[start:end] == curPat:
            out.append(start)
            count += 1
            start = sufArPos[curPatPos + count]
            end = start + curPatLen
    tmp = ""
    for o in out:
        tmp += str(o) + " "
    print(tmp[:-1])

    