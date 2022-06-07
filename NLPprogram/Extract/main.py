#메인 파일 만들어야 함....
import mvDir #pdf로 확장자 다 바꿔서 해당 pdf 위치를 리턴함.
import pdfTojpg #pdf위치를 갖고 이미지 merge해서 그 merged.jpg 위치를 리턴함.
import extractTable #pdf위치를 갖고 거기서 table 뽑아서 output.csv 위치 리턴
import saveOutputCSV #output.csv 위치 가지고 dateInfo ... 등등 csv 파일 만듦. 이 때 그 위치는 이 파이썬 파일 있는 곳.
import extractTxt #merged.jpg 위치를 가지고 거기서 text 뽑아내서 text 리턴함.
import preprocessEng #위에서 받은 text를 summarized_text로 만들어 리턴
import preprocessKor #위와 같음
import saveTxtCSV #위에서 받은 text를 csv 파일로 만듦. 이 때 그 위치는 이 파이썬 파일 있는 곳.
import linkToServer #csv 파일을 서버에 저장함

origin_file_path = ""

pdf_file_path = mvDir.moveDir(origin_file_path)
output_file_path = extractTable.pdf_extract_table_info(pdf_file_path)
saveOutputCSV.makeTotalCSV(output_file_path) #info csv 만들기.

merged_img_path = pdfTojpg.pdf_to_jpg(pdf_file_path)
origin_text = extractTxt.extract_txt_from_img(merged_img_path)
summarized_text = preprocessKor.summarized_kor(origin_text)
saveTxtCSV.makeCSV(summarized_text) #desc csv 만들기.

linkToServer.linkToserver('applan.cryaditlm9jj.us-east-2.rds.amazonaws.com', 'local ip 주소 / 저는 10.200.13.243 썼어요!!')
