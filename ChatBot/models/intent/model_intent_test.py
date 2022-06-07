from utils.chatbotPreprocessing import Preprocess
from models.intent.intentModel import IntentModel
from models.intent.returnQuestion import returnQ
from models.intent.returnAnswer import returnAnswer

p = Preprocess(word2index_dic=r'D:\school\team_project\chatBot\chatbotEngine\train_tools\dict\chatbot_dict.bin',
               userdic=r'D:\school\team_project\chatBot\chatbotEngine\utils\user_dict.tsv')

intent = IntentModel(model_name=r'D:\school\team_project\chatBot\chatbotEngine\models\intent\intent_model.h5', proprocess= p)


query = returnQ()
predict = intent.predict_class(query)
predict_label = intent.labels[predict]

print(query)
print("의도 예측 클래스 : ", predict)
print("의도 예측 라벨 : ", predict_label)

print(returnAnswer.returnA(predict))
