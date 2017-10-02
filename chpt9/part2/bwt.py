import sys
import fileinput

def comp(s1, s2):
    #0 --> s1 < s2
    #1 --> s1 > s2
    if s1 == "$":
        return 0
    elif s2 == "$":
        return 1
    elif s1 < s2:
        return 0
    else:
        return 1

def dgns(s1, s1C, s2, s2C, out):
    print(s1)
    print(str(s1C))
    print(s2)
    print(str(s2C))
    print(out)

def ms(string1):
    sL = len(string1)
    if sL == 0:
        return ""
    if sL == 1:
        return string1
    mid = sL // 2
    s1 = string1[:mid]
    s1L = len(s1)
    s1 = ms(s1)
    s2 = string1[mid:]
    s2L = len(s2)
    s2 = ms(s2)
    out = ""
    s1C= 0
    s2C = 0
    while s1C < s1L and s2C < s2L:
        minFlag = comp(s1[s1C], s2[s2C])
        if minFlag == 0:
            out += s1[s1C]
            s1C += 1
            #dgns(s1, s1C, s2, s2C, out)
            while s1C < s1L and comp(s1[s1C], s2[s2C]) == 0:
                out += s1[s1C]
                s1C += 1
                #dgns(s1, s1C, s2, s2C, out)
            out += s2[s2C]
            s2C += 1
            #dgns(s1, s1C, s2, s2C, out)
        else:
            out += s2[s2C]
            s2C += 1
            #dgns(s1, s1C, s2, s2C, out)
            while s2C < s2L and comp(s1[s1C], s2[s2C]) == 1:
                out += s2[s2C]
                s2C += 1
                #dgns(s1, s1C, s2, s2C, out)
            out += s1[s1C]
            s1C += 1
            #dgns(s1, s1C, s2, s2C, out)
    if s1C < s1L:
        out += s1[s1C:]
        #dgns(s1, s1C, s2, s2C, out)
    if s2C < s2L:
        out += s2[s2C:]
        #dgns(s1, s1C, s2, s2C, out)
    return out

def getStarts(first):
    out = [None]*4
    out[0] = first.index("A")
    out[1] = first.index("C")
    out[2] = first.index("G")
    out[3] = first.index("T")
    return out

def getStarts2(first):
    out = [None]*5
    out[0] = first.index("$")
    out[1] = first.index("A")
    out[2] = first.index("C")
    out[3] = first.index("G")
    out[4] = first.index("T")
    return out

def lastToFirst(first, last):
    starts = getStarts(first)
    count = [0]*4
    lastToFirst = [None]*len(last)
    for s in range(0, len(last)):
        if last[s] == "$":
            lastToFirst[s] = 0
        elif last[s] == "A":
            lastToFirst[s] = starts[0] + count[0]
            count[0] += 1
        elif last[s] == "C":
            lastToFirst[s] = starts[1] + count[1]
            count[1] += 1
        elif last[s] == "G":
            lastToFirst[s] = starts[2] + count[2]
            count[2] += 1
        else:
            lastToFirst[s] = starts[3] + count[3]
            count[3] += 1
    return lastToFirst

def transform(first, last):
    lTf = lastToFirst(first, last)
    curRow = 0
    count = 1
    out = "$"
    while count < len(last):
        out = last[curRow] + out
        curRow = lTf[curRow]
        count += 1
    return out

def partialSuffixArray(text, k):
    sufAr = []
    for a in range(0, len(text)):
        sufAr.append((text[a:],a))
    sufAr.sort()
    out = []
    for s in range(0, len(sufAr)):
        if sufAr[s][1] % k == 0:
            out.append((s, sufAr[s][1]))
    return out

def match1(first, last, pat, lTf):
    sL = len(last)
    lastRev = last[-1:-len(last)-1:-1]
    top = 0
    bot = len(last) - 1
    #traverse pattern backwards
    for p in range(len(pat)-1, -1, -1):
        if top > bot:
            return 0
        curChar = pat[p]
        #curChar is matched, continue
        top = last.find(curChar, top, bot+1)
        if top != -1:
            bot = sL - lastRev.find(curChar, sL - bot - 1) - 1
            top = lTf[top]
            bot = lTf[bot]
        else:
            return 0
    return bot - top + 1

