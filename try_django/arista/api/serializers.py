from rest_framework import serializers
from .models import *
from django.contrib.auth.models import User

class ProfileSerializer(serializers.ModelSerializer):
    user = serializers.StringRelatedField(read_only=True)
    class Meta:
        model = Profile
        fields = '__all__'

class UserSerializer(serializers.ModelSerializer):
    profile = ProfileSerializer(required=True)
    class Meta:
        model = User
        fields = '__all__'
    
    def create(self, validated_data):
        # create user 
        user = User.objects.create(
            username = validated_data['username'],
            email = validated_data['email'],
            password = validated_data['password']
        )
        profile_data = validated_data.pop('profile')
        
        # create profile
        profile = Profile.objects.create(
            user = user,
            name = profile_data['name'],
        )
        return user

class GroupSerializer(serializers.ModelSerializer):
    class Meta:
        model = Group
        fields = '__all__'


class Group_UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = Group_User
        fields = ['groupid']


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
