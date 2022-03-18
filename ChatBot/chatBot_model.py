'''
모델을 학습시킴

학습 데이터를 데이터베이스에 저장 필요. -> 학습프로그램을 만들어도 되지만 정해진 엑셀 양식에 질문과 답변을 정리해서 DB에 저장해도 ok
'''

#import chatBot_excel #그냥 엑셀 읽어오는
import numpy as np #행렬처리
from konlpy.tag import Komoran #속도가 빠르고 품사 태그가 다양
from gensim.models import Word2Vec
import time

def read_review_data(filename):
    with open(filename, 'r') as f:
        data = [line.split('/t') for line in f.read().splitlines()]
        data = data[1:]
    return data

start = time.time()

print('1) 말뭉치 데이터 읽기 시작')
review_data = read_review_data(r'.\ratings.txt') # python3부터는 ANSI만 지원
print (len(review_data))
print('1) 말뭉치 데이터 읽기 완료 : ', time.time() - start)


print('2) 명사만 추출 시작')
komoran = Komoran()
docs = [komoran.nouns(sentence[1]) for sentence in review_data]
print('2) 명사 추출 완료 : ', time.time() - start)

print('3) 모델 학습 시작')
model = Word2Vec(sentences = docs, vector_size= 200, window= 4, hs= 1, min_count= 2, sg = 1)
#모델 학습에 필요한 문장 데이터이자 입력값, 단어 임베딩 벡터 차원, 주변 단어 윈도우 크기, 1은 softmax 0은 음수 샘플링? , 학습할 단어 최소 빈도값, 0은 CBOW 1은 skip-gram
print('3) word2vec 모델 학습 완료 : ', time.time() - start)

print('4) 학습된 모델 저장 시작')
model.save('nvmc.model')
print('4) 학습된 모델 저장 완료 : ', time.time() - start)

print('학습된 말뭉치 수 : ', model.corpus_count)
print('전체 단어 수 : ', model.corpus_total_words)


