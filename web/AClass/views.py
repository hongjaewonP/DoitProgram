from django.shortcuts import render
from .models import Class
from rest_framework import viewsets
from .serializers import ClassSerializers


# Create your views here.

#화면에 index.html을 뿌려줍니다. 간단하게 class의 모든 객체들을 화면에 나타냅니다.
def index(requests):
    classes = Class.objects.all()
    return render(requests, "index.html", {"classes":classes})

#viewset은 여러 API 기능을 통합해 하나의 set로 제공합니다. 
class ClassViewSet(viewsets.ModelViewSet):
    queryset = Class.objects.all()
    serializer_class = ClassSerializers
