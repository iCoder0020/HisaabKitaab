from django.db import models

#TO-FIX: on_delete = models.CASCADE everywhere for now

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
    user = models.ForeignKey('User', on_delete = models.CASCADE)
    group = models.ForeignKey('Group', on_delete = models.CASCADE)

    class Meta:
        unique_together = (("user", "group"))

    def __str__(self):
        return str(self.group) + ", " + str(self.user)

# class Friends(models.Model):
#     group = models.ForeignKey('Group', on_delete = models.CASCADE, primary_key = True)
#     user1 = models.ForeignKey('User', related_name = "User1", on_delete = models.CASCADE)
#     user2 = models.ForeignKey('User', related_name = "User2", on_delete = models.CASCADE)

#     def __str__(self):
#         return str(self.user1) + ", " + str(self.user2)

class Payment_Whole(models.Model):
    payment_id = models.AutoField(primary_key = True)
    group = models.ForeignKey('Group', on_delete = models.CASCADE)
    amount = models.FloatField()
    timestamp = models.DateTimeField(auto_now_add = True)
    description = models.TextField()

    def __str__(self):
        return self.description

class Payment_Individual(models.Model):
    payment = models.ForeignKey('Payment_Whole', on_delete = models.CASCADE)
    lender = models.ForeignKey('User', on_delete = models.CASCADE)
    amount = models.FloatField()

    class Meta:
        unique_together = (("payment", "lender"))

    def __str__(self):
        return str(self.payment) + ", " + str(self.lender)

class Transaction(models.Model):
    transaction_id = models.AutoField(primary_key = True)
    payment = models.ForeignKey('Payment_Whole', on_delete = models.CASCADE)
    lender = models.ForeignKey('User', related_name = "Lender", on_delete = models.CASCADE)
    borrower = models.ForeignKey('User', related_name = "Borrower", on_delete = models.CASCADE)
    amount = models.FloatField()

    def __str__(self):
        return str(self.payment) + ": " + str(self.borrower) + "->" + str(self.lender)

