from django.db import models

# Create your models here.
class User(models.Model):
    user_id = models.AutoField(primary_key = True)
    user_name = models.CharField(max_length = 20)
    password = models.CharField(max_length = 30)
    name = models.TextField()
    email = models.TextField()
    phone_number = models.TextField()
    category = models.CharField(max_length=1)
    
    def __str__(self):
        return self.user_name


class Group(models.Model):
    group_id = models.AutoField(primary_key = True)
    group_name = models.TextField()
    simplified = models.BooleanField(default = False)

    def __str__(self):
        return self.group_name


class Group_User(models.Model):
    user_id = models.ForeignKey('User', on_delete = models.CASCADE)
    group_id = models.ForeignKey('Group', on_delete = models.CASCADE)

    class Meta:
        unique_together = (("user_id","group_id"))

    def __str__(self):
        return (str(self.user_id), str(self.group_id))