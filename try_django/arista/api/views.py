from api.serializers import *
from rest_framework.views import APIView
from rest_framework.response import Response
from django.contrib.auth import login, logout, authenticate
import json
from rest_framework.permissions import IsAuthenticated 


class AccountView(APIView):

    permission_classes = (IsAuthenticated,)

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
            return Response("invalid get in AccountView")

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
            return Response("invalid post in AccountView")


class GroupView(APIView):

    @staticmethod
    def get(request):
        action = request.data.get('type')

        if action == 'groupname':
            groupid = request.data.get('groupid')

            group = Group.objects.get(pk=groupid)

            return Response(str(group))

        elif action == 'grouplist':
            userid = request.data.get('userid')
            user = User.objects.get(pk=userid)
            group_user = Group_User.objects.filter(user=user)

            serializer = Group_UserSerializer(group_user, many=True)

            return Response(serializer.data)

            # grouplist = [str(g.group) for g in group_user]
            # grouplist_as_json = json.dumps(grouplist)
            # return Response(grouplist_as_json)

        elif action == 'groupmembers':
            groupid = request.data.get('groupid')

            group = Group.objects.get(pk=groupid)
            group_user = Group_User.objects.filter(group=group)

            serializer = Group_UserSerializer(group_user, many=True)

            return Response(serializer.data)

        else:
            return Response('invalid get in GroupView')

    @staticmethod
    def put(request):
        action = request.data.get('type')


class UserView(APIView):

    @staticmethod
    def get(request):
        action = request.data.get('type')   

        if action == 'username':
            userid = request.data.get('userid')

            user = User.objects.get(pk=userid)

            return Response(str(user))

        else:
            return Response('invalid get in UserView')

    @staticmethod
    def put(request):
        action = request.data.get('type')
