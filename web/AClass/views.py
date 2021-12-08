from django.shortcuts import render
from .models import Class
from rest_framework import viewsets
from .serializers import ClassSerializers


# Create your views here.

def index(requests):
    classes = Class.objects.all()
    return render(requests, "index.html", {"classes":classes})


class ClassViewSet(viewsets.ModelViewSet):
    queryset = Class.objects.all()
    serializer_class = ClassSerializers
