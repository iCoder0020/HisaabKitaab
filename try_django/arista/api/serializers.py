from rest_framework import serializers
from .models import *
from django.contrib.auth.models import User


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = '__all__'


class ProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = Profile
        fields = '__all__'


class GroupSerializer(serializers.ModelSerializer):
    class Meta:
        model = Group
        fields = '__all__'


class Group_UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = Group_User
        fields = '__all__'


class FriendSerializer(serializers.ModelSerializer):
    class Meta:
        model = Friend
        fields = '__all__'


class Payment_WholeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Payment_Whole
        fields = '__all__'


class Payment_IndividualSerializer(serializers.ModelSerializer):
    class Meta:
        model = Payment_Individual
        fields = '__all__'


class TransactionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Transaction
        fields = '__all__'
