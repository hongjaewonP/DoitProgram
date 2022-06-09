from django.shortcuts import render
from .models import Todo
from .models import Class
from rest_framework import viewsets
from .serializers import ClassSerializers
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from django.http import HttpResponse, JsonResponse

# Create your views here.

#화면에 index.html을 뿌려줍니다. 간단하게 class의 모든 객체들을 화면에 나타냅니다.
def index(requests):
    classes = Class.objects.all()
    return render(requests, "index.html", {"classes":classes})

#viewset은 여러 API 기능을 통합해 하나의 set로 제공합니다. 
class ClassViewSet(viewsets.ModelViewSet):
    queryset = Todo.objects.all()
    serializer_class = ClassSerializers

#일반적인 강의 조회(GET)/생성(POST)입니다.
@csrf_exempt
def class_info(request):
    if request.method == 'GET':
        queryset = Todo.objects.all()
        serializer = ClassSerializers(queryset, many=True)
        return JsonResponse(serializer.data, json_dumps_params={'ensure_ascii': False}, safe=False)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = ClassSerializers(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, json_dumps_params={'ensure_ascii': False}, status=201)
        return JsonResponse(serializer.errors, status=400)


#특정 id의 강의 조회 / 수정입니다.
@csrf_exempt
def class_action(request, idnums):

    obj = Todo.objects.filter(idnums = idnums);

    if request.method == 'GET':
        serializer = ClassSerializers(obj, many=True)
        return JsonResponse(serializer.data, json_dumps_params={'ensure_ascii': False}, safe=False)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = ClassSerializers(obj, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, json_dumps_params={'ensure_ascii': False}, status=201)
        return JsonResponse(serializer.errors, status=400)

    elif request.method == 'DELETE':
        obj.delete()
        return HttpResponse(status=204)
