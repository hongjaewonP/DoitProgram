from rest_framework import serializers
from .models import Class

<<<<<<< HEAD
=======
#rest API통신을 위해 만들어야하는 serializer 입니다.
#파이썬 데이터를 JSON으로 변환하기 위해 필요합니다.
>>>>>>> b9361f4fac4f82056a1adb917ecf64e59ecba63c
class ClassSerializers(serializers.ModelSerializer):
    class Meta:
        model = Class
        fields = ('__all__')
