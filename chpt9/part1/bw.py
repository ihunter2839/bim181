import sys

if __name__ == '__main__':
    fileIn = open(sys.argv[1])
    text = fileIn.readline()
    rots = [text]
    tL = len(text)
    for t in range(1, tL):
        rots.append(text[t:tL] + text[:t])
    rots.sort()
    out = ""
    for r in rots:
        out += r[-1:]
    print(out)
 
