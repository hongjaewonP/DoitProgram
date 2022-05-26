# 모델 가지고 의도분류 모델 만들기
import tensorflow as tf
from tensorflow.keras.models import Model, load_model
from tensorflow.keras import  preprocessing

#의도 분류 모델 모듈
class IntentModel:
    def __init__(self, model_name, proprocess):
        #의도 클래스별 label
        self.labels = {0: "인사", 1: "욕설", 2: "강의 추가", 3: "강의 삭제", 4: "잡담", 5 : "학교 홈페이지 연결"}

        #의도분류모델 불러오기
        self.model = load_model(model_name)

        #챗봇 preprocess 객체
        self.p = proprocess

    #의도 클래스 예측
    def predict_class(self, query):
        pos = self.p.pos(query) #형태소 분석

        #문장 내 키워드 추출
        keywords = self.p.get_keywords(pos, without_tag= True)
        sequences = [self.p.get_wordidx_sequence(keywords)]

        #단어 sequence vector 크기
        from config.GlobalParams import MAX_SEQ_LEN

        #padding 처리
        padded_seqs = preprocessing.sequence.pad_sequences(sequences, maxlen = MAX_SEQ_LEN, padding = 'post')

        predict = self.model.predict(padded_seqs)
        predict_class = tf.math.argmax(predict, axis = 1)
        return predict_class.numpy()[0]
