import sys

def main():
    fileIn = open(sys.argv[1])
    rawPerm = fileIn.read()
    rawPerm = rawPerm[1:len(rawPerm)-1]
    permAr = rawPerm.split()
    permAr = ['+0'] + permAr + [('+'+str(len(permAr)+1))]

    numBreaks = 0
    for a in range(0, len(permAr)-2):
        if int(permAr[a]) + 1 == int(permAr[a+1]) :
            continue
        else:
            numBreaks += 1

    print(numBreaks)

if __name__ == '__main__':
  main()