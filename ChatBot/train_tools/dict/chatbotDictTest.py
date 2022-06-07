import pickle
from utils.chatbotPreprocessing import Preprocess

#단어 사전 불러오기
f = open("../dict/chatbot_dict.bin", "rb")
word_index = pickle.load(f)
f.close()

sent = "헬로우 좋은 하루 보내길 바래!!!"

#전처리 객체 생성
p = Preprocess(userdic='utils/user_dict.tsv')
#형태소 분석기
pos = p.pos(sent)

#품사 태그 없이 키워드 출력
keywords = p.get_keywords(pos, without_tag= True)
for word in keywords:
    try:
        print(word, word_index[word])
    except KeyError:
        print(word, word_index['OOV'])