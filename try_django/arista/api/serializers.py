from rest_framework import serializers

class UserSerializer(serializers.Serializer):
    user_name = serializers.CharField()
    name = serializers.CharField()
    email = serializers.CharField()
    phone_number = serializers.CharField()
    category = serializers.CharField()

class GroupSerializer(serializers.Serializer):
    group_name = serializers.CharField()
    simplified = serializers.BooleanField()

class Group_UserSerializer(serializers.Serializer):
    user = serializers.PrimaryKeyRelatedField(read_only = True)
    group = serializers.PrimaryKeyRelatedField(read_only = True)

class FriendsSerialzer(serializers.Serializer):
    group = serializers.PrimaryKeyRelatedField(read_only = True)
    user1 = serializers.PrimaryKeyRelatedField(read_only = True)
    user2 = serializers.PrimaryKeyRelatedField(read_only = True)

class Payment_WholeSerializer(serializers.Serializer):
    group = serializers.PrimaryKeyRelatedField(read_only = True)
    amount = serializers.FloatField()
    timestamp = serializers.DateTimeField()
    description = serializers.CharField()

class Payment_IndividualSerializer(serializers.Serializer):
    payment = serializers.PrimaryKeyRelatedField(read_only = True)
    lender = serializers.PrimaryKeyRelatedField(read_only = True)
    amount = serializers.FloatField()

class TransactionSerializer(serializers.Serializer):
    payment = serializers.PrimaryKeyRelatedField(read_only = True)
    lender = serializers.PrimaryKeyRelatedField(read_only = True)
    borrower = serializers.PrimaryKeyRelatedField(read_only = True)
    amount = serializers.FloatField()
