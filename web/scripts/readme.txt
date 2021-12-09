 *서버 테스트를 진행할 경우, 현재 크롤링 코드와 연동하는 데 오류가 발생하고 있습니다.
 *따라서 서버 테스트는 main.py로, 크롤링 테스트는 new_main.py로 진행해주세요.
 *이후 new_main.py로도 오류가 없도록 구현할 예정입니다.
 
 main.py : 서버 테스트용 크롤링 코드입니다.
 new_main.py : 크롤링 테스트용 코드입니다.


* 크롤링 코드 실행 전 준비 사항
 1. beautiful soup를 설치해줍니다.
 cmd > pip install beautifulsoup4
 2. selenium을 설치해줍니다.
 cmd > pip install selenium
 3. Selenium_screenshot을 설치해줍니다.
 cmd > pip install selenium_screenshot
 4. pillow를 설치해줍니다.
 cmd > pip install pillow
 5. web driver을 설치해줍니다. * chrome 버전 확인!
 https://sites.google.com/a/chromium.org/chromedriver/downloads
 
 6. 코드 내 28줄과 29줄에서, path와 downloadPath를 각각 자기 컴퓨터에 맞는 경로로 설정해주세요.
 path : chrome driver가 저장된 위치를 입력해줍니다.
 downloadPath : 다운받을 파일을 저장할 위치를 입력해줍니다.

 
*  크롤링 코드 실행 :
 '학기를 설정해주세요'라는 문장이 출력되면, 1~28의 숫자 중 하나를 입력해서 학기를 설정해주세요.
 프로그램이 실행됩니다. 파일이 자동으로 지정된 위치에 저장됩니다.
