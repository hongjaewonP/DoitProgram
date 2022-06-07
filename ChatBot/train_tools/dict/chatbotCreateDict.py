# 다운받은 말뭉치를 바탕으로 사전 파일을 생성한다.

from utils.chatbotPreprocessing import Preprocess
from tensorflow.keras import preprocessing
from konlpy.tag import Komoran
import pickle

#말뭉치 데이터 읽어오기 함수
def read_corpus_data(filename):
    with open(filename, 'r', encoding='UTF8') as f:
        data = [line.split('\t') for line in f.read().splitlines()]
        data = data[1:] #헤더 제거
    return data


#말뭉치 데이터 가져오기
print("말뭉치 데이터 가져오는 중")
corpus_data = read_corpus_data('./corpus3.txt')

#키워드 추출, 사전 리스트 생성
print("사전 리스트 생성 중")
p = Preprocess()
dict = []
for c in corpus_data:
    pos = p.pos(c[1])
    for k in pos:
        dict.append(k[0])

#사전에 사용될 word2index 생성
#사전의 첫 번째 index에 OOV 사용
tokenizer = preprocessing.text.Tokenizer(oov_token = 'OOV')
tokenizer.fit_on_texts(dict)
word_index =  tokenizer.word_index

#사전 파일 생성
print("사전 파일 생성 중")
f = open("chatbot_dict.bin", "wb")
try:
    pickle.dump(word_index,f)
except Exception as e:
    print(e)
finally:
    f.close()