from django.db import models

# Create your models here.
class Class(models.Model):
    number = models.CharField(max_length=200)
    subnum = models.CharField(max_length=50)
    title = models.CharField(max_length=200)

class Courses(models.Model):
    course_no = models.CharField(max_length=50)
    course_title = models.CharField(max_length=200)
    department = models.CharField(max_length=300)
    prof_name = models.CharField(max_length=100)
    class_time = models.CharField(max_length=300)

    def __str__(self):
        template = '{0.course_no} {0.course_title} {0.department} {0.prof_name} {0.class_time}'
        return template.format(self)

