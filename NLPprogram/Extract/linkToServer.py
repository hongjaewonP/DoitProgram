import ftplib
import os
import paramiko


host = '10.200.13.243'
port = 22

transprot = paramiko.transport.Transport(host,port)
userId = "start23"  # example
password = 'ewhastart23' # example

# 연결
transprot.connect(username = userId, password = password)
sftp = paramiko.SFTPClient.from_transport(transprot)

# Upload - 파일 업로드
remotepath = 'remote_test_file.csv' # sftp에 업로드 될때 파일 경로와 파일이름(이렇게 저장이 됨)을 써줍니다.
localpath  = 'C:/Users/wonai/mystatus/Doit_program/DoitProgram/NLPprogram/Extract/dayInfo.csv' # local피시의 파일 경로와 파일이름(pc에 저장되어있는 파일이름)을 써줍니다.
sftp.put(localpath, remotepath)

sftp.close()