from django.contrib import admin
from django.urls import path

from django.conf import settings
from django.conf.urls.static import static

import NaturesPark.views

urlpatterns = [
    path("admin/", admin.site.urls),
    path("images/", NaturesPark.views.index, name="images")
]

urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)