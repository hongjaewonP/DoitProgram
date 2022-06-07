#-*- copding: utf-8 -*-
import os

path = "C:\\Users\\wonai\\mystatus\\Doit_program\\DoitProgram\\NLPprogram\\new"
exefile = 'hwp5html'


res = []
for root, dirs, files in os.walk(path):
    rootpath = os.path.join(os.path.abspath(path), root)
    for file in files:
        filepath = os.path.join(rootpath, file)
        res.append(filepath)

    for result in res:
        filename = result[:-4]+ ".html"
        output = '--output ' + '"' + filename + '"'
        result = '"' + result + '"'
        print(exefile + " " + output + " " + result)
        os.system(exefile + " " + output + " " + result)