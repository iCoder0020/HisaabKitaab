from django.contrib import admin
from .models import *
# Register your models here.


#admin.site.register(User)
admin.site.register(Profile)
admin.site.register(Group)
admin.site.register(Group_User)
admin.site.register(Payment_Individual)
admin.site.register(Payment_Whole)
admin.site.register(Friend)
admin.site.register(Transaction)