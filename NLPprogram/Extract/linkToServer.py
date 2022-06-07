import paramiko

def linkToserver(remotepath, localpath):
    host = '10.200.13.243'
    port = 3306

    transprot = paramiko.transport.Transport(host,port)
    userId = "start23"
    password = 'ewhastart23'

    # 연결
    transprot.connect(username = userId, password = password)
    sftp = paramiko.SFTPClient.from_transport(transprot)

    # Upload - 파일 업로드
    sftp.put(localpath, remotepath)

    sftp.close()