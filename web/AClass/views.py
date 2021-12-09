from django.shortcuts import render
from .models import *
from rest_framework import viewsets
from .serializers import ClassSerializers
from django.http import HttpResponse
import csv
import openpyxl
import pandas as pd
# Create your views here.

def index(requests):
    classes = Class.objects.all()
    return render(requests, "index.html", {"classes":classes})


class ClassViewSet(viewsets.ModelViewSet):
    queryset = Class.objects.all()
    serializer_class = ClassSerializers

def course_info(requests):
    #경로 지정
    path = 'C:/Users/pop/PycharmProjects/pythonProject1/tables/강의시간표강의계획안_20211124225307'
    df = pd.read_excel(path+'.xlsx')
    df.to_csv(path+'.csv', index=None, header=True)
    file = open(path+'.csv', 'r', encoding='utf-8')
    reader = csv.reader(file)
    print('------', reader)
    list = []
    for row in reader:
        list.append(Courses(course_no = row[0]+'-'+row[1],
                course_title = row[2],
                department = row[5],
                prof_name = row[7],
                class_time = row[10]))
    Courses.objects.bulk_create(list)
    return HttpResponse('course information upload')