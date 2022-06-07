import olefile
import struct
import os

path = '/home/ec2-user/web/scripts/nari.hwp'
exepath = '/home/ec2-user/myenv/bin/hwp5html.exe'


def get_hwp_text(f):
    filename = path[:-4]+ ".html"
    output = '--output '+ '"' + filename + '"'
    result = '"' + path + '"'
    os.system(exepath + " " + output + " " + result)
