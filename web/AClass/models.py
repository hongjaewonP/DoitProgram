<<<<<<< HEAD

from django.db import models

# Create your models here.
class Class(models.Model):
    number = models.CharField(max_length=200)
    subnum = models.CharField(max_length=50)
    title = models.CharField(max_length=200)
=======
from django.db import models

# Create your models here.
#강의에 대한 class 를 정의합니다. 기본적으로 charField로 선언하였습니다
#number = 학수번호, subnum = 분반, title=강의명 div_course=교과목구분, professor=교수명, crawled_time=크롤링된 시간

class Class(models.Model):
    number = models.CharField(max_length=200,null=True,default='')
    subnum= models.CharField(max_length=50,null=True,default='')
    title = models.CharField(max_length=200,null=True,default='')
    div_course= models.CharField(max_length=100,null=True,default='')
    professor = models.CharField(max_length=200,null=True,default='')
    crawled_time = models.CharField(max_length=200,null=True,default='')
>>>>>>> b9361f4fac4f82056a1adb917ecf64e59ecba63c
