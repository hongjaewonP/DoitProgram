# -*- coding: utf-8 -*-

#텍스트 전처리
#영강 국문강의래도 이중 언어 코퍼스임.
#정제 순서: 전각문자 제거, 대소문자 통일 -> 정규 표현식
# -> 문장단위 분절 -> 분절(mecab, moses) -> 병렬 코퍼스 정제(필요한가?)
# -> 서브워드 분절(BPE사용) -> 분절 복원

# 띄어쓰기
# kss로 문장별 \n처리
# khaiii로 형태소 분석

from konlpy.tag import Kkma, Twitter
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

def spacing_kkma(wrongSentence):
    kkma = Kkma()
    tagged = kkma.pos(wrongSentence)
    corrected = ""
    for i in tagged:
        if i[1][0] in "JEXSO":
            corrected += i[0]
        else:
            corrected += " "+i[0]
    if corrected[0] == " ":
        corrected = corrected[1:]
    return corrected

def spacing_all(Sentences):
    total_list = []
    for i in Sentences:
        info_list = []
        info_list.append(i[0])
        info_list.append(spacing_kkma(i[1]))
        total_list.append(info_list)
    return total_list


def kor_preprocessing(text):
    tagger = Twitter()
    return ['/'.join(t) for t in tagger.pos(text, norm=True, stem=True)]

def kor_all_preprocessing(text_list):
    string_list = spacing_all(text_list)
    preprocessed_list = []
    for i in string_list:
        preprocessed_list.append(kor_preprocessing(i[1]))
    return preprocessed_list

# mylist =
#
# stringtext = kor_all_preprocessing(mylist)
# print(stringtext)
