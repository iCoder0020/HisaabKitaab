from api.serializers import *
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from django.contrib.auth import login, logout, authenticate
from django.views.decorators.csrf import csrf_exempt


class UserList(APIView):

    @staticmethod
    def get(request):
        profiles = Profile.objects.all()
        serializer = ProfileSerializer(profiles, many=True)
        return Response(serializer.data)

    @staticmethod
    def post(request):
        serializer = UserSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class Account(APIView):

    @staticmethod
    def get(request):
        action = request.data.get('type')

        if action == 'login':
            username = request.data.get('username')
            password = request.data.get('password')

            if username is None or password is None:
                return Response("Missing Credentials")

            user = authenticate(username=username, password=password)

            if user is None:
                return Response("Wrong Credentials")
            else:
                login(request, user)
                return Response("Welcome " + str(user.first_name))

        if action == 'logout':
            logout(request)
            return Response("Successfully logged out")

        else:
            return Response("Welcome to the API")

    @staticmethod
    def post(request):
        print("hello")
        username = request.data.get('username')
        password = request.data.get('password')
        email = request.data.get('email')
        name = request.data.get('name')
        phone_number = request.data.get('phone_number')
        category = request.data.get('category')

        print(username, password, email, name, phone_number, category)

        user = User(username=username, password=password, email=email)

        user.save()

        profile = Profile(user=user, name=name, phone_number=phone_number, category=category)

        # profile.save()

        return Response("Account created")

