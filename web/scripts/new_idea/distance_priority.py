'''
가장 효율적인 시간표 추천 기능
효율? : 연강인데 장소 간 거리 / 징검다리 우주공강 / 5연강 등등...
사용자가 우선순위를 설정할 수 있도록 만들기 (ex> 거리 우선 / 공강 우선 / 강의 평가 기능이 포함될 경우 평점 우선 등으로...)
각 항목 당 추천도를 매겨서 추천 시간표 보이기

* 연강일 경우 장소 간 거리 우선
1. 수강할 강의를 입력하면 조합하여 가능한 모든 시간표를 만든다. -> make_timetable.py
2. 강의실 정보를 받아와서 정해진 장소로 치환한다.
3. 네이버지도에서 거리나 시간 정보를 크롤링해온다.
4. 점수를 매긴다
정확한 시간은 나타낼 필요 없고 점수 매기기가 목적
=> 학생들이 자주 쓰는 지름길 같은 건 어떻게 반영하면 좋을까? => 새로운 기술을 적용할 수 있을까...?

* 연강이 3개 이상 있을 경우 추천도를 깎기
* 하루동안 학교에 있는 시간이 길수록 추천도를 깎기
* 공강이 있으면 추천도 올리기

-window 환경에서만 테스트함
-두 위치의 시간, 거리 가져오는 것까지만 구현해봄
'''
from selenium import webdriver
from bs4 import BeautifulSoup
import time
from selenium.webdriver.common.keys import Keys

start = "이화여자대학교 학관"
finish = "이화여자대학교 공과대학"
data = []

webdriver_path = r"D:\school\team_project\chromedriver_win32\chromedriver.exe"  #*******chrome driver 위치로 수정****
op = webdriver.ChromeOptions()
op.add_argument('headless')
op.add_argument('--start-fullscreen')
driver = webdriver.Chrome(webdriver_path, chrome_options= op
                          )
driver.get(r"https://map.naver.com/v5/directions/-/-/-/mode?c=14107103.1786139,4494701.9630842,15,0,0,0,dh")


#도보버튼 클릭
driver.find_element_by_xpath('//*[@id="container"]/shrinkable-layout/div/directions-layout/directions-result/div[1]/div[1]/ul/li[3]/a').click()
driver.implicitly_wait(5)
#출발지 입력창 클릭
el = driver.find_element_by_xpath('//*[@id="directionStart0"]')
el.click()
driver.implicitly_wait(5)
#출발지에 이전 강의실 위치 입력
el.send_keys(start)
el.send_keys(Keys.ENTER)
time.sleep(3)
#도착지 입력창 클릭
el.send_keys(Keys.ENTER)
#도착지에 이후 강의실 위치 입력
ar = driver.find_element_by_xpath('//*[@id="directionGoal1"]')
ar.send_keys(finish)
ar.send_keys(Keys.ENTER)
time.sleep(3)
#길찾기 버튼 클릭
ar.send_keys(Keys.ENTER)
time.sleep(3)

# 정보 가져오기
html = driver.page_source
soup = BeautifulSoup(html, 'html.parser')

score_result = soup.find('div', {'class': 'summary_box'})
score_result = str(score_result)
times, length = score_result.split("추천", 1)

length = length.split(">")
length = length[7]
length = length.split("<")
length = length[0]

times = times.split(">")
times = times[5]
times = times.split("<")
times = times[0]

print("거리 : " + length + "\n시간 : " + times)
