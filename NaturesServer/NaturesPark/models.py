from django.db import models

# Create your models here.

class Plant(models.Model):
    plant_name = models.CharField(max_length = 20)
    plant_leaf_state = models.CharField(max_length = 20)