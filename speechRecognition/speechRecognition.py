import speech_recognition as sr # pip install SpeechRecognition

r = sr.Recognizer()
'''
이 인스턴스는 다양한 API를 사용하여 음성인식을 함
총 7가지 API를 사용한 음성인식 메서드가 존재함
recognize_bing(): Microsoft Bing Speech
recognize_google(): Google Web Speech API
recognize_google_cloud(): Google Cloud Speech - requires installation of the google-cloud-speech package
recognize_houndify(): Houndify by SoundHound
recognize_ibm(): IBM Speech to Text
recognize_sphinx(): CMU Sphinx - requires installing PocketSphinx -> 이것만 오프라인 가능
recognize_wit(): Wit.ai

API key가 필요!

지원하는 오디오 파일 형식 : WAV, AIFF, AIFF-C, FLAC
'''

audioFile = sr.AudioFile(r"D:\school\team_project\SpeechRecognition\TEST FILE\kor_test1.wav") # 파일 위치
with audioFile as source:
    audio = r.record(source)

print(type(audio))
print(r.recognize_google(audio,None,"ko-KO"))   # 기본은 영어를 인식, 한글 언어 인식 가능. 테스트 해보니 잘 됨
# google web speech api는 무료고 편리한데 하루 50번만 돌려볼 수 있다는 커다란 단점 존재... 유료로도 횟수를 늘릴 수 >없음<
# 다음 학기 때 API 구매하는 데 예산 요청해도 괜찮을 것 같다



