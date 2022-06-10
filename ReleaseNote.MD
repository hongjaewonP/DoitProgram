1. 어플 앱을 android 스마트폰에 download 한다. (하단에 링크 있습니다)
2. 회원가입 버튼을 누르고 임의 이메일, 비밀번호, 학교, 이름을 입력하여 계정을 생성한다.
3. 촤측 상단 메뉴를 클릭한 후, 주간 시간표로 이동한다.
4. 우측 하단의 아이콘을 클릭하면 아이콘 3개가 뜨는데 위에서부터 강의계획서 검색 일정 추가, 임의 일정 추가, 챗봇 버튼이다.
5. 강의계획서 검색 일정 추가버튼을 눌러 분반과 학수번호를 입력하여 강의 일정을 추가한다.
6. 임의 일정 추가 버튼을 눌러 임의의 일정을 추가한다. (시간 입력은 입력창을 더블클릭하면 시간 입력용 창이 뜬다)
7. 추가된 일정을 길게 눌러 삭제한다.
8. 우측 하단의 챗봇 버튼을 눌러 인사, 강의 추가, 강의 삭제, 학교 홈페이지 연결 주제로 대화할 수 있다.
  ex> 강의 추가는 어떻게 할 수 있어? / 학교 공지사항은 어디서 확인해?
[app-debug.zip](https://github.com/HellenHong/DoitProgram/files/8875670/app-debug.zip)
압축 풀면 apk 파일이있습니다. 

![i1](./image/i1.jpg)
![i2](./image/i2.jpg)
![i3](./image/i3.jpg)

****챗봇 기능이 돌아가지 않을 경우 테스트하는 방법****
1. DoitProgram(main)에서 ChatBot 폴더를 찾는다.
2. 다음 파일들을 다운받는다.
경로도 함께 지정해주어야 한다.
github에 올려둔 경로 기준
ChatBot - utils - chatbotPreprocessing.py
ChatBot - utils - user_dict.csv
ChatBot - config - GlobalParams.py
ChatBot - models - intent - intentModel.py
ChatBot - models - intent - model_intent_test.py
ChatBot - models - intent - returnAnswer.py
ChatBot - models - intent - returnQuestion.py
ChatBot - models - intent -> readme.txt의 다운로드링크에서 intent_model.h5 다운 (intent 폴더에 넣어주면 됩니다)
ChatBot - train_tools - dict - chatbot_dict.bin

필요한 라이브러리는
konlpy -> Komoran 이용
tensorflow -> keras 이용
json

위 파일들을 저장한 후 pycharm이나 colaboratory에서 실행한다.
model_intent_test.py 파일을 실행한다.

질문을 입력한다.
답변을 확인한다.

![i4](./image/i4.png)




**자연어처리 NLP program 폴더 설명**

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