def makeCount(last):
    out = [[0]*5 for a in range(0, len(last)+1)]
    #0 is $
    #1 is A
    #2 is C
    #3 is G
    #4 is T
    curCounts = [0]*5
    for r in range(1, len(last)+1):
        l = last[r-1]
        if l == "$":
            curCounts[0] += 1
        elif l == "A":
            curCounts[1] += 1
        elif l == "C":
            curCounts[2] += 1
        elif l == "G":
            curCounts[3] += 1
        else:
            curCounts[4] += 1
        for i in range(0, 5):
            out[r][i] = curCounts[i]
    return out

def checkpointCount(count, k):
    tmp = []
    a = 0
    while a < len(count):
        tmp.append(count[a])
        a += k
    return tmp


def charToNum(s):
    if s=="$":
        return 0
    elif s=="A":
        return 1
    elif s=="C":
        return 2
    elif s=="G":
        return 3
    else:
        return 4

def match2(firstOc, count, last, pat):
    top = 0
    bot = len(last)-1
    for p in range(len(pat)-1,-1,-1):
        if top > bot:
            return 0
        curChar = pat[p]
        if last.find(curChar) != -1:
            curVal = charToNum(curChar)
            top = firstOc[curVal] + count[top][curVal]
            bot = firstOc[curVal] + count[bot+1][curVal] - 1
        else:
            return 0
    return bot - top + 1

def findOccurence(fisrtOc, count, sufAr, last, pat, k):
    top = 0
    bot = len(last)-1
    for p in range(len(pat)-1,-1,-1):
        if top > bot:
            return -1
        curChar = pat[p]
        if last.find(curChar, top, bot + 1) != -1:
            curVal = charToNum(curChar)
            #need to scroll through remainder of count
            countBase = count[top // k][curVal]
            countRemainder = top % k
            totalCount = 0
            start = (top // k) * k
            for a in range(start, start + countRemainder):
                if last[a] == curChar:
                    totalCount += 1
            totalCount += countBase
            top = firstOc[curVal] + totalCount

            countBase = count[bot // k][curVal]
            countRemainder = bot % k
            totalCount = 0
            start = (bot // k) * k
            for b in range(start, start + countRemainder + 1):
                if last[b] == curChar:
                    totalCount += 1      
            totalCount += countBase     
            bot = firstOc[curVal] + totalCount - 1
        else:
            return -1
    #[top]-[bot] all match pattern. find pos in text
    out = []
    for r in range(top, bot+1):
        stepCount = 1
        cur = r
        while cur % k != 0:
            curChar = last[cur]
            curVal = charToNum(curChar)
            countBase = count[cur // k][curVal]
            countRemainder = cur % k
            totalCount = 0
            start = (cur // k) * k
            for c in range(start, start + countRemainder):
                if last[c] == curChar:
                    totalCount += 1
            totalCount += countBase
            cur = firstOc[curVal] + totalCount
            stepCount += 1
        patPos = 0
        print(cur)
        for s in sufAr:
            if s[0] == cur:
                patPos = s[1] + stepCount
        out.append(patPos)

    return out

def getLast(string1):
    string1 += "$"
    sL = len(string1)
    rots = []
    for a in range(0, sL):
        tmp = string1[a:] + string1[:a]
        rots.append(tmp)
    rots.sort()
    out = []
    for r in rots:
        out.append(r[sL-1])
    return out

if __name__ == "__main__":
    fileIn = open(sys.argv[1])
    orgiText = fileIn.readline()[:-1]
    lastAr = getLast(orgiText)
    bwtLast = ""
    for b in lastAr:
        bwtLast += b
    bwtFirst = ms(bwtLast)
    pats = []
    tmp = fileIn.readline()
    while tmp != "":
        pats.append(tmp[:-1])
        tmp = fileIn.readline()

    #transform last to create partial suffix array
    t = transform(bwtFirst, bwtLast)
    k = 4

    sufAr = partialSuffixArray(t, k)
    t = 0
    #get first occurences
    firstOc = getStarts2(bwtFirst)
    bwtFirst = 0
    #get checkpoint array
    count = makeCount(bwtLast)
    count = checkpointCount(count, k)

    out = []
    for p in pats:
        val = findOccurence(firstOc, count, sufAr, bwtLast, p, k)
        if val != -1:
            for v in val:
                out.append(v)
    out.sort()
    tmp = ""
    for o in out:
        tmp += str(o) + " "

    print(tmp[:-1])


