from rest_framework import serializers
from .models import Todo

#rest API통신을 위해 만들어야하는 serializer 입니다.
#파이썬 데이터를 JSON으로 변환하기 위해 필요합니다.
class ClassSerializers(serializers.ModelSerializer):
    class Meta:
        model = Todo
        fields = ('days', 'activities', 'period', 'classroom' )
