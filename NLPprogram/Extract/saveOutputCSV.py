import csv

def makeDaylist(file_name):
    f = open(file_name, 'r', newline='')
    rdr = csv.reader(f)
    date_list = []
    flag = False
    for line in rdr:
        for word in line:
            if word == '주차':
                flag = True
                break

        for word in line:
            if word == '강의 관련':
                flag = False
                break
        if flag == True:
            date_list.append(line)
        else:
            continue
    f.close()

    for line in range(1, len(date_list)):
        if date_list[line][0] == '':
            date_list[line][0] = date_list[line-1][0]

    return date_list



def makeEvaluation(file_name):
    f = open(file_name, 'r', newline='')
    rdr = csv.reader(f)
    info = []
    flag = False

    for line in rdr:
        for word in line:
            if word == '강의':
                flag = True
            elif word == '중간고사':
                return
            else:
                continue

        if flag == True:
            info.append(line)
        else:
            continue
    f.close()

    evaluation_list = []
    for line in info:
        evaluation_list.append([x for x in line if x])
    return evaluation_list

def makeLecture(file_name):
    f = open(file_name, 'r', newline='')
    rdr = csv.reader(f)
    info = []
    flag = False

    for line in rdr:
        for word in line:
            if word == '중간고사':
                flag = True
            elif word == '주차':
                return
            else:
                continue

        if flag == True:
            info.append(line)
        else:
            continue

    lecture_list = []
    for line in info:
        lecture_list.append([x for x in line if x])
    return lecture_list


def makeTableCSV(info_list, name):
    #근데 이렇게 하면 다음 내용을 이 csv파일에 추가 못하는데 이거 한 번 방법 알아보기.
    #그... lecture evaluation date 각각 3개 csv 파일 만들어야 함??? 음...
    if info_list is None:
        return
    else:
        f = open(name, 'w', newline='')
        writer = csv.writer(f)
        writer.writerows(info_list)
        f.close

def makeTotalCSV(filename):
    makeTableCSV(makeLecture(filename), 'lectureInfo.csv')
    makeTableCSV(makeEvaluation(filename), 'evaluationInfo.csv')
    makeTableCSV(makeDaylist(filename), 'dateInfo.csv')

filename = 'C:/Users/wonai/mystatus/Doit_program/DoitProgram/NLPprogram/Extract/testpdf/output.csv'
makeTotalCSV(filename)