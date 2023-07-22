from django.db import models

# Create your models here.

class ReceiveImg(models.Model):
    image = models.ImageField(upload_to="uploads/")
