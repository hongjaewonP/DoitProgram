from utils.chatbotPreprocessing import Preprocess
from models.ner.NerModel import NerModel

p = Preprocess(word2index_dic=r'D:\school\team_project\chatBot\chatbotEngine\train_tools\dict\chatbot_dict.bin',
               userdic = r'D:\school\team_project\chatBot\chatbotEngine\utils\user_dict.tsv')

ner = NerModel(model_name=r'D:\school\team_project\chatBot\chatbotEngine\models\ner\ner_model.h5', proprocess= p)
query = "8월 18일 오후 3시 40분에 과제 일정 하나 추가해 줘."
predicts = ner.predict(query)
print(predicts)