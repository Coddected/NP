import os
import uuid

import cv2
import numpy as np

import openai

import matplotlib.pyplot as plt

from django.shortcuts import render
from django.conf import settings

from pathlib import Path

from rest_framework.response import Response
from rest_framework.decorators import api_view
from .models import Plant
from .serializers import ReceivePlantSerializer

import torch
import torchvision.transforms as transforms
from torchvision.datasets import ImageFolder 

# Create your views here.

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

data_dir = "./New Plant Diseases Dataset(Augmented)/New Plant Diseases Dataset(Augmented)"
train_dir = data_dir + "/train"
valid_dir = data_dir + "/valid"

train = ImageFolder(train_dir, transform=transforms.ToTensor())

model = torch.jit.load('./plant-disease-model.pt')

model.eval()

tf_r = transforms.Compose([transforms.Resize((256, 256))])

OPENAI_KEY = ""
openai.api_key = OPENAI_KEY

classes = []

def to_device(data, device):
    if isinstance(data, (list, tuple)):
        return [to_device(x, device) for x in data]
    
    return data.to(device, non_blocking=True)

@api_view(['POST'])
def img_save(request):
    path = str(settings.BASE_DIR) + '/media/' + str(uuid.uuid4()) + '.png'

    with open(path, 'wb') as output:
        output.write(request.data['image'].read())

    return Response({"status": "success", "data": settings.SERVER_ADDRESS + '/media/' + os.path.split(path)[1]})

@api_view(['POST'])
@torch.no_grad()
def img_classify(request):
    path = str(settings.BASE_DIR) + '/media/'
    ori = cv2.imread(path + request.data['image_url'].split('/')[-1], cv2.COLOR_RGBA2BGR)

    print(ori.shape)

    ori = cv2.rotate(ori, cv2.ROTATE_90_CLOCKWISE)

    print(tf_r(torch.Tensor(ori).permute(2, 1, 0)))

    xb = to_device(to_device(tf_r(torch.Tensor(ori).permute(2, 1, 0)), device).unsqueeze(0), device)
    yb = model(xb)

    _, preds = torch.max(yb, dim=1)

    print(train.classes[preds[0].item()].split('___')[1])

    return Response({"status": "success", "data": train.classes[preds[0].item()].split('___')[1]})

@api_view(['GET'])
def plant_info(request):

    serializer = ReceivePlantSerializer(data=request.data)

    plant_info = Plant(plant_name = request.data['plant_name'], plant_leaf_state = request.data['plant_leaf_state'])

    plant_info.save()

    if serializer.is_valid():
        print(request.data["plant_name"])

        MODEL = "gpt-3.5-turbo"
        USER_INPUT_MSG = f"1. {request.data['plant_name']}에 {request.data['plant_leaf_state']}인 경우에는 어떻게 해결해야 하니? \
            2. {request.data['plant_name']}의 물을 주는 주기는 어떻게 되니?"

        response = openai.ChatCompletion.create(
        model=MODEL,
        messages=[
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": USER_INPUT_MSG}, 
            {"role": "assistant", "content": "Who's there?"},
        ],
        temperature=0,
        )

        print(response)

        print(response['choices'][0]['message']['content'])

        response = response['choices'][0]['message']['content']

        return Response({"status": "error", "data": response})
    else:
        return Response({"status": "error", "data": serializer.errors})