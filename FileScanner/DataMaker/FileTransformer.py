#아직 hwp를 이미지로 바꾸는 건 성공 못함...
#hwp -> pdf 하려면 특정 라이브러리 설치해야 하는데 이 방법 말고 다른 방법 찾고 있음.

#-*- coding: utf-8 -*-

#pdf to jpg 코드에 필요한 라이브러리
import os

#word to pdf 코드에 필요한 라이브러리
import comtypes
import ntpath
from pdf2jpg import pdf2jpg

#hwp to pdf 코드에 필요한 라이브러리

import win32com.client as win32  # 한/글 열 수 있는 모듈

def pdf_to_jpg(file):
    dest = os.path.dirname(file)
    if not os.path.isdir(dest):
        os.mkdir(dest)
    pdf2jpg.convert_pdf2jpg(file, dest, dpi = 300, pages ='ALL')
    #이미지 파일 저장된 dir 위치를 return한다. 그래야 이 return 값을 받은 imgTextExtractor이 dir 내 이미지에서 text 추출한다.
    return (file+'_dir')


#word 파일을 먼저 pdf로 바꿔준다.
def word_to_pdf(file):
    dest = os.path.dirname(file)

    word = comtypes.client.CreateObject('Word.Application')
    word.Visible = False
    doc = word.Documents.Open(file)

    file_name = ntpath.basename(file)
    output_file_path = os.path.join(dest, file_name + ".pdf")
    doc.SaveAs(output_file_path, FileFormat=17)
    doc.Close()
    return output_file_path

def word_to_jpg(file):
    pdf_to_jpg(word_to_pdf(file))

#pdf_to_jpg('C:/Users/wonai/mystatus/Doit_program/test1.pdf')









#한글 파일을 pdf로 바꾸는데.

# def hwp_to_pdf(file):
#
#     hwp = win32.gencache.EnsureDispatch('HWPFrame.HwpObject')
#     hwp.RegisterModule('FilePathCheckDLL', 'SecurityModule')
#
#     hwp.Open(file)
#
#     hwp.HAction.GetDefault("FileSaveAs_S", hwp.HParameterSet.HFileOpenSave.HSet)
#     hwp.HParameterSet.HFileOpenSave.filename = os.path.join(file, ".pdf")
#     hwp.HParameterSet.HFileOpenSave.Format = "PDF"
#     hwp.HAction.Execute("FileSaveAs_S", hwp.HParameterSet.HFileOpenSave.HSet)
#
#     hwp.Quit()
#     return hwp.HParameterSet.HFileOpenSave.filename
#
# def hwp_to_jpg(file):
#     pdf_to_jpg(hwp_to_pdf(file))

#pdf_to_jpg('C:/Users/wonai/mystatus/Doit_program/00test4kor_origin.pdf')