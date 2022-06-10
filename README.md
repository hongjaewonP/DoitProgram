#tabula를 이용한 pdf->csv 파일 - extract_file/extract.py 확인
#django server에 파일을 올리는 코드  - web-changes 폴더에 있습니다 (서버 코드는 DoitProgram의 web폴더)
web 폴더에 있는 파일 중 urls.py, admin.py, models.py, views.py은 web-changes 폴더에 올린 urls.py, admin.py, models.py, views.py로 바꿔주셔야 하고, migrations폴더에 migration이 없어서 /migrations/0002_courses.py를 추가해 주셔야 
django 서버 접속 시 파일 정보들이 업로드 된 것을 확인할 수 있습니다.


**핵심 구동 단계**

0. 서버 생성

1. 교내 강의계획안 사이트로부터 크롤링으로 강의계획서 파일 저장

2. 다양한 형식의 파일을 통일된 형식으로 변환

3. tabula OCR API를 통해 자연어 추출, csv파일 생성

4-1. google vision OCR API를 통해 자연어 추출, csv파일 생성

4-2. 추출한 문자를 koNLPY와 NLTK를 이용하여 자연어처리 및 요약

5. 강의계획서 및 csv 파일을 서버의 database에 저장

* 4-2, 5의 강의계획서 저장 부분은 아직 미구현인 상태입니다.


**main 폴더 설명**

ChatBot : 딥러닝 챗봇 구현과 관련된 폴더입니다.

FileScanner : Google vision API를 사용한 OCR 추출 관련 폴더입니다. 

data_1200 : 테스트를 쉽게 하기 위해 강의계획서 1200개를 수동으로 다운받아 모은 폴더입니다.

extract_file : tabula를 이용하여 정보 추출, pdf->csv 파일 변환 - extract_file 폴더의 extract.py 확인

#test.py는 기말 발표전 적절한 기술을 찾을 때 tesseract를 이용했던 코드입니다. extract.py 를 확인해주세요

web : 서버 구현, 크롤링 구현과 관련된 폴더입니다.

web-changes : 크롤링으로 받은 파일 중 어플 일정표에 쓰일 정보들을 django server에 올리는 코드  
#web 폴더에 있는 파일 중 urls.py, admin.py, models.py, views.py은 web-changes 폴더에 올린 
urls.py, admin.py, models.py, views.py로 바꿔주셔야 하고, migrations폴더에 migration이 없어서 /migrations/0002_courses.py를 추가해 주셔야 django 서버 접속 시 파일 정보들이 업로드 된 것을 확인할 수 있습니다.

test.png : AVD로 어플 실행화면 캡쳐

jolp : 파일업로드(+ firebase의 realtime db에 업로드)/ 메인화면(월별 시간표)/ 주간 시간표 코드 
       navigation drawer를 이용하여 activity 간 이동 

#기존 mysql - php 연동 로그인+파일 업로드 코드는 pull request push하면 확인할 수 있습니다!


**NLP program 폴더 설명**

내부 extract 파일로 들어가면 강의계획서를 자연어처리를 통해 요약하고 일정을 csv 파일로 추출하는 파이썬 코드가 있습니다.

mvDir.py: 강의계획서를 pdf확장자로 바꾸고 일괄적으로 dir 위치를 옮깁니다.

pdfTojpg.py: pdf 강의계획서를 jpg 파일로 바꾸어 ocr 이 적용되게 합니다.

extractTable.py: pdf 강의계획서에서 표 형식의 내용을 추출하여 csv로 만듭니다.

extractTxt.py: jpg 강의계획서에서 줄글 형식의 내용을 추출하여 text list 를 반환합니다.

preprocessEng.py: 위에서 반환된 text list를 전처리하고 요약하는 영어 지원 프로그램입니다.

preprocessKor.py: 위에서 반환된 text list를 전처리하고 요약하는 한글 지원 프로그램입니다.

saveOutputCSV.py: 추출된 table.csv에서 필요한 정보값을 뽑아내고 일정을 추출합니다.

saveTxtCSV.py: 요약 처리된 text list 정보들을 csv 파일로 만들어 저장합니다.

linkToServer.py: 강의계획서의 내용을 담은 csv 파일을 서버에 저장케 하는 파일입니다.

main.py: 위의 모든 파일을 일괄적으로 실행케 하는 main 파일입니다.
