from django.contrib import admin
from django.urls import path

from django.conf import settings
from django.conf.urls.static import static

import NaturesPark.views

urlpatterns = [
    path("admin/", admin.site.urls),
    path("images/save", NaturesPark.views.img_save, name="images"),
    path("images/classify", NaturesPark.views.img_classify, name="classify"),
    path("plant/info", NaturesPark.views.plant_info, name="plant_info"),
]

urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)