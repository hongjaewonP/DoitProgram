class Class(models.Model):
    number = models.CharField(max_length=200,null=True,default='')
    title = models.CharField(max_length=200, null=True, default='')
    subnum= models.CharField(max_length=50,null=True,default='')
    professor = models.CharField(max_length=200, null=True, default='')
    downloadPath = models.CharField(max_length=200, null=True, default='')
    filename = models.CharField(max_length=200, null=True, default='')
    crawled_time = models.DateTimeField(auto_now=True,null=True)
    weeks = models.CharField(max_length=50,null=True,default='')
    days = models.CharField(max_length=50,null=True,default='')
    Activities = models.CharField(max_length=50,null=True,default='')
    Ranges = models.CharField(max_length=50,null=True,default='')
    Materials = models.CharField(max_length=50,null=True,default='')
    Assignments = models.CharField(max_length=50,null=True,default='')


