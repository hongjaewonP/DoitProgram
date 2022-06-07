"""web URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include
from AClass.views import index, class_info, class_action
from rest_framework import routers
import AClass.views

#rest API 통신을 위해 라우터를 생성합니다.
router = routers.DefaultRouter()
router.register("classes", AClass.views.ClassViewSet)

#주소 뒤 각 path를 붙이면 해당 url과 페이지 항목을 연결해줍니다.
#통상 viewset의 url은 router로 설정해줍니다.
urlpatterns = [
    path('admin/', admin.site.urls),
    path('', index),
    path('api/',include(router.urls)),
    path('class_info/',class_info),
    path('class_action/<int:pk>/',class_action)
]
