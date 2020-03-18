from django.db import models
from django.contrib.auth.models import User
from django.db.models.signals import post_save
from django.dispatch import receiver


# TO-FIX: on_delete = models.CASCADE everywhere for now

class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, primary_key=True)
    name = models.CharField(max_length=64)
    phone_number = models.CharField(max_length=10)
    category = models.CharField(max_length=1)

    def __str__(self):
        return str(self.user)

    # The following signals are used so our Profile model will be automatically created/updated when we create/update
    # User instances.
    @receiver(post_save, sender=User)
    def create_user_profile(sender, instance, created, **kwargs):
        if created:
            Profile.objects.create(user=instance)

    @receiver(post_save, sender=User)
    def save_user_profile(sender, instance, **kwargs):
        instance.profile.save()


class Group(models.Model):
    group_name = models.CharField(max_length=16)
    simplified = models.BooleanField(default=False)

    def __str__(self):
        return self.group_name


class Group_User(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    group = models.ForeignKey('Group', on_delete=models.CASCADE)

    class Meta:
        unique_together = ("user", "group")

    def __str__(self):
        return str(self.group) + ", " + str(self.user)


class Friend(models.Model):
    group = models.ForeignKey('Group', on_delete=models.CASCADE)
    user1 = models.ForeignKey(User, related_name="User1", on_delete=models.CASCADE)
    user2 = models.ForeignKey(User, related_name="User2", on_delete=models.CASCADE)

    def __str__(self):
        return str(self.user1) + ", " + str(self.user2)


class Payment_Whole(models.Model):
    group = models.ForeignKey('Group', on_delete=models.CASCADE)
    amount = models.FloatField()
    timestamp = models.DateTimeField(auto_now_add=True)
    description = models.CharField(max_length=128)

    def __str__(self):
        return self.description


class Payment_Individual(models.Model):
    payment = models.ForeignKey('Payment_Whole', on_delete=models.CASCADE)
    lender = models.ForeignKey(User, on_delete=models.CASCADE)
    amount = models.FloatField()

    class Meta:
        unique_together = ("payment", "lender")

    def __str__(self):
        return str(self.payment) + ", " + str(self.lender)


class Transaction(models.Model):
    payment = models.ForeignKey('Payment_Whole', on_delete=models.CASCADE)
    lender = models.ForeignKey(User, related_name="Lender", on_delete=models.CASCADE)
    borrower = models.ForeignKey(User, related_name="Borrower", on_delete=models.CASCADE)
    amount = models.FloatField()

    def __str__(self):
        return str(self.payment) + ": " + str(self.borrower) + "->" + str(self.lender)
