#-*- coding: utf-8 -*-

import os
import shutil
import win32com.client
from docx2pdf import convert  # 라이브러리 import
import tabula
from PIL import Image
from pdf2jpg import pdf2jpg
import io
import re


def extract_txt_from_img(file_name):
    from google.cloud import vision
    client = vision.ImageAnnotatorClient()

    with io.open(file_name, 'rb') as image_file:
        content = image_file.read()

    image = vision.Image(content=content)

    response = client.text_detection(image=image)
    texts = response.text_annotations

    text_list =[]
    #text_list 하나에는 한 jpg 파일의 모든 [단어, 단어bound] 원소쌍이 들어있다.
    for text in texts:
        word_list =[]
        #print('\n"{}"'.format(text.description))
        word_list.append(text.description)
        vertices = (['({},{})'.format(vertex.x, vertex.y)
                    for vertex in text.bounding_poly.vertices])
        word_list.append(vertices)
        #print('bounds: {}'.format(','.join(vertices)))
        text_list.append(word_list)

    if response.error.message:
        raise Exception(
            '{}\nFor more info on error messages, check: '
            'https://cloud.google.com/apis/design/errors'.format(
                 response.error.message))

    return text_list
    #파일의 모든 단어와 단어의 bound를 list에 담아 리턴한다.
#print(extract_txt_from_img('C:/Users/wonai/mystatus/Doit_program/test1.pdf_dir/merged.jpg'))


def str_to_bound(word):
    #word는 ['단어', [(LT), (RT), (RB), (LB)]] list이다.
    #text_list의 bound는 str 형식이라 int 형으로 바꿔준다.
    word_list = [word[0]]
    tuple_list = []
    for element in word[1]:
        bounds = re.findall(r'\d+', element)
        x_bound = int(bounds[0])
        y_bound = int(bounds[1])
        t1 = (x_bound, y_bound)
        tuple_list.append(t1)
    word_list.append(tuple_list)
    return word_list

# 줄글 파트 추출
# 추출할 파트:
#   - 교과목 개요 course description : Description
#   - 선수학습사항 prerequisites : Prerequisites
#   - 강의 방식 course Format : Format
#   - 교과목표 course objectives : Objectives
#   - 학습평가방식 evaluation system : ㅅㅂ evaluation system 둘이 합친 형태 써야 할듯. 한번에 출력되는 경우도 있으니 or 문 써서 시발
#   - 주교재 required material : Required
#   - 부교재 supplementary materials : Supplementary
#   - 참고문헌 optional additional readings : optional
#   - 수업운영규정 course policies : Policies

# 위의 영어 이름 bound를 기준으로 잡고 standard list 만든기
# 만약 위의 파트 중 빠진 게 있다면 걍 뺀 상태로 standard list

def standard_location(text_list):
    #text 추출 기준이 될 단어 위치 반환.
    # [['total', [(),(),(),()]], [['교과목'],(),(),(),()], 개설, 수업, 면담] 값으로 리턴
    standard_list = []
    standard_name_list = ['Description', 'Prerequisites', 'Format', 'Objectives', 'Evaluation', 'Required', 'Supplementary', 'optional','Policies' ]

    total = str_to_bound(text_list[0])
    standard_list.append(['total_bound', total[1]])
    for w in range(1, len(text_list)):
        word = str_to_bound(text_list[w])
        if word[0] in standard_name_list:
            standard_list.append(word)
        #스위치 문으로 word[0]이 description, required 등이라면 standard_list에 삽입.
    return standard_list


def extract_information(text_list, std_list):
    # std_list[0]은 토탈 x길이 y길이임.
    # 2번 문항... 다 정리.
    std_num = len(std_list) #기준의 개수.
    # 1번 문항은 std_list1과 2 사이 y축 바운드 + x축은 페이지 전체 std_list[0]에서 따오기. 즉 y bound로 std_list[1][1][3][1] ~ std_list[2][1][0][1] 사이
    info_list = []
    for index in range(1, std_num-1):
        part_list = []
        part_list.append(std_list[index])
        for w in text_list:
            word = str_to_bound(w)
            if word[1][0][1] > std_list[index][1][3][1] and word[1][0][1] < std_list[index+1][1][0][1]:
                part_list.append(word)
        info_list.append(part_list)
    return info_list

def make_string(info_list):
    info_num = len(info_list)  # 파트 개수.
    #info_list[0][1~끝까지] = 1번 description 관한 내용.
    string_list = []
    for index in range(info_num):
        str_list = []
        str_list.append(info_list[index][0][0])
        str = ""
        for i in range(1, len(info_list[index])):
            str += info_list[index][i][0]
            str += " "
        str_list.append(str)
        string_list.append(str_list)
    return string_list
    #info_list[0][1~끝까지][0]를 모아 string을 만들 거임.


def extract_txt(file_name):
    total_list = extract_txt_from_img(file_name)
    standard_list = standard_location(total_list)
    info_list = extract_information(total_list, standard_list)
    information = make_string(info_list)
    return information



def pdf_to_jpg(file):
    pyfile = os.path.dirname(os.path.abspath(__file__))
    pyfile = pyfile.replace("\\", "/")
    file_path = os.path.splitext(file)[0]
    file_name = file_path.split('/')[-1]
    dest = pyfile + "/" + file_name

    imgfile = dest + "/" + file_name + ".pdf_dir"

    if not os.path.isdir(dest):
        os.mkdir(dest)

    pdf2jpg.convert_pdf2jpg(file, dest, dpi = 300, pages ='ALL')

    return imgfile #이미지 파일이 있는 dir 절대 경로. 이대로 img_merge 함수 파라미터로 넘기기.


def img_merge(imgfile):
    images_list = []
    for file_name in os.listdir(imgfile):
        abs_path = os.path.join(imgfile, file_name)
        path = abs_path.replace('\\', '/')
        images_list.append(path)

    imgs = [Image.open(i) for i in images_list]

    min_img_width = min(i.width for i in imgs)

    total_height = 0
    for i, img in enumerate(imgs):
        if img.width > min_img_width:
            imgs[i] = img.resize((min_img_width, int(img.height / img.width * min_img_width)), Image.ANTIALIAS)
        total_height += imgs[i].height

    img_merge = Image.new(imgs[0].mode, (min_img_width, total_height))
    y = 0

    pyfile = os.path.dirname(os.path.abspath(__file__))
    pyfile = pyfile.replace("\\", "/")
    file_path = os.path.splitext(imgfile)[0]
    file_name = file_path.split('/')[-1]
    dest = pyfile + "/" + file_name

    for img in imgs:
        img_merge.paste(img, (0, y))

        y += img.height
    merged_path = dest +  '/merged.jpg'
    img_merge.save(merged_path)

    return merged_path ##merged 이미지의 절대 경로




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

#pdf_extract_table_info(file)

#df.to_csv('output.csv', encoding='utf-8')
#특정 디렉토리 안의 pdf들의 표를 추출하는 코드
#크롤링으로 받은 강의계획서들을 한 디렉토리에 받은 후, 다음 코드를 실행할 예정
#tabula.convert_into_by_batch("directory_with_pdfs", output_format="csv", pages='all')

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



#origin_file = "C:/Users/wonai/NLPprogram/testpdf.pdf"
#pdf_file = moveDir(origin_file)
#pdf_extract_table_info(pdf_file)
#merged_img = img_merge(pdf_to_jpg(pdf_file))
#word_list = extract_txt_from_img(merged_img)
print()