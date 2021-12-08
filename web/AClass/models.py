from django.db import models

# Create your models here.
class Class(models.Model):
    number = models.CharField(max_length=200,null=True,default='')
    subnum= models.CharField(max_length=50,null=True,default='')
    title = models.CharField(max_length=200,null=True,default='')
    div_course= models.CharField(max_length=100,null=True,default='')
    professor = models.CharField(max_length=200,null=True,default='')
    crawled_time = models.CharField(max_length=200,null=True,default='')
