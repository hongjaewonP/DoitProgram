from utils.chatbotPreprocessing import Preprocess
from models.intent.intentModel import IntentModel

p = Preprocess(word2index_dic=r'D:\school\team_project\chatBot\chatbotEngine\train_tools\dict\chatbot_dict.bin',
               userdic=r'D:\school\team_project\chatBot\chatbotEngine\utils\user_dict.tsv')

intent = IntentModel(model_name=r'D:\school\team_project\chatBot\chatbotEngine\models\intent\intent_model.h5', proprocess= p)


query = input("무엇이 궁금한가요? : \n")
predict = intent.predict_class(query)
predict_label = intent.labels[predict]

print(query)
print("의도 예측 클래스 : ", predict)
print("의도 예측 라벨 : ", predict_label)