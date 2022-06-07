# -*- encoding: utf-8 -*-
import csv


def save_csv(word_list):
    for i in word_list:
        i[1] = i[1].replace('\u2022', "")

    file_path = "csv 저장 위치. 절대 경로."

    with open(file_path+'info.csv', 'w', newline='') as f:
        # using csv.writer method from CSV package
        write = csv.writer(f)

        write.writerows(word_list)
    #return file_path