import os
import tabula
file_path = r"S19CE-EngineeringSyllabus.pdf"

df = tabula.read_pdf(file_path, pages='all', stream = True,lattice=False, encoding='Windows 1252')
def pdf_extract_info(file):
    #dst = os.path.dirname(file)
    #dst 디렉토리 생성 - csv 파일 저장
    dst = "tables"
    if not os.path.isdir(dst):
        os.mkdir(dst)
    else:
        tabula.convert_into(file, dst+"/output.csv", pages='all', output_format="csv", stream=True,lattice=False)
print(df)
#df.to_csv('output.csv', encoding='utf-8')
pdf_extract_info(file_path)
#특정 디렉토리 안의 pdf들의 표를 추출하는 코드
#tabula.convert_into_by_batch("directory_with_pdfs", output_format="csv", pages='all')