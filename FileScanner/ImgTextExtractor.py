    #추출할 필드
#표: 교과목 명
#표: 학수번호
#표: 학점/시간
#표: 개설 전공
#표: 수업시간/강의실 : 자연어처리 필요? 하여간 정제 필요
#표: 담당 교수 성명 / 소속 / 이메일 / 연락처 : 정제 필요
#표: 면담 장소 / 시간 : 정제 필요

#선수학습사항
#강의 방식 : 표 + 자연어처리
#학습 평가 방식 : 표 + 자연어처리
#교재

#표: 차시별 강의 계획 : 날짜 column만 date 형식으로 바꾸고 나머지는 그대로 내용 때려박으면 안되나? 그래서 해당 날짜에 그 알람 울리게 하고...


#path = 'C:/Users/wonai/mystatus/Doit_program/(2021-1)의료사회복지론 강의계획안(2차수정).pdf_dir/0_(2021-1)의료사회복지론 강의계획안(2차수정).pdf.jpg'
#C:\Users\wonai\mystatus\Doit_program\FileScanner\venv\lib\site-packages\vision-1.0.0-py3.8-nspkg.pth 삭제 필수

import os
import io

from google.cloud import vision
from google.cloud.vision_v1 import types
#함수로 만들어서 dir 안의 모든 이미지를 text 추출하게끔 해야 함.
#파라미터는 dir 절대경로
#추출한 txt를 .txt 파일에 저장해서 위치를 return???
#근데 띄어쓰기마다 \n처리 되어있는데 이거 어떻게 복구하지
def extract_txt_from_img(dir):
    client = vision.ImageAnnotatorClient()

    for file_name in os.listdir(dir):
        with io.open(os.path.join(dir, file_name), 'rb') as image_file:
            content = image_file.read()

        image = types.Image(content=content)

        response = client.text_detection(image=image)
        labels = response.text_annotations

        print('Label:')
        for label in labels:
            print(label.description)


