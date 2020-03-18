from api.serializers import *
from rest_framework.views import APIView
from rest_framework.response import Response
from django.contrib.auth import login, logout, authenticate


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

        elif action == 'logout':
            logout(request)
            return Response("Successfully logged out")

        else:
            return Response("invalid get type in Account")

    @staticmethod
    def post(request):
        action = request.data.get('type')

        if action == 'create':

            username = request.data.get('username')
            password = request.data.get('password')
            email = request.data.get('email')
            name = request.data.get('name')
            phone_number = request.data.get('phone_number')
            category = request.data.get('category')

            username_already_used = User.objects.filter(username=username).exists()
            if username_already_used:
                return Response("Username already is use")

            email_already_used = User.objects.filter(email=email).exists()
            if email_already_used:
                return Response("Email already in use")

            # ensure email is in goa.bits-pilani.ac.in domain

            user = User.objects.create_user(username=username, password=password, email=email)

            profile = Profile(user=user, name=name, phone_number=phone_number, category=category)
            profile.save()

            return Response("Account created")

        elif action == 'delete':

            username = request.data.get('username')

            user = User.objects.get(username=username)
            user.delete()

            return Response("Account deleted")

        else:

            return Response("invalid get type in Account")
