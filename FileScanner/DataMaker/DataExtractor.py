# -*- coding: utf-8 -*-

#img text extractor에서 받은 text list를 input으로 받고
#output으로는 줄글 정보를 출력한다.

import re

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
    standard_name_list = ['Description', 'Prerequisites', 'Format', 'Objectives', 'Required', 'Supplementary', 'optional','Policies' ]

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
        str_list.append(str)
        string_list.append(str_list)
    return string_list
    #info_list[0][1~끝까지][0]를 모아 string을 만들 거임.



# mylist=
#
# std = standard_location(mylist)
# info_list = extract_information(mylist, std)
# print(make_string(info_list))