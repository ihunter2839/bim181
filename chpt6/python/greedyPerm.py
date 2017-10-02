import sys

def main():
    file = open(sys.argv[1], "r");
    p1 = file.read()
    p2 = p1[1:len(p1)-1]
    perm = p2.split()
    numReverse = 0
    for a in range(0, len(perm)):
         # if value is already sorted 
        if perm[a] == a+1:
            continue
        else:
            #find index of next item in list
            swapInd = -1
            for b in range(a, len(perm)):
                if abs(int(perm[b])) == a+1:
                    swapInd = b
                    break
            #swap values between b, swapInd + 1
            if swapInd != a:
                if a-1 == -1:
                    tempStr = perm[swapInd::-1]
                else:
                    tempStr = perm[swapInd:a-1:-1]
                for c in range(0, len(tempStr)):
                    toSwap = str(0-int(tempStr[c]))
                    if int(toSwap) > 0:
                        toSwap = '+' + toSwap
                    perm[c+a] = toSwap
                numReverse += 1
                sys.stdout.write('(')
                for z in range(0, len(perm)):
                    sys.stdout.write(perm[z])
                    if(z != len(perm)-1):
                        sys.stdout.write(' ')
                sys.stdout.write(')')
                sys.stdout.flush()
                print()
            #check that value has correct sign
            if int(perm[a]) < 0:
                perm[a] = '+' + str(abs(int(perm[a])))
                numReverse += 1
                sys.stdout.write('(')
                for y in range(0, len(perm)):
                    sys.stdout.write(perm[y])
                    if(y != len(perm)-1):
                        sys.stdout.write(' ')
                sys.stdout.write(')')
                sys.stdout.flush()
                print()
    print(numReverse)

if __name__ == '__main__':
  main()