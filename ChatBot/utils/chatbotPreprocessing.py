from konlpy.tag import Komoran
import pickle

class Preprocess:
    def __init__(self, word2index_dic='', userdic=None):

        self.komoran = Komoran(userdic= userdic)
        self.exclusion_tags = [
            'JKS', 'JKC', 'JKG', 'JKO', 'JKB', 'JKV', 'JKQ',
            'JX', 'JC', 'SF', 'SP', 'SE', 'SS', 'SO',
            'EP', 'EF', 'EC', 'ETM', 'ETN',
            'XSN', 'XSV', 'XSA'
        ]

        # 단어 인덱스 사전 불러오기
        if (word2index_dic != ''):
            f = open(word2index_dic, "rb")
            self.word_index = pickle.load(f)
            f.close()
        else:
            self.word_index = None

    #형태소 분석기 pos 태거 : 외부에서는 komoran 객체를 직접적으로 호출할 일이 없도록 정의한 래퍼 함수. 종류를 바꿀 경우 이 함수 내용만 변경하면 된다.
    def pos(self, sentence):
        return self.komoran.pos(sentence)

    #불용어( 제외된 품사 ) 제거 후 필요한 품사 정보 가져오는 함수
    def get_keywords(self, pos, without_tag = False):
        f = lambda x: x in self.exclusion_tags
        word_list = []
        for p in pos:
            if f(p[1]) is False:
                word_list.append(p if without_tag is False else p[0])
        return word_list

    # 키워드를 단어 index sequence로 변환
    def get_wordidx_sequence(self, keywords):
        if self.word_index is None:
            return []
        w2i = []
        for word in keywords:
            try:
                w2i.append(self.word_index[word])
            except KeyError:
                # 해당 단어가 사전에 없는 경우 OOV 처리
                w2i.append(self.word_index['OOV'])
        return w2i