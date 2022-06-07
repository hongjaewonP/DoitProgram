#-*- coding: utf-8 -*-
#본인 컴퓨터에 한글이 설치 되지 않아서 한글 파일은 실행을 못하는 중.
import os
import shutil
import win32com.client
from docx2pdf import convert  # 라이브러리 import

#file = "C:/Users/wonai/NLPprogram/testpdf.pdf"


def moveDir(file):
    path, ext = os.path.splitext(file)

    pyfile = os.path.dirname(os.path.abspath(__file__))
    pyfile = pyfile.replace("\\", "/")
    file_path = os.path.splitext(file)[0]
    file_name = file_path.split('/')[-1]
    dest = pyfile + "/" + file_name

    pdf_dest = dest+"/"+file_name+".pdf"
    if not os.path.isdir(dest):
        os.mkdir(dest)

    if ext == ".pdf":
        shutil.move(file, dest)
        return pdf_dest ##변환된 pdf 파일 위치.

    elif ext == ".doc" or ext == ".docx":
        convert(file, dest+"/"+file_name+".pdf")
        return pdf_dest


    elif ext == ".hwp":
        hwp = win32com.client.gencache.EnsureDispatch('HWPFrame.HwpObject')
        hwp.RegisterModule('FilePathCheckDLL', 'SecurityModule')

        # HWP to PDF
        hwp.Open(file)
        hwp.HAction.GetDefault("FileSaveAs_S", hwp.HParameterSet.HFileOpenSave.HSet)
        hwp.HParameterSet.HFileOpenSave.FileName = os.path.join(dest, path + ".pdf")
        hwp.HParameterSet.HFileOpenSave.Format = "PDF"
        hwp.HAction.Execute("FileSaveAs_S", hwp.HParameterSet.HFileOpenSave.HSet)

        hwp.Quit()
        return pdf_dest

    else:
        print("ext not supported!!") ##워드 한글 pdf 아닌 경우.


