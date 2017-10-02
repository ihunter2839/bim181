import sys
#synteny block number
__VAL__ = None
#edge leaving this synteny block
__H__ = None
#edge entering this synteny block 
__T__ == None

def makeSyn(synVal, head, tail):
    __VAL__ = synVal
    __H__ = head
    __T__ = tail

def getVal():
    return __VAL__

def getH():
    return __H__

def getT():
    return __T__

def getSyn():
    tmp = [__VAL__, __H__, __T__]
    return tmp
