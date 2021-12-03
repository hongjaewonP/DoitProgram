from django.db import models

# Create your models here.
class Class(models.Model):
    className = models.CharField(max_length=200)
    classNum= models.CharField(max_length=50)
    professor = models.CharField(max_length=200)
