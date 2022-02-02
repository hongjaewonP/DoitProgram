'''
입력된 강의목록으로 시간표를 만드는 코드
담아놓은 강의계획서 기반으로 만들기
만약 classes.name이 동일할 경우 / 학수번호가 같을 경우 동일한 강의 -> 경우의 수를 나눔
입력된 강의목록으로 가능한 모든 시간표를 만들어 return할 수 있도록 구현 필요
'''
from dataclasses import dataclass

# 강의 시간을 저장하는 구조체 classes
class classes:
    name : str = None
    start : int =  None
    end : int =  None
    room : str =  None

timetable = [[0]*7 for i in range (10)] #각각 요일(7)과 교시(10)

def check_same(a,b) :
    if (a == b):
        return True

def make_timetable():
    a = classes()
    timetable = [[a]* 7 for i in range(10)]
    return timetable

def check_timetable(a = classes()):
    timetable[a.start[0][0]][a.start[0][1]] += 1
    timetable[a.start[1][0]][a.start[1][1]] += 1
    for i in range(0, 2):
        if check_same(a.start[i], a.end[i]) != True:
            timetable[a.end[i][0]][a.end[i][1]] += 1


#크롤링해서 정보를 불러왔다고 침
class1 = classes()
class1.name = "캡스톤디자인프로젝트B"
class1.start = [[3,1],[4,4]] #화요일 4교시에 시작 , 금요일 5교시에 시작 (교시, 요일) **시간표가 다양한데 크기는 유동적으로 조정할 예정**
class1.end = [[3,1],[5,4]]  #화요일 4교시에 종료 , 금요일 6교시에 종료
class1.room = ["공대강당","공대강당"]

check_timetable(class1)

#출력해보기
print("월 화 수 목 금 토 일 ")
for i in range (10):
    print(str(i + 1) + "교시 : ", end="")
    for j in range (7):
        print(str(timetable[i][j]) + " ", end = '')
    print()

'''
가능한 모든 시간표 만드는 방법
숫자가 2가 넘어가먄 겹치는 것 -> 기각 
모든 숫자가 1 이하면 저장
'''''
if not i > 1 in timetable:
    timetableA = make_timetable()
    print("make it!")

    pos = []
    for i in range (10):
        for j in range (7):
            if timetable[i][j] == 1 :
                pos.append([i,j])

    for p in range(len(pos)):
        timetableA[pos[p][0]][pos[p][1]] = class1


    print("\t\t월\t\t화\t\t수\t\t목\t\t금\t\t토\t\t일 ")
    for i in range (10):
        print(str(i + 1) + "교시 : \t", end="")
        for j in range (7):
            print(str(timetableA[i][j].name), end = '\t')
        print()
