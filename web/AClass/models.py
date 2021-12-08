
from django.db import models

# Create your models here.
class Class(models.Model):
    number = models.CharField(max_length=200)
    subnum = models.CharField(max_length=50)
    title = models.CharField(max_length=200)
