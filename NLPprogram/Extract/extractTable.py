import os
import tabula

file = 'C:/Users/wonai/mystatus/Doit_program/DoitProgram/NLPprogram/test2.pdf' ##pdf 파일


def pdf_extract_table_info(file):
    pyfile = os.path.dirname(os.path.abspath(__file__))
    pyfile = pyfile.replace("\\", "/")
    file_path = os.path.splitext(file)[0]
    file_name = file_path.split('/')[-1]
    dst = pyfile + "/" + file_name

    if not os.path.isdir(dst):
        os.mkdir(dst)
        tabula.convert_into(file, dst+"/output.csv", pages='all', output_format="csv", stream=True,lattice=False)
    else:
        tabula.convert_into(file, dst+"/output.csv", pages='all', output_format="csv", stream=True,lattice=False)

    return dst

print(pdf_extract_table_info(file))

#df.to_csv('output.csv', encoding='utf-8')
#특정 디렉토리 안의 pdf들의 표를 추출하는 코드
#크롤링으로 받은 강의계획서들을 한 디렉토리에 받은 후, 다음 코드를 실행할 예정
#tabula.convert_into_by_batch("directory_with_pdfs", output_format="csv", pages='all')