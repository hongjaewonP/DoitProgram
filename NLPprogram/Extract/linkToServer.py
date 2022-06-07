import ftplib
import os
import paramiko


host = 'applan.cryaditlm9jj.us-east-2.rds.amazonaws.com'
username = 'start23'
ssh = paramiko.SSHClient()
ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
ssh.connect(host, username=username, key_filename='key파일명.pem')
sftp = ssh.open_sftp()

localpath = 'C:/Users/wonai/mystatus/Doit_program/DoitProgram/NLPprogram/Extract/dateInfo.csv'
filepath = ''

sftp.put(localpath, filepath)  # 파일 업로드

sftp.close()
ssh.close()