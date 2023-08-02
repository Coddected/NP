from rest_framework import serializers
from NaturesPark.models import Plant

class ReceivePlantSerializer(serializers.ModelSerializer):
    plant_name = serializers.CharField(max_length = 20)
    plant_leaf_state = serializers.CharField(max_length = 20)

    class Meta:
        model = Plant
        fields = ("plant_name", "plant_leaf_state")
        