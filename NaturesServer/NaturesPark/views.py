import os
import uuid

import cv2
import numpy as np

import matplotlib.pyplot as plt

from django.shortcuts import render
from django.conf import settings

from pathlib import Path

from rest_framework.response import Response
from rest_framework.decorators import api_view
from .models import ReceiveImg
from .serializers import ReceiveImgSerializer

# Create your views here.

@api_view(['POST'])
def index(request):
    serializer = ReceiveImgSerializer(data=request.data)

    if serializer.is_valid():
        img = request.FILES['image'].read()

        img = cv2.imdecode(np.frombuffer(img, np.uint8), cv2.IMREAD_UNCHANGED)
        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

        if not os.path.exists(str(settings.BASE_DIR) + '/media/'):
            os.makedirs(str(settings.BASE_DIR) + '/media/')

        path = str(settings.BASE_DIR) + '/media/' + str(uuid.uuid4()) + '.png'

        cv2.imwrite(path, img)

        return Response({"status": "success", "data": settings.SERVER_ADDRESS + '/media/' + os.path.split(path)[1]})
    else:
        return Response({"status": "error", "data": serializer.errors})