import pandas as pd
import tensorflow as tf
from tensorflow.keras import preprocessing
from tensorflow.keras.models import Model
from tensorflow.keras.layers import Input, Embedding, Dense, Dropout, Conv1D, GlobalMaxPool1D, concatenate
import matplotlib.pyplot as plt     #그래프

#데이터 읽어오기
train_file = "total_train_data.csv"
print("데이터 불러오는 중 : " + train_file +"\n")
data = pd.read_csv(train_file, encoding= 'cp949', delimiter=',')
queries = data['query'].tolist()
intents = data['intent'].tolist()

from utils.chatbotPreprocessing import Preprocess

p = Preprocess(word2index_dic = r'D:\school\team_project\chatBot\chatbotEngine\train_tools\dict\chatbot_dict.bin',
               userdic=r'D:\school\team_project\chatBot\chatbotEngine\utils\user_dict.tsv')

print("데이터 불러오기 완료\n")

#단어 sequence 생성
print("단어 시퀀스 생성 중\n")
sequences = []
for sentence in queries:
    pos = p.pos(sentence)
    keywords = p.get_keywords(pos, without_tag= True)
    seq = p.get_wordidx_sequence(keywords)
    sequences.append(seq)
print("단어 시퀀스 생성 완료\n")

#단어 index sequence vector 생성
#단어 sequence vector 크기
print("단어 인덱스 시퀀스 벡터 생성 중\n")
from config.GlobalParams import MAX_SEQ_LEN
padded_seqs = preprocessing.sequence.pad_sequences(sequences, maxlen= MAX_SEQ_LEN, padding = 'post')
print("단어 인덱스 시퀀스 벡터 생성 완료\n")

#학습용 검증용 테스트용 데이터셋 생성 7:2:1
print("데이터셋 분류 중\n")
ds = tf.data.Dataset.from_tensor_slices((padded_seqs, intents))
ds = ds.shuffle(len(queries))

train_size = int(len(padded_seqs)*0.7)
val_size = int(len(padded_seqs)*0.2)
test_size = int(len(padded_seqs)*0.1)

train_ds = ds.take(train_size).batch(20)
val_ds = ds.skip(train_size).take(val_size).batch(20)
test_ds = ds.skip(train_size+val_size).take(test_size).batch(20)
print("데이터셋 생성 완료\n")

#하이퍼파라미터 설정
drop_prob = 0.5
EMB_SIZE = 128
EPOCH = 5
VOCAB_SIZE = len(p.word_index) +1   #전체 단어 수

#CNN 모델 정의
input_layer = Input(shape = (MAX_SEQ_LEN,))
embedding_layer = Embedding(VOCAB_SIZE, EMB_SIZE, input_length = MAX_SEQ_LEN)(input_layer)
dropout_emb = Dropout(rate = drop_prob)(embedding_layer)

conv1 = Conv1D(
    filters = 128, kernel_size = 3, padding = 'valid', activation = tf.nn.relu)(dropout_emb)
pool1 = GlobalMaxPool1D()(conv1)

conv2 = Conv1D(
    filters = 128, kernel_size = 4, padding = 'valid', activation = tf.nn.relu)(dropout_emb)
pool2 = GlobalMaxPool1D()(conv2)

conv3 = Conv1D(
    filters = 128, kernel_size = 5, padding = 'valid', activation = tf.nn.relu)(dropout_emb)
pool3 = GlobalMaxPool1D()(conv3)

#3,4,5 gram 후 합치기
concat = concatenate([pool1, pool2, pool3])

hidden = Dense(128, activation = tf.nn.relu)(concat)
dropout_hidden = Dropout(rate = drop_prob)(hidden)
logits = Dense(6, name='logits')(dropout_hidden)    #의도 개수 작성
predictions = Dense(6, activation = tf.nn.softmax)(logits)

#모델 생성
print("CNN 모델 생성 중\n")
model = Model(inputs = input_layer, outputs = predictions)
model.compile(optimizer = 'adam', loss = 'sparse_categorical_crossentropy', metrics = ['accuracy'])
print("CNN 모델 생성 완료\n")

#모델학습
print("CNN 모델 학습 중\n")
result = model.fit(train_ds, validation_data = val_ds, epochs = EPOCH, verbose = 1)
print("CNN 모델 학습 완료\n")

#모델의 평가
loss, accuracy = model.evaluate(test_ds, verbose = 1)
print("Accuracy : %f" % (accuracy*100))
print('loss : %f' % (loss))

#모델 그래프
print(result.history.keys())
plt.plot(result.history['accuracy'])
plt.plot(result.history['val_accuracy'])
plt.title('model accuracy')
plt.ylabel('accuracy')
plt.xlabel('accuracy')
plt.xlabel('epoch')
plt.legend(['train', 'test'], loc = 'upper left')
plt.show()

# 모델 저장
model.save('intent_model.h5')
print("CNN 모델 저장 완료\n")