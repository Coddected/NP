from rest_framework import serializers
from NaturesPark.models import ReceiveImg

class ReceiveImgSerializer(serializers.ModelSerializer):
    image = serializers.ImageField(use_url=False)

    class Meta:
        model = ReceiveImg
        fields = ('image', )